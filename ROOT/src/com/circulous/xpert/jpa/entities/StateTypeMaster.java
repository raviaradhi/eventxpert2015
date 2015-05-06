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
@Table(name = "state_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StateTypeMaster.findAll", query = "SELECT s FROM StateTypeMaster s"),
    @NamedQuery(name = "StateTypeMaster.findByStateCode", query = "SELECT s FROM StateTypeMaster s WHERE s.stateCode = :stateCode"),
    @NamedQuery(name = "StateTypeMaster.findByStateEffectiveDate", query = "SELECT s FROM StateTypeMaster s WHERE s.stateEffectiveDate = :stateEffectiveDate"),
    @NamedQuery(name = "StateTypeMaster.findByStateName", query = "SELECT s FROM StateTypeMaster s WHERE s.stateName = :stateName"),
    @NamedQuery(name = "StateTypeMaster.findByIsActive", query = "SELECT s FROM StateTypeMaster s WHERE s.isActive = :isActive"),
    @NamedQuery(name = "StateTypeMaster.findByIsDeleted", query = "SELECT s FROM StateTypeMaster s WHERE s.isDeleted = :isDeleted"),
    @NamedQuery(name = "StateTypeMaster.findByCreatedUser", query = "SELECT s FROM StateTypeMaster s WHERE s.createdUser = :createdUser"),
    @NamedQuery(name = "StateTypeMaster.findByCreatedDate", query = "SELECT s FROM StateTypeMaster s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "StateTypeMaster.findByUpdatedUser", query = "SELECT s FROM StateTypeMaster s WHERE s.updatedUser = :updatedUser"),
    @NamedQuery(name = "StateTypeMaster.findByUpdatedDate", query = "SELECT s FROM StateTypeMaster s WHERE s.updatedDate = :updatedDate")})
public class StateTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "STATE_CODE")
    private String stateCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATE_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stateEffectiveDate;
    @Size(max = 100)
    @Column(name = "STATE_NAME")
    private String stateName;
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
    @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE")
    @ManyToOne(optional = false)
    private CountryTypeMaster countryCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stateCode")
    private Collection<CityTypeMaster> cityTypeMasterCollection;

    public StateTypeMaster() {
    }

    public StateTypeMaster(String stateCode) {
        this.stateCode = stateCode;
    }

    public StateTypeMaster(String stateCode, Date stateEffectiveDate) {
        this.stateCode = stateCode;
        this.stateEffectiveDate = stateEffectiveDate;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Date getStateEffectiveDate() {
        return stateEffectiveDate;
    }

    public void setStateEffectiveDate(Date stateEffectiveDate) {
        this.stateEffectiveDate = stateEffectiveDate;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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

    public CountryTypeMaster getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryTypeMaster countryCode) {
        this.countryCode = countryCode;
    }

    @XmlTransient
    public Collection<CityTypeMaster> getCityTypeMasterCollection() {
        return cityTypeMasterCollection;
    }

    public void setCityTypeMasterCollection(Collection<CityTypeMaster> cityTypeMasterCollection) {
        this.cityTypeMasterCollection = cityTypeMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stateCode != null ? stateCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StateTypeMaster)) {
            return false;
        }
        StateTypeMaster other = (StateTypeMaster) object;
        if ((this.stateCode == null && other.stateCode != null) || (this.stateCode != null && !this.stateCode.equals(other.stateCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.StateTypeMaster[ stateCode=" + stateCode + " ]";
    }
    
}
