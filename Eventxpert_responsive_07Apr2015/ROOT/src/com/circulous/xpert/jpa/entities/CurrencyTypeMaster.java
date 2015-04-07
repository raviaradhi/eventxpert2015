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
@Table(name = "currency_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CurrencyTypeMaster.findAll", query = "SELECT c FROM CurrencyTypeMaster c"),
    @NamedQuery(name = "CurrencyTypeMaster.findByCurrencyId", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.currencyId = :currencyId"),
    @NamedQuery(name = "CurrencyTypeMaster.findByCurrencyType", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.currencyType = :currencyType"),
    @NamedQuery(name = "CurrencyTypeMaster.findByCurrencyEffectiveDate", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.currencyEffectiveDate = :currencyEffectiveDate"),
    @NamedQuery(name = "CurrencyTypeMaster.findByCurrencyDescription", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.currencyDescription = :currencyDescription"),
    @NamedQuery(name = "CurrencyTypeMaster.findByIsActive", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "CurrencyTypeMaster.findByIsDeleted", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.isDeleted = :isDeleted"),
    @NamedQuery(name = "CurrencyTypeMaster.findByCreatedUser", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.createdUser = :createdUser"),
    @NamedQuery(name = "CurrencyTypeMaster.findByCreatedDate", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CurrencyTypeMaster.findByUpdatedUser", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.updatedUser = :updatedUser"),
    @NamedQuery(name = "CurrencyTypeMaster.findByUpdatedDate", query = "SELECT c FROM CurrencyTypeMaster c WHERE c.updatedDate = :updatedDate")})
public class CurrencyTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CURRENCY_ID")
    private Integer currencyId;
    @Size(max = 5)
    @Column(name = "CURRENCY_TYPE")
    private String currencyType;
    @Column(name = "CURRENCY_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date currencyEffectiveDate;
    @Size(max = 100)
    @Column(name = "CURRENCY_DESCRIPTION")
    private String currencyDescription;
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
    @OneToMany(mappedBy = "currencyId")
    private Collection<VenueInfo> venueInfoCollection;

    public CurrencyTypeMaster() {
    }

    public CurrencyTypeMaster(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Date getCurrencyEffectiveDate() {
        return currencyEffectiveDate;
    }

    public void setCurrencyEffectiveDate(Date currencyEffectiveDate) {
        this.currencyEffectiveDate = currencyEffectiveDate;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
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
    public Collection<VenueInfo> getVenueInfoCollection() {
        return venueInfoCollection;
    }

    public void setVenueInfoCollection(Collection<VenueInfo> venueInfoCollection) {
        this.venueInfoCollection = venueInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (currencyId != null ? currencyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CurrencyTypeMaster)) {
            return false;
        }
        CurrencyTypeMaster other = (CurrencyTypeMaster) object;
        if ((this.currencyId == null && other.currencyId != null) || (this.currencyId != null && !this.currencyId.equals(other.currencyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.CurrencyTypeMaster[ currencyId=" + currencyId + " ]";
    }
    
}
