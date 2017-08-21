package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.graphics.Bitmap;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public interface SearchActivityView {
    void showLoading();
    void hideLoading();
    void showPhotosByTag(List<PhotoItem> photoItems, boolean isLoadingMore);
    void showErrorMessage(String message);

    interface PhotoAdapterView{
        void onBitmapLoaded(Bitmap bitmap);
        void onBitmapFailed();
    }

}
