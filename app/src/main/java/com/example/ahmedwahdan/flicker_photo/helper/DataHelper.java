package com.example.ahmedwahdan.flicker_photo.helper;

import android.content.Context;

import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;
import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;

/**
*Class used to store Photo Search Result retrieved from Flickr API
 and save it in Shared Preferences To retrieve it again on no internet connection
 , at first any request call on  on application first launch
 */

public class DataHelper {
    public static final String SP_NAME = "DataHelper";
    private static GSONSharedPreferences gsonSharedPreferences;

    public static void saveGSONdateToSharedPrefrenace(Context context , PhotoSearch photoSearch){
        gsonSharedPreferences(context).saveObject(photoSearch);
    }
    public static PhotoSearch getPhotoSearchFromSharedPrefrence(Context context){
        PhotoSearch photoSearch = null;
        try {
            photoSearch = (PhotoSearch) gsonSharedPreferences(context).getObject(new PhotoSearch());
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        return photoSearch;
    }
    public static GSONSharedPreferences gsonSharedPreferences (Context context){
        if (gsonSharedPreferences == null){
            gsonSharedPreferences = new GSONSharedPreferences(context,SP_NAME);
        return gsonSharedPreferences;
        }
        else
            return gsonSharedPreferences;

    }
}
