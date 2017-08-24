package com.example.ahmedwahdan.flicker_photo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;

import java.util.List;

/**
 * Created by ahmedwahdan on 9/3/17.
 */

@Dao
public interface MyDao {
    //***** Group Search Queries ********/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(GroupItem... groupItems);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroups(List<GroupItem> groupItems);
    @Query("SELECT * FROM groupItem")
    List<GroupItem> loadAllGroupItems();
    @Query("SELECT * FROM "+GroupItem.TABLENAME +" WHERE "+ GroupItem.GroupSearch+" LIKE :search")
    List<GroupItem> loadGroupItemsByTag(String search);

    //***** Photo Search Queries ********/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(PhotoItem... photoItems);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhotos(List<PhotoItem> photoItems);
    @Query("SELECT * FROM " + PhotoItem.TABLENAME)
    List<PhotoItem> loadAllPhotoItems();
    @Query("SELECT * FROM "+PhotoItem.TABLENAME +" WHERE "+ PhotoItem.PHOTO_TAG+" LIKE :search AND "+PhotoItem.Offline + " = :offline")
    List<PhotoItem> loadPhotoItemsByTag(String search, boolean offline);
    @Update
    void updatePhotoItem(PhotoItem b);
}
