package com.example.ahmedwahdan.flicker_photo;

import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.request.RequestListener;
import com.example.ahmedwahdan.flicker_photo.request.SearchRequest;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchPresenterImp implements SearchPresenter {

    private  SearchActivityView searchView;
    private static String TAG = "SearchPresenter";

     SearchPresenterImp (SearchActivityView searchView){
        this.searchView = searchView;
    }
    @Override
    public void getPhotoByTag(String tagSearch) {

        searchView.showLoading();
        SearchRequest.index(tagSearch,TAG ,  new RequestListener.searchListener() {
            @Override
            public void onSearchResult(List<PhotoItem> photos) {

                if (photos.size() > 0)
                    searchView.showPhotosByTag(photos);
                    searchView.hideLoading();
            }

            @Override
            public void onError(String error) {
                Log.d("SearchActivity", error);
                searchView.hideLoading();

            }
        });
    }
}
