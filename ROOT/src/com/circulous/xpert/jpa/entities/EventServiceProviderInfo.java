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
@Table(name = "event_service_provider_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventServiceProviderInfo.findAll", query = "SELECT e FROM EventServiceProviderInfo e"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderId", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderId = :eventServiceProviderId"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventId", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventId = :eventId"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderEffectiveDate", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderEffectiveDate = :eventServiceProviderEffectiveDate"),
    @NamedQuery(name = "EventServiceProviderInfo.findByServiceProviderId", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.serviceProviderId = :serviceProviderId"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderEtDate", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderEtDate = :eventServiceProviderEtDate"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderTax", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderTax = :eventServiceProviderTax"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderTaxamt", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderTaxamt = :eventServiceProviderTaxamt"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderTotamt", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderTotamt = :eventServiceProviderTotamt"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderCost", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderCost = :eventServiceProviderCost"),
    @NamedQuery(name = "EventServiceProviderInfo.findByTotalGuestToBeServed", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.totalGuestToBeServed = :totalGuestToBeServed"),
    @NamedQuery(name = "EventServiceProviderInfo.findByEventServiceProviderPaidAmount", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.eventServiceProviderPaidAmount = :eventServiceProviderPaidAmount"),
    @NamedQuery(name = "EventServiceProviderInfo.findByVenue", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.venue = :venue"),
    @NamedQuery(name = "EventServiceProviderInfo.findByIsActive", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.isActive = :isActive"),
    @NamedQuery(name = "EventServiceProviderInfo.findByIsDeleted", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.isDeleted = :isDeleted"),
    @NamedQuery(name = "EventServiceProviderInfo.findByCreatedUser", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.createdUser = :createdUser"),
    @NamedQuery(name = "EventServiceProviderInfo.findByCreatedDate", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.createdDate = :createdDate"),
    @NamedQuery(name = "EventServiceProviderInfo.findByUpdatedUser", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.updatedUser = :updatedUser"),
    @NamedQuery(name = "EventServiceProviderInfo.findByUpdatedDate", query = "SELECT e FROM EventServiceProviderInfo e WHERE e.updatedDate = :updatedDate")})
public class EventServiceProviderInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_SERVICE_PROVIDER_ID")
    private Integer eventServiceProviderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_SERVICE_PROVIDER_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventServiceProviderEffectiveDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SERVICE_PROVIDER_ID")
    private int serviceProviderId;
    @Column(name = "EVENT_SERVICE_PROVIDER_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventServiceProviderEtDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "EVENT_SERVICE_PROVIDER_TAX")
    private BigDecimal eventServiceProviderTax;
    @Column(name = "EVENT_SERVICE_PROVIDER_TAXAMT")
    private BigDecimal eventServiceProviderTaxamt;
    @Column(name = "EVENT_SERVICE_PROVIDER_TOTAMT")
    private BigDecimal eventServiceProviderTotamt;
    @Column(name = "EVENT_SERVICE_PROVIDER_COST")
    private BigDecimal eventServiceProviderCost;
    @Column(name = "TOTAL_GUEST_TO_BE_SERVED")
    private Integer totalGuestToBeServed;
    @Column(name = "EVENT_SERVICE_PROVIDER_PAID_AMOUNT")
    private BigDecimal eventServiceProviderPaidAmount;
    @Column(name = "venue")
    private Character venue;
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
    @JoinColumn(name = "hall_id", referencedColumnName = "Hall_ID")
    @ManyToOne
    private VenueHallInfo hallId;
    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
    @ManyToOne(optional = false)
    private EventInfo eventId;

    public EventServiceProviderInfo() {
    }

    public EventServiceProviderInfo(Integer eventServiceProviderId) {
        this.eventServiceProviderId = eventServiceProviderId;
    }

    public EventServiceProviderInfo(Integer eventServiceProviderId, Date eventServiceProviderEffectiveDate, int serviceProviderId) {
        this.eventServiceProviderId = eventServiceProviderId;
        this.eventServiceProviderEffectiveDate = eventServiceProviderEffectiveDate;
        this.serviceProviderId = serviceProviderId;
    }

    public Integer getEventServiceProviderId() {
        return eventServiceProviderId;
    }

    public void setEventServiceProviderId(Integer eventServiceProviderId) {
        this.eventServiceProviderId = eventServiceProviderId;
    }

    public Date getEventServiceProviderEffectiveDate() {
        return eventServiceProviderEffectiveDate;
    }

    public void setEventServiceProviderEffectiveDate(Date eventServiceProviderEffectiveDate) {
        this.eventServiceProviderEffectiveDate = eventServiceProviderEffectiveDate;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Date getEventServiceProviderEtDate() {
        return eventServiceProviderEtDate;
    }

    public void setEventServiceProviderEtDate(Date eventServiceProviderEtDate) {
        this.eventServiceProviderEtDate = eventServiceProviderEtDate;
    }

    public BigDecimal getEventServiceProviderTax() {
        return eventServiceProviderTax;
    }

    public void setEventServiceProviderTax(BigDecimal eventServiceProviderTax) {
        this.eventServiceProviderTax = eventServiceProviderTax;
    }

    public BigDecimal getEventServiceProviderTaxamt() {
        return eventServiceProviderTaxamt;
    }

    public void setEventServiceProviderTaxamt(BigDecimal eventServiceProviderTaxamt) {
        this.eventServiceProviderTaxamt = eventServiceProviderTaxamt;
    }

    public BigDecimal getEventServiceProviderTotamt() {
        return eventServiceProviderTotamt;
    }

    public void setEventServiceProviderTotamt(BigDecimal eventServiceProviderTotamt) {
        this.eventServiceProviderTotamt = eventServiceProviderTotamt;
    }

    public BigDecimal getEventServiceProviderCost() {
        return eventServiceProviderCost;
    }

    public void setEventServiceProviderCost(BigDecimal eventServiceProviderCost) {
        this.eventServiceProviderCost = eventServiceProviderCost;
    }

    public Integer getTotalGuestToBeServed() {
        return totalGuestToBeServed;
    }

    public void setTotalGuestToBeServed(Integer totalGuestToBeServed) {
        this.totalGuestToBeServed = totalGuestToBeServed;
    }

    public BigDecimal getEventServiceProviderPaidAmount() {
        return eventServiceProviderPaidAmount;
    }

    public void setEventServiceProviderPaidAmount(BigDecimal eventServiceProviderPaidAmount) {
        this.eventServiceProviderPaidAmount = eventServiceProviderPaidAmount;
    }

    public Character getVenue() {
        return venue;
    }

    public void setVenue(Character venue) {
        this.venue = venue;
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

    public VenueHallInfo getHallId() {
        return hallId;
    }

    public void setHallId(VenueHallInfo hallId) {
        this.hallId = hallId;
    }

    public EventInfo getEventId() {
        return eventId;
    }

    public void setEventId(EventInfo eventId) {
        this.eventId = eventId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventServiceProviderId != null ? eventServiceProviderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventServiceProviderInfo)) {
            return false;
        }
        EventServiceProviderInfo other = (EventServiceProviderInfo) object;
        if ((this.eventServiceProviderId == null && other.eventServiceProviderId != null) || (this.eventServiceProviderId != null && !this.eventServiceProviderId.equals(other.eventServiceProviderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EventServiceProviderInfo[ eventServiceProviderId=" + eventServiceProviderId + " ]";
    }
    
}
