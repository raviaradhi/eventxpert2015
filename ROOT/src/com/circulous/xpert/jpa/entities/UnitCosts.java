/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "unit_costs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnitCosts.findAll", query = "SELECT u FROM UnitCosts u"),
    @NamedQuery(name = "UnitCosts.findById", query = "SELECT u FROM UnitCosts u WHERE u.id = :id"),
    @NamedQuery(name = "UnitCosts.findByUnitCostType", query = "SELECT u FROM UnitCosts u WHERE u.unitCostType = :unitCostType"),
    @NamedQuery(name = "UnitCosts.findByServiceTypeCode", query = "SELECT u FROM UnitCosts u WHERE u.serviceTypeCode = :serviceTypeCode")})
public class UnitCosts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 100)
    @Column(name = "UNIT_COST_TYPE")
    private String unitCostType;
    @Size(max = 100)
    @Column(name = "SERVICE_TYPE_CODE")
    private String serviceTypeCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unitOfCost")
    private Collection<PackageInfo> packageInfoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unitOfCost")
    private Collection<VenuePackageInfo> venuePackageInfoCollection;

    public UnitCosts() {
    }

    public UnitCosts(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitCostType() {
        return unitCostType;
    }

    public void setUnitCostType(String unitCostType) {
        this.unitCostType = unitCostType;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    @XmlTransient
    public Collection<PackageInfo> getPackageInfoCollection() {
        return packageInfoCollection;
    }

    public void setPackageInfoCollection(Collection<PackageInfo> packageInfoCollection) {
        this.packageInfoCollection = packageInfoCollection;
    }

    @XmlTransient
    public Collection<VenuePackageInfo> getVenuePackageInfoCollection() {
        return venuePackageInfoCollection;
    }

    public void setVenuePackageInfoCollection(Collection<VenuePackageInfo> venuePackageInfoCollection) {
        this.venuePackageInfoCollection = venuePackageInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnitCosts)) {
            return false;
        }
        UnitCosts other = (UnitCosts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.UnitCosts[ id=" + id + " ]";
    }
    
}
