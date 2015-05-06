package com.circulous.xpert.event.managedbean;

public class ServiceVO {

private String id;
private String imgSrc;
private String heading;
private String desc;

private boolean selected =false;

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getImgSrc() {
	return imgSrc;
}
public void setImgSrc(String imgSrc) {
	this.imgSrc = imgSrc;
}
public String getHeading() {
	return heading;
}
public void setHeading(String heading) {
	this.heading = heading;
}
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}


public boolean isSelected() {
	return selected;
}
public void setSelected(boolean selected) {
	this.selected = selected;
}
public ServiceVO(String id, String imgSrc, String heading, String desc) {
	super();
	this.id = id;
	this.imgSrc = imgSrc;
	this.heading = heading;
	this.desc = desc;
}
@Override
public String toString() {
	return "ServiceVO [id=" + id + ", imgSrc=" + imgSrc + ", heading="
			+ heading + ", desc=" + desc + ", selected=" + selected + "]";
}






}
