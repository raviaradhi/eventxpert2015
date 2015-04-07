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
@Table(name = "package_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PackageInfo.findAll", query = "SELECT p FROM PackageInfo p"),
    @NamedQuery(name = "PackageInfo.findByPackageId", query = "SELECT p FROM PackageInfo p WHERE p.packageId = :packageId"),
    @NamedQuery(name = "PackageInfo.findByServiceProviderId", query = "SELECT p FROM PackageInfo p WHERE p.serviceProviderId = :serviceProviderId"),
    @NamedQuery(name = "PackageInfo.findByPackageEffectiveDate", query = "SELECT p FROM PackageInfo p WHERE p.packageEffectiveDate = :packageEffectiveDate"),
    @NamedQuery(name = "PackageInfo.findByPackageEtDate", query = "SELECT p FROM PackageInfo p WHERE p.packageEtDate = :packageEtDate"),
    @NamedQuery(name = "PackageInfo.findByPackageName", query = "SELECT p FROM PackageInfo p WHERE p.packageName = :packageName"),
    @NamedQuery(name = "PackageInfo.findByDescription", query = "SELECT p FROM PackageInfo p WHERE p.description = :description"),
    @NamedQuery(name = "PackageInfo.findByCost", query = "SELECT p FROM PackageInfo p WHERE p.cost = :cost"),
    @NamedQuery(name = "PackageInfo.findByUnitOfCost", query = "SELECT p FROM PackageInfo p WHERE p.unitOfCost = :unitOfCost"),
    @NamedQuery(name = "PackageInfo.findByIsActive", query = "SELECT p FROM PackageInfo p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "PackageInfo.findByIsDeleted", query = "SELECT p FROM PackageInfo p WHERE p.isDeleted = :isDeleted"),
    @NamedQuery(name = "PackageInfo.findByCreatedUser", query = "SELECT p FROM PackageInfo p WHERE p.createdUser = :createdUser"),
    @NamedQuery(name = "PackageInfo.findByCreatedDate", query = "SELECT p FROM PackageInfo p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "PackageInfo.findByUpdatedUser", query = "SELECT p FROM PackageInfo p WHERE p.updatedUser = :updatedUser"),
    @NamedQuery(name = "PackageInfo.findByUpdatedDate", query = "SELECT p FROM PackageInfo p WHERE p.updatedDate = :updatedDate"),
    @NamedQuery(name = "PackageInfo.findByEventType", query = "SELECT p FROM PackageInfo p WHERE p.eventType = :eventType"),
    @NamedQuery(name = "PackageInfo.findByMinGuests", query = "SELECT p FROM PackageInfo p WHERE p.minGuests = :minGuests")})
public class PackageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "package_id")
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
    @Size(max = 500)
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "MIN_GUESTS")
    private int minGuests;
    @JoinColumn(name = "event_type", referencedColumnName = "EVENT_ID")
    @ManyToOne
    private EventTypeMaster eventType;
    @JoinColumn(name = "SERVICE_PROVIDER_ID", referencedColumnName = "service_provider_id")
    @ManyToOne(optional = false)
    private ServiceProviderInfo serviceProviderId;
    @JoinColumn(name = "UNIT_OF_COST", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnitCosts unitOfCost;

    public PackageInfo() {
    }

    public PackageInfo(Integer packageId) {
        this.packageId = packageId;
    }

    public PackageInfo(Integer packageId, BigDecimal cost, int minGuests) {
        this.packageId = packageId;
        this.cost = cost;
        this.minGuests = minGuests;
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

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public EventTypeMaster getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeMaster eventType) {
        this.eventType = eventType;
    }

    public ServiceProviderInfo getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(ServiceProviderInfo serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
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
        if (!(object instanceof PackageInfo)) {
            return false;
        }
        PackageInfo other = (PackageInfo) object;
        if ((this.packageId == null && other.packageId != null) || (this.packageId != null && !this.packageId.equals(other.packageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.PackageInfo[ packageId=" + packageId + " ]";
    }
    
}
