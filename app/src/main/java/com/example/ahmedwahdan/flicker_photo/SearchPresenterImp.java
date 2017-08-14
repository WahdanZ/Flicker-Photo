package com.example.ahmedwahdan.flicker_photo;

import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.request.RequestListener;
import com.example.ahmedwahdan.flicker_photo.request.SearchRequest;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchPresenterImp implements SearchPresenter, RequestListener.searchListener {

    private static String TAG = "SearchPresenter";
    String tagSearch;
    private SearchActivityView searchView;
    private boolean loadMore;

     SearchPresenterImp (SearchActivityView searchView){
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
