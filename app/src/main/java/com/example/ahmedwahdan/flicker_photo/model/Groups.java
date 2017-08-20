package com.example.ahmedwahdan.flicker_photo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Groups{

	@SerializedName("perpage")
	private int perpage;

	@SerializedName("total")
	private String total;

	@SerializedName("pages")
	private int pages;

	@SerializedName("page")
	private int page;

	@SerializedName("group")
	private List<GroupItem> group;

	public void setPerpage(int perpage){
		this.perpage = perpage;
	}

	public int getPerpage(){
		return perpage;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
	}

	public void setPages(int pages){
		this.pages = pages;
	}

	public int getPages(){
		return pages;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setGroup(List<GroupItem> group){
		this.group = group;
	}

	public List<GroupItem> getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"Groups{" + 
			"perpage = '" + perpage + '\'' + 
			",total = '" + total + '\'' + 
			",pages = '" + pages + '\'' + 
			",page = '" + page + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}