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
@Table(name = "country_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CountryTypeMaster.findAll", query = "SELECT c FROM CountryTypeMaster c"),
    @NamedQuery(name = "CountryTypeMaster.findByCountryCode", query = "SELECT c FROM CountryTypeMaster c WHERE c.countryCode = :countryCode"),
    @NamedQuery(name = "CountryTypeMaster.findByCountryEffectiveDate", query = "SELECT c FROM CountryTypeMaster c WHERE c.countryEffectiveDate = :countryEffectiveDate"),
    @NamedQuery(name = "CountryTypeMaster.findByCountryName", query = "SELECT c FROM CountryTypeMaster c WHERE c.countryName = :countryName"),
    @NamedQuery(name = "CountryTypeMaster.findByIsActive", query = "SELECT c FROM CountryTypeMaster c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "CountryTypeMaster.findByIsDeleted", query = "SELECT c FROM CountryTypeMaster c WHERE c.isDeleted = :isDeleted"),
    @NamedQuery(name = "CountryTypeMaster.findByCreatedUser", query = "SELECT c FROM CountryTypeMaster c WHERE c.createdUser = :createdUser"),
    @NamedQuery(name = "CountryTypeMaster.findByCreatedDate", query = "SELECT c FROM CountryTypeMaster c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CountryTypeMaster.findByUpdatedUser", query = "SELECT c FROM CountryTypeMaster c WHERE c.updatedUser = :updatedUser"),
    @NamedQuery(name = "CountryTypeMaster.findByUpdatedDate", query = "SELECT c FROM CountryTypeMaster c WHERE c.updatedDate = :updatedDate")})
public class CountryTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "COUNTRY_CODE")
    private String countryCode;
    @Column(name = "COUNTRY_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date countryEffectiveDate;
    @Size(max = 100)
    @Column(name = "COUNTRY_NAME")
    private String countryName;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countryCode")
    private Collection<StateTypeMaster> stateTypeMasterCollection;

    public CountryTypeMaster() {
    }

    public CountryTypeMaster(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Date getCountryEffectiveDate() {
        return countryEffectiveDate;
    }

    public void setCountryEffectiveDate(Date countryEffectiveDate) {
        this.countryEffectiveDate = countryEffectiveDate;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
    public Collection<StateTypeMaster> getStateTypeMasterCollection() {
        return stateTypeMasterCollection;
    }

    public void setStateTypeMasterCollection(Collection<StateTypeMaster> stateTypeMasterCollection) {
        this.stateTypeMasterCollection = stateTypeMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (countryCode != null ? countryCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountryTypeMaster)) {
            return false;
        }
        CountryTypeMaster other = (CountryTypeMaster) object;
        if ((this.countryCode == null && other.countryCode != null) || (this.countryCode != null && !this.countryCode.equals(other.countryCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.CountryTypeMaster[ countryCode=" + countryCode + " ]";
    }
    
}
