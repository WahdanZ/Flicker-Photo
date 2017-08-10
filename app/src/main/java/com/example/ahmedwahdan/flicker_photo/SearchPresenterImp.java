package com.example.ahmedwahdan.flicker_photo;

import android.content.Context;
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
    private  Context context;

    public SearchPresenterImp (Context context , SearchActivityView searchView){
        this.context = context;
        this.searchView = searchView;
    }
    @Override
    public List<PhotoItem> getPhotoByTag(String tag) {
        searchView.showLoading();
        SearchRequest.index(tag, new RequestListener.searchListener() {
            @Override
            public void onSearchResault(List<PhotoItem> photos) {

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
        return null;
    }
}
