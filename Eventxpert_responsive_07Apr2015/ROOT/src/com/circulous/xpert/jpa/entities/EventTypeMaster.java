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
@Table(name = "event_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventTypeMaster.findAll", query = "SELECT e FROM EventTypeMaster e"),
    @NamedQuery(name = "EventTypeMaster.findByEventId", query = "SELECT e FROM EventTypeMaster e WHERE e.eventId = :eventId"),
    @NamedQuery(name = "EventTypeMaster.findByEventType", query = "SELECT e FROM EventTypeMaster e WHERE e.eventType = :eventType"),
    @NamedQuery(name = "EventTypeMaster.findByEventEffectiveDate", query = "SELECT e FROM EventTypeMaster e WHERE e.eventEffectiveDate = :eventEffectiveDate"),
    @NamedQuery(name = "EventTypeMaster.findByEventDescription", query = "SELECT e FROM EventTypeMaster e WHERE e.eventDescription = :eventDescription"),
    @NamedQuery(name = "EventTypeMaster.findByIsActive", query = "SELECT e FROM EventTypeMaster e WHERE e.isActive = :isActive"),
    @NamedQuery(name = "EventTypeMaster.findByIsDeleted", query = "SELECT e FROM EventTypeMaster e WHERE e.isDeleted = :isDeleted"),
    @NamedQuery(name = "EventTypeMaster.findByCreatedUser", query = "SELECT e FROM EventTypeMaster e WHERE e.createdUser = :createdUser"),
    @NamedQuery(name = "EventTypeMaster.findByCreatedDate", query = "SELECT e FROM EventTypeMaster e WHERE e.createdDate = :createdDate"),
    @NamedQuery(name = "EventTypeMaster.findByUpdatedUser", query = "SELECT e FROM EventTypeMaster e WHERE e.updatedUser = :updatedUser"),
    @NamedQuery(name = "EventTypeMaster.findByUpdatedDate", query = "SELECT e FROM EventTypeMaster e WHERE e.updatedDate = :updatedDate")})
public class EventTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_ID")
    private Integer eventId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EVENT_TYPE")
    private String eventType;
    @Column(name = "EVENT_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventEffectiveDate;
    @Size(max = 100)
    @Column(name = "EVENT_DESCRIPTION")
    private String eventDescription;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventType")
    private Collection<VenuePackageInfo> venuePackageInfoCollection;
    @OneToMany(mappedBy = "eventType")
    private Collection<PackageInfo> packageInfoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventType")
    private Collection<EventInfo> eventInfoCollection;

    public EventTypeMaster() {
    }

    public EventTypeMaster(Integer eventId) {
        this.eventId = eventId;
    }

    public EventTypeMaster(Integer eventId, String eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getEventEffectiveDate() {
        return eventEffectiveDate;
    }

    public void setEventEffectiveDate(Date eventEffectiveDate) {
        this.eventEffectiveDate = eventEffectiveDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
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
    public Collection<EventInfo> getEventInfoCollection() {
        return eventInfoCollection;
    }

    public void setEventInfoCollection(Collection<EventInfo> eventInfoCollection) {
        this.eventInfoCollection = eventInfoCollection;
    }
    
    @XmlTransient
    public Collection<PackageInfo> getPackageInfoCollection() {
        return packageInfoCollection;
    }

    public void setPackageInfoCollection(Collection<PackageInfo> packageInfoCollection) {
        this.packageInfoCollection = packageInfoCollection;
    }

    
    @XmlTransient
    public Collection<VenuePackageInfo> getVenuePackageInfoCollection() {
        return venuePackageInfoCollection;
    }

    public void setVenuePackageInfoCollection(Collection<VenuePackageInfo> venuePackageInfoCollection) {
        this.venuePackageInfoCollection = venuePackageInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventTypeMaster)) {
            return false;
        }
        EventTypeMaster other = (EventTypeMaster) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EventTypeMaster[ eventId=" + eventId + " ]";
    }
    
}
