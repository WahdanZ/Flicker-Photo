package com.example.ahmedwahdan.flicker_photo.helper;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.ahmedwahdan.flicker_photo.App;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.network.RequestListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SaveFileAsync extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "Downloader";
    private RequestListener.imageDownloadListener listener;
    private  PhotoItem photoItem;
    private  String fileName;
    private  Bitmap bitmap;

    public SaveFileAsync(Bitmap bitmap , PhotoItem photoItem , RequestListener.imageDownloadListener listener) {
        this.photoItem = photoItem;
        this.bitmap = bitmap;
        this.listener = listener;
        fileName = FileHelper.getFlickrFilename(photoItem);
    }


    @Override
    protected Boolean doInBackground(Void... strings) {
        final File myImageFile = new File(FileHelper.getDefaultSaveDir(), fileName);
        // Create image file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myImageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (IOException e) {
            e.printStackTrace();
            listener.onBitmapFailed();
            return false;
        } finally {
            try {
                fos.close();
                FileHelper.cachesFiles.add(fileName);
                photoItem.setOffline(true);
                App.getAppDatabase().myDao().updatePhotoItem(photoItem);
                listener.onBitmapLoaded(bitmap);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                listener.onBitmapFailed();
                return false;
            }
        }
    }
}
