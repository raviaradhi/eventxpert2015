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
@Table(name = "venue_tax_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VenueTaxMaster.findAll", query = "SELECT v FROM VenueTaxMaster v"),
    @NamedQuery(name = "VenueTaxMaster.findByTaxId", query = "SELECT v FROM VenueTaxMaster v WHERE v.taxId = :taxId"),
    @NamedQuery(name = "VenueTaxMaster.findByVendorId", query = "SELECT v FROM VenueTaxMaster v WHERE v.vendorId = :vendorId"),
    @NamedQuery(name = "VenueTaxMaster.findByTax", query = "SELECT v FROM VenueTaxMaster v WHERE v.tax = :tax"),
    @NamedQuery(name = "VenueTaxMaster.findByVendorType", query = "SELECT v FROM VenueTaxMaster v WHERE v.vendorType = :vendorType"),
    @NamedQuery(name = "VenueTaxMaster.findByIsActive", query = "SELECT v FROM VenueTaxMaster v WHERE v.isActive = :isActive")})
public class VenueTaxMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "tax_id")
    private Integer taxId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vendor_id")
    private int vendorId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "tax")
    private BigDecimal tax;
    @Column(name = "vendor_type")
    private Character vendorType;
    @Column(name = "isActive")
    private Character isActive;

    public VenueTaxMaster() {
    }

    public VenueTaxMaster(Integer taxId) {
        this.taxId = taxId;
    }

    public VenueTaxMaster(Integer taxId, int vendorId, BigDecimal tax) {
        this.taxId = taxId;
        this.vendorId = vendorId;
        this.tax = tax;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Character getVendorType() {
        return vendorType;
    }

    public void setVendorType(Character vendorType) {
        this.vendorType = vendorType;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxId != null ? taxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenueTaxMaster)) {
            return false;
        }
        VenueTaxMaster other = (VenueTaxMaster) object;
        if ((this.taxId == null && other.taxId != null) || (this.taxId != null && !this.taxId.equals(other.taxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.VenueTaxMaster[ taxId=" + taxId + " ]";
    }
    
}
