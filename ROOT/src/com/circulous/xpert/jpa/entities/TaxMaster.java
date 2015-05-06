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
@Table(name = "tax_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxMaster.findAll", query = "SELECT t FROM TaxMaster t"),
    @NamedQuery(name = "TaxMaster.findByTaxId", query = "SELECT t FROM TaxMaster t WHERE t.taxId = :taxId"),
    @NamedQuery(name = "TaxMaster.findByVendorId", query = "SELECT t FROM TaxMaster t WHERE t.vendorId = :vendorId"),
    @NamedQuery(name = "TaxMaster.findByTax", query = "SELECT t FROM TaxMaster t WHERE t.tax = :tax"),
    @NamedQuery(name = "TaxMaster.findByVendorType", query = "SELECT t FROM TaxMaster t WHERE t.vendorType = :vendorType"),
    @NamedQuery(name = "TaxMaster.findByIsActive", query = "SELECT t FROM TaxMaster t WHERE t.isActive = :isActive")})
public class TaxMaster implements Serializable {
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

    public TaxMaster() {
    }

    public TaxMaster(Integer taxId) {
        this.taxId = taxId;
    }

    public TaxMaster(Integer taxId, int vendorId, BigDecimal tax) {
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
        if (!(object instanceof TaxMaster)) {
            return false;
        }
        TaxMaster other = (TaxMaster) object;
        if ((this.taxId == null && other.taxId != null) || (this.taxId != null && !this.taxId.equals(other.taxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.TaxMaster[ taxId=" + taxId + " ]";
    }
    
}
