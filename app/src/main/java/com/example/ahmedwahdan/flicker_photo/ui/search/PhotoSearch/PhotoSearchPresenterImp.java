package com.example.ahmedwahdan.flicker_photo.ui.search.PhotoSearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.GlobalBus;
import com.example.ahmedwahdan.flicker_photo.db.AppDatabase;
import com.example.ahmedwahdan.flicker_photo.helper.DataHelper;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;
import com.example.ahmedwahdan.flicker_photo.network.ImageRequestDownloader;
import com.example.ahmedwahdan.flicker_photo.network.PhotoSearchRequest;
import com.example.ahmedwahdan.flicker_photo.network.RequestListener;
import com.example.ahmedwahdan.flicker_photo.ui.search.Events;
import com.example.ahmedwahdan.flicker_photo.ui.search.MVPViewer;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenter;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenterImp;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmedwahdan on 8/23/17.
 */

public class PhotoSearchPresenterImp extends SearchPresenterImp implements SearchPresenter.PhotoSearchPresenter , RequestListener.photoSearchListener {
    private  AppDatabase db;
    MVPViewer.PhotoSearchFragment viewTarget;

    private boolean loadMore;
    private List<PhotoItem> photItems;
    private ArrayList<PhotoItem> photoItems;
    private PhotoSearch photoSearch;
    private String TAG = "PhotoSearchPresenterImp" ;
    Context context;
    private String currentTag;
    private String group_id ="";

    public PhotoSearchPresenterImp(MVPViewer.PhotoSearchFragment photoSearchFragment ) {
        super((MVPViewer.SearchActivityView) photoSearchFragment.getContext());
        this.viewTarget = photoSearchFragment;
        db = App.getAppDatabase();
        photoItems = new ArrayList<>();

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
    public void getPhotosByTag(String tagSearch, String group_id) {
        currentTag = tagSearch;
        this.group_id = group_id;
        viewTarget.setCurrentQuery(tagSearch);
        getPhotosItemFromDB(tagSearch);
        if (!checkInternetConnection()) {
           return;
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
        getPhotosByTag(tagSearch.getQuery(),"");
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
            ImageRequestDownloader.index(item.getGetURl(),item, new RequestListener.imageDownloadListener() {
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
            dbSave(photoSearch.getPhotos().getPhoto());
            viewTarget.hideLoading();
            viewTarget.showPhotosByTag(photItems, loadMore);
        }

    }
    private void onSearchResultOffline(List<PhotoItem> photoItems) {
        if (photoItems !=null) {
            viewTarget.hideLoading();
            viewTarget.showPhotosByTag(photoItems, loadMore);
        }
    }
    private void getPhotosItemFromDB(final String query) {
        Log.d(TAG, "getPhotosItemFromDB: "+query);
        new AsyncTask<Object, Object, List<PhotoItem>>() {
            @Override
            protected List<PhotoItem> doInBackground(Object... voids) {
                viewTarget.hideLoading();
                Log.d("PhotoSearchPresenterImp", "" + db.myDao().loadPhotoItemsByTag(query, true).size());
                return db.myDao().loadPhotoItemsByTag(query , true);
            }

            @Override
            protected void onPostExecute(List<PhotoItem> photoItems) {
                onSearchResultOffline(photoItems);
            }
        }.execute();

    }
    private void dbSave(final List<PhotoItem> photosItems) {
        Log.d(TAG, "save result to db: ");
        this.photoItems.clear();
        for (PhotoItem photoItem : photosItems) {
            photoItem.setTag(currentTag);
            this.photoItems.add(photoItem);
        }
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                db.myDao().insertPhotos(photoItems);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean saved) {
                Log.d(TAG, "onPostExecute: " + saved);
            }
        }.execute();
    }

    @Subscribe
    public void  onGroupItemClick(Events.GroupItem item){
        group_id = item.getNsid();
        currentTag = item.getName();
        getPhotosByTag("",group_id);
    }

    @Override
    public void onError(String error) {
        Log.d("PhotoSearchPresenterImp", error);
    }
}
