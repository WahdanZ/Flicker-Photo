package com.example.ahmedwahdan.flicker_photo;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ahmedwahdan on 8/11/17.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return   (int) (dpWidth / 180);
    }
}
