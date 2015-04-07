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
@Table(name = "venue_hall_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VenueHallInfo.findAll", query = "SELECT v FROM VenueHallInfo v"),
    @NamedQuery(name = "VenueHallInfo.findByHallID", query = "SELECT v FROM VenueHallInfo v WHERE v.hallID = :hallID"),
    @NamedQuery(name = "VenueHallInfo.findByVenueID", query = "SELECT v FROM VenueHallInfo v WHERE v.venueID = :venueID"),
    @NamedQuery(name = "VenueHallInfo.findByVenueIDActive", query = "SELECT v FROM VenueHallInfo v WHERE v.venueID = :venueID and v.active = :active order by v.hallID"),
    @NamedQuery(name = "VenueHallInfo.findByHallName", query = "SELECT v FROM VenueHallInfo v WHERE v.hallName = :hallName"),
    @NamedQuery(name = "VenueHallInfo.findByCapacity", query = "SELECT v FROM VenueHallInfo v WHERE v.capacity = :capacity"),
    @NamedQuery(name = "VenueHallInfo.findByMinCapacity", query = "SELECT v FROM VenueHallInfo v WHERE v.minCapacity = :minCapacity"),
    @NamedQuery(name = "VenueHallInfo.findByAirConditioned", query = "SELECT v FROM VenueHallInfo v WHERE v.airConditioned = :airConditioned"),
    @NamedQuery(name = "VenueHallInfo.findByStage", query = "SELECT v FROM VenueHallInfo v WHERE v.stage = :stage"),
    @NamedQuery(name = "VenueHallInfo.findByImages", query = "SELECT v FROM VenueHallInfo v WHERE v.images = :images"),
    @NamedQuery(name = "VenueHallInfo.findByActive", query = "SELECT v FROM VenueHallInfo v WHERE v.active = :active"),
    @NamedQuery(name = "VenueHallInfo.findByHallEffectiveDate", query = "SELECT v FROM VenueHallInfo v WHERE v.hallEffectiveDate = :hallEffectiveDate"),
    @NamedQuery(name = "VenueHallInfo.findByCreatedDate", query = "SELECT v FROM VenueHallInfo v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VenueHallInfo.findByCreatedBy", query = "SELECT v FROM VenueHallInfo v WHERE v.createdBy = :createdBy"),
    @NamedQuery(name = "VenueHallInfo.findByModifiedDate", query = "SELECT v FROM VenueHallInfo v WHERE v.modifiedDate = :modifiedDate")})
public class VenueHallInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "Hall_ID")
    private Integer hallID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Venue_ID")
    private int venueID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Hall_Name")
    private String hallName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Capacity")
    private int capacity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Min_Capacity")
    private int minCapacity;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Air_Conditioned")
    private char airConditioned;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Stage")
    private char stage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Images")
    private String images;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Active")
    private char active;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Hall_Effective_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hallEffectiveDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Created_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Created_By")
    private String createdBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Modified_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @OneToMany(mappedBy = "hallId")
    private Collection<EventServiceProviderInfo> eventServiceProviderInfoCollection;

    public VenueHallInfo() {
    }

    public VenueHallInfo(Integer hallID) {
        this.hallID = hallID;
    }

    public VenueHallInfo(Integer hallID, int venueID, String hallName, int capacity, int minCapacity, String description, char airConditioned, char stage, String images, char active, Date hallEffectiveDate, Date createdDate, String createdBy, Date modifiedDate) {
        this.hallID = hallID;
        this.venueID = venueID;
        this.hallName = hallName;
        this.capacity = capacity;
        this.minCapacity = minCapacity;
        this.description = description;
        this.airConditioned = airConditioned;
        this.stage = stage;
        this.images = images;
        this.active = active;
        this.hallEffectiveDate = hallEffectiveDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
    }

    public Integer getHallID() {
        return hallID;
    }

    public void setHallID(Integer hallID) {
        this.hallID = hallID;
    }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(char airConditioned) {
        this.airConditioned = airConditioned;
    }

    public char getStage() {
        return stage;
    }

    public void setStage(char stage) {
        this.stage = stage;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public char getActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }

    public Date getHallEffectiveDate() {
        return hallEffectiveDate;
    }

    public void setHallEffectiveDate(Date hallEffectiveDate) {
        this.hallEffectiveDate = hallEffectiveDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @XmlTransient
    public Collection<EventServiceProviderInfo> getEventServiceProviderInfoCollection() {
        return eventServiceProviderInfoCollection;
    }

    public void setEventServiceProviderInfoCollection(Collection<EventServiceProviderInfo> eventServiceProviderInfoCollection) {
        this.eventServiceProviderInfoCollection = eventServiceProviderInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hallID != null ? hallID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenueHallInfo)) {
            return false;
        }
        VenueHallInfo other = (VenueHallInfo) object;
        if ((this.hallID == null && other.hallID != null) || (this.hallID != null && !this.hallID.equals(other.hallID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.VenueHallInfo[ hallID=" + hallID + " ]";
    }
    
}
