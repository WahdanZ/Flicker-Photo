package com.example.ahmedwahdan.flicker_photo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class App  extends Application{
    private static App mAppController ;

    private RequestQueue mRequestQueue;
    private Gson mGson;
    private Picasso picasso;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppController =this;
         picasso = new Picasso.Builder(this)
                   .loggingEnabled(true)
                 .indicatorsEnabled(true)
                     .build();
        FileHelper.cachesFiles = FileHelper.getCacheFiles();
        Log.d("App", "FileHelper.cachesFiles:" + FileHelper.cachesFiles);
        Picasso.setSingletonInstance(picasso);
        this.context = this;


    }

    public static synchronized App getInstance() {
        return mAppController;
    }

    public Context getContext() {
        if (context == null)
            context = this;
        return context;
    }

    public Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return this.mGson;
    }



    public <T> void addToRequestQueue(Request<T> req , String  tag) {
        req.setTag(tag);
        req.setShouldCache(false);
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
