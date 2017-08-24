package com.example.ahmedwahdan.flicker_photo.network;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.helper.SaveFileAsync;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

/**
 * Created by ahmedwahdan on 8/20/17.
 */

public class ImageRequestDownloader {
    private static final String TAG = ImageRequestDownloader.class.getSimpleName();
    private static App mAppController = App.getInstance();
    public static void index(String imageUrl , final PhotoItem item  , final RequestListener.imageDownloadListener listener) {
        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                imageUrl, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        new SaveFileAsync(bitmap,item,listener).execute();
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.FIT_XY,
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onBitmapFailed();

                    }
                }
        );

        mAppController.addToRequestQueue(imageRequest,TAG);

    }
    }
