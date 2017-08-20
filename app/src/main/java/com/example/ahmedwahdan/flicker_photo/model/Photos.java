package com.example.ahmedwahdan.flicker_photo.model;

import java.util.List;

public class Photos{
	private int perpage;
	private String total;
	private int pages;
	private List<PhotoItem> photo;
	private int page;

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

	public void setPhoto(List<PhotoItem> photo){
		this.photo = photo;
	}

	public List<PhotoItem> getPhoto(){
		return photo;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	@Override
 	public String toString(){
		return 
			"Photos{" + 
			"perpage = '" + perpage + '\'' + 
			",total = '" + total + '\'' + 
			",pages = '" + pages + '\'' + 
			",photo = '" + photo + '\'' + 
			",page = '" + page + '\'' + 
			"}";
		}
}