package com.expedite.apps.kumkum.model;

public class StudListitemDetails {

	private String imagepath;
	private String name;
	private int image;
	private int studid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getImagePathName() {
		return imagepath;
	}

	public void setImagePath(String imagepath) {
		this.imagepath = imagepath;
	}

	public Integer getstudid() {
		return studid;
	}

	public void setstudid(Integer studid) {
		this.studid = studid;
	}

}
