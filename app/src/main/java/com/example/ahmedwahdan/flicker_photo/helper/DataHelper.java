package com.example.ahmedwahdan.flicker_photo.helper;

import android.content.Context;

import com.example.ahmedwahdan.flicker_photo.model.GroupSearch;
import com.example.ahmedwahdan.flicker_photo.model.PhotoSearch;

/**
*Class used to store Photo Search Result retrieved from Flickr API
 and save it in Shared Preferences To retrieve it again on no internet connection
 , at first any request call on  on application first launch
 */

public class DataHelper {
    public static final String SP_NAME = "DataHelper";
    private static GSONSharedPreferences gsonSharedPreferences;


    public static void saveGOONDateToSharedPreference(Context context , PhotoSearch photoSearch , String tag){
//        PhotoSearch search = getPhotoSearchFromSharedPreference(context,tag);
//        if ( search != null){
//            if (search.getPhotos() != null){
//            int numberOfPage = search.getPhotos().getPage() + Const.MAX_NUMBER_PER_REQUEST;
//
//                search.getPhotos().setPage(numberOfPage);
//                search.getPhotos().getPhoto().addAll(photoSearch.getPhotos().getPhoto());
//
//        gsonSharedPreferences(context).saveObjectWithTag(search,tag);
//            }
//        }
//        else
        gsonSharedPreferences(context).saveObjectWithTag(photoSearch,tag);
    }
    public static void saveGOONDateToSharedPreference(Context context , GroupSearch groupSearch, String tag){
//            GroupSearch search = getGroupSearchFromSharedPreference(context,tag);
//        if (search != null){
//            if (search.getGroups() != null){
//            int numberOfPage =search.getGroups().getPage() + Const.MAX_NUMBER_PER_REQUEST;
//                search.getGroups().setPage(numberOfPage);
//                search.getGroups().getGroup().addAll(groupSearch.getGroups().getGroup());
//            gsonSharedPreferences(context).saveObject(search,tag);
//            }
//        }else
            gsonSharedPreferences(context).saveObjectWithTag(groupSearch,tag);
    }
    public static PhotoSearch getPhotoSearchFromSharedPreference(Context context , String tag){

        return (PhotoSearch) gsonSharedPreferences(context).getObjectWithTag(new PhotoSearch(),tag);
    }
    public static GroupSearch getGroupSearchFromSharedPreference(Context context , String tag){
            return  (GroupSearch) gsonSharedPreferences(context).getObjectWithTag(new GroupSearch(),tag);

    }
     private static GSONSharedPreferences gsonSharedPreferences(Context context){
        if (gsonSharedPreferences == null){
            gsonSharedPreferences = new GSONSharedPreferences(context,SP_NAME);
        return gsonSharedPreferences;
        }
        else
            return gsonSharedPreferences;

    }
}
