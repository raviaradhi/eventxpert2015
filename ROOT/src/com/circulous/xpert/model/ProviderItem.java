/**
 * 
 */
package com.circulous.xpert.model;

import java.io.Serializable;



/**
 * @author jagadish
 *
 */
public class ProviderItem  implements Serializable{

	private String itemId;
	private String serviceProviderId;
	private String unitOfCost;
	private double itemCost;
	private double rate;
	private String category;
	private String itemDesc;
	private int providerItemId;
	private boolean delete;
	
	
	
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public int getProviderItemId() {
		return providerItemId;
	}
	public void setProviderItemId(int providerItemId) {
		this.providerItemId = providerItemId;
	}
	public double getItemCost() {
		return itemCost;
	}
	public void setItemCost(double itemCost) {
		this.itemCost = itemCost;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	public String getUnitOfCost() {
		return unitOfCost;
	}
	public void setUnitOfCost(String unitOfCost) {
		this.unitOfCost = unitOfCost;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

	
}
