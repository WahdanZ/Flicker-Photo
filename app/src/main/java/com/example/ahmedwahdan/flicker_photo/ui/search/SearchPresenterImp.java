package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.network.RequestListener;
import com.example.ahmedwahdan.flicker_photo.network.SearchRequest;
import com.example.ahmedwahdan.flicker_photo.network.model.PhotoItem;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchPresenterImp implements SearchPresenter, RequestListener.searchListener {

    private static String TAG = "SearchPresenter";
    String tagSearch;
    private SearchActivityView searchView;
    private boolean loadMore;

     public SearchPresenterImp(SearchActivityView searchView){
        this.searchView = searchView;
    }
    @Override
    public void getPhotosByTag(String tagSearch) {
        loadMore = false;
        this.tagSearch = tagSearch;
        searchView.showLoading();
        SearchRequest.index(tagSearch, TAG, 1, this);
    }

    @Override
    public void loadMorePhoto(int page) {
        Log.d(TAG, "loadMore Page Number :" + page);
        loadMore = true;
        searchView.showLoading();
        SearchRequest.index(tagSearch, TAG, page, this);

    }


    @Override
    public void onSearchResult(List<PhotoItem> photos) {
        searchView.hideLoading();
        searchView.showPhotosByTag(photos, loadMore);
    }

    @Override
    public void onError(String error) {

    }
}
