package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.content.Context;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.helper.DataHelper;
import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;
import com.example.ahmedwahdan.flicker_photo.network.ImageRequestDownloader;
import com.example.ahmedwahdan.flicker_photo.network.RequestListener;
import com.example.ahmedwahdan.flicker_photo.network.SearchRequest;
import com.example.ahmedwahdan.flicker_photo.utils.Const;

import java.io.File;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchPresenterImp implements SearchPresenter, RequestListener.searchListener {

    private static String TAG = "SearchPresenter";
    private  Context context;
    String tagSearch;
    private SearchActivityView searchView;
    private boolean loadMore;

     public SearchPresenterImp(SearchActivityView searchView, Context context){
         this.context = context;
        this.searchView = searchView;
    }
    @Override
    public void getPhotosByTag(String tagSearch) {
        loadMore = false;
        this.tagSearch = tagSearch;
        searchView.showLoading();
       PhotoSearch res = DataHelper.getPhotoSearchFromSharedPrefrence(context);
        if (res != null && res.getPhotos() != null) {
            searchView.showPhotosByTag(res.getPhotos().getPhoto().subList(0, Const.MAX_NUMBER_PER_REQUEST-1),false);
        }
        SearchRequest.index(tagSearch, TAG, 1, this);
    }

    @Override
    public void loadMorePhoto(int page) {
        Log.d(TAG, "loadMore Page Number :" + page);
        loadMore = true;
        searchView.showLoading();
        SearchRequest.index(tagSearch, TAG, page, this);

    }

    @Override
    public void downloadPhoto(final PhotoItem item) {
        String fileName = FileHelper.getFlickrFilename(item);
        File imageFile = new File(FileHelper.getImagesDir(), fileName);
        if (!imageFile.exists()){
            ImageRequestDownloader.index(item.getGetURl(),fileName, new RequestListener.imageDownloadListener() {
                @Override
                public void onBitmapLoaded() {
                    searchView.onBitmapLoaded(item);
                    Log.i("Done", "PIC_STATUS_SAVED");
                }

                @Override
                public void onBitmapFailed() {
                    searchView.onBitmapFailed(item);
                    Log.i("Failed", "PIC_STATUS_FAIL");
                }
            });
        }
        else
            searchView.onBitmapLoaded(item);

    }


    @Override
    public void onSearchResult(PhotoSearch photoSearch) {
        searchView.hideLoading();
        if (!loadMore)
        DataHelper.saveGSONdateToSharedPrefrenace(context,photoSearch);
        searchView.showPhotosByTag(photoSearch.getPhotos().getPhoto(), loadMore);
    }

    @Override
    public void onError(String error) {

    }
}
