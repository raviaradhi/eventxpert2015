/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author SANJANA
 */
@Embeddable
public class PackageItemInfoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITEM_ID")
    private int itemId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PACKAGE_ID")
    private int packageId;
////    @Basic(optional = false)
////    @NotNull
////    @Column(name = "ITEM_ID")
////    private int itemId;
////    @Basic(optional = false)
////    @NotNull
////    @Column(name = "PACKAGE_ID")
//    private int packageId;

    public PackageItemInfoPK() {
    }

    public PackageItemInfoPK(int itemId, int packageId) {
        this.itemId = itemId;
        this.packageId = packageId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) itemId;
        hash += (int) packageId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PackageItemInfoPK)) {
            return false;
        }
        PackageItemInfoPK other = (PackageItemInfoPK) object;
        if (this.itemId != other.itemId) {
            return false;
        }
        if (this.packageId != other.packageId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.PackageItemInfoPK[ itemId=" + itemId + ", packageId=" + packageId + " ]";
    }

//    public PackageItemInfoPK(int itemId, int packageId) {
//        this.itemId = itemId;
//        this.packageId = packageId;
//    }
//
//    public int getItemId() {
//        return itemId;
//    }
//
//    public void setItemId(int itemId) {
//        this.itemId = itemId;
//    }
//
//    public int getPackageId() {
//        return packageId;
//    }
//
//    public void setPackageId(int packageId) {
//        this.packageId = packageId;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (int) itemId;
//        hash += (int) packageId;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof PackageItemInfoPK)) {
//            return false;
//        }
//        PackageItemInfoPK other = (PackageItemInfoPK) object;
//        if (this.itemId != other.itemId) {
//            return false;
//        }
//        if (this.packageId != other.packageId) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.circulous.xpert.jpa.entities.PackageItemInfoPK[ itemId=" + itemId + ", packageId=" + packageId + " ]";
//    }
    
}
