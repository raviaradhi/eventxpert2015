/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */ 
@Entity
@Table(name = "event_dates")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventDates.findAll", query = "SELECT e FROM EventDates e"),
    @NamedQuery(name = "EventDates.findByDateId", query = "SELECT e FROM EventDates e WHERE e.dateId = :dateId"),
    @NamedQuery(name = "EventDates.findByEventDate", query = "SELECT e FROM EventDates e WHERE e.eventDate = :eventDate"),
    @NamedQuery(name = "EventDates.findByServiceProviderId", query = "SELECT e FROM EventDates e WHERE e.serviceProviderId = :serviceProviderId")})
public class EventDates implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_id")
    private Integer dateId;
    @Column(name = "event_date")
    @Temporal(TemporalType.DATE)
    private Date eventDate;
    @Column(name = "service_provider_id")
    private Integer serviceProviderId;

    public EventDates() {
    }

    public EventDates(Integer dateId) {
        this.dateId = dateId;
    }

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dateId != null ? dateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventDates)) {
            return false;
        }
        EventDates other = (EventDates) object;
        if ((this.dateId == null && other.dateId != null) || (this.dateId != null && !this.dateId.equals(other.dateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.EventDates[ dateId=" + dateId + " ]";
    }
    
}
