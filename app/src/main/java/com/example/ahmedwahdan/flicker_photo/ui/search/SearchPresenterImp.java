package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.helper.DataHelper;
import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;
import com.example.ahmedwahdan.flicker_photo.network.ImageRequestDownloader;
import com.example.ahmedwahdan.flicker_photo.network.RequestListener;
import com.example.ahmedwahdan.flicker_photo.network.SearchRequest;
import com.example.ahmedwahdan.flicker_photo.utils.Const;
import com.example.ahmedwahdan.flicker_photo.utils.NetworkUtils;

import java.io.File;
import java.util.List;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchPresenterImp implements SearchPresenter, RequestListener.searchListener {

    private static String TAG = "SearchPresenter";
    private  Context context;
    String tagSearch;
    private SearchActivityView searchView;
    private boolean loadMore;
    private List<PhotoItem> photItems;
    private PhotoSearch photoSearch;

    public SearchPresenterImp(SearchActivityView searchView, Context context){
         this.context = context;
        this.searchView = searchView;
    }
    @Override
    public void getPhotosByTag(String tagSearch) {
        if (!checkInternetConnection()) {
            return;
        }
        loadMore = false;
        this.tagSearch = tagSearch;
        searchView.showLoading();
        photoSearch = DataHelper.getPhotoSearchFromSharedPrefrence(context);
        if (photoSearch != null) {
             photItems = photoSearch.getPhotos().getPhoto();
            searchView.hideLoading();
           if ( photItems.size() > Const.MAX_NUMBER_PER_REQUEST)
            searchView.showPhotosByTag(photItems.subList(0, Const.MAX_NUMBER_PER_REQUEST-1),false);
        }
        SearchRequest.index(tagSearch, TAG, 1, this);
    }

    private boolean checkInternetConnection() {
        Boolean isConnect = NetworkUtils.isInternetConnected(context);
        if (!isConnect)
            searchView.showErrorMessage("No internet connection!");
        return isConnect;
    }

    @Override
    public void displayLastPhotoSearch() {
        photoSearch = DataHelper.getPhotoSearchFromSharedPrefrence(context);
        if (photoSearch == null)
            return;
        photItems = photoSearch.getPhotos().getPhoto();
        if (photItems.size() > Const.MAX_NUMBER_PER_REQUEST)
        searchView.showPhotosByTag(photItems.subList(0, Const.MAX_NUMBER_PER_REQUEST-1),false);
        else searchView.showPhotosByTag(photItems,false);
    }

    @Override
    public void loadMorePhoto(int page) {
        if (!checkInternetConnection())
            return;
        Log.d(TAG, "loadMore Page Number :" + page);
        loadMore = true;
        searchView.showLoading();
        SearchRequest.index(tagSearch, TAG, page, this);

    }

    @Override
    public void downloadPhoto(PhotoItem item, final SearchActivityView.PhotoAdapterView adapterView) {
        String fileName = FileHelper.getFlickrFilename(item);
        File imageFile = new File(FileHelper.getDefaultSaveFile(), fileName);
        if (!imageFile.exists()){
            ImageRequestDownloader.index(item.getGetURl(),fileName, new RequestListener.imageDownloadListener() {


                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    adapterView.onBitmapLoaded(bitmap);
                    Log.i("Done", "PIC_STATUS_SAVED");
                }

                @Override
                public void onBitmapFailed() {
                    adapterView.onBitmapFailed();
                    Log.i("Failed", "PIC_STATUS_FAIL");
                }
            });
        }
        else{
            adapterView.onBitmapLoaded(null);
            Log.i(TAG, "downloadPhoto: file already downloaded ");
        }

    }



    @Override
    public void onSearchResult(PhotoSearch photoSearch) {
        if (this.photoSearch == null){
            this.photoSearch = photoSearch;
        }
        searchView.hideLoading();
        if (!loadMore)
        DataHelper.saveGSONdateToSharedPrefrenace(context,photoSearch);

        searchView.showPhotosByTag(photoSearch.getPhotos().getPhoto(), loadMore);
    }

    @Override
    public void onError(String error) {
        searchView.showErrorMessage(error);

    }
}
