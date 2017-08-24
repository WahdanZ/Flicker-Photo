package com.example.ahmedwahdan.flicker_photo.ui.search;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.GlobalBus;
import com.example.ahmedwahdan.flicker_photo.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchPresenterImp implements SearchPresenter {

    private static String TAG = "SearchPresenter";

    private MVPViewer.SearchActivityView searchView;

    public SearchPresenterImp(MVPViewer.SearchActivityView searchView){
        this.searchView = searchView;
    }


    protected boolean checkInternetConnection() {
        Boolean isConnect = NetworkUtils.isInternetConnected(App.getInstance().getContext());
        if (!isConnect && searchView !=null)
            searchView.showErrorMessage("No internet connection!");
        return isConnect;
    }

    @Override
    public void onSubmitSearch(String tagSearch) {
        GlobalBus.getBus().post(new Events.Query.Group(tagSearch));
        GlobalBus.getBus().post(new Events.Query.Photo(tagSearch));

    }
    @Override
    public void onResume() {
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        GlobalBus.getBus().unregister(this);

    }

    @Subscribe
    public void onPhotoItemClick(Events.photoItemPath itemPath){
        searchView.openPhotoViewer(itemPath.getPhotoPath());
    }
    @Subscribe
    public void  onGroupItemClick(Events.GroupItem item){
        searchView.onNavigatePhotoSearch(item.getName());
    }



}
