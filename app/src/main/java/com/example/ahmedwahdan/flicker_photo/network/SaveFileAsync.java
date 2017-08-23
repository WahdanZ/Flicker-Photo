package com.example.ahmedwahdan.flicker_photo.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SaveFileAsync extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "Downloader";
    private final RequestListener.imageDownloadListener listener;
    private  String fileName;
    private  Bitmap bitmap;

    public SaveFileAsync(Bitmap bitmap , String  fileName , RequestListener.imageDownloadListener listener) {
        this.fileName = fileName;
        this.bitmap = bitmap;
        this.listener = listener;
    }


    @Override
    protected Boolean doInBackground(Void... strings) {
        final File myImageFile = new File(FileHelper.getDefaultSaveFile(), fileName);
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
