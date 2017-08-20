package com.example.ahmedwahdan.flicker_photo.model;

import com.google.gson.annotations.SerializedName;

public class GroupSearch{

	@SerializedName("stat")
	private String stat;

	@SerializedName("groups")
	private Groups groups;

	public void setStat(String stat){
		this.stat = stat;
	}

	public String getStat(){
		return stat;
	}

	public void setGroups(Groups groups){
		this.groups = groups;
	}

	public Groups getGroups(){
		return groups;
	}

	@Override
 	public String toString(){
		return 
			"GroupSearch{" + 
			"stat = '" + stat + '\'' + 
			",groups = '" + groups + '\'' + 
			"}";
		}
}