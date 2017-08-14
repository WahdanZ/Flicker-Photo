package com.example.ahmedwahdan.flicker_photo;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public interface SearchPresenter {
    void getPhotosByTag(String tag);

    void loadMorePhoto(int page);

}
