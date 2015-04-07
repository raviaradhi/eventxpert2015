/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "view_venue_package")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewVenuePackage.findAll", query = "SELECT v FROM ViewVenuePackage v"),
    @NamedQuery(name = "ViewVenuePackage.findByPackageId", query = "SELECT v FROM ViewVenuePackage v WHERE v.packageId = :packageId"),
    @NamedQuery(name = "ViewVenuePackage.findByVenueId", query = "SELECT v FROM ViewVenuePackage v WHERE v.venueId = :venueId"),
    @NamedQuery(name = "ViewVenuePackage.findByVenueIdEType", query = "SELECT v FROM ViewVenuePackage v WHERE (v.venueId = :venueId and  v.eventType = :eventType) or (v.venueId = :venueId and v.eventType = -1)"),
    @NamedQuery(name = "ViewVenuePackage.findByPackageName", query = "SELECT v FROM ViewVenuePackage v WHERE v.packageName = :packageName"),
    @NamedQuery(name = "ViewVenuePackage.findByCost", query = "SELECT v FROM ViewVenuePackage v WHERE v.cost = :cost"),
    @NamedQuery(name = "ViewVenuePackage.findByUnitOfCost", query = "SELECT v FROM ViewVenuePackage v WHERE v.unitOfCost = :unitOfCost"),
    @NamedQuery(name = "ViewVenuePackage.findByEventType", query = "SELECT v FROM ViewVenuePackage v WHERE v.eventType = :eventType"),
    @NamedQuery(name = "ViewVenuePackage.findByCostId", query = "SELECT v FROM ViewVenuePackage v WHERE v.costId = :costId"),
    @NamedQuery(name = "ViewVenuePackage.findByNegotiatedCost", query = "SELECT v FROM ViewVenuePackage v WHERE v.negotiatedCost = :negotiatedCost"),
    @NamedQuery(name = "ViewVenuePackage.findByCustomerDiscount", query = "SELECT v FROM ViewVenuePackage v WHERE v.customerDiscount = :customerDiscount"),
    @NamedQuery(name = "ViewVenuePackage.findByFinalAmount", query = "SELECT v FROM ViewVenuePackage v WHERE v.finalAmount = :finalAmount"),
    @NamedQuery(name = "ViewVenuePackage.findByVenueCODE", query = "SELECT v FROM ViewVenuePackage v WHERE v.venueCODE = :venueCODE"),
    @NamedQuery(name = "ViewVenuePackage.findByVenueNAME", query = "SELECT v FROM ViewVenuePackage v WHERE v.venueNAME = :venueNAME"),
    @NamedQuery(name = "ViewVenuePackage.findByTax", query = "SELECT v FROM ViewVenuePackage v WHERE v.tax = :tax")})
public class ViewVenuePackage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "package_id")
    @Id
    private int packageId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "venue_id")
    private int venueId;
    @Size(max = 50)
    @Column(name = "package_name")
    private String packageName;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost")
    private BigDecimal cost;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "unit_of_cost")
    private String unitOfCost;
    @Column(name = "event_type")
    private Integer eventType;
    @Column(name = "cost_id")
    private Integer costId;
    @Column(name = "negotiated_cost")
    private BigDecimal negotiatedCost;
    @Column(name = "customer_discount")
    private BigDecimal customerDiscount;
    @Column(name = "final_amount")
    private BigDecimal finalAmount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "Venue_CODE")
    private String venueCODE;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "venue_NAME")
    private String venueNAME;
    @Column(name = "tax")
    private BigDecimal tax;

    public ViewVenuePackage() {
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
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

    public String getUnitOfCost() {
        return unitOfCost;
    }

    public void setUnitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }

    public BigDecimal getNegotiatedCost() {
        return negotiatedCost;
    }

    public void setNegotiatedCost(BigDecimal negotiatedCost) {
        this.negotiatedCost = negotiatedCost;
    }

    public BigDecimal getCustomerDiscount() {
        return customerDiscount;
    }

    public void setCustomerDiscount(BigDecimal customerDiscount) {
        this.customerDiscount = customerDiscount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getVenueCODE() {
        return venueCODE;
    }

    public void setVenueCODE(String venueCODE) {
        this.venueCODE = venueCODE;
    }

    public String getVenueNAME() {
        return venueNAME;
    }

    public void setVenueNAME(String venueNAME) {
        this.venueNAME = venueNAME;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
}
