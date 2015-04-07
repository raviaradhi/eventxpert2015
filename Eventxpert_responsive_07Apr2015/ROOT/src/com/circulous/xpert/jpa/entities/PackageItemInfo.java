/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "package_item_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PackageItemInfo.findAll", query = "SELECT p FROM PackageItemInfo p"),
    @NamedQuery(name = "PackageItemInfo.findByItemId", query = "SELECT p FROM PackageItemInfo p WHERE p.packageItemInfoPK.itemId = :itemId"),
    @NamedQuery(name = "PackageItemInfo.findByPackageId", query = "SELECT p FROM PackageItemInfo p WHERE p.packageItemInfoPK.packageId = :packageId"),
    @NamedQuery(name = "PackageItemInfo.findByIsActive", query = "SELECT p FROM PackageItemInfo p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "PackageItemInfo.findByIsDeleted", query = "SELECT p FROM PackageItemInfo p WHERE p.isDeleted = :isDeleted"),
    @NamedQuery(name = "PackageItemInfo.findByCreatedUser", query = "SELECT p FROM PackageItemInfo p WHERE p.createdUser = :createdUser"),
    @NamedQuery(name = "PackageItemInfo.findByCreatedDate", query = "SELECT p FROM PackageItemInfo p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "PackageItemInfo.findByUpdatedUser", query = "SELECT p FROM PackageItemInfo p WHERE p.updatedUser = :updatedUser"),
    @NamedQuery(name = "PackageItemInfo.findByUpdatedDate", query = "SELECT p FROM PackageItemInfo p WHERE p.updatedDate = :updatedDate"),
    @NamedQuery(name = "PackageItemInfo.findByPackageItemID", query = "SELECT p FROM PackageItemInfo p WHERE p.packageItemID = :packageItemID")})
public class PackageItemInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PackageItemInfoPK packageItemInfoPK;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
    @Size(max = 50)
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 50)
    @Column(name = "UPDATED_USER")
    private String updatedUser;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Size(max = 45)
    @Column(name = "Package_Item_ID")
    private String packageItemID;
    @JoinColumn(name = "PACKAGE_ID", referencedColumnName = "package_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PackageInfo packageInfo;
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ItemMaster itemMaster;

    public PackageItemInfo() {
    }

    public PackageItemInfo(PackageItemInfoPK packageItemInfoPK) {
        this.packageItemInfoPK = packageItemInfoPK;
    }

    public PackageItemInfo(int itemId, int packageId) {
        this.packageItemInfoPK = new PackageItemInfoPK(itemId, packageId);
    }

    public PackageItemInfoPK getPackageItemInfoPK() {
        return packageItemInfoPK;
    }

    public void setPackageItemInfoPK(PackageItemInfoPK packageItemInfoPK) {
        this.packageItemInfoPK = packageItemInfoPK;
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

    public String getPackageItemID() {
        return packageItemID;
    }

    public void setPackageItemID(String packageItemID) {
        this.packageItemID = packageItemID;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public ItemMaster getItemMaster() {
        return itemMaster;
    }

    public void setItemMaster(ItemMaster itemMaster) {
        this.itemMaster = itemMaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (packageItemInfoPK != null ? packageItemInfoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PackageItemInfo)) {
            return false;
        }
        PackageItemInfo other = (PackageItemInfo) object;
        if ((this.packageItemInfoPK == null && other.packageItemInfoPK != null) || (this.packageItemInfoPK != null && !this.packageItemInfoPK.equals(other.packageItemInfoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.PackageItemInfo[ packageItemInfoPK=" + packageItemInfoPK + " ]";
    }
    
}
