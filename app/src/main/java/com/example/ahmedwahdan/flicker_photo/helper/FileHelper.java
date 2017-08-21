package com.example.ahmedwahdan.flicker_photo.helper;

import android.os.Environment;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.io.File;

/**
 File Helper used to get  path of cache dictionary to save image on it
 and path in internal storage if user want to save image on it
 */

public class FileHelper {
    public static final String  TAG ="FileHelper";
    public static File getAppCacheDir(){

       String diskCachePath = "FlickrDownload";
        File dir = new File(App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ,diskCachePath );
        if(!dir.exists()){
          boolean b =    dir.mkdir();
        }

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
}
