/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "item_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemMaster.findAll", query = "SELECT i FROM ItemMaster i"),
    @NamedQuery(name = "ItemMaster.findByItemId", query = "SELECT i FROM ItemMaster i WHERE i.itemId = :itemId"),
    @NamedQuery(name = "ItemMaster.findByItemEffectiveDate", query = "SELECT i FROM ItemMaster i WHERE i.itemEffectiveDate = :itemEffectiveDate"),
    @NamedQuery(name = "ItemMaster.findByItemName", query = "SELECT i FROM ItemMaster i WHERE i.itemName = :itemName"),
    @NamedQuery(name = "ItemMaster.findByItemDescription", query = "SELECT i FROM ItemMaster i WHERE i.itemDescription = :itemDescription"),
    @NamedQuery(name = "ItemMaster.findByIsActive", query = "SELECT i FROM ItemMaster i WHERE i.isActive = :isActive"),
    @NamedQuery(name = "ItemMaster.findByIsDeleted", query = "SELECT i FROM ItemMaster i WHERE i.isDeleted = :isDeleted"),
    @NamedQuery(name = "ItemMaster.findByCreatedUser", query = "SELECT i FROM ItemMaster i WHERE i.createdUser = :createdUser"),
    @NamedQuery(name = "ItemMaster.findByCreatedDate", query = "SELECT i FROM ItemMaster i WHERE i.createdDate = :createdDate"),
    @NamedQuery(name = "ItemMaster.findByUpdatedUser", query = "SELECT i FROM ItemMaster i WHERE i.updatedUser = :updatedUser"),
    @NamedQuery(name = "ItemMaster.findByUpdatedDate", query = "SELECT i FROM ItemMaster i WHERE i.updatedDate = :updatedDate")})
public class ItemMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "ITEM_ID")
    private Integer itemId;
    @Column(name = "ITEM_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemEffectiveDate;
    //@Size(max = 100)
    @Column(name = "ITEM_NAME")
    private String itemName;
   // @Size(max = 500)
    @Column(name = "ITEM_DESCRIPTION")
    private String itemDescription;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
    //@Size(max = 50)
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
    @OneToMany(mappedBy = "itemId")
    private Collection<EspPackageItemInfo> espPackageItemInfoCollection;
    @JoinColumn(name = "ITEM_CATEGORY_ID", referencedColumnName = "ITEM_CATEGORY_ID")
    @ManyToOne(optional = false)
    private ItemCategoryMaster itemCategoryId;

    public ItemMaster() {
    }

    public ItemMaster(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Date getItemEffectiveDate() {
        return itemEffectiveDate;
    }

    public void setItemEffectiveDate(Date itemEffectiveDate) {
        this.itemEffectiveDate = itemEffectiveDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
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

    @XmlTransient
    public Collection<EspPackageItemInfo> getEspPackageItemInfoCollection() {
        return espPackageItemInfoCollection;
    }

    public void setEspPackageItemInfoCollection(Collection<EspPackageItemInfo> espPackageItemInfoCollection) {
        this.espPackageItemInfoCollection = espPackageItemInfoCollection;
    }

    public ItemCategoryMaster getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(ItemCategoryMaster itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemMaster)) {
            return false;
        }
        ItemMaster other = (ItemMaster) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ItemMaster[ itemId=" + itemId + " ]";
    }
    
}
