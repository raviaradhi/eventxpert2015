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
@Table(name = "view_service_provider_package")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewServiceProviderPackage.findAll", query = "SELECT v FROM ViewServiceProviderPackage v"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByPackageId", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.packageId = :packageId"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByServiceProviderId", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.serviceProviderId = :serviceProviderId"),
    @NamedQuery(name = "ViewServiceProviderPackage.findBySrvPrvdIdEType", query = "SELECT v FROM ViewServiceProviderPackage v WHERE (v.serviceProviderId = :serviceProviderId and  v.eventType = :eventType) or (v.serviceProviderId = :serviceProviderId and v.eventType = -1)"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByPackageName", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.packageName = :packageName"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByDescription", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.description = :description"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByCost", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.cost = :cost"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByUnitOfCost", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.unitOfCost = :unitOfCost"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByEventType", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.eventType = :eventType"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByCostId", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.costId = :costId"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByNegotiatedCost", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.negotiatedCost = :negotiatedCost"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByCustomerDiscount", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.customerDiscount = :customerDiscount"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByFinalAmount", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.finalAmount = :finalAmount"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByServiceTypeCode", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.serviceTypeCode = :serviceTypeCode"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByServiceProviderName", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.serviceProviderName = :serviceProviderName"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByServiceProviderCode", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.serviceProviderCode = :serviceProviderCode"),
    @NamedQuery(name = "ViewServiceProviderPackage.findByTax", query = "SELECT v FROM ViewServiceProviderPackage v WHERE v.tax = :tax")})
public class ViewServiceProviderPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "package_id")
    @Id
    private int packageId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "service_provider_id")
    private int serviceProviderId;
    @Size(max = 50)
    @Column(name = "package_name")
    private String packageName;
    @Size(max = 500)
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
    @Size(min = 1, max = 10)
    @Column(name = "SERVICE_TYPE_CODE")
    private String serviceTypeCode;
    @Size(max = 45)
    @Column(name = "SERVICE_PROVIDER_NAME")
    private String serviceProviderName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "SERVICE_PROVIDER_CODE")
    private String serviceProviderCode;
    @Column(name = "tax")
    private BigDecimal tax;

    public ViewServiceProviderPackage() {
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
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

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceProviderCode() {
        return serviceProviderCode;
    }

    public void setServiceProviderCode(String serviceProviderCode) {
        this.serviceProviderCode = serviceProviderCode;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
}
