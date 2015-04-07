package com.circulous.xpert.event.managedbean;

//import java.io.Serializable;

public class VenueVO {

private String id;
private String imgSrc;
private String heading;
private String desc;
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
public VenueVO(String id, String imgSrc, String heading, String desc) {
	super();
	this.id = id;
	this.imgSrc = imgSrc;
	this.heading = heading;
	this.desc = desc;
}
@Override
public String toString() {
	return "VenueVO [id=" + id + ", imgSrc=" + imgSrc + ", heading=" + heading + ", desc=" + desc + "]";
}



}
