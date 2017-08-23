package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.util.Log;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.util.List;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by ahmedwahdan on 8/22/17.
 */

public class Events {

    public static class PhotoEvents {
        List<PhotoItem> photoItems;
        boolean isLoading;

        public PhotoEvents(List<PhotoItem> photoItems, boolean isLoading) {
            Log.d(TAG, "Events: " + photoItems.size());
            this.photoItems = photoItems;
            this.isLoading = isLoading;
        }

        public List<PhotoItem> getPhotoItems() {
            return photoItems;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }

    public static class Query{

      public static class Photo{
           String query;

        public Photo(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
        }

        public static class Group {
            String query;
            public Group(String query) {
                this.query = query;
            }
            public String getQuery() {
                return query;
            }
        }

    }
    public static class QueryPhotoPage {
        int page;

        public QueryPhotoPage(int page) {
            this.page = page;
        }

        public int getPage() {
            return page;
        }
    }
    public static class QueryGroupPage {
        int page;

        public QueryGroupPage(int page) {
            this.page = page;
        }

        public int getPage() {
            return page;
        }
    }

    public static class photoItemPath{
        String photoPath;

        public photoItemPath(String photoPath) {
            this.photoPath = photoPath;
        }

        public String getPhotoPath() {
            return photoPath;
        }
    }

    public static class GroupItem {
        String nsid;
        String name;
        public GroupItem(String nsid ,String name) {
            this.nsid = nsid;
            this.name = name;
        }
        public String getNsid() {
            return nsid;
        }

        public String getName() {
            return name;
        }
    }
}
