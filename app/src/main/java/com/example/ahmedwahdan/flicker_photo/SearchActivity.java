package com.example.ahmedwahdan.flicker_photo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener, SearchActivityView {
    public final static String PHOTO_ITEMS = "photo_items";
    private static final String TAG = SearchActivity.class.getSimpleName();
    List<PhotoItem> photosList ;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    SearchPresenter presenter;
    List<PhotoItem> photoItems;
    private MenuItem search;
    private SearchView searchView;
    private PhotoAdapter adapter;
    private GridLayoutManager mGridLayoutManager;
    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        photosList = new ArrayList<>();
        presenter = new SearchPresenterImp(this);

        // get number of Columns should depend on screen size using Utility Class
        // it get screen width and  divided it by 180 as 180 fixed image width
        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
        // create new instance of Gridlayout manager
        mGridLayoutManager = new GridLayoutManager(this,mNoOfColumns);

        recyclerView.setLayoutManager(mGridLayoutManager);
        onLoadMoreItemList();
        // check if there is savedInstance  value during change ordination
        // and get value of photo list to display it again on recyclerView
        if(savedInstanceState != null){
            photoItems = (List<PhotoItem>) savedInstanceState.getSerializable(PHOTO_ITEMS);
        }


    }

    private void onLoadMoreItemList() {
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mGridLayoutManager) {

            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 0;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                page++;

                presenter.loadMorePhoto(page);
            }
        });
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
        mGridLayoutManager = new GridLayoutManager(this,Utility.calculateNoOfColumns(getApplicationContext()));
        recyclerView.setLayoutManager(mGridLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();
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

    @Override
    public void showPhotosByTag(List<PhotoItem> photoItems, boolean isLoadingMore) {
        Log.d("SearchActivity", "showPhotoResult");
        if (adapter == null) {
            Log.d("SearchActivity", "showPhotoResult : Create New Adapter");

            // create  instance of recyclerView Adapter
            adapter = new PhotoAdapter(photoItems, this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d("SearchActivity", "showPhotoResult : adapter is exist load more or get new search result");

            if (isLoadingMore) {
                Log.d("SearchActivity", "showPhotoResult : Load more photo");

                adapter.loadMorePhoto(photoItems);
            } else {
                Log.d("SearchActivity", "showPhotoResult : Display new search result " + photoItems.size());
                photosList = photoItems;

                adapter.updatePhotoList(photosList);

            }
        }

    }

    // Method called after user submit photo tag name
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.equalsIgnoreCase("")) {
            currentQuery = query;
            // ask presenter to perform  networking call to get photo
            presenter.getPhotosByTag(currentQuery);
            search.collapseActionView();

        }
            return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
