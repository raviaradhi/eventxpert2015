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
@Table(name = "event_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventInfo.findAll", query = "SELECT e FROM EventInfo e"),
    @NamedQuery(name = "EventInfo.findByEventId", query = "SELECT e FROM EventInfo e WHERE e.eventId = :eventId"),
    @NamedQuery(name = "EventInfo.findByEventEffectiveDate", query = "SELECT e FROM EventInfo e WHERE e.eventEffectiveDate = :eventEffectiveDate"),
    @NamedQuery(name = "EventInfo.findByEventEtDate", query = "SELECT e FROM EventInfo e WHERE e.eventEtDate = :eventEtDate"),
    @NamedQuery(name = "EventInfo.findByTotalAmount", query = "SELECT e FROM EventInfo e WHERE e.totalAmount = :totalAmount"),
    @NamedQuery(name = "EventInfo.findByServiceCharges", query = "SELECT e FROM EventInfo e WHERE e.serviceCharges = :serviceCharges"),
    @NamedQuery(name = "EventInfo.findByServiceTax", query = "SELECT e FROM EventInfo e WHERE e.serviceTax = :serviceTax"),
    @NamedQuery(name = "EventInfo.findByCost", query = "SELECT e FROM EventInfo e WHERE e.cost = :cost"),
    @NamedQuery(name = "EventInfo.findByCustomerId", query = "SELECT e FROM EventInfo e WHERE e.customerId = :customerId"),
    @NamedQuery(name = "EventInfo.findByAmountPaidByCustomer", query = "SELECT e FROM EventInfo e WHERE e.amountPaidByCustomer = :amountPaidByCustomer"),
    @NamedQuery(name = "EventInfo.findByEstimatedGuests", query = "SELECT e FROM EventInfo e WHERE e.estimatedGuests = :estimatedGuests"),
    @NamedQuery(name = "EventInfo.findByIsActive", query = "SELECT e FROM EventInfo e WHERE e.isActive = :isActive"),
    @NamedQuery(name = "EventInfo.findByIsDeleted", query = "SELECT e FROM EventInfo e WHERE e.isDeleted = :isDeleted"),
    @NamedQuery(name = "EventInfo.findByCreatedUser", query = "SELECT e FROM EventInfo e WHERE e.createdUser = :createdUser"),
    @NamedQuery(name = "EventInfo.findByCreatedDate", query = "SELECT e FROM EventInfo e WHERE e.createdDate = :createdDate"),
    @NamedQuery(name = "EventInfo.findByUpdatedUser", query = "SELECT e FROM EventInfo e WHERE e.updatedUser = :updatedUser"),
    @NamedQuery(name = "EventInfo.findByUpdatedDate", query = "SELECT e FROM EventInfo e WHERE e.updatedDate = :updatedDate")})
public class EventInfo implements Serializable, Comparable<EventInfo> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_ID")
    private Integer eventId;
    @Column(name = "EVENT_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventEffectiveDate;
    @Column(name = "EVENT_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventEtDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;
    @Column(name = "SERVICE_CHARGES")
    private BigDecimal serviceCharges;
    @Column(name = "SERVICE_TAX")
    private BigDecimal serviceTax;
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "AMOUNT_PAID_BY_CUSTOMER")
    private BigDecimal amountPaidByCustomer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTIMATED_GUESTS")
    private int estimatedGuests;
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
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private CustomerInfo customerId;
    @JoinColumn(name = "EVENT_TYPE", referencedColumnName = "EVENT_ID")
    @ManyToOne
    private EventTypeMaster eventType;
    @JoinColumn(name = "VENUE_TYPE", referencedColumnName = "VENUE_TYPE_ID")
    @ManyToOne
    private VenueTypeMaster venueType;
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "CURRENCY_ID")
    @ManyToOne(optional = false)
    private CurrencyTypeMaster currencyId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
    @ManyToOne(optional = false)
    private AreaTypeMaster areaId;

    public EventInfo() {
    }

    public EventInfo(Integer eventId) {
        this.eventId = eventId;
    }

    public EventInfo(Integer eventId, int estimatedGuests) {
        this.eventId = eventId;
        this.estimatedGuests = estimatedGuests;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Date getEventEffectiveDate() {
        return eventEffectiveDate;
    }

    public void setEventEffectiveDate(Date eventEffectiveDate) {
        this.eventEffectiveDate = eventEffectiveDate;
    }

    public Date getEventEtDate() {
        return eventEtDate;
    }

    public void setEventEtDate(Date eventEtDate) {
        this.eventEtDate = eventEtDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(BigDecimal serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public BigDecimal getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(BigDecimal serviceTax) {
        this.serviceTax = serviceTax;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getAmountPaidByCustomer() {
        return amountPaidByCustomer;
    }

    public void setAmountPaidByCustomer(BigDecimal amountPaidByCustomer) {
        this.amountPaidByCustomer = amountPaidByCustomer;
    }

    public int getEstimatedGuests() {
        return estimatedGuests;
    }

    public void setEstimatedGuests(int estimatedGuests) {
        this.estimatedGuests = estimatedGuests;
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

    public CustomerInfo getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerInfo customerId) {
        this.customerId = customerId;
    }

    public EventTypeMaster getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeMaster eventType) {
        this.eventType = eventType;
    }

    public VenueTypeMaster getVenueType() {
        return venueType;
    }

    public void setVenueType(VenueTypeMaster venueType) {
        this.venueType = venueType;
    }

    public CurrencyTypeMaster getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyTypeMaster currencyId) {
        this.currencyId = currencyId;
    }

    public AreaTypeMaster getAreaId() {
        return areaId;
    }

    public void setAreaId(AreaTypeMaster areaId) {
        this.areaId = areaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventInfo)) {
            return false;
        }
        EventInfo other = (EventInfo) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EventInfo[ eventId=" + eventId + " ]";
    }
    
      @Override
    public int compareTo(EventInfo o) {
        return getEventEffectiveDate().compareTo(o.getEventEffectiveDate());
    }
}
