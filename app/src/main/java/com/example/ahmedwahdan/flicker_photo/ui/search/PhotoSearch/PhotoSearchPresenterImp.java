package com.example.ahmedwahdan.flicker_photo.ui.search.PhotoSearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.GlobalBus;
import com.example.ahmedwahdan.flicker_photo.helper.DataHelper;
import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;
import com.example.ahmedwahdan.flicker_photo.network.ImageRequestDownloader;
import com.example.ahmedwahdan.flicker_photo.network.PhotoSearchRequest;
import com.example.ahmedwahdan.flicker_photo.network.RequestListener;
import com.example.ahmedwahdan.flicker_photo.ui.search.Events;
import com.example.ahmedwahdan.flicker_photo.ui.search.MVPViewer;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenter;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenterImp;
import com.example.ahmedwahdan.flicker_photo.utils.Const;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/23/17.
 */

public class PhotoSearchPresenterImp extends SearchPresenterImp implements SearchPresenter.PhotoSearchPresenter , RequestListener.photoSearchListener {
    MVPViewer.PhotoSearchFragment viewTarget;

    private boolean loadMore;
    private List<PhotoItem> photItems;
    private PhotoSearch photoSearch;
    private String TAG = "PhotoSearchPresenterImp" ;
    Context context;
    private String currentTag;
    private String group_id ="";

    public PhotoSearchPresenterImp(MVPViewer.PhotoSearchFragment photoSearchFragment ) {
        super((MVPViewer.SearchActivityView) photoSearchFragment.getContext());
        this.viewTarget = photoSearchFragment;
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        GlobalBus.getBus().unregister(this);
    }



    @Override
    public void displayLastPhotoSearch() {

    }

    @Override
    public void getPhotosByTag(String tagSearch) {
        currentTag = tagSearch;

        if (!checkInternetConnection()) {

            viewTarget.setCurrentQuery(tagSearch);
            viewTarget.showLoading();
            photoSearch = DataHelper.getPhotoSearchFromSharedPreference(App.getInstance(), tagSearch);
            if (photoSearch != null) {
                photItems = photoSearch.getPhotos().getPhoto();
                viewTarget.hideLoading();
                if (photItems.size() > Const.MAX_NUMBER_PER_REQUEST)
                    onSearchResultOffline(photoSearch);
            }
        }else {
            loadMore = false;
            viewTarget.setCurrentQuery(tagSearch);
            viewTarget.showLoading();
            PhotoSearchRequest.index(tagSearch, TAG, 1, group_id,this);
        }
    }



    @Subscribe
    public void onSubmitSearch(Events.Query.Photo tagSearch) {
        viewTarget.setCurrentQuery(tagSearch.getQuery());
        getPhotosByTag(tagSearch.getQuery());
    }

    @Override
    public void loadMorePhoto(String query , int page) {
        if (!checkInternetConnection())
            return;
        Log.d(TAG, "loadMore Page Number :" + page);
        loadMore = true;
        PhotoSearchRequest.index(query, TAG, page,group_id, this);
    }

    @Override
    public void downloadPhoto(PhotoItem item, final MVPViewer.PhotoAdapterView adapterView){
            ImageRequestDownloader.index(item.getGetURl(),FileHelper.getFlickrFilename(item), new RequestListener.imageDownloadListener() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    adapterView.onBitmapLoaded(bitmap);
                }

                @Override
                public void onBitmapFailed() {
                    adapterView.onBitmapFailed();
                    Log.i("Failed", "PIC_STATUS_FAIL");
                }
            });

    }

    @Override
    public void onPhotoItemClicked(String flickrFilePath) {
        GlobalBus.getBus().post(new Events.photoItemPath(flickrFilePath));
    }


    @Override
    public void onSearchResult(PhotoSearch photoSearch) {
        if (photoSearch !=null) {
            photItems = photoSearch.getPhotos().getPhoto();
            DataHelper.saveGOONDateToSharedPreference(App.getInstance().getContext(),photoSearch,currentTag);
            viewTarget.hideLoading();
            viewTarget.showPhotosByTag(photItems, loadMore);
        }

    }
    private void onSearchResultOffline(PhotoSearch photoSearch) {
        if (photoSearch !=null) {
            photItems = photoSearch.getPhotos().getPhoto();
            viewTarget.hideLoading();
            viewTarget.showPhotosByTag(photItems, loadMore);
        }
    }

    @Subscribe
    public void  onGroupItemClick(Events.GroupItem item){
        group_id = item.getNsid();
        getPhotosByTag("");
    }

    @Override
    public void onError(String error) {
        Log.d("PhotoSearchPresenterImp", error);
    }
}
