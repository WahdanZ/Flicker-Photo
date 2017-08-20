package com.example.ahmedwahdan.flicker_photo.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ahmedwahdan on 8/20/17.
 */

public class PicassoHelper {

    public static Picasso MyPicasso;
    private static final String TAG = "PicassoHelper";

    public static void init(Context context){
        PicassoHelper.MyPicasso = Picasso.with(context);
    }

    public static class DownloadTarget implements Target {
        public String fileName;
        public Runnable onBitmapLoaded;
        public Runnable onBitmapFailed;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.i(TAG, "onBitmapLoaded");
           SearchActivity.protectedFromGarbageCollectorTargets.remove(this);
            try {

                File picFile = new File(FileHelper.getAppExternalDir(), fileName);
                if(picFile.exists()){
                    picFile.delete();
                }
                picFile.createNewFile();
                FileOutputStream out = new FileOutputStream(picFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                onBitmapLoaded.run();
            } catch (IOException e) {
                e.printStackTrace();
                onBitmapFailed.run();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            SearchActivity.protectedFromGarbageCollectorTargets.remove(this);

            onBitmapFailed.run();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

}
