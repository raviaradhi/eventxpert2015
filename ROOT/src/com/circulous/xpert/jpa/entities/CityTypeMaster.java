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
@Table(name = "city_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CityTypeMaster.findAll", query = "SELECT c FROM CityTypeMaster c"),
    @NamedQuery(name = "CityTypeMaster.findByCityId", query = "SELECT c FROM CityTypeMaster c WHERE c.cityId = :cityId"),
    @NamedQuery(name = "CityTypeMaster.findByCityCode", query = "SELECT c FROM CityTypeMaster c WHERE c.cityCode = :cityCode"),
    @NamedQuery(name = "CityTypeMaster.findByCityEffectiveDate", query = "SELECT c FROM CityTypeMaster c WHERE c.cityEffectiveDate = :cityEffectiveDate"),
    @NamedQuery(name = "CityTypeMaster.findByCityName", query = "SELECT c FROM CityTypeMaster c WHERE c.cityName = :cityName"),
    @NamedQuery(name = "CityTypeMaster.findByIsActive", query = "SELECT c FROM CityTypeMaster c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "CityTypeMaster.findByIsDeleted", query = "SELECT c FROM CityTypeMaster c WHERE c.isDeleted = :isDeleted"),
    @NamedQuery(name = "CityTypeMaster.findByCreatedUser", query = "SELECT c FROM CityTypeMaster c WHERE c.createdUser = :createdUser"),
    @NamedQuery(name = "CityTypeMaster.findByCreatedDate", query = "SELECT c FROM CityTypeMaster c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CityTypeMaster.findByUpdatedUser", query = "SELECT c FROM CityTypeMaster c WHERE c.updatedUser = :updatedUser"),
    @NamedQuery(name = "CityTypeMaster.findByUpdatedDate", query = "SELECT c FROM CityTypeMaster c WHERE c.updatedDate = :updatedDate")})
public class CityTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CITY_ID")
    private Integer cityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CITY_CODE")
    private String cityCode;
    @Column(name = "CITY_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cityEffectiveDate;
    @Size(max = 100)
    @Column(name = "CITY_NAME")
    private String cityName;
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
    @JoinColumn(name = "STATE_CODE", referencedColumnName = "STATE_CODE")
    @ManyToOne(optional = false)
    private StateTypeMaster stateCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cityId")
    private Collection<AreaTypeMaster> areaTypeMasterCollection;

    public CityTypeMaster() {
    }

    public CityTypeMaster(Integer cityId) {
        this.cityId = cityId;
    }

    public CityTypeMaster(Integer cityId, String cityCode) {
        this.cityId = cityId;
        this.cityCode = cityCode;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Date getCityEffectiveDate() {
        return cityEffectiveDate;
    }

    public void setCityEffectiveDate(Date cityEffectiveDate) {
        this.cityEffectiveDate = cityEffectiveDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public StateTypeMaster getStateCode() {
        return stateCode;
    }

    public void setStateCode(StateTypeMaster stateCode) {
        this.stateCode = stateCode;
    }

    @XmlTransient
    public Collection<AreaTypeMaster> getAreaTypeMasterCollection() {
        return areaTypeMasterCollection;
    }

    public void setAreaTypeMasterCollection(Collection<AreaTypeMaster> areaTypeMasterCollection) {
        this.areaTypeMasterCollection = areaTypeMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cityId != null ? cityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CityTypeMaster)) {
            return false;
        }
        CityTypeMaster other = (CityTypeMaster) object;
        if ((this.cityId == null && other.cityId != null) || (this.cityId != null && !this.cityId.equals(other.cityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.CityTypeMaster[ cityId=" + cityId + " ]";
    }
    
}
