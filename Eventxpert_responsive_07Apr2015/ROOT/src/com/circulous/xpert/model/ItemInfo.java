/**
 * 
 */
package com.circulous.xpert.model;

import java.io.Serializable;

/**
 * @author jagadish
 *
 */
public class ItemInfo  implements Serializable{
	
	private int itemId;
	private String itemName;
	private String description;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
