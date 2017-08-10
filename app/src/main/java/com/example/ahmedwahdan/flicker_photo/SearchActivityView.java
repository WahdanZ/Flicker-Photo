package com.example.ahmedwahdan.flicker_photo;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public interface SearchActivityView {
    void showLoading();
    void hideLoading();
    void showPhotosByTag(List<PhotoItem> photoItems);

    }
