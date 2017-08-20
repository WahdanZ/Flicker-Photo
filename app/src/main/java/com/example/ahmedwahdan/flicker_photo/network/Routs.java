package com.example.ahmedwahdan.flicker_photo.network;

import com.example.ahmedwahdan.flicker_photo.utils.Const;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class Routs {
    public final static String baseUrl = "https://api.flickr.com/services/rest/?" +
            "method=flickr.photos.search&" +
            "format=json&" +
            "per_page="+ Const.MAX_NUMBER_PER_REQUEST+
            "&api_key="+Const.APIKEY+
            "&nojsoncallback=1&" +
            "&tags=";
}
