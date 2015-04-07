/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//import com.sun.istack.internal.NotNull;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "item_category_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemCategoryMaster.findAll", query = "SELECT i FROM ItemCategoryMaster i"),
    @NamedQuery(name = "ItemCategoryMaster.findByItemCategoryId", query = "SELECT i FROM ItemCategoryMaster i WHERE i.itemCategoryId = :itemCategoryId"),
    @NamedQuery(name = "ItemCategoryMaster.findByItemCategoryEffectiveDate", query = "SELECT i FROM ItemCategoryMaster i WHERE i.itemCategoryEffectiveDate = :itemCategoryEffectiveDate"),
    @NamedQuery(name = "ItemCategoryMaster.findByItemCategoryName", query = "SELECT i FROM ItemCategoryMaster i WHERE i.itemCategoryName = :itemCategoryName"),
    @NamedQuery(name = "ItemCategoryMaster.findByItemCategoryDescription", query = "SELECT i FROM ItemCategoryMaster i WHERE i.itemCategoryDescription = :itemCategoryDescription"),
    @NamedQuery(name = "ItemCategoryMaster.findByIsActive", query = "SELECT i FROM ItemCategoryMaster i WHERE i.isActive = :isActive"),
    @NamedQuery(name = "ItemCategoryMaster.findByIsDeleted", query = "SELECT i FROM ItemCategoryMaster i WHERE i.isDeleted = :isDeleted"),
    @NamedQuery(name = "ItemCategoryMaster.findByCreatedUser", query = "SELECT i FROM ItemCategoryMaster i WHERE i.createdUser = :createdUser"),
    @NamedQuery(name = "ItemCategoryMaster.findByCreatedDate", query = "SELECT i FROM ItemCategoryMaster i WHERE i.createdDate = :createdDate"),
    @NamedQuery(name = "ItemCategoryMaster.findByUpdatedUser", query = "SELECT i FROM ItemCategoryMaster i WHERE i.updatedUser = :updatedUser"),
    @NamedQuery(name = "ItemCategoryMaster.findByUpdatedDate", query = "SELECT i FROM ItemCategoryMaster i WHERE i.updatedDate = :updatedDate"),
    @NamedQuery(name = "ItemCategoryMaster.findByServiceTypeCode", query = "SELECT i FROM ItemCategoryMaster i WHERE i.serviceTypeCode = :serviceTypeCode")})
public class ItemCategoryMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
   // @NotNull
    @Column(name = "ITEM_CATEGORY_ID")
    private Integer itemCategoryId;
    @Column(name = "ITEM_CATEGORY_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemCategoryEffectiveDate;
  //  @Size(max = 100)
    @Column(name = "ITEM_CATEGORY_NAME")
    private String itemCategoryName;
  //  @Size(max = 500)
    @Column(name = "ITEM_CATEGORY_DESCRIPTION")
    private String itemCategoryDescription;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
  //  @Size(max = 50)
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
   // @Size(max = 50)
    @Column(name = "UPDATED_USER")
    private String updatedUser;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
  //  @NotNull
   // @Size(min = 1, max = 5)
    @Column(name = "SERVICE_TYPE_CODE")
    private String serviceTypeCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemCategoryId")
    private Collection<ItemMaster> itemMasterCollection;

    public ItemCategoryMaster() {
    }

    public ItemCategoryMaster(Integer itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public ItemCategoryMaster(Integer itemCategoryId, String serviceTypeCode) {
        this.itemCategoryId = itemCategoryId;
        this.serviceTypeCode = serviceTypeCode;
    }

    public Integer getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Integer itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public Date getItemCategoryEffectiveDate() {
        return itemCategoryEffectiveDate;
    }

    public void setItemCategoryEffectiveDate(Date itemCategoryEffectiveDate) {
        this.itemCategoryEffectiveDate = itemCategoryEffectiveDate;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public String getItemCategoryDescription() {
        return itemCategoryDescription;
    }

    public void setItemCategoryDescription(String itemCategoryDescription) {
        this.itemCategoryDescription = itemCategoryDescription;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    public Character getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Character isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    @XmlTransient
    public Collection<ItemMaster> getItemMasterCollection() {
        return itemMasterCollection;
    }

    public void setItemMasterCollection(Collection<ItemMaster> itemMasterCollection) {
        this.itemMasterCollection = itemMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemCategoryId != null ? itemCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemCategoryMaster)) {
            return false;
        }
        ItemCategoryMaster other = (ItemCategoryMaster) object;
        if ((this.itemCategoryId == null && other.itemCategoryId != null) || (this.itemCategoryId != null && !this.itemCategoryId.equals(other.itemCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ItemCategoryMaster[ itemCategoryId=" + itemCategoryId + " ]";
    }
    
}
