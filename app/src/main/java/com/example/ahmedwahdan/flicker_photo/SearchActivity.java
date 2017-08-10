package com.example.ahmedwahdan.flicker_photo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.request.RequestListener;
import com.example.ahmedwahdan.flicker_photo.request.SearchRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity  implements SearchActivityView {

    List<PhotoItem> photosList ;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    EditText et_search;
    private PhotoAdapter adapter;
    private ProgressDialog progressDialog;
    SearchPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setMessage("Loading...");

        photosList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
          adapter = new PhotoAdapter(photosList,this);
        recyclerView.setAdapter(adapter);
        presenter = new SearchPresenterImp(this,this);


    }

    public void onSearch(View view) {
        String tag = et_search.getText().toString();
        if (!tag.isEmpty()){
          presenter.getPhotoByTag(tag);
        }
    }


    @Override
    public void showLoading() {
        progressDialog.show();

    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();

    }

    @Override
    public void showPhotosByTag(List<PhotoItem> photoItems) {
        adapter.updateList(photoItems);

    }
}
