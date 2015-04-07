/**
 * 
 */
package com.circulous.xpert.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jagadish
 *
 */
public class EventScheduleInfo  implements Serializable{
	
	private String eventDate;
	private String packagepName;
	private int packageCost;
	private String location;
	private int clientContact;
	
	
	
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getPackagepName() {
		return packagepName;
	}
	public void setPackagepName(String packagepName) {
		this.packagepName = packagepName;
	}
	public int getPackageCost() {
		return packageCost;
	}
	public void setPackageCost(int packageCost) {
		this.packageCost = packageCost;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getClientContact() {
		return clientContact;
	}
	public void setClientContact(int clientContact) {
		this.clientContact = clientContact;
	}
	

}
