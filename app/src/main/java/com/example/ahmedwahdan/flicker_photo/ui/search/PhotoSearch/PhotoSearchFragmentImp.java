package com.example.ahmedwahdan.flicker_photo.ui.search.PhotoSearch;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.ui.helper.EndlessRecyclerViewScrollListener;
import com.example.ahmedwahdan.flicker_photo.ui.search.MVPViewer;
import com.example.ahmedwahdan.flicker_photo.utils.ScreenUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link}
 * interface.
 */
public class PhotoSearchFragmentImp extends Fragment implements MVPViewer.PhotoSearchFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "PhotoSearchFragmentImp";
    // TODO: Customize parameters
    private int mColumnCount = 3;
    @BindView(R.id.rv_photo)
    RecyclerView photoRecyclerView;
    @BindView(R.id.search_photo_progress)
    ProgressBar progressBar;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private PhotoAdapter adapter;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView.OnScrollListener scrollListener;
    private List<PhotoItem> photosList;
    private String currentQuery;
    private boolean isLoadingMore;
    private int currentPage;
    private PhotoSearchPresenterImp presenter;
    private int numColumns;
    private String PHOTO_ITEMS = "photo_fragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoSearchFragmentImp() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PhotoSearchFragmentImp newInstance() {
        PhotoSearchFragmentImp fragment = new PhotoSearchFragmentImp();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            photosList = (List<PhotoItem>) savedInstanceState.getSerializable(PHOTO_ITEMS);
        }

    }
    private void init() {
        presenter = new PhotoSearchPresenterImp(this);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photosearch, container, false);
        ButterKnife.bind(this, view);
        init();

        recyclerSetup();
        return view;
    }
    private void recyclerSetup() {
        getGridLayoutManager();
        photoRecyclerView.setLayoutManager(mGridLayoutManager);
        scrollListener =  getScrollListener();
        photoRecyclerView.addOnScrollListener(scrollListener);
        photoRecyclerView.setHasFixedSize(true);
        photoRecyclerView.setItemViewCacheSize(20);
        photoRecyclerView.setDrawingCacheEnabled(true);
        photoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        photoRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }
    public  void getGridLayoutManager(){
        // get number of Columns should depend on screen size using ScreenUtils Class
        // it get screen width and  divided it by 180 as 180 fixed image width
         numColumns = ScreenUtils.calculateNoOfColumns(getContext());
        Log.d(TAG, "numColumns:" + numColumns);

        mGridLayoutManager  =  new GridLayoutManager(getActivity(),
                numColumns);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case PhotoAdapter.VIEW_DIVIDE:
                        return mGridLayoutManager.getSpanCount();
                    case PhotoAdapter.VIEW_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPhotosByTag(List<PhotoItem> photoItems, boolean isLoadingMore) {
        emptyView.setVisibility(View.INVISIBLE);
        if (adapter == null) {
            Log.d(TAG, "showPhotosByTag: No Adabter");
            // create  instance of recyclerView Adapter
            adapter = new PhotoAdapter( photoItems, getContext() , presenter);
            photoRecyclerView.setAdapter(adapter);
        }
        else {
            Log.i(TAG, "showPhotoResult : adapter is exist load more or get new search result");
            if (isLoadingMore) {
                Log.d(TAG, "showPhotoResult : Load more photo");
                adapter.loadMorePhoto(photoItems);
            } else {
                Log.d(TAG, "showPhotoResult : Display new search result " + photoItems);
                photoRecyclerView.removeOnScrollListener(scrollListener);
                photoRecyclerView.addOnScrollListener(getScrollListener());
                photosList = photoItems;
                adapter.updatePhotoList(photoItems);
            }
            //  photoItemPicassoDownloader(photoItems);


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
        Log.d(TAG, message);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getGridLayoutManager();
        photoRecyclerView.setLayoutManager(mGridLayoutManager);
        photoRecyclerView.addOnScrollListener(getScrollListener());

    }

    private RecyclerView.OnScrollListener getScrollListener() {

        return new EndlessRecyclerViewScrollListener(mGridLayoutManager) {

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
                   presenter.loadMorePhoto(currentQuery, page);
                }
            }
        };
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListPhotoSearchListener) {
//            mListener = (OnListPhotoSearchListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTO_ITEMS, (Serializable) photosList);

    }
}
