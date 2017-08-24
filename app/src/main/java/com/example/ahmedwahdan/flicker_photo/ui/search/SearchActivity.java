package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.provider.MySuggestionProvider;
import com.example.ahmedwahdan.flicker_photo.ui.PhotoViewer.PhotoViewer;
import com.example.ahmedwahdan.flicker_photo.ui.search.GroupSearch.GroupSearchFragmentImp;
import com.example.ahmedwahdan.flicker_photo.ui.search.PhotoSearch.PhotoSearchFragmentImp;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener, MVPViewer.SearchActivityView {
    public final static Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();

    public final static String PHOTO_ITEMS = "photo_items";
    private static final String TAG = SearchActivity.class.getSimpleName();
    //***UI****/
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
   @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

   static SearchPresenter presenter;
    Serializable photoItems;
    private MenuItem search;
    private SearchView searchView;

    private String currentQuery;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        attachPresenter();


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

        //PicassoHelper.init(this);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void attachPresenter() {
        presenter = (SearchPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null)
        presenter = new SearchPresenterImp(this);

    }
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    /***** Ui init ***/
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PhotoSearchFragmentImp(), "Photo");
        adapter.addFragment(new GroupSearchFragmentImp(), "Group");
        viewPager.setAdapter(adapter);
    }


    // Method called after user submit photo tag name
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: " + query);
        if (!query.equalsIgnoreCase("")) {
           // isLoadingMore = false;
            currentQuery = query;
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            // ask presenter to perform  networking call to get photo
            presenter.onSubmitSearch(currentQuery);
            search.collapseActionView();
        }
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    //*** on item click in RecyclerView ***/
//    @Override
//    public void onImageClicked(View view, String imagePath) {
//        transition(view , imagePath);
//
//    }


    //save state of photo item to restore it on onCreate Method to prevent it from disappear
      /*
     *Will be called when rotate the Device
     * to update number of item display on  LayoutManger with new screen width
     * */


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
         //getGridLayoutManager();
       // recyclerView.setLayoutManager(mGridLayoutManager);

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
        searchView.setIconified(false); //will show the search view expanded
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        search.expandActionView();
        return true;
    }

    //******* Interaction with  presenter actions *****/

    @Override
    public void showLoading() {
    //    progressBar.setVisibility(View.VISIBLE);

    }
    @Override
    public void hideLoading() {
        //progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setCurrentQuery(String query) {

    }


    @Override
    public void showErrorMessage( String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        presenter.onSubmitSearch(currentQuery);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void openPhotoViewer(String photoPath) {
        Intent intent = new Intent(this, PhotoViewer.class);
        intent.putExtra(PhotoViewer.IMAGE_KEY , photoPath);
        startActivity(intent);
    }

    @Override
    public void onNavigatePhotoSearch(String item) {
        toolbar.setTitle(item);
        tabLayout.getTabAt(0).select();
    }


    /**
    private void photoItemPicassoDownloader(List<PhotoItem> photoItems) {
//        adapter.notifyDataSetChanged();
//        for ( PhotoItem photoItem : photoItems) {
//            presenter.downloadPhoto(photoItem);
//            adapter.picStatusList.put(photoItem.getId(),PhotoState.PIC_STATUS_LOADING);


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
        //}
    }**/
//    @Override
//    public void onBitmapLoaded(PhotoItem item) {
//        adapter.picStatusList.put(item.getId(), PhotoState.PIC_STATUS_SAVED);
//        Log.i("Done", "PIC_STATUS_SAVED");
//        adapter.notifyDataSetChanged();
//
//    }

//    @Override
//    public void onBitmapFailed(PhotoItem item) {
//        adapter.picStatusList.put(item.getId(),PhotoState.PIC_STATUS_FAIL);
//        Log.i("Failed", "PIC_STATUS_FAIL");
//        adapter.notifyDataSetChanged();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }




}
