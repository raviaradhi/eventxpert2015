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
@Table(name = "customer_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerInfo.findAll", query = "SELECT c FROM CustomerInfo c"),
    @NamedQuery(name = "CustomerInfo.findByCustomerId", query = "SELECT c FROM CustomerInfo c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "CustomerInfo.findByCustomerEffectiveDate", query = "SELECT c FROM CustomerInfo c WHERE c.customerEffectiveDate = :customerEffectiveDate"),
    @NamedQuery(name = "CustomerInfo.findByCustomerEtDate", query = "SELECT c FROM CustomerInfo c WHERE c.customerEtDate = :customerEtDate"),
    @NamedQuery(name = "CustomerInfo.findByFirstname", query = "SELECT c FROM CustomerInfo c WHERE c.firstname = :firstname"),
    @NamedQuery(name = "CustomerInfo.findByLastname", query = "SELECT c FROM CustomerInfo c WHERE c.lastname = :lastname"),
    @NamedQuery(name = "CustomerInfo.findByUserId", query = "SELECT c FROM CustomerInfo c WHERE c.userId = :userId"),
    @NamedQuery(name = "CustomerInfo.findByPassword", query = "SELECT c FROM CustomerInfo c WHERE c.password = :password"),
    @NamedQuery(name = "CustomerInfo.findByDescription", query = "SELECT c FROM CustomerInfo c WHERE c.description = :description"),
    @NamedQuery(name = "CustomerInfo.findByPhone", query = "SELECT c FROM CustomerInfo c WHERE c.phone = :phone"),
    @NamedQuery(name = "CustomerInfo.findByEmail", query = "SELECT c FROM CustomerInfo c WHERE c.email = :email"),
    @NamedQuery(name = "CustomerInfo.findByIsActive", query = "SELECT c FROM CustomerInfo c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "CustomerInfo.findByIsDeleted", query = "SELECT c FROM CustomerInfo c WHERE c.isDeleted = :isDeleted"),
    @NamedQuery(name = "CustomerInfo.findByCreatedUser", query = "SELECT c FROM CustomerInfo c WHERE c.createdUser = :createdUser"),
    @NamedQuery(name = "CustomerInfo.findByCreatedDate", query = "SELECT c FROM CustomerInfo c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CustomerInfo.findByUpdatedUser", query = "SELECT c FROM CustomerInfo c WHERE c.updatedUser = :updatedUser"),
    @NamedQuery(name = "CustomerInfo.findByUpdatedDate", query = "SELECT c FROM CustomerInfo c WHERE c.updatedDate = :updatedDate"),
    @NamedQuery(name = "CustomerInfo.findByActivationKey", query = "SELECT c FROM CustomerInfo c WHERE c.activationKey = :activationKey")})
public class CustomerInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Column(name = "CUSTOMER_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date customerEffectiveDate;
    @Column(name = "CUSTOMER_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date customerEtDate;
    @Size(max = 50)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 50)
    @Column(name = "LASTNAME")
    private String lastname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_ID")
    private String userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 10)
    @Column(name = "phone")
    private String phone;
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
    @Size(max = 200)
    @Column(name = "activation_key")
    private String activationKey;
    @OneToMany(mappedBy = "customerId")
    private Collection<EventInfo> eventInfoCollection;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
    @ManyToOne
    private AreaTypeMaster areaId;
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @ManyToOne
    private AddressInfo addressId;
    @Column(name = "last_login")
    @Temporal(TemporalType.DATE)
    private Date lastLogin;

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    
    public CustomerInfo() {
    }

    public CustomerInfo(Integer customerId) {
        this.customerId = customerId;
    }

    public CustomerInfo(Integer customerId, String userId, String password) {
        this.customerId = customerId;
        this.userId = userId;
        this.password = password;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getCustomerEffectiveDate() {
        return customerEffectiveDate;
    }

    public void setCustomerEffectiveDate(Date customerEffectiveDate) {
        this.customerEffectiveDate = customerEffectiveDate;
    }

    public Date getCustomerEtDate() {
        return customerEtDate;
    }

    public void setCustomerEtDate(Date customerEtDate) {
        this.customerEtDate = customerEtDate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    @XmlTransient
    public Collection<EventInfo> getEventInfoCollection() {
        return eventInfoCollection;
    }

    public void setEventInfoCollection(Collection<EventInfo> eventInfoCollection) {
        this.eventInfoCollection = eventInfoCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerInfo)) {
            return false;
        }
        CustomerInfo other = (CustomerInfo) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.CustomerInfo[ customerId=" + customerId + " ]";
    }
    
}
