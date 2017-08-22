package com.example.ahmedwahdan.flicker_photo.network;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ahmedwahdan on 8/20/17.
 */

public class ImageRequestDownloader {
    private static final String TAG = ImageRequestDownloader.class.getSimpleName();
    private static App mAppController = App.getInstance();
    public static void index(String imageUrl , final String fileName  , final RequestListener.imageDownloadListener listener) {
        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                imageUrl, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap bitmap) {
                    
                        final File myImageFile = new File(FileHelper.getDefaultSaveFile(), fileName);
                        // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            Log.i(TAG, "onResponse: file saved" );
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onBitmapFailed();

                            return;
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                listener.onBitmapFailed();
                                return;
                            }
                        }
                        FileHelper.cachesFiles.add(myImageFile.getAbsolutePath());
                        listener.onBitmapLoaded(bitmap);
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
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
