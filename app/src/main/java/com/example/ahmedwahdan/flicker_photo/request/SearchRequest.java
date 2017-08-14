package com.example.ahmedwahdan.flicker_photo.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.model.searchRes;
import com.google.gson.Gson;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class SearchRequest {
    private static final String TAG = SearchRequest.class.getSimpleName();
    private static App mAppController = App.getInstance();
    private static Gson mGson = mAppController.getGson();

    public static void index(String searchTag, String tag, int page, final RequestListener.searchListener listener) {
        mAppController.cancelPendingRequests(tag);
        String url = Routs.baseUrl + searchTag + "&page=" + page;
        Log.d(TAG, "URL: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String >() {

                    @Override
                    public void onResponse(String response) {
                        searchRes res = mGson.fromJson(response, searchRes.class);
                        if (res != null)
                        listener.onSearchResult(res.getPhotos().getPhoto());


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });
        mAppController.addToRequestQueue(request,tag);

    }

}
