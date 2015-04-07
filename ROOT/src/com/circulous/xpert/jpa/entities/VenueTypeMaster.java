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
@Table(name = "venue_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VenueTypeMaster.findAll", query = "SELECT v FROM VenueTypeMaster v"),
    @NamedQuery(name = "VenueTypeMaster.findByVenueTypeId", query = "SELECT v FROM VenueTypeMaster v WHERE v.venueTypeId = :venueTypeId"),
    @NamedQuery(name = "VenueTypeMaster.findByVenueType", query = "SELECT v FROM VenueTypeMaster v WHERE v.venueType = :venueType"),
    @NamedQuery(name = "VenueTypeMaster.findByVenueEffectiveDate", query = "SELECT v FROM VenueTypeMaster v WHERE v.venueEffectiveDate = :venueEffectiveDate"),
    @NamedQuery(name = "VenueTypeMaster.findByVenueDescription", query = "SELECT v FROM VenueTypeMaster v WHERE v.venueDescription = :venueDescription"),
    @NamedQuery(name = "VenueTypeMaster.findByIsActive", query = "SELECT v FROM VenueTypeMaster v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VenueTypeMaster.findByIsDeleted", query = "SELECT v FROM VenueTypeMaster v WHERE v.isDeleted = :isDeleted"),
    @NamedQuery(name = "VenueTypeMaster.findByCreatedUser", query = "SELECT v FROM VenueTypeMaster v WHERE v.createdUser = :createdUser"),
    @NamedQuery(name = "VenueTypeMaster.findByCreatedDate", query = "SELECT v FROM VenueTypeMaster v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VenueTypeMaster.findByUpdatedUser", query = "SELECT v FROM VenueTypeMaster v WHERE v.updatedUser = :updatedUser"),
    @NamedQuery(name = "VenueTypeMaster.findByUpdatedDate", query = "SELECT v FROM VenueTypeMaster v WHERE v.updatedDate = :updatedDate")})
public class VenueTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "VENUE_TYPE_ID")
    private Integer venueTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "VENUE_TYPE")
    private String venueType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VENUE_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date venueEffectiveDate;
    @Size(max = 100)
    @Column(name = "VENUE_DESCRIPTION")
    private String venueDescription;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venueTypeId")
    private Collection<VenueInfo> venueInfoCollection;

    public VenueTypeMaster() {
    }

    public VenueTypeMaster(Integer venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public VenueTypeMaster(Integer venueTypeId, String venueType, Date venueEffectiveDate) {
        this.venueTypeId = venueTypeId;
        this.venueType = venueType;
        this.venueEffectiveDate = venueEffectiveDate;
    }

    public Integer getVenueTypeId() {
        return venueTypeId;
    }

    public void setVenueTypeId(Integer venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    public Date getVenueEffectiveDate() {
        return venueEffectiveDate;
    }

    public void setVenueEffectiveDate(Date venueEffectiveDate) {
        this.venueEffectiveDate = venueEffectiveDate;
    }

    public String getVenueDescription() {
        return venueDescription;
    }

    public void setVenueDescription(String venueDescription) {
        this.venueDescription = venueDescription;
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
        hash += (venueTypeId != null ? venueTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenueTypeMaster)) {
            return false;
        }
        VenueTypeMaster other = (VenueTypeMaster) object;
        if ((this.venueTypeId == null && other.venueTypeId != null) || (this.venueTypeId != null && !this.venueTypeId.equals(other.venueTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.VenueTypeMaster[ venueTypeId=" + venueTypeId + " ]";
    }
    
}
