/**
 * 
 */
package com.circulous.xpert.model;

import com.circulous.xpert.jpa.entities.EventTypeMaster;
import java.math.BigDecimal;
import java.util.Date;

import com.circulous.xpert.jpa.entities.ServiceProviderInfo;
import com.circulous.xpert.jpa.entities.UnitCosts;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jagadish
 *
 */
public class PackageDetails  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int packageId;
	private String packageName;
	//private String eventType;
        private EventTypeMaster eventType;
	private String description;
	private BigDecimal cost;
	private UnitCosts unitOfCost;
	private ServiceProviderInfo serviceProviderInfo;
        private List<Image> imagesPreview = new ArrayList<Image>();

    public List<Image> getImagesPreview() {
        return imagesPreview;
    }

    public void setImagesPreview(List<Image> imagesPreview) {
        this.imagesPreview = imagesPreview;
    }
        
        
	public ServiceProviderInfo getServiceProviderInfo() {
		return serviceProviderInfo;
	}



	public void setServiceProviderInfo(ServiceProviderInfo serviceProviderInfo) {
		this.serviceProviderInfo = serviceProviderInfo;
	}



	private boolean delete;
	private Date pkgFromDt;
	private Date pkgToDt;
	private String strFrom;
	private String strTo;
	
	public boolean isDelete() {
		return delete;
	}



	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public PackageDetails() {
		
	}


	public PackageDetails(int packageId, String packageName, EventTypeMaster eventType, String description, BigDecimal cost,  UnitCosts unitOfCost, ServiceProviderInfo serviceProviderInfo, 
			Date pkgFromDt, Date pkgToDt, String strFrom, String strTo,List<Image> imagesPreview){
		this.packageId = packageId;
		this.packageName = packageName;
		this.eventType = eventType;
		this.description = description;
		this.cost = cost;
		this.unitOfCost = unitOfCost;
		this.serviceProviderInfo = serviceProviderInfo;
		this.pkgFromDt = pkgFromDt;
		this.pkgToDt = pkgToDt;
		this.strFrom = strFrom;
		this.strTo = strTo;
                this.imagesPreview = imagesPreview;
	}
	
	
	
	public UnitCosts getUnitOfCost() {
		return unitOfCost;
	}



	public void setUnitOfCost(UnitCosts unitOfCost) {
		this.unitOfCost = unitOfCost;
	}



	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public EventTypeMaster getEventType() {
		return eventType;
	}



	public void setEventType(EventTypeMaster eventType) {
		this.eventType = eventType;
	}



	public Date getPkgFromDt() {
		return pkgFromDt;
	}



	public void setPkgFromDt(Date pkgFromDt) {
		this.pkgFromDt = pkgFromDt;
		
	}



	public Date getPkgToDt() {
		return pkgToDt;
	}



	public void setPkgToDt(Date pkgToDt) {
		this.pkgToDt = pkgToDt;
		
	}



	public String getStrFrom() {
		return strFrom;
	}



	public void setStrFrom(String strFrom) {
		this.strFrom = strFrom;
	}



	public String getStrTo() {
		return strTo;
	}



	public void setStrTo(String strTo) {
		this.strTo = strTo;
	}
	
	
	
	
}
