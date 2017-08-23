package com.example.ahmedwahdan.flicker_photo.ui.search.GroupSearch;

import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.GlobalBus;
import com.example.ahmedwahdan.flicker_photo.helper.DataHelper;
import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.model.GroupSearch;
import com.example.ahmedwahdan.flicker_photo.network.GroupSearchRequest;
import com.example.ahmedwahdan.flicker_photo.network.RequestListener;
import com.example.ahmedwahdan.flicker_photo.ui.search.Events;
import com.example.ahmedwahdan.flicker_photo.ui.search.MVPViewer;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenter;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenterImp;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ahmedwahdan on 8/23/17.
 */

public class GroupSearchPresenterImp extends SearchPresenterImp implements SearchPresenter.GroupSearchPresenter , RequestListener.groupSearchListener {
    private MVPViewer.GroupSearchFragment  fragment;
    MVPViewer.GroupSearchFragment viewTarget;

    private boolean loadMore;
    private List<GroupItem> groupItems;
    private GroupSearch groupSearch;
    private String TAG  ="GroupSearchPresenter";
    private String currentTag;
    private boolean isOffline;

    public GroupSearchPresenterImp(MVPViewer.GroupSearchFragment  photoSearchFragment) {
        super((MVPViewer.SearchActivityView) photoSearchFragment.getContext());
        this.viewTarget = photoSearchFragment;
    }

    @Override
    public void getGroupByTag(String tag) {
        if (!checkInternetConnection()) {
            groupSearch = DataHelper.getGroupSearchFromSharedPreference(App.getInstance().getContext(),tag);
            if (groupSearch != null)
                onSearchResultOffline(groupSearch);
            Log.d("GroupSearchPresenterImp", "groupSearches.size():" + groupSearch.getGroups().getGroup().size());
        }else {
        loadMore = false;
        viewTarget.setCurrentQuery(tag);
        viewTarget.showLoading();
        GroupSearchRequest.index(tag, TAG, 1, this);
        }
    }

    @Override
    public void loadMoreGroups(String query , int page) {
        if (!checkInternetConnection()) {
            return;
        }
        loadMore = true;
        viewTarget.setCurrentQuery(query);
        viewTarget.showLoading();
        GroupSearchRequest.index(query, TAG, page, this);

    }

    @Override
    public void onGroupItemClick(String nsid ,String groupName) {
        GlobalBus.getBus().post(new Events.GroupItem(nsid,groupName));
    }


    public void onResume()
    {
        GlobalBus.getBus().register(this);
    }


    public void onPause()
    {
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void onSubmitSearch(Events.Query.Group tagSearch) {
        currentTag = tagSearch.getQuery();
        viewTarget.setCurrentQuery(tagSearch.getQuery());
        getGroupByTag(tagSearch.getQuery());
    }

    @Override
    public void onSearchResult(GroupSearch groupSearch) {
        if (groupSearch != null) {
        groupItems = groupSearch.getGroups().getGroup();
            DataHelper.saveGOONDateToSharedPreference(App.getInstance().getContext(),groupSearch,currentTag);
        viewTarget.hideLoading();
        viewTarget.showGroupByTag(groupItems , loadMore);
        }
    }
    public void onSearchResultOffline(GroupSearch groupSearch) {
        if (groupSearch != null) {
            groupItems = groupSearch.getGroups().getGroup();
            viewTarget.hideLoading();
            viewTarget.showGroupByTag(groupItems , loadMore);
        }
    }
    @Override
    public void onError(String error) {

    }
}
