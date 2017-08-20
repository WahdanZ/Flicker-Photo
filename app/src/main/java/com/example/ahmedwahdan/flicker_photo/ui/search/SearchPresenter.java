package com.example.ahmedwahdan.flicker_photo.ui.search;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public interface SearchPresenter {
    void getPhotosByTag(String tag);
    void loadMorePhoto(int page);
    void downloadPhoto(PhotoItem photoItem);

}
