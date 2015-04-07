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
@Table(name = "address_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AddressInfo.findAll", query = "SELECT a FROM AddressInfo a"),
    @NamedQuery(name = "AddressInfo.findByAddressId", query = "SELECT a FROM AddressInfo a WHERE a.addressId = :addressId"),
    @NamedQuery(name = "AddressInfo.findByAddressLine1", query = "SELECT a FROM AddressInfo a WHERE a.addressLine1 = :addressLine1"),
    @NamedQuery(name = "AddressInfo.findByAddressLine2", query = "SELECT a FROM AddressInfo a WHERE a.addressLine2 = :addressLine2"),
    @NamedQuery(name = "AddressInfo.findByPostalCode", query = "SELECT a FROM AddressInfo a WHERE a.postalCode = :postalCode"),
    @NamedQuery(name = "AddressInfo.findByContactName", query = "SELECT a FROM AddressInfo a WHERE a.contactName = :contactName"),
    @NamedQuery(name = "AddressInfo.findByContactPhone", query = "SELECT a FROM AddressInfo a WHERE a.contactPhone = :contactPhone"),
    @NamedQuery(name = "AddressInfo.findByDescription", query = "SELECT a FROM AddressInfo a WHERE a.description = :description"),
    @NamedQuery(name = "AddressInfo.findByEmail", query = "SELECT a FROM AddressInfo a WHERE a.email = :email"),
    @NamedQuery(name = "AddressInfo.findByIsActive", query = "SELECT a FROM AddressInfo a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "AddressInfo.findByIsDeleted", query = "SELECT a FROM AddressInfo a WHERE a.isDeleted = :isDeleted"),
    @NamedQuery(name = "AddressInfo.findByCreatedUser", query = "SELECT a FROM AddressInfo a WHERE a.createdUser = :createdUser"),
    @NamedQuery(name = "AddressInfo.findByCreatedDate", query = "SELECT a FROM AddressInfo a WHERE a.createdDate = :createdDate"),
    @NamedQuery(name = "AddressInfo.findByUpdatedUser", query = "SELECT a FROM AddressInfo a WHERE a.updatedUser = :updatedUser"),
    @NamedQuery(name = "AddressInfo.findByUpdatedDate", query = "SELECT a FROM AddressInfo a WHERE a.updatedDate = :updatedDate"),
    @NamedQuery(name = "AddressInfo.findByCityId", query = "SELECT a FROM AddressInfo a WHERE a.cityId = :cityId"),
    @NamedQuery(name = "AddressInfo.findByLatitude", query = "SELECT a FROM AddressInfo a WHERE a.latitude = :latitude"),
    @NamedQuery(name = "AddressInfo.findByLongitude", query = "SELECT a FROM AddressInfo a WHERE a.longitude = :longitude")})
public class AddressInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDRESS_ID")
    private Integer addressId;
    @Size(max = 200)
    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;
    @Size(max = 200)
    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;
    @Size(max = 10)
    @Column(name = "POSTAL_CODE")
    private String postalCode;
    @Size(max = 50)
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Size(max = 12)
    @Column(name = "CONTACT_PHONE")
    private String contactPhone;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
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
    @Column(name = "CITY_ID")
    private Integer cityId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressId")
    private Collection<VenueInfo> venueInfoCollection;
     @OneToMany(mappedBy = "addressId")
    private Collection<CustomerInfo> customerInfoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressId")
    private Collection<ServiceProviderInfo> serviceProviderInfoCollection;
    @Size(max = 10)
    @Column(name = "LATITUDE")
    private String latitude;
    @Size(max = 10)
    @Column(name = "LONGITUDE")
    private String longitude;


    public AddressInfo() {
    }

    public AddressInfo(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @XmlTransient
    public Collection<VenueInfo> getVenueInfoCollection() {
        return venueInfoCollection;
    }

    public void setVenueInfoCollection(Collection<VenueInfo> venueInfoCollection) {
        this.venueInfoCollection = venueInfoCollection;
    }

     @XmlTransient
    public Collection<CustomerInfo> getCustomerInfoCollection() {
        return customerInfoCollection;
    }

    public void setCustomerInfoCollection(Collection<CustomerInfo> customerInfoCollection) {
        this.customerInfoCollection = customerInfoCollection;
    }

    @XmlTransient
    public Collection<ServiceProviderInfo> getServiceProviderInfoCollection() {
        return serviceProviderInfoCollection;
    }

    public void setServiceProviderInfoCollection(Collection<ServiceProviderInfo> serviceProviderInfoCollection) {
        this.serviceProviderInfoCollection = serviceProviderInfoCollection;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressId != null ? addressId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AddressInfo)) {
            return false;
        }
        AddressInfo other = (AddressInfo) object;
        if ((this.addressId == null && other.addressId != null) || (this.addressId != null && !this.addressId.equals(other.addressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.AddressInfo[ addressId=" + addressId + " ]";
    }
    
}
