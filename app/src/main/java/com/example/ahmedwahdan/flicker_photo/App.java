package com.example.ahmedwahdan.flicker_photo;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class App  extends Application{
    private static App mAppController ;

    private RequestQueue mRequestQueue;
    private Gson mGson;
    private Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppController =this;
        OkHttpClient client = new OkHttpClient.Builder().build();
         picasso = new Picasso.Builder(this)
                     .loggingEnabled(true)
                     .downloader(new OkHttp3Downloader(client))
                     .build();

        Picasso.setSingletonInstance(picasso);


    }

    public static synchronized App getInstance() {
        return mAppController;
    }

    public Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return this.mGson;
    }



    public <T> void addToRequestQueue(Request<T> req , String  tag) {
        req.setTag(tag);
        getRequestQueue().add(req);

    }

    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

}
