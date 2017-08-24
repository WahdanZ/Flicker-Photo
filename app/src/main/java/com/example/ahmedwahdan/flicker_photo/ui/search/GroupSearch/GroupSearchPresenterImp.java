package com.example.ahmedwahdan.flicker_photo.ui.search.GroupSearch;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.GlobalBus;
import com.example.ahmedwahdan.flicker_photo.db.AppDatabase;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ahmedwahdan on 8/23/17.
 */

public class GroupSearchPresenterImp extends SearchPresenterImp implements SearchPresenter.GroupSearchPresenter , RequestListener.groupSearchListener {
    private  AppDatabase db;
    private MVPViewer.GroupSearchFragment  fragment;
    MVPViewer.GroupSearchFragment viewTarget;

    private boolean loadMore;
    private List<GroupItem> groupItems;
    private ArrayList<GroupItem> groupsItems;
    private GroupSearch groupSearch;
    private String TAG  ="GroupSearchPresenter";
    private String currentTag;
    private boolean isOffline;

    public GroupSearchPresenterImp(MVPViewer.GroupSearchFragment  photoSearchFragment) {
        super((MVPViewer.SearchActivityView) photoSearchFragment.getContext());
        this.viewTarget = photoSearchFragment;
        db = App.getAppDatabase();
        groupsItems = new ArrayList<>();
    }

    @Override
    public void getGroupByTag(String tag) {
        getGroupItemFromDB(tag);
        if (!checkInternetConnection()) {
            return;
        }else {
        loadMore = false;
        viewTarget.setCurrentQuery(tag);
        viewTarget.showLoading();
        GroupSearchRequest.index(tag, TAG, 1, this);
        }
    }

    @Override
    public void loadMoreGroups(final String query , int page) {
        if (!checkInternetConnection()) {
            getGroupItemFromDB(query);
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
            dbSave(groupItems);
            viewTarget.hideLoading();
             viewTarget.showGroupByTag(groupItems , loadMore);
        }
    }
    private void getGroupItemFromDB(final String query) {
        Log.d(TAG, "getGroupItemFromDB: " + query);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("GroupSearchPresenter", "" + db.myDao().loadGroupItemsByTag(query).size());
                onSearchResultOffline(db.myDao().loadGroupItemsByTag(query));
                return null;
            }
        }.execute();

    }
    public void dbSave(final List<GroupItem> groupItems) {
        groupsItems.clear();
        for (GroupItem groupItem : groupItems) {
            groupItem.setGroupSearch(currentTag);
            groupsItems.add(groupItem);

        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
               db.myDao().insertGroups(groupsItems);
                return null;
            }
        }.execute();
    }

    public void onSearchResultOffline(List<GroupItem> groupItems) {
        if (groupItems != null) {
            viewTarget.hideLoading();
            viewTarget.showGroupByTag(groupItems , loadMore);
        }
    }
    @Override
    public void onError(String error) {
        Log.d("GroupSearchPresenterImp", error);

    }
}
