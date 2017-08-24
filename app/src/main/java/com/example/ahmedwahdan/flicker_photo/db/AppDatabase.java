package com.example.ahmedwahdan.flicker_photo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;


/**
 * Created by ahmedwahdan on 9/3/17.
 */
@Database(entities = {GroupItem.class , PhotoItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
