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
@Table(name = "service_provider_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceProviderInfo.findAll", query = "SELECT s FROM ServiceProviderInfo s"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceProviderId", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceProviderId = :serviceProviderId"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceProviderCode", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceProviderCode = :serviceProviderCode"),
    @NamedQuery(name = "ServiceProviderInfo.findByUserId", query = "SELECT s FROM ServiceProviderInfo s WHERE s.userId = :userId"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceProviderName", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceProviderName = :serviceProviderName"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceProviderEffectiveDate", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceProviderEffectiveDate = :serviceProviderEffectiveDate"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceProviderEtDate", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceProviderEtDate = :serviceProviderEtDate"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceTypeCode", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceTypeCode = :serviceTypeCode"),
    @NamedQuery(name = "ServiceProviderInfo.findByServiceTypeCodeActive", query = "SELECT s FROM ServiceProviderInfo s WHERE s.serviceTypeCode = :serviceTypeCode and s.isActive = :isActive"),
    @NamedQuery(name = "ServiceProviderInfo.findByIsActive", query = "SELECT s FROM ServiceProviderInfo s WHERE s.isActive = :isActive"),
    @NamedQuery(name = "ServiceProviderInfo.findByIsDeleted", query = "SELECT s FROM ServiceProviderInfo s WHERE s.isDeleted = :isDeleted"),
    @NamedQuery(name = "ServiceProviderInfo.findByCreatedUser", query = "SELECT s FROM ServiceProviderInfo s WHERE s.createdUser = :createdUser"),
    @NamedQuery(name = "ServiceProviderInfo.findByCreatedDate", query = "SELECT s FROM ServiceProviderInfo s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "ServiceProviderInfo.findByUpdatedUser", query = "SELECT s FROM ServiceProviderInfo s WHERE s.updatedUser = :updatedUser"),
    @NamedQuery(name = "ServiceProviderInfo.findByUpdatedDate", query = "SELECT s FROM ServiceProviderInfo s WHERE s.updatedDate = :updatedDate"),
    @NamedQuery(name = "ServiceProviderInfo.findByAcceptTermsConditions", query = "SELECT s FROM ServiceProviderInfo s WHERE s.acceptTermsConditions = :acceptTermsConditions")})
public class ServiceProviderInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "service_provider_id")
    private Integer serviceProviderId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "SERVICE_PROVIDER_CODE")
    private String serviceProviderCode;
    @Size(max = 45)
    @Column(name = "SERVICE_PROVIDER_NAME")
    private String serviceProviderName;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "SERVICE_PROVIDER_DESC")
    private String serviceProviderDesc;
    @Column(name = "SERVICE_PROVIDER_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceProviderEffectiveDate;
    @Column(name = "SERVICE_PROVIDER_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceProviderEtDate;
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
    @Column(name = "ACCEPT_TERMS_CONDITIONS")
    private Character acceptTermsConditions;
    @JoinColumn(name = "USER_ID", referencedColumnName = "user_id")
    @ManyToOne
    private EventxpertUser userId;
    @JoinColumn(name = "SERVICE_TYPE_CODE", referencedColumnName = "SERVICE_TYPE_CODE")
    @ManyToOne(optional = false)
    private ServiceTypeMaster serviceTypeCode;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
    @ManyToOne(optional = false)
    private AreaTypeMaster areaId;
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @ManyToOne(optional = false)
    private AddressInfo addressId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceProviderId")
    private Collection<PackageInfo> packageInfoCollection;

    public ServiceProviderInfo() {
    }

    public ServiceProviderInfo(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public ServiceProviderInfo(Integer serviceProviderId, String serviceProviderCode) {
        this.serviceProviderId = serviceProviderId;
        this.serviceProviderCode = serviceProviderCode;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderCode() {
        return serviceProviderCode;
    }

    public void setServiceProviderCode(String serviceProviderCode) {
        this.serviceProviderCode = serviceProviderCode;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceProviderDesc() {
        return serviceProviderDesc;
    }

    public void setServiceProviderDesc(String serviceProviderDesc) {
        this.serviceProviderDesc = serviceProviderDesc;
    }

    public Date getServiceProviderEffectiveDate() {
        return serviceProviderEffectiveDate;
    }

    public void setServiceProviderEffectiveDate(Date serviceProviderEffectiveDate) {
        this.serviceProviderEffectiveDate = serviceProviderEffectiveDate;
    }

    public Date getServiceProviderEtDate() {
        return serviceProviderEtDate;
    }

    public void setServiceProviderEtDate(Date serviceProviderEtDate) {
        this.serviceProviderEtDate = serviceProviderEtDate;
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

    public Character getAcceptTermsConditions() {
        return acceptTermsConditions;
    }

    public void setAcceptTermsConditions(Character acceptTermsConditions) {
        this.acceptTermsConditions = acceptTermsConditions;
    }

    public EventxpertUser getUserId() {
        return userId;
    }

    public void setUserId(EventxpertUser userId) {
        this.userId = userId;
    }

    public ServiceTypeMaster getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(ServiceTypeMaster serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public AreaTypeMaster getAreaId() {
        return areaId;
    }

    public void setAreaId(AreaTypeMaster areaId) {
        this.areaId = areaId;
    }

    public AddressInfo getAddressId() {
        return addressId;
    }

    public void setAddressId(AddressInfo addressId) {
        this.addressId = addressId;
    }

    @XmlTransient
    public Collection<PackageInfo> getPackageInfoCollection() {
        return packageInfoCollection;
    }

    public void setPackageInfoCollection(Collection<PackageInfo> packageInfoCollection) {
        this.packageInfoCollection = packageInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceProviderId != null ? serviceProviderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceProviderInfo)) {
            return false;
        }
        ServiceProviderInfo other = (ServiceProviderInfo) object;
        if ((this.serviceProviderId == null && other.serviceProviderId != null) || (this.serviceProviderId != null && !this.serviceProviderId.equals(other.serviceProviderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ServiceProviderInfo[ serviceProviderId=" + serviceProviderId + " ]";
    }
    
}
