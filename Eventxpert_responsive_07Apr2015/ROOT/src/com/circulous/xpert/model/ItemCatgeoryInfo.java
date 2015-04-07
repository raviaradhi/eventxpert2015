/**
 * 
 */
package com.circulous.xpert.model;

import java.io.Serializable;

/**
 * @author jagadish
 *
 */
public class ItemCatgeoryInfo  implements Serializable{
	
	private int itemId;
	private String itemName;
	private int itemCategoryId;
	private String categoryName;
	private String description;
	
	public int getItemCategoryId() {
		return itemCategoryId;
	}
	public void setItemCategoryId(int itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
	
	

}
