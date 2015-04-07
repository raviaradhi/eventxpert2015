/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "venue_package_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VenuePackageInfo.findAll", query = "SELECT v FROM VenuePackageInfo v"),
    @NamedQuery(name = "VenuePackageInfo.findByPackageId", query = "SELECT v FROM VenuePackageInfo v WHERE v.packageId = :packageId"),
    @NamedQuery(name = "VenuePackageInfo.findByVenueId", query = "SELECT v FROM VenuePackageInfo v WHERE v.venueId = :venueId"),
    @NamedQuery(name = "VenuePackageInfo.findByPackageEffectiveDate", query = "SELECT v FROM VenuePackageInfo v WHERE v.packageEffectiveDate = :packageEffectiveDate"),
    @NamedQuery(name = "VenuePackageInfo.findByPackageEtDate", query = "SELECT v FROM VenuePackageInfo v WHERE v.packageEtDate = :packageEtDate"),
    @NamedQuery(name = "VenuePackageInfo.findByPackageName", query = "SELECT v FROM VenuePackageInfo v WHERE v.packageName = :packageName"),
    @NamedQuery(name = "VenuePackageInfo.findByCost", query = "SELECT v FROM VenuePackageInfo v WHERE v.cost = :cost"),
    @NamedQuery(name = "VenuePackageInfo.findByUnitOfCost", query = "SELECT v FROM VenuePackageInfo v WHERE v.unitOfCost = :unitOfCost"),
    @NamedQuery(name = "VenuePackageInfo.findByEventType", query = "SELECT v FROM VenuePackageInfo v WHERE v.eventType = :eventType"),
    @NamedQuery(name = "VenuePackageInfo.findByIsActive", query = "SELECT v FROM VenuePackageInfo v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VenuePackageInfo.findByIsDeleted", query = "SELECT v FROM VenuePackageInfo v WHERE v.isDeleted = :isDeleted"),
    @NamedQuery(name = "VenuePackageInfo.findByCreatedUser", query = "SELECT v FROM VenuePackageInfo v WHERE v.createdUser = :createdUser"),
    @NamedQuery(name = "VenuePackageInfo.findByCreatedDate", query = "SELECT v FROM VenuePackageInfo v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VenuePackageInfo.findByUpdatedUser", query = "SELECT v FROM VenuePackageInfo v WHERE v.updatedUser = :updatedUser"),
    @NamedQuery(name = "VenuePackageInfo.findByUpdatedDate", query = "SELECT v FROM VenuePackageInfo v WHERE v.updatedDate = :updatedDate")})
public class VenuePackageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PACKAGE_ID")
    private Integer packageId;
    @Column(name = "PACKAGE_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date packageEffectiveDate;
    @Column(name = "PACKAGE_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date packageEtDate;
    @Size(max = 50)
    @Column(name = "PACKAGE_NAME")
    private String packageName;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "DESCRIPTION")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "COST")
    private BigDecimal cost;
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
    @JoinColumn(name = "VENUE_ID", referencedColumnName = "VENUE_ID")
    @ManyToOne(optional = false)
    private VenueInfo venueId;
    @JoinColumn(name = "EVENT_TYPE", referencedColumnName = "EVENT_ID")
    @ManyToOne
    private EventTypeMaster eventType;
    @JoinColumn(name = "UNIT_OF_COST", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnitCosts unitOfCost;

    public VenuePackageInfo() {
    }

    public VenuePackageInfo(Integer packageId) {
        this.packageId = packageId;
    }

    public VenuePackageInfo(Integer packageId, BigDecimal cost) {
        this.packageId = packageId;
        this.cost = cost;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Date getPackageEffectiveDate() {
        return packageEffectiveDate;
    }

    public void setPackageEffectiveDate(Date packageEffectiveDate) {
        this.packageEffectiveDate = packageEffectiveDate;
    }

    public Date getPackageEtDate() {
        return packageEtDate;
    }

    public void setPackageEtDate(Date packageEtDate) {
        this.packageEtDate = packageEtDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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

    public VenueInfo getVenueId() {
        return venueId;
    }

    public void setVenueId(VenueInfo venueId) {
        this.venueId = venueId;
    }

    public EventTypeMaster getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeMaster eventType) {
        this.eventType = eventType;
    }

    public UnitCosts getUnitOfCost() {
        return unitOfCost;
    }

    public void setUnitOfCost(UnitCosts unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (packageId != null ? packageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenuePackageInfo)) {
            return false;
        }
        VenuePackageInfo other = (VenuePackageInfo) object;
        if ((this.packageId == null && other.packageId != null) || (this.packageId != null && !this.packageId.equals(other.packageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.VenuePackageInfo[ packageId=" + packageId + " ]";
    }
    
}
