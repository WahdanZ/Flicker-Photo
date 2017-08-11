package com.example.ahmedwahdan.flicker_photo.request;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */
/*
*
*
* */
public interface RequestListener {
     interface searchListener{
        void onSearchResult(List<PhotoItem> photos);
        void onError(String error);
    }
}
