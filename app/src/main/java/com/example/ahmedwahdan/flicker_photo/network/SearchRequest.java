package com.example.ahmedwahdan.flicker_photo.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.network.model.PhotoSearch;
import com.google.gson.Gson;

import org.json.JSONObject;

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        PhotoSearch res = mGson.fromJson(String.valueOf(response), PhotoSearch.class);
                        if (res != null)
                            listener.onSearchResult(res.getPhotos().getPhoto());
                        else
                            listener.onError("Failed to fetch data! ");
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
