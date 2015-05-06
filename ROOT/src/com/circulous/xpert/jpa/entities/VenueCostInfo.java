/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "venue_cost_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VenueCostInfo.findAll", query = "SELECT v FROM VenueCostInfo v"),
    @NamedQuery(name = "VenueCostInfo.findByCostId", query = "SELECT v FROM VenueCostInfo v WHERE v.costId = :costId"),
    @NamedQuery(name = "VenueCostInfo.findByPackageId", query = "SELECT v FROM VenueCostInfo v WHERE v.packageId = :packageId"),
    @NamedQuery(name = "VenueCostInfo.findByVendorCost", query = "SELECT v FROM VenueCostInfo v WHERE v.vendorCost = :vendorCost"),
    @NamedQuery(name = "VenueCostInfo.findByNegotiatedCost", query = "SELECT v FROM VenueCostInfo v WHERE v.negotiatedCost = :negotiatedCost"),
    @NamedQuery(name = "VenueCostInfo.findByCustomerDiscount", query = "SELECT v FROM VenueCostInfo v WHERE v.customerDiscount = :customerDiscount"),
    @NamedQuery(name = "VenueCostInfo.findByFinalAmount", query = "SELECT v FROM VenueCostInfo v WHERE v.finalAmount = :finalAmount")})
public class VenueCostInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost_id")
    private Integer costId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vendor_cost")
    private BigDecimal vendorCost;
    @Column(name = "negotiated_cost")
    private BigDecimal negotiatedCost;
    @Column(name = "customer_discount")
    private BigDecimal customerDiscount;
    @Column(name = "final_amount")
    private BigDecimal finalAmount;
    @JoinColumn(name = "package_id", referencedColumnName = "PACKAGE_ID")
    @ManyToOne(optional = false)
    private VenuePackageInfo packageId;

    public VenueCostInfo() {
    }

    public VenueCostInfo(Integer costId) {
        this.costId = costId;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }

    public BigDecimal getVendorCost() {
        return vendorCost;
    }

    public void setVendorCost(BigDecimal vendorCost) {
        this.vendorCost = vendorCost;
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

    public VenuePackageInfo getPackageId() {
        return packageId;
    }

    public void setPackageId(VenuePackageInfo packageId) {
        this.packageId = packageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (costId != null ? costId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenueCostInfo)) {
            return false;
        }
        VenueCostInfo other = (VenueCostInfo) object;
        if ((this.costId == null && other.costId != null) || (this.costId != null && !this.costId.equals(other.costId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.VenueCostInfo[ costId=" + costId + " ]";
    }
    
}
