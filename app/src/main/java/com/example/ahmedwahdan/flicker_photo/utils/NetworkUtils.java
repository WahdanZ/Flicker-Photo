package com.example.ahmedwahdan.flicker_photo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ahmedwahdan on 8/19/17.
 */

public class NetworkUtils {


     public static boolean isInternetConnected (Context context) {
         ConnectivityManager connectivityMgr = (ConnectivityManager) context
                 .getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetwork = connectivityMgr.getActiveNetworkInfo();
         // Check if wifi or mobile network is available or not. If any of them is
         // available or connected then it will return true, otherwise false;
         if (activeNetwork != null) { // connected to the internet
             if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                 // connected to wifi
               //  Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                 return true;
             } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                 // connected to the mobile provider's data plan
             //    Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                 return true;
             }
         } else {
             return false;
             // not connected to the internet
         }
         return false;
     }
}
