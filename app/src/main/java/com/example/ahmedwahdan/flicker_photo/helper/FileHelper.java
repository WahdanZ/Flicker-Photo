package com.example.ahmedwahdan.flicker_photo.helper;

import android.os.Environment;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.io.File;
import java.util.ArrayList;

/**
 File Helper used to get  path of cache dictionary to save image on it
 and path in internal storage if user want to save image on it
 */

public class FileHelper {
    public static final String  TAG ="FileHelper";
    public static ArrayList<String> cachesFiles ;
    private static String CACHE_FOLDER_PATH;

    public static File getAppCacheDir(){

       String diskCachePath = "FlickrDownload";
        File dir = new File(App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ,diskCachePath );
        if(!dir.exists()){
          boolean b =    dir.mkdir();
        }
        else
            CACHE_FOLDER_PATH = dir.getAbsolutePath();

        return dir;
    }
    public static File getAppExternalDir(){
        File dir = new File(Environment.getExternalStorageDirectory(), "FlickrDownload");
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public static File getImagesDir(){
        File dir = new File(getAppExternalDir(), "pictures");
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }
    public static String getFlickrFilename(PhotoItem photoItem){
        return photoItem.getId()+"_"+photoItem.getSecret()+".jpg";
    }

    public static File getDefaultSaveFile() {
        return getAppCacheDir();
    }
    public static ArrayList<String> getCacheFiles(){
        ArrayList<String> result = new ArrayList<String>(); //ArrayList cause you don't know how many files there is
        File folder = new File(getDefaultSaveFile().getAbsolutePath()); //This is just to cast to a File type since you pass it as a String
        File[] filesInFolder = folder.listFiles(); // This returns all the folders and files in your path
        for (File file : filesInFolder) { //For each of the entries do:
            if (!file.isDirectory()) { //check that it's not a dir
                result.add(file.getName()); //push the filename as a string
            }
        }

        return result;
    }

    public static boolean isFileFound(PhotoItem item) {
       return cachesFiles.contains(getFlickrFilename(item));
    }

    public static String getFlickrFilePath(PhotoItem item) {
      return   CACHE_FOLDER_PATH+"/"+getFlickrFilename(item);
    }
}
