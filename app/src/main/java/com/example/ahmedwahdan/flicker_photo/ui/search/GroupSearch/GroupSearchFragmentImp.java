package com.example.ahmedwahdan.flicker_photo.ui.search.GroupSearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.ui.helper.EndlessRecyclerViewScrollListener;
import com.example.ahmedwahdan.flicker_photo.ui.search.MVPViewer;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GroupSearchFragmentImp extends Fragment  implements MVPViewer.GroupSearchFragment{
    @BindView(R.id.rv_group)
    RecyclerView photoRecyclerView;
    @BindView(R.id.search_group_progress)
    ProgressBar progressBar;
    private SearchPresenter.GroupSearchPresenter presenter;
    private String currentQuery;
    private LinearLayoutManager linearLayout;
    private RecyclerView.OnScrollListener scrollListener;
    private int currentPage;
    private boolean isLoadingMore;
    private String TAG ="GroupSearchFragmentImp" ;
    private GroupAdapter adapter;

    public GroupSearchFragmentImp() {
        // Required empty public constructor
        init();
    }

    private void init() {
       presenter = new GroupSearchPresenterImp(this);


    }
    public static GroupSearchFragmentImp newInstance(String param1, String param2) {
        GroupSearchFragmentImp fragment = new GroupSearchFragmentImp();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_search, container, false);
        ButterKnife.bind(this, view);
        recyclerSetup();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        if (currentQuery != null)
            presenter.getGroupByTag(currentQuery);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
    private void recyclerSetup() {
        linearLayout = new LinearLayoutManager(getContext());
        photoRecyclerView.setLayoutManager(linearLayout);
        scrollListener =  getScrollListener();
        photoRecyclerView.addOnScrollListener(scrollListener);
        photoRecyclerView.setHasFixedSize(true);
        photoRecyclerView.setItemViewCacheSize(20);
        photoRecyclerView.setDrawingCacheEnabled(true);
        photoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        photoRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    public void showGroupByTag(List<GroupItem> groupItems, boolean isLoadingMore) {
        if (adapter == null) {
            Log.d(TAG, "showPhotosByTag: No Adabter");
            // create  instance of recyclerView Adapter
            adapter = new GroupAdapter( groupItems, getContext() , presenter);
            photoRecyclerView.setAdapter(adapter);
        }
        else {
            if (isLoadingMore) {
                Log.d(TAG, "showPhotoResult : Load more photo");
                adapter.loadMorePhoto(groupItems);
            } else {
                Log.d(TAG, "showPhotoResult : Display new search result " + groupItems.size());
                photoRecyclerView.removeOnScrollListener(scrollListener);
                photoRecyclerView.addOnScrollListener(getScrollListener());
                adapter.updatePhotoList(groupItems);
            }
        }

    }
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setCurrentQuery(String query) {
        currentQuery = query;
    }


    @Override
    public void showErrorMessage(String message) {

    }


    private RecyclerView.OnScrollListener getScrollListener() {

        return new EndlessRecyclerViewScrollListener(linearLayout) {

            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "onLoadMore: ");
                if (currentQuery != null){
                    Log.d(TAG, "onLoadMore: " +page++ + " " +currentQuery);
                    currentPage = page;
                    isLoadingMore = true;
                    presenter.loadMoreGroups(currentQuery, page);
                }
            }
        };
    }


}
