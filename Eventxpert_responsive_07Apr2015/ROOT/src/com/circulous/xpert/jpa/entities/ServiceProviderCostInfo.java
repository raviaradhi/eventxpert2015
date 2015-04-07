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
@Table(name = "service_provider_cost_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceProviderCostInfo.findAll", query = "SELECT s FROM ServiceProviderCostInfo s"),
    @NamedQuery(name = "ServiceProviderCostInfo.findByCostId", query = "SELECT s FROM ServiceProviderCostInfo s WHERE s.costId = :costId"),
    @NamedQuery(name = "ServiceProviderCostInfo.findByPackageId", query = "SELECT s FROM ServiceProviderCostInfo s WHERE s.packageId = :packageId"),
    @NamedQuery(name = "ServiceProviderCostInfo.findByVendorCost", query = "SELECT s FROM ServiceProviderCostInfo s WHERE s.vendorCost = :vendorCost"),
    @NamedQuery(name = "ServiceProviderCostInfo.findByNegotiatedCost", query = "SELECT s FROM ServiceProviderCostInfo s WHERE s.negotiatedCost = :negotiatedCost"),
    @NamedQuery(name = "ServiceProviderCostInfo.findByCustomerDiscount", query = "SELECT s FROM ServiceProviderCostInfo s WHERE s.customerDiscount = :customerDiscount"),
    @NamedQuery(name = "ServiceProviderCostInfo.findByFinalAmount", query = "SELECT s FROM ServiceProviderCostInfo s WHERE s.finalAmount = :finalAmount")})
public class ServiceProviderCostInfo implements Serializable {
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
    @JoinColumn(name = "package_id", referencedColumnName = "package_id")
    @ManyToOne(optional = false)
    private PackageInfo packageId;

    public ServiceProviderCostInfo() {
    }

    public ServiceProviderCostInfo(Integer costId) {
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

    public PackageInfo getPackageId() {
        return packageId;
    }

    public void setPackageId(PackageInfo packageId) {
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
        if (!(object instanceof ServiceProviderCostInfo)) {
            return false;
        }
        ServiceProviderCostInfo other = (ServiceProviderCostInfo) object;
        if ((this.costId == null && other.costId != null) || (this.costId != null && !this.costId.equals(other.costId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.ServiceProviderCostInfo[ costId=" + costId + " ]";
    }
    
}
