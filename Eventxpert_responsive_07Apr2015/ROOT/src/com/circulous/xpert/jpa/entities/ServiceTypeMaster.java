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
@Table(name = "service_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceTypeMaster.findAll", query = "SELECT s FROM ServiceTypeMaster s"),
    @NamedQuery(name = "ServiceTypeMaster.findByServiceTypeCode", query = "SELECT s FROM ServiceTypeMaster s WHERE s.serviceTypeCode = :serviceTypeCode"),
    @NamedQuery(name = "ServiceTypeMaster.findByServiceName", query = "SELECT s FROM ServiceTypeMaster s WHERE s.serviceName = :serviceName"),
    @NamedQuery(name = "ServiceTypeMaster.findByServiceEffectiveDate", query = "SELECT s FROM ServiceTypeMaster s WHERE s.serviceEffectiveDate = :serviceEffectiveDate"),
    @NamedQuery(name = "ServiceTypeMaster.findByServiceDescription", query = "SELECT s FROM ServiceTypeMaster s WHERE s.serviceDescription = :serviceDescription"),
    @NamedQuery(name = "ServiceTypeMaster.findByIsActive", query = "SELECT s FROM ServiceTypeMaster s WHERE s.isActive = :isActive"),
    @NamedQuery(name = "ServiceTypeMaster.findByIsDeleted", query = "SELECT s FROM ServiceTypeMaster s WHERE s.isDeleted = :isDeleted"),
    @NamedQuery(name = "ServiceTypeMaster.findByCreatedUser", query = "SELECT s FROM ServiceTypeMaster s WHERE s.createdUser = :createdUser"),
    @NamedQuery(name = "ServiceTypeMaster.findByCreatedDate", query = "SELECT s FROM ServiceTypeMaster s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "ServiceTypeMaster.findByUpdatedUser", query = "SELECT s FROM ServiceTypeMaster s WHERE s.updatedUser = :updatedUser"),
    @NamedQuery(name = "ServiceTypeMaster.findByUpdatedDate", query = "SELECT s FROM ServiceTypeMaster s WHERE s.updatedDate = :updatedDate")})
public class ServiceTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SERVICE_TYPE_CODE")
    private String serviceTypeCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERVICE_NAME")
    private String serviceName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SERVICE_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceEffectiveDate;
    @Size(max = 100)
    @Column(name = "SERVICE_DESCRIPTION")
    private String serviceDescription;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceTypeCode")
    private Collection<ServiceProviderInfo> serviceProviderInfoCollection;

    public ServiceTypeMaster() {
    }

    public ServiceTypeMaster(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public ServiceTypeMaster(String serviceTypeCode, String serviceName, Date serviceEffectiveDate) {
        this.serviceTypeCode = serviceTypeCode;
        this.serviceName = serviceName;
        this.serviceEffectiveDate = serviceEffectiveDate;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getServiceEffectiveDate() {
        return serviceEffectiveDate;
    }

    public void setServiceEffectiveDate(Date serviceEffectiveDate) {
        this.serviceEffectiveDate = serviceEffectiveDate;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
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
    public Collection<ServiceProviderInfo> getServiceProviderInfoCollection() {
        return serviceProviderInfoCollection;
    }

    public void setServiceProviderInfoCollection(Collection<ServiceProviderInfo> serviceProviderInfoCollection) {
        this.serviceProviderInfoCollection = serviceProviderInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceTypeCode != null ? serviceTypeCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceTypeMaster)) {
            return false;
        }
        ServiceTypeMaster other = (ServiceTypeMaster) object;
        if ((this.serviceTypeCode == null && other.serviceTypeCode != null) || (this.serviceTypeCode != null && !this.serviceTypeCode.equals(other.serviceTypeCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ServiceTypeMaster[ serviceTypeCode=" + serviceTypeCode + " ]";
    }
    
}
