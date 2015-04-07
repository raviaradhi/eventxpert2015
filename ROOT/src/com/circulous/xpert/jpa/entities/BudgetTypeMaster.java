/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "budget_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BudgetTypeMaster.findAll", query = "SELECT b FROM BudgetTypeMaster b"),
    @NamedQuery(name = "BudgetTypeMaster.findByBudgetId", query = "SELECT b FROM BudgetTypeMaster b WHERE b.budgetId = :budgetId"),
    @NamedQuery(name = "BudgetTypeMaster.findByBudgetType", query = "SELECT b FROM BudgetTypeMaster b WHERE b.budgetType = :budgetType"),
    @NamedQuery(name = "BudgetTypeMaster.findByBudgetEffectiveDate", query = "SELECT b FROM BudgetTypeMaster b WHERE b.budgetEffectiveDate = :budgetEffectiveDate"),
    @NamedQuery(name = "BudgetTypeMaster.findByBudgetDescription", query = "SELECT b FROM BudgetTypeMaster b WHERE b.budgetDescription = :budgetDescription"),
    @NamedQuery(name = "BudgetTypeMaster.findByIsActive", query = "SELECT b FROM BudgetTypeMaster b WHERE b.isActive = :isActive"),
    @NamedQuery(name = "BudgetTypeMaster.findByIsDeleted", query = "SELECT b FROM BudgetTypeMaster b WHERE b.isDeleted = :isDeleted"),
    @NamedQuery(name = "BudgetTypeMaster.findByCreatedUser", query = "SELECT b FROM BudgetTypeMaster b WHERE b.createdUser = :createdUser"),
    @NamedQuery(name = "BudgetTypeMaster.findByCreatedDate", query = "SELECT b FROM BudgetTypeMaster b WHERE b.createdDate = :createdDate"),
    @NamedQuery(name = "BudgetTypeMaster.findByUpdatedUser", query = "SELECT b FROM BudgetTypeMaster b WHERE b.updatedUser = :updatedUser"),
    @NamedQuery(name = "BudgetTypeMaster.findByUpdatedDate", query = "SELECT b FROM BudgetTypeMaster b WHERE b.updatedDate = :updatedDate")})
public class BudgetTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BUDGET_ID")
    private Integer budgetId;
   // @Size(max = 4)
    @Column(name = "BUDGET_TYPE")
    private String budgetType;
    @Column(name = "BUDGET_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date budgetEffectiveDate;
   // @Size(max = 100)
    @Column(name = "BUDGET_DESCRIPTION")
    private String budgetDescription;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
   // @Size(max = 50)
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
   // @Size(max = 50)
    @Column(name = "UPDATED_USER")
    private String updatedUser;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "budgetId")
    private Collection<BudgetCurrencyTypeMaster> budgetCurrencyTypeMasterCollection;

    public BudgetTypeMaster() {
    }

    public BudgetTypeMaster(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public Integer getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public Date getBudgetEffectiveDate() {
        return budgetEffectiveDate;
    }

    public void setBudgetEffectiveDate(Date budgetEffectiveDate) {
        this.budgetEffectiveDate = budgetEffectiveDate;
    }

    public String getBudgetDescription() {
        return budgetDescription;
    }

    public void setBudgetDescription(String budgetDescription) {
        this.budgetDescription = budgetDescription;
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

    @XmlTransient
    public Collection<BudgetCurrencyTypeMaster> getBudgetCurrencyTypeMasterCollection() {
        return budgetCurrencyTypeMasterCollection;
    }

    public void setBudgetCurrencyTypeMasterCollection(Collection<BudgetCurrencyTypeMaster> budgetCurrencyTypeMasterCollection) {
        this.budgetCurrencyTypeMasterCollection = budgetCurrencyTypeMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (budgetId != null ? budgetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BudgetTypeMaster)) {
            return false;
        }
        BudgetTypeMaster other = (BudgetTypeMaster) object;
        if ((this.budgetId == null && other.budgetId != null) || (this.budgetId != null && !this.budgetId.equals(other.budgetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.BudgetTypeMaster[ budgetId=" + budgetId + " ]";
    }
    
}
