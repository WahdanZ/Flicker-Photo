package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewer extends AppCompatActivity {

    public static final String IMAGE_KEY = "image_key";
    @BindView(R.id.photo_view)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);
        String url =  getIntent().getStringExtra(IMAGE_KEY);
        final File file = new File(url);
        Log.d("PhotoViewer", url);
        Log.d("PhotoViewer", "file.exists():" + file.exists());
        Picasso.with(this)
                .load(file)
                .into(photoView);

    }
}
