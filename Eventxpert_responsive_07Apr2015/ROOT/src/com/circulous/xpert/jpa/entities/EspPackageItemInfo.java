/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "esp_package_item_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspPackageItemInfo.findAll", query = "SELECT e FROM EspPackageItemInfo e"),
    @NamedQuery(name = "EspPackageItemInfo.findByEspPackitemId", query = "SELECT e FROM EspPackageItemInfo e WHERE e.espPackitemId = :espPackitemId"),
    @NamedQuery(name = "EspPackageItemInfo.findByItemCost", query = "SELECT e FROM EspPackageItemInfo e WHERE e.itemCost = :itemCost"),
    @NamedQuery(name = "EspPackageItemInfo.findByIsActive", query = "SELECT e FROM EspPackageItemInfo e WHERE e.isActive = :isActive"),
    @NamedQuery(name = "EspPackageItemInfo.findByIsDeleted", query = "SELECT e FROM EspPackageItemInfo e WHERE e.isDeleted = :isDeleted"),
    @NamedQuery(name = "EspPackageItemInfo.findByCreatedUser", query = "SELECT e FROM EspPackageItemInfo e WHERE e.createdUser = :createdUser"),
    @NamedQuery(name = "EspPackageItemInfo.findByCreatedDate", query = "SELECT e FROM EspPackageItemInfo e WHERE e.createdDate = :createdDate"),
    @NamedQuery(name = "EspPackageItemInfo.findByUpdatedUser", query = "SELECT e FROM EspPackageItemInfo e WHERE e.updatedUser = :updatedUser"),
    @NamedQuery(name = "EspPackageItemInfo.findByUpdatedDate", query = "SELECT e FROM EspPackageItemInfo e WHERE e.updatedDate = :updatedDate")})
public class EspPackageItemInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "ESP_PACKITEM_ID")
    private Integer espPackitemId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ITEM_COST")
    private BigDecimal itemCost;
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
    @JoinColumn(name = "ESP_PACKAGE_ID", referencedColumnName = "ESP_PACKAGE_ID")
    @ManyToOne(optional = false)
    private EspPackageInfo espPackageId;
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
    @ManyToOne
    private ItemMaster itemId;

    public EspPackageItemInfo() {
    }

    public EspPackageItemInfo(Integer espPackitemId) {
        this.espPackitemId = espPackitemId;
    }

    public Integer getEspPackitemId() {
        return espPackitemId;
    }

    public void setEspPackitemId(Integer espPackitemId) {
        this.espPackitemId = espPackitemId;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
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

    public EspPackageInfo getEspPackageId() {
        return espPackageId;
    }

    public void setEspPackageId(EspPackageInfo espPackageId) {
        this.espPackageId = espPackageId;
    }

    public ItemMaster getItemId() {
        return itemId;
    }

    public void setItemId(ItemMaster itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (espPackitemId != null ? espPackitemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspPackageItemInfo)) {
            return false;
        }
        EspPackageItemInfo other = (EspPackageItemInfo) object;
        if ((this.espPackitemId == null && other.espPackitemId != null) || (this.espPackitemId != null && !this.espPackitemId.equals(other.espPackitemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EspPackageItemInfo[ espPackitemId=" + espPackitemId + " ]";
    }
    
}
