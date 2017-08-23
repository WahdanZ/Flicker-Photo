package com.example.ahmedwahdan.flicker_photo.network;

import android.graphics.Bitmap;

import com.example.ahmedwahdan.flicker_photo.model.GroupSearch;
import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;

/**
 * Created by ahmedwahdan on 8/10/17.
 */
/*
*
*
* */
public interface RequestListener {
     interface photoSearchListener {
        void onSearchResult(PhotoSearch photoSearch);
        void onError(String error);
    }
    interface groupSearchListener {
        void onSearchResult(GroupSearch photoSearch);
        void onError(String error);
    }
    interface  imageDownloadListener{
        void onBitmapLoaded(Bitmap bitmap);
        void onBitmapFailed();
    }
}
