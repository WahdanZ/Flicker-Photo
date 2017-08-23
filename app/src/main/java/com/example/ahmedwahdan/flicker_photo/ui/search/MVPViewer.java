package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public interface MVPViewer {
    void showLoading();
    void hideLoading();
    Context getContext();
    void setCurrentQuery(String query);
    void showErrorMessage(String message);
    interface SearchActivityView extends MVPViewer {
        void openPhotoViewer(String photoPath);

        void onNavigatePhotoSearch(String item);
    }

    interface PhotoSearchFragment extends MVPViewer {
       void showPhotosByTag(List<PhotoItem> photoItems, boolean isLoadingMore);


    }
     interface GroupSearchFragment extends MVPViewer {
        void showGroupByTag(List<GroupItem> groupItems, boolean isLoadingMore);

    }
    interface PhotoAdapterView{
        void onBitmapLoaded(Bitmap bitmap);
        void onBitmapFailed();
    }



}
