package com.example.ahmedwahdan.flicker_photo.model;

public class PhotoSearch {
	private String stat;
	private Photos photos;

	public void setStat(String stat){
		this.stat = stat;
	}

	public String getStat(){
		return stat;
	}

	public void setPhotos(Photos photos){
		this.photos = photos;
	}

	public Photos getPhotos(){
		return photos;
	}

	@Override
 	public String toString(){
		return 
			"PhotoSearch{" +
			"stat = '" + stat + '\'' + 
			",photos = '" + photos + '\'' + 
			"}";
		}
}
