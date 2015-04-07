/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SANJANA
 */
@Entity
@Table(name = "budget_currency_type_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findAll", query = "SELECT b FROM BudgetCurrencyTypeMaster b"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByBudgetCurrencyId", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.budgetCurrencyId = :budgetCurrencyId"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByBudgetCurrencyEffectiveDate", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.budgetCurrencyEffectiveDate = :budgetCurrencyEffectiveDate"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByIsActive", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.isActive = :isActive"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByIsDeleted", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.isDeleted = :isDeleted"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByCreatedUser", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.createdUser = :createdUser"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByCreatedDate", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.createdDate = :createdDate"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByUpdatedUser", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.updatedUser = :updatedUser"),
    @NamedQuery(name = "BudgetCurrencyTypeMaster.findByUpdatedDate", query = "SELECT b FROM BudgetCurrencyTypeMaster b WHERE b.updatedDate = :updatedDate")})
public class BudgetCurrencyTypeMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BUDGET_CURRENCY_ID")
    private Integer budgetCurrencyId;
    @Column(name = "BUDGET_CURRENCY_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date budgetCurrencyEffectiveDate;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
    //@Size(max = 50)
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
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "CURRENCY_ID")
    @ManyToOne(optional = false)
    private CurrencyTypeMaster currencyId;
    @JoinColumn(name = "BUDGET_ID", referencedColumnName = "BUDGET_ID")
    @ManyToOne(optional = false)
    private BudgetTypeMaster budgetId;

    public BudgetCurrencyTypeMaster() {
    }

    public BudgetCurrencyTypeMaster(Integer budgetCurrencyId) {
        this.budgetCurrencyId = budgetCurrencyId;
    }

    public Integer getBudgetCurrencyId() {
        return budgetCurrencyId;
    }

    public void setBudgetCurrencyId(Integer budgetCurrencyId) {
        this.budgetCurrencyId = budgetCurrencyId;
    }

    public Date getBudgetCurrencyEffectiveDate() {
        return budgetCurrencyEffectiveDate;
    }

    public void setBudgetCurrencyEffectiveDate(Date budgetCurrencyEffectiveDate) {
        this.budgetCurrencyEffectiveDate = budgetCurrencyEffectiveDate;
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

    public CurrencyTypeMaster getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyTypeMaster currencyId) {
        this.currencyId = currencyId;
    }

    public BudgetTypeMaster getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(BudgetTypeMaster budgetId) {
        this.budgetId = budgetId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (budgetCurrencyId != null ? budgetCurrencyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BudgetCurrencyTypeMaster)) {
            return false;
        }
        BudgetCurrencyTypeMaster other = (BudgetCurrencyTypeMaster) object;
        if ((this.budgetCurrencyId == null && other.budgetCurrencyId != null) || (this.budgetCurrencyId != null && !this.budgetCurrencyId.equals(other.budgetCurrencyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.BudgetCurrencyTypeMaster[ budgetCurrencyId=" + budgetCurrencyId + " ]";
    }
    
}
