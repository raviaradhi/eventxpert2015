/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "area_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaTypeMaster.findAll", query = "SELECT a FROM AreaTypeMaster a"),
    @NamedQuery(name = "AreaTypeMaster.findByAreaId", query = "SELECT a FROM AreaTypeMaster a WHERE a.areaId = :areaId"),
    @NamedQuery(name = "AreaTypeMaster.findByAreaCode", query = "SELECT a FROM AreaTypeMaster a WHERE a.areaCode = :areaCode"),
    @NamedQuery(name = "AreaTypeMaster.findByAreaEffectiveDate", query = "SELECT a FROM AreaTypeMaster a WHERE a.areaEffectiveDate = :areaEffectiveDate"),
    @NamedQuery(name = "AreaTypeMaster.findByAreaName", query = "SELECT a FROM AreaTypeMaster a WHERE a.areaName = :areaName"),
    @NamedQuery(name = "AreaTypeMaster.findByIsActive", query = "SELECT a FROM AreaTypeMaster a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "AreaTypeMaster.findByIsDeleted", query = "SELECT a FROM AreaTypeMaster a WHERE a.isDeleted = :isDeleted"),
    @NamedQuery(name = "AreaTypeMaster.findByCreatedUser", query = "SELECT a FROM AreaTypeMaster a WHERE a.createdUser = :createdUser"),
    @NamedQuery(name = "AreaTypeMaster.findByCreatedDate", query = "SELECT a FROM AreaTypeMaster a WHERE a.createdDate = :createdDate"),
    @NamedQuery(name = "AreaTypeMaster.findByUpdatedUser", query = "SELECT a FROM AreaTypeMaster a WHERE a.updatedUser = :updatedUser"),
    @NamedQuery(name = "AreaTypeMaster.findByUpdatedDate", query = "SELECT a FROM AreaTypeMaster a WHERE a.updatedDate = :updatedDate"),
    @NamedQuery(name = "AreaTypeMaster.findByLatitude", query = "SELECT a FROM AreaTypeMaster a WHERE a.latitude = :latitude"),
    @NamedQuery(name = "AreaTypeMaster.findByLongitude", query = "SELECT a FROM AreaTypeMaster a WHERE a.longitude = :longitude")})
public class AreaTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "AREA_ID")
    private Integer areaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AREA_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date areaEffectiveDate;
    @Size(max = 100)
    @Column(name = "AREA_NAME")
    private String areaName;
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
    @Size(max = 10)
    @Column(name = "LATITUDE")
    private String latitude;
    @Size(max = 10)
    @Column(name = "LONGITUDE")
    private String longitude;
    @JoinColumn(name = "CITY_ID", referencedColumnName = "CITY_ID")
    @ManyToOne(optional = false)
    private CityTypeMaster cityId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaId")
    private Collection<VenueInfo> venueInfoCollection;

    public AreaTypeMaster() {
    }

    public AreaTypeMaster(Integer areaId) {
        this.areaId = areaId;
    }

    public AreaTypeMaster(Integer areaId, String areaCode, Date areaEffectiveDate) {
        this.areaId = areaId;
        this.areaCode = areaCode;
        this.areaEffectiveDate = areaEffectiveDate;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Date getAreaEffectiveDate() {
        return areaEffectiveDate;
    }

    public void setAreaEffectiveDate(Date areaEffectiveDate) {
        this.areaEffectiveDate = areaEffectiveDate;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public CityTypeMaster getCityId() {
        return cityId;
    }

    public void setCityId(CityTypeMaster cityId) {
        this.cityId = cityId;
    }

    @XmlTransient
    public Collection<VenueInfo> getVenueInfoCollection() {
        return venueInfoCollection;
    }

    public void setVenueInfoCollection(Collection<VenueInfo> venueInfoCollection) {
        this.venueInfoCollection = venueInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (areaId != null ? areaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreaTypeMaster)) {
            return false;
        }
        AreaTypeMaster other = (AreaTypeMaster) object;
        if ((this.areaId == null && other.areaId != null) || (this.areaId != null && !this.areaId.equals(other.areaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.AreaTypeMaster[ areaId=" + areaId + " ]";
    }
    
}
