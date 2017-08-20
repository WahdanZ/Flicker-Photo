package com.example.ahmedwahdan.flicker_photo.helper;

import android.os.Environment;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.io.File;

/**
 * Created by ahmedwahdan on 8/20/17.
 */

public class FileHelper {
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
}
