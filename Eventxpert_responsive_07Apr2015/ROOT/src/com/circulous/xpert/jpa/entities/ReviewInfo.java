/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "review_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReviewInfo.findAll", query = "SELECT r FROM ReviewInfo r"),
    @NamedQuery(name = "ReviewInfo.findByReviewId", query = "SELECT r FROM ReviewInfo r WHERE r.reviewId = :reviewId"),
    @NamedQuery(name = "ReviewInfo.findByReviewEffectiveDate", query = "SELECT r FROM ReviewInfo r WHERE r.reviewEffectiveDate = :reviewEffectiveDate"),
    @NamedQuery(name = "ReviewInfo.findByReviewEtDate", query = "SELECT r FROM ReviewInfo r WHERE r.reviewEtDate = :reviewEtDate"),
    @NamedQuery(name = "ReviewInfo.findByReviewRating", query = "SELECT r FROM ReviewInfo r WHERE r.reviewRating = :reviewRating"),
    @NamedQuery(name = "ReviewInfo.findByReviewDescription", query = "SELECT r FROM ReviewInfo r WHERE r.reviewDescription = :reviewDescription"),
    @NamedQuery(name = "ReviewInfo.findByIsActive", query = "SELECT r FROM ReviewInfo r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "ReviewInfo.findByIsDeleted", query = "SELECT r FROM ReviewInfo r WHERE r.isDeleted = :isDeleted"),
    @NamedQuery(name = "ReviewInfo.findByCreatedUser", query = "SELECT r FROM ReviewInfo r WHERE r.createdUser = :createdUser"),
    @NamedQuery(name = "ReviewInfo.findByCreatedDate", query = "SELECT r FROM ReviewInfo r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "ReviewInfo.findByUpdatedUser", query = "SELECT r FROM ReviewInfo r WHERE r.updatedUser = :updatedUser"),
    @NamedQuery(name = "ReviewInfo.findByUpdatedDate", query = "SELECT r FROM ReviewInfo r WHERE r.updatedDate = :updatedDate")})
public class ReviewInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "REVIEW_ID")
    private Integer reviewId;
    @Column(name = "REVIEW_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewEffectiveDate;
    @Column(name = "REVIEW_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewEtDate;
    @Column(name = "REVIEW_RATING")
    private Integer reviewRating;
    @Size(max = 500)
    @Column(name = "REVIEW_DESCRIPTION")
    private String reviewDescription;
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
    @JoinColumn(name = "SERVICE_PROVIDER_ID", referencedColumnName = "service_provider_id")
    @ManyToOne(optional = false)
    private ServiceProviderInfo serviceProviderId;

    public ReviewInfo() {
    }

    public ReviewInfo(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Date getReviewEffectiveDate() {
        return reviewEffectiveDate;
    }

    public void setReviewEffectiveDate(Date reviewEffectiveDate) {
        this.reviewEffectiveDate = reviewEffectiveDate;
    }

    public Date getReviewEtDate() {
        return reviewEtDate;
    }

    public void setReviewEtDate(Date reviewEtDate) {
        this.reviewEtDate = reviewEtDate;
    }

    public Integer getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(Integer reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
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

    public ServiceProviderInfo getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(ServiceProviderInfo serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewId != null ? reviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReviewInfo)) {
            return false;
        }
        ReviewInfo other = (ReviewInfo) object;
        if ((this.reviewId == null && other.reviewId != null) || (this.reviewId != null && !this.reviewId.equals(other.reviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ReviewInfo[ reviewId=" + reviewId + " ]";
    }
    
}
