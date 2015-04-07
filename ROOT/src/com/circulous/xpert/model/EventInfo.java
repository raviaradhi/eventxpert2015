/**
 * 
 */
package com.circulous.xpert.model;

import java.io.Serializable;

/**
 * @author jagadish
 *
 */
public class EventInfo  implements Serializable{
	
	private String eventType;
	private int eventId;
	private String eventDescription;
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	

}
