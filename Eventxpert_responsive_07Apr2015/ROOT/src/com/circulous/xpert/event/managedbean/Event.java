package com.circulous.xpert.event.managedbean;

import java.io.Serializable;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int eventType;
	private String eventDescription;
	private String color;
	private String eventDesc;

	public Event(int eventType, String eventDescription, String color) {
		this.eventType = eventType;
		this.eventDescription = eventDescription;
		this.color = color;
	}
	
	public Event(int eventType, String eventDescription, String color, String eventDesc) {
		this.eventType = eventType;
		this.eventDescription = eventDescription;
		this.color = color;
		this.eventDesc = eventDesc;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	/**
	 * @return the eventDesc
	 */
	public String getEventDesc() {
		return eventDesc;
	}

	/**
	 * @param eventDesc
	 *            the eventDesc to set
	 */
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	@Override
	public String toString() {
		return "Event [eventType=" + eventType + ", eventDescription=" + eventDescription + "]";
	}

}
