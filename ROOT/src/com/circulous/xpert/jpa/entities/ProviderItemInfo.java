package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the provider_item_info database table.
 * 
 */
@Entity
@Table(name="provider_item_info")
public class ProviderItemInfo implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "provider_item_id")
//    private Integer providerItemId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ITEM_COST")
    private Double itemCost;
    @Column(name = "EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROVIDER_ITEM_INFO_PROVIDERITEMID_GENERATOR", sequenceName="PII")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROVIDER_ITEM_INFO_PROVIDERITEMID_GENERATOR")
	@Column(name="PROVIDER_ITEM_ID")
	private Integer providerItemId;

	@Column(name="CREATED_USER")
	private String createdUser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="ITEM_ID")
	private String itemId;

	@Column(name="SERVICE_PROVIDER_ID")
	private String serviceProviderId;

	@Column(name="UNIT_OF_COST")
	private String unitOfCost;

	@Column(name="UPDATED_USER")
	private String updatedUser;
	
	
	public ProviderItemInfo() {
	}

	public int getProviderItemId() {
		return this.providerItemId;
	}

	public void setProviderItemId(int providerItemId) {
		this.providerItemId = providerItemId;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return this.createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public double getItemCost() {
		return this.itemCost;
	}

	public void setItemCost(double itemCost) {
		this.itemCost = itemCost;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getServiceProviderId() {
		return this.serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public String getUnitOfCost() {
		return this.unitOfCost;
	}

	public void setUnitOfCost(String unitOfCost) {
		this.unitOfCost = unitOfCost;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedUser() {
		return this.updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

    public ProviderItemInfo(Integer providerItemId) {
        this.providerItemId = providerItemId;
    }

//    public Integer getProviderItemId() {
//        return providerItemId;
//    }
//
//    public void setProviderItemId(Integer providerItemId) {
//        this.providerItemId = providerItemId;
//    }

//    public Double getItemCost() {
//        return itemCost;
//    }
//
//    public void setItemCost(Double itemCost) {
//        this.itemCost = itemCost;
//    }
//
//    public Date getEffectiveDate() {
//        return effectiveDate;
//    }
//
//    public void setEffectiveDate(Date effectiveDate) {
//        this.effectiveDate = effectiveDate;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public Date getUpdatedDate() {
//        return updatedDate;
//    }
//
//    public void setUpdatedDate(Date updatedDate) {
//        this.updatedDate = updatedDate;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (providerItemId != null ? providerItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProviderItemInfo)) {
            return false;
        }
        ProviderItemInfo other = (ProviderItemInfo) object;
        if ((this.providerItemId == null && other.providerItemId != null) || (this.providerItemId != null && !this.providerItemId.equals(other.providerItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ProviderItemInfo[ providerItemId=" + providerItemId + " ]";
    }

}
