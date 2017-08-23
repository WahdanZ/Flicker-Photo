package com.example.ahmedwahdan.flicker_photo.network;

import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.utils.Const;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class Routs {
    public final static String baseUrl = "https://api.flickr.com/services/rest/?" +
            "format=json&" +
            "per_page="+ Const.MAX_NUMBER_PER_REQUEST+
            "&api_key="+Const.APIKEY+
            "&nojsoncallback=1&" ;

    public final static String photoSearchUrl = baseUrl +"method=flickr.photos.search" + "&tags=";
    public final static String groupSearchUrl = baseUrl + "method=flickr.groups.search"+"&text=";

    public static String getGroupIconUrl (GroupItem groupItem) {
        return " http://farm"+groupItem.getIconfarm()+".staticflickr.com/"+
                groupItem.getIconserver() +
                        "/buddyicons/" + groupItem.getNsid() + ".jpg";

    }
    public static String getPhotoUrl (PhotoItem photoItem){
        return  "https://farm"+photoItem.getFarm()+".staticflickr.com/"+
                photoItem.getServer()+"/"+photoItem.getId()+"_"+photoItem.getSecret()+".jpg";

    }
}
