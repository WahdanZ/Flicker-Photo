package com.example.ahmedwahdan.flicker_photo.model;

import com.google.gson.annotations.SerializedName;

public class GroupItem {

	@SerializedName("nsid")
	private String nsid;

	@SerializedName("iconfarm")
	private int iconfarm;

	@SerializedName("iconserver")
	private String iconserver;

	@SerializedName("members")
	private String members;

	@SerializedName("name")
	private String name;

	@SerializedName("pool_count")
	private String poolCount;

	@SerializedName("privacy")
	private String privacy;

	@SerializedName("topic_count")
	private String topicCount;

	@SerializedName("eighteenplus")
	private int eighteenplus;

	public GroupItem() {
	}
	public GroupItem(String nsid, int iconfarm, String iconserver, String members, String name, String poolCount, String privacy, String topicCount, int eighteenplus) {
		this.nsid = nsid;
		this.iconfarm = iconfarm;
		this.iconserver = iconserver;
		this.members = members;
		this.name = name;
		this.poolCount = poolCount;
		this.privacy = privacy;
		this.topicCount = topicCount;
		this.eighteenplus = eighteenplus;
	}

	public void setNsid(String nsid){
		this.nsid = nsid;
	}

	public String getNsid(){
		return nsid;
	}

	public void setIconfarm(int iconfarm){
		this.iconfarm = iconfarm;
	}

	public int getIconfarm(){
		return iconfarm;
	}

	public void setIconserver(String iconserver){
		this.iconserver = iconserver;
	}

	public String getIconserver(){
		return iconserver;
	}

	public void setMembers(String members){
		this.members = members;
	}

	public String getMembers(){
		return members;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPoolCount(String poolCount){
		this.poolCount = poolCount;
	}

	public String getPoolCount(){
		return poolCount;
	}

	public void setPrivacy(String privacy){
		this.privacy = privacy;
	}

	public String getPrivacy(){
		return privacy;
	}

	public void setTopicCount(String topicCount){
		this.topicCount = topicCount;
	}

	public String getTopicCount(){
		return topicCount;
	}

	public void setEighteenplus(int eighteenplus){
		this.eighteenplus = eighteenplus;
	}

	public int getEighteenplus(){
		return eighteenplus;
	}

	@Override
 	public String toString(){
		return 
			"GroupItem{" + 
			"nsid = '" + nsid + '\'' + 
			",iconfarm = '" + iconfarm + '\'' + 
			",iconserver = '" + iconserver + '\'' + 
			",members = '" + members + '\'' + 
			",name = '" + name + '\'' + 
			",pool_count = '" + poolCount + '\'' + 
			",privacy = '" + privacy + '\'' + 
			",topic_count = '" + topicCount + '\'' + 
			",eighteenplus = '" + eighteenplus + '\'' + 
			"}";
		}
}