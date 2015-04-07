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
@Table(name = "eventxpert_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventxpertUser.findAll", query = "SELECT e FROM EventxpertUser e"),
    @NamedQuery(name = "EventxpertUser.findByUserId", query = "SELECT e FROM EventxpertUser e WHERE e.userId = :userId"),
    @NamedQuery(name = "EventxpertUser.findByUsername", query = "SELECT e FROM EventxpertUser e WHERE e.username = :username"),
    @NamedQuery(name = "EventxpertUser.findByPassword", query = "SELECT e FROM EventxpertUser e WHERE e.password = :password"),
    @NamedQuery(name = "EventxpertUser.findByEmailaddr", query = "SELECT e FROM EventxpertUser e WHERE e.emailaddr = :emailaddr"),
    @NamedQuery(name = "EventxpertUser.findByUserCategory", query = "SELECT e FROM EventxpertUser e WHERE e.userCategory = :userCategory"),
    @NamedQuery(name = "EventxpertUser.findByUserType", query = "SELECT e FROM EventxpertUser e WHERE e.userType = :userType"),
    @NamedQuery(name = "EventxpertUser.findByPasswordHint", query = "SELECT e FROM EventxpertUser e WHERE e.passwordHint = :passwordHint"),
    @NamedQuery(name = "EventxpertUser.findByPasswordAns", query = "SELECT e FROM EventxpertUser e WHERE e.passwordAns = :passwordAns"),
    @NamedQuery(name = "EventxpertUser.findByActivationKey", query = "SELECT e FROM EventxpertUser e WHERE e.activationKey = :activationKey"),
    @NamedQuery(name = "EventxpertUser.findByISActive", query = "SELECT e FROM EventxpertUser e WHERE e.iSActive = :iSActive"),
    @NamedQuery(name = "EventxpertUser.findByCreatedBy", query = "SELECT e FROM EventxpertUser e WHERE e.createdBy = :createdBy"),
    @NamedQuery(name = "EventxpertUser.findByCreatedDate", query = "SELECT e FROM EventxpertUser e WHERE e.createdDate = :createdDate"),
    @NamedQuery(name = "EventxpertUser.findByModifiedBy", query = "SELECT e FROM EventxpertUser e WHERE e.modifiedBy = :modifiedBy"),
    @NamedQuery(name = "EventxpertUser.findByLastLogin", query = "SELECT e FROM EventxpertUser e WHERE e.lastLogin = :lastLogin")})
public class EventxpertUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    @Size(max = 20)
    @Column(name = "username")
    private String username;
    @Size(max = 50)
    @Column(name = "password")
    private String password;
    @Size(max = 45)
    @Column(name = "emailaddr")
    private String emailaddr;
    @Size(max = 40)
    @Column(name = "user_category")
    private String userCategory;
    @Size(max = 40)
    @Column(name = "user_type")
    private String userType;
    @Size(max = 150)
    @Column(name = "password_hint")
    private String passwordHint;
    @Size(max = 150)
    @Column(name = "password_ans")
    private String passwordAns;
    @Size(max = 50)
    @Column(name = "activation_key")
    private String activationKey;
    @Size(max = 2)
    @Column(name = "IS_Active")
    private String iSActive;
    @Size(max = 45)
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Size(max = 45)
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "last_login")
    @Temporal(TemporalType.DATE)
    private Date lastLogin;
    @OneToMany(mappedBy = "userId")
    private Collection<VenueInfo> venueInfoCollection;

    public EventxpertUser() {
    }

    public EventxpertUser(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailaddr() {
        return emailaddr;
    }

    public void setEmailaddr(String emailaddr) {
        this.emailaddr = emailaddr;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getPasswordAns() {
        return passwordAns;
    }

    public void setPasswordAns(String passwordAns) {
        this.passwordAns = passwordAns;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getISActive() {
        return iSActive;
    }

    public void setISActive(String iSActive) {
        this.iSActive = iSActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventxpertUser)) {
            return false;
        }
        EventxpertUser other = (EventxpertUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EventxpertUser[ userId=" + userId + " ]";
    }
    
}
