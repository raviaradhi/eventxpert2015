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
@Table(name = "view_service_provider")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewServiceProvider.findAll", query = "SELECT v FROM ViewServiceProvider v"),
    @NamedQuery(name = "ViewServiceProvider.findByServiceProviderId", query = "SELECT v FROM ViewServiceProvider v WHERE v.serviceProviderId = :serviceProviderId"),
    @NamedQuery(name = "ViewServiceProvider.findByServiceProviderName", query = "SELECT v FROM ViewServiceProvider v WHERE v.serviceProviderName = :serviceProviderName"),
    @NamedQuery(name = "ViewServiceProvider.findByServiceProviderCode", query = "SELECT v FROM ViewServiceProvider v WHERE v.serviceProviderCode = :serviceProviderCode"),
    @NamedQuery(name = "ViewServiceProvider.findByServiceTypeCode", query = "SELECT v FROM ViewServiceProvider v WHERE v.serviceTypeCode = :serviceTypeCode"),
    @NamedQuery(name = "ViewServiceProvider.findByIsActive", query = "SELECT v FROM ViewServiceProvider v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "ViewServiceProvider.findByAddress", query = "SELECT v FROM ViewServiceProvider v WHERE v.address = :address"),
    @NamedQuery(name = "ViewServiceProvider.findByLatitude", query = "SELECT v FROM ViewServiceProvider v WHERE v.latitude = :latitude"),
    @NamedQuery(name = "ViewServiceProvider.findByLongitude", query = "SELECT v FROM ViewServiceProvider v WHERE v.longitude = :longitude"),
    @NamedQuery(name = "ViewServiceProvider.findByAreaName", query = "SELECT v FROM ViewServiceProvider v WHERE v.areaName = :areaName"),
    @NamedQuery(name = "ViewServiceProvider.findByCityName", query = "SELECT v FROM ViewServiceProvider v WHERE v.cityName = :cityName"),
    @NamedQuery(name = "ViewServiceProvider.findByCost", query = "SELECT v FROM ViewServiceProvider v WHERE v.cost = :cost"),
    @NamedQuery(name = "ViewServiceProvider.findByFinalCost", query = "SELECT v FROM ViewServiceProvider v WHERE v.finalCost = :finalCost"),
    @NamedQuery(name = "ViewServiceProvider.findByUnitOfCost", query = "SELECT v FROM ViewServiceProvider v WHERE v.unitOfCost = :unitOfCost"),
    @NamedQuery(name = "ViewServiceProvider.findByMinGuests", query = "SELECT v FROM ViewServiceProvider v WHERE v.minGuests = :minGuests")})
public class ViewServiceProvider implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SERVICE_PROVIDER_ID")
    @Id
    private int serviceProviderId;
    @Size(max = 45)
    @Column(name = "SERVICE_PROVIDER_NAME")
    private String serviceProviderName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "SERVICE_PROVIDER_CODE")
    private String serviceProviderCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SERVICE_TYPE_CODE")
    private String serviceTypeCode;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Size(max = 200)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 10)
    @Column(name = "LATITUDE")
    private String latitude;
    @Size(max = 10)
    @Column(name = "LONGITUDE")
    private String longitude;
    @Size(max = 100)
    @Column(name = "AREA_NAME")
    private String areaName;
    @Size(max = 100)
    @Column(name = "CITY_NAME")
    private String cityName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "final_cost")
    private BigDecimal finalCost;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "SERVICE_PROVIDER_DESC")
    private String serviceProviderDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "UNIT_OF_COST")
    private String unitOfCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MIN_GUESTS")
    private int minGuests;

    public ViewServiceProvider() {
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
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

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

    public String getServiceProviderDesc() {
        return serviceProviderDesc;
    }

    public void setServiceProviderDesc(String serviceProviderDesc) {
        this.serviceProviderDesc = serviceProviderDesc;
    }

    public String getUnitOfCost() {
        return unitOfCost;
    }

    public void setUnitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }
    
}
