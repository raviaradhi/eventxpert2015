/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "esp_package_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspPackageInfo.findAll", query = "SELECT e FROM EspPackageInfo e"),
    @NamedQuery(name = "EspPackageInfo.findByEspPackageId", query = "SELECT e FROM EspPackageInfo e WHERE e.espPackageId = :espPackageId"),
    @NamedQuery(name = "EspPackageInfo.findByPackageId", query = "SELECT e FROM EspPackageInfo e WHERE e.packageId = :packageId"),
    @NamedQuery(name = "EspPackageInfo.findByEspPackageEffectiveDate", query = "SELECT e FROM EspPackageInfo e WHERE e.espPackageEffectiveDate = :espPackageEffectiveDate"),
    @NamedQuery(name = "EspPackageInfo.findByEspPackageEtDate", query = "SELECT e FROM EspPackageInfo e WHERE e.espPackageEtDate = :espPackageEtDate"),
    @NamedQuery(name = "EspPackageInfo.findByIsActive", query = "SELECT e FROM EspPackageInfo e WHERE e.isActive = :isActive"),
    @NamedQuery(name = "EspPackageInfo.findByIsDeleted", query = "SELECT e FROM EspPackageInfo e WHERE e.isDeleted = :isDeleted"),
    @NamedQuery(name = "EspPackageInfo.findByCreatedUser", query = "SELECT e FROM EspPackageInfo e WHERE e.createdUser = :createdUser"),
    @NamedQuery(name = "EspPackageInfo.findByCreatedDate", query = "SELECT e FROM EspPackageInfo e WHERE e.createdDate = :createdDate"),
    @NamedQuery(name = "EspPackageInfo.findByUpdatedUser", query = "SELECT e FROM EspPackageInfo e WHERE e.updatedUser = :updatedUser"),
    @NamedQuery(name = "EspPackageInfo.findByUpdatedDate", query = "SELECT e FROM EspPackageInfo e WHERE e.updatedDate = :updatedDate")})
public class EspPackageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "ESP_PACKAGE_ID")
    private Integer espPackageId;
    @Column(name = "PACKAGE_ID")
    private Integer packageId;
    @Column(name = "ESP_PACKAGE_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date espPackageEffectiveDate;
    @Column(name = "ESP_PACKAGE_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date espPackageEtDate;
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
    @JoinColumn(name = "EVENT_SERVICE_PROVIDER_ID", referencedColumnName = "EVENT_SERVICE_PROVIDER_ID")
    @ManyToOne(optional = false)
    private EventServiceProviderInfo eventServiceProviderId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espPackageId")
    private Collection<EspPackageItemInfo> espPackageItemInfoCollection;

    public EspPackageInfo() {
    }

    public EspPackageInfo(Integer espPackageId) {
        this.espPackageId = espPackageId;
    }

    public Integer getEspPackageId() {
        return espPackageId;
    }

    public void setEspPackageId(Integer espPackageId) {
        this.espPackageId = espPackageId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Date getEspPackageEffectiveDate() {
        return espPackageEffectiveDate;
    }

    public void setEspPackageEffectiveDate(Date espPackageEffectiveDate) {
        this.espPackageEffectiveDate = espPackageEffectiveDate;
    }

    public Date getEspPackageEtDate() {
        return espPackageEtDate;
    }

    public void setEspPackageEtDate(Date espPackageEtDate) {
        this.espPackageEtDate = espPackageEtDate;
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

    public EventServiceProviderInfo getEventServiceProviderId() {
        return eventServiceProviderId;
    }

    public void setEventServiceProviderId(EventServiceProviderInfo eventServiceProviderId) {
        this.eventServiceProviderId = eventServiceProviderId;
    }

    @XmlTransient
    public Collection<EspPackageItemInfo> getEspPackageItemInfoCollection() {
        return espPackageItemInfoCollection;
    }

    public void setEspPackageItemInfoCollection(Collection<EspPackageItemInfo> espPackageItemInfoCollection) {
        this.espPackageItemInfoCollection = espPackageItemInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (espPackageId != null ? espPackageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspPackageInfo)) {
            return false;
        }
        EspPackageInfo other = (EspPackageInfo) object;
        if ((this.espPackageId == null && other.espPackageId != null) || (this.espPackageId != null && !this.espPackageId.equals(other.espPackageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EspPackageInfo[ espPackageId=" + espPackageId + " ]";
    }
    
}
