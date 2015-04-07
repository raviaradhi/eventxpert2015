package com.circulous.xpert.model;

import java.io.Serializable;

public class Image  implements Serializable{
	
	private String path;
	private String imageName;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	

}
