package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.helper.PhotoState;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.network.PicassoHelper;
import com.example.ahmedwahdan.flicker_photo.provider.MySuggestionProvider;
import com.example.ahmedwahdan.flicker_photo.ui.helper.EndlessRecyclerViewScrollListener;
import com.example.ahmedwahdan.flicker_photo.utils.ScreenUtils;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener, SearchActivityView {
    public final static Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();

    public final static String PHOTO_ITEMS = "photo_items";
    private static final String TAG = SearchActivity.class.getSimpleName();
    List<PhotoItem> photosList ;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.content_view)
    View contenView;
    SearchPresenter presenter;
    Serializable photoItems;
    private MenuItem search;
    private SearchView searchView;
    private PhotoAdapter adapter;
    private GridLayoutManager mGridLayoutManager;
    private String currentQuery;
    private RecyclerView.OnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        photosList = new ArrayList<>();
        presenter = new SearchPresenterImp(this,this);


        // create new instance of Gridlayout manager
        getGridLayoutManager();
        recyclerView.setLayoutManager(mGridLayoutManager);
        scrollListener = getScrollListener();
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // check if there is savedInstance  value during change ordination
        // and get value of photo list to display it again on recyclerView
        if(savedInstanceState != null){
            photoItems =  savedInstanceState.getSerializable(PHOTO_ITEMS);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.expandActionView();
            }
        });
        setupUI(contenView);
        PicassoHelper.init(this);
    }

    private RecyclerView.OnScrollListener getScrollListener() {

        return new EndlessRecyclerViewScrollListener(mGridLayoutManager) {

            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "onLoadMore: " + page);
                presenter.loadMorePhoto(page);
            }
        };
    }

    //save state of photo item to restore it on onCreate Method to prevent it from disappear
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(PHOTO_ITEMS, (Serializable) photosList);
    }

    /*
     *Will be called when rotate the Device
     * to update number of item display on  LayoutManger with new screen width
     * */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
         getGridLayoutManager();
        recyclerView.setLayoutManager(mGridLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search =  menu.findItem(R.id.action_search);
         searchView = (SearchView)search.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);

        return true;
    }



    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);

    }
    @Override protected void onPause() {
        super.onPause();

    }
    @Override
    public void showPhotosByTag(List<PhotoItem> photoItems, boolean isLoadingMore) {
        if (adapter == null) {
            // create  instance of recyclerView Adapter
            adapter = new PhotoAdapter( photoItems, this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.i("SearchActivity", "showPhotoResult : adapter is exist load more or get new search result");

            if (isLoadingMore) {
                Log.d("SearchActivity", "showPhotoResult : Load more photo");
                adapter.loadMorePhoto(photoItems);
            } else {
                Log.d("SearchActivity", "showPhotoResult : Display new search result " + photoItems.size());
                recyclerView.removeOnScrollListener(scrollListener);
                recyclerView.addOnScrollListener(getScrollListener());
                photosList = photoItems;
                adapter.updatePhotoList(photosList);
            }
            photoItemPicassoDownloader(photoItems);


        }

    }

    private void photoItemPicassoDownloader(List<PhotoItem> photoItems) {

        Log.i("Done", "PIC_STATUS_LOADING");
        adapter.notifyDataSetChanged();
        for ( PhotoItem photoItem : photoItems) {
            presenter.downloadPhoto(photoItem);
            adapter.picStatusList.put(photoItem.getId(),PhotoState.PIC_STATUS_LOADING);


//            PicassoHelper.DownloadTarget target = new PicassoHelper.DownloadTarget();
//            target.fileName = photoItem.getId() + ".jpg";
//            target.onBitmapLoaded = new Runnable(){
//                @Override
//                public void run() {
//                    adapter.picStatusList.put(item.getId(),PhotoState.PIC_STATUS_SAVED);
//                    Log.i("Done", "PIC_STATUS_SAVED");
//
//                    adapter.notifyDataSetChanged();
//                }
//            };
//
//            target.onBitmapFailed = new Runnable() {
//                @Override
//                public void run() {
//                    adapter.picStatusList.put(item.getId(),PhotoState.PIC_STATUS_FAIL);
//                    Log.i("Failed", "PIC_STATUS_FAIL");
//                    adapter.notifyDataSetChanged();
//                }
//            };
//            MyPicasso.load(photoItem.getGetURl()).tag(this).into(target);
        }
    }
    @Override
    public void onBitmapLoaded(PhotoItem item) {
        adapter.picStatusList.put(item.getId(), PhotoState.PIC_STATUS_SAVED);
        Log.i("Done", "PIC_STATUS_SAVED");
    }

    @Override
    public void onBitmapFailed(PhotoItem item) {
        adapter.picStatusList.put(item.getId(),PhotoState.PIC_STATUS_FAIL);
        Log.i("Failed", "PIC_STATUS_FAIL");
        adapter.notifyDataSetChanged();
    }
    // Method called after user submit photo tag name
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: " + query);
        if (!query.equalsIgnoreCase("")) {
            currentQuery = query;
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            // ask presenter to perform  networking call to get photo
            presenter.getPhotosByTag(currentQuery);
            search.collapseActionView();
        }
            return false;
    }
    public void setupUI(View view) {

        if(!(view instanceof SearchView)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    search.collapseActionView();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public  void getGridLayoutManager(){
        // get number of Columns should depend on screen size using ScreenUtils Class
        // it get screen width and  divided it by 180 as 180 fixed image width
        int numColumns = ScreenUtils.calculateNoOfColumns(getApplicationContext());
         mGridLayoutManager  =  new GridLayoutManager(this,
                numColumns);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case PhotoAdapter.VIEW_DIVIDE:
                        return mGridLayoutManager.getSpanCount();
                    case PhotoAdapter.VIEW_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
    }
}