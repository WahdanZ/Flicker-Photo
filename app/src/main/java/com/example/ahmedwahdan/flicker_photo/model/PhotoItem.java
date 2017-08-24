package com.example.ahmedwahdan.flicker_photo.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static com.example.ahmedwahdan.flicker_photo.model.PhotoItem.Offline;
import static com.example.ahmedwahdan.flicker_photo.model.PhotoItem.PHOTO_TAG;
import static com.example.ahmedwahdan.flicker_photo.model.PhotoItem.TABLENAME;


@Entity(tableName = TABLENAME , indices = {@Index(value = {PHOTO_TAG , Offline})})
public class PhotoItem  implements Serializable{
	public static final String TABLENAME = "photo_item";
	public static final String PHOTO_TAG = "photo_tag";
	public static final String Offline = "offline";
	@ColumnInfo(name = PHOTO_TAG)
	private String tag ;
	private String owner;
	private String server;
	private int ispublic;
	private int isfriend;
	private int farm;
	@PrimaryKey
	private String id;
	private String secret;
	private String title;
	private int isfamily;
	private String getURl;
	@ColumnInfo(name = Offline)
	private boolean isOffline;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setServer(String server){
		this.server = server;
	}

	public String getServer(){
		return server;
	}

	public void setIspublic(int ispublic){
		this.ispublic = ispublic;
	}

	public int getIspublic(){
		return ispublic;
	}

	public void setIsfriend(int isfriend){
		this.isfriend = isfriend;
	}

	public int getIsfriend(){
		return isfriend;
	}

	public void setFarm(int farm){
		this.farm = farm;
	}

	public int getFarm(){
		return farm;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSecret(String secret){
		this.secret = secret;
	}

	public String getSecret(){
		return secret;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIsfamily(int isfamily){
		this.isfamily = isfamily;
	}

	public int getIsfamily(){
		return isfamily;
	}
	public String getGetURl(){
		return  "https://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+".jpg";
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isOffline() {
		return isOffline;
	}

	public void setOffline(boolean offline) {
		isOffline = offline;
	}

	public void setGetURl(String getURl) {
		this.getURl = getURl;
	}

	@Override
 	public String toString(){
		return 
			"PhotoItem{" + 
			"owner = '" + owner + '\'' + 
			",server = '" + server + '\'' + 
			",ispublic = '" + ispublic + '\'' + 
			",isfriend = '" + isfriend + '\'' + 
			",farm = '" + farm + '\'' + 
			",id = '" + id + '\'' + 
			",secret = '" + secret + '\'' + 
			",title = '" + title + '\'' + 
			",isfamily = '" + isfamily + '\'' + 
			"}";
		}
}
