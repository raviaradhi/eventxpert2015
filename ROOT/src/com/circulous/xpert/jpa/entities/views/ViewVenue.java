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
@Table(name = "view_venue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewVenue.findAll", query = "SELECT v FROM ViewVenue v"),
    @NamedQuery(name = "ViewVenue.findByVenueId", query = "SELECT v FROM ViewVenue v WHERE v.venueId = :venueId"),
    @NamedQuery(name = "ViewVenue.findByVenueCode", query = "SELECT v FROM ViewVenue v WHERE v.venueCode = :venueCode"),
    @NamedQuery(name = "ViewVenue.findByVenueName", query = "SELECT v FROM ViewVenue v WHERE v.venueName = :venueName"),
    @NamedQuery(name = "ViewVenue.findByVenueDesc", query = "SELECT v FROM ViewVenue v WHERE v.venueDesc = :venueDesc"),
    @NamedQuery(name = "ViewVenue.findByVenueTypeId", query = "SELECT v FROM ViewVenue v WHERE v.venueTypeId = :venueTypeId"),
    @NamedQuery(name = "ViewVenue.findByIsParkingAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isParkingAvailable = :isParkingAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsDiningAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isDiningAvailable = :isDiningAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsAccomodationAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isAccomodationAvailable = :isAccomodationAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsStewartsAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isStewartsAvailable = :isStewartsAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsCutleryAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isCutleryAvailable = :isCutleryAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsAcAccomodationAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isAcAccomodationAvailable = :isAcAccomodationAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsMusicSystemAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isMusicSystemAvailable = :isMusicSystemAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsDanceFloorAvailable", query = "SELECT v FROM ViewVenue v WHERE v.isDanceFloorAvailable = :isDanceFloorAvailable"),
    @NamedQuery(name = "ViewVenue.findByIsMusicDjAvialable", query = "SELECT v FROM ViewVenue v WHERE v.isMusicDjAvialable = :isMusicDjAvialable"),
    @NamedQuery(name = "ViewVenue.findByParkingNumber", query = "SELECT v FROM ViewVenue v WHERE v.parkingNumber = :parkingNumber"),
    @NamedQuery(name = "ViewVenue.findByDiningCapacityNumber", query = "SELECT v FROM ViewVenue v WHERE v.diningCapacityNumber = :diningCapacityNumber"),
    @NamedQuery(name = "ViewVenue.findByCurrencyId", query = "SELECT v FROM ViewVenue v WHERE v.currencyId = :currencyId"),
    @NamedQuery(name = "ViewVenue.findByAddress", query = "SELECT v FROM ViewVenue v WHERE v.address = :address"),
    @NamedQuery(name = "ViewVenue.findByLatitude", query = "SELECT v FROM ViewVenue v WHERE v.latitude = :latitude"),
    @NamedQuery(name = "ViewVenue.findByLongitude", query = "SELECT v FROM ViewVenue v WHERE v.longitude = :longitude"),
    @NamedQuery(name = "ViewVenue.findByAreaName", query = "SELECT v FROM ViewVenue v WHERE v.areaName = :areaName"),
    @NamedQuery(name = "ViewVenue.findByVenueCost", query = "SELECT v FROM ViewVenue v WHERE v.venueCost = :venueCost"),
    @NamedQuery(name = "ViewVenue.findByFinalCOST", query = "SELECT v FROM ViewVenue v WHERE v.finalCOST = :finalCOST"),
    @NamedQuery(name = "ViewVenue.findByCapacity", query = "SELECT v FROM ViewVenue v WHERE v.capacity = :capacity"),
    @NamedQuery(name = "ViewVenue.findByCityName", query = "SELECT v FROM ViewVenue v WHERE v.cityName = :cityName")})
public class ViewVenue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VENUE_ID")
    @Id
    private int venueId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "VENUE_CODE")
    private String venueCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "VENUE_NAME")
    private String venueName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VENUE_DESC")
    private String venueDesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VENUE_TYPE_ID")
    private int venueTypeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_PARKING_AVAILABLE")
    private char isParkingAvailable;
    @Column(name = "IS_DINING_AVAILABLE")
    private Character isDiningAvailable;
    @Column(name = "IS_ACCOMODATION_AVAILABLE")
    private Character isAccomodationAvailable;
    @Column(name = "IS_STEWARTS_AVAILABLE")
    private Character isStewartsAvailable;
    @Column(name = "IS_CUTLERY_AVAILABLE")
    private Character isCutleryAvailable;
    @Size(max = 1)
    @Column(name = "IS_AC_ACCOMODATION_AVAILABLE")
    private String isAcAccomodationAvailable;
    @Column(name = "IS_MUSIC_SYSTEM_AVAILABLE")
    private Character isMusicSystemAvailable;
    @Column(name = "IS_DANCE_FLOOR_AVAILABLE")
    private Character isDanceFloorAvailable;
    @Column(name = "IS_MUSIC_DJ_AVIALABLE")
    private Character isMusicDjAvialable;
    @Column(name = "PARKING_NUMBER")
    private Integer parkingNumber;
    @Column(name = "DINING_CAPACITY_NUMBER")
    private Integer diningCapacityNumber;
    @Column(name = "CURRENCY_ID")
    private Integer currencyId;
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VENUE_COST")
    private BigDecimal venueCost;
    @Column(name = "final_COST")
    private BigDecimal finalCOST;
    @Column(name = "CAPACITY")
    private Integer capacity;
    @Size(max = 100)
    @Column(name = "CITY_NAME")
    private String cityName;

    public ViewVenue() {
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueCode() {
        return venueCode;
    }

    public void setVenueCode(String venueCode) {
        this.venueCode = venueCode;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueDesc() {
        return venueDesc;
    }

    public void setVenueDesc(String venueDesc) {
        this.venueDesc = venueDesc;
    }

    public int getVenueTypeId() {
        return venueTypeId;
    }

    public void setVenueTypeId(int venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public char getIsParkingAvailable() {
        return isParkingAvailable;
    }

    public void setIsParkingAvailable(char isParkingAvailable) {
        this.isParkingAvailable = isParkingAvailable;
    }

    public Character getIsDiningAvailable() {
        return isDiningAvailable;
    }

    public void setIsDiningAvailable(Character isDiningAvailable) {
        this.isDiningAvailable = isDiningAvailable;
    }

    public Character getIsAccomodationAvailable() {
        return isAccomodationAvailable;
    }

    public void setIsAccomodationAvailable(Character isAccomodationAvailable) {
        this.isAccomodationAvailable = isAccomodationAvailable;
    }

    public Character getIsStewartsAvailable() {
        return isStewartsAvailable;
    }

    public void setIsStewartsAvailable(Character isStewartsAvailable) {
        this.isStewartsAvailable = isStewartsAvailable;
    }

    public Character getIsCutleryAvailable() {
        return isCutleryAvailable;
    }

    public void setIsCutleryAvailable(Character isCutleryAvailable) {
        this.isCutleryAvailable = isCutleryAvailable;
    }

    public String getIsAcAccomodationAvailable() {
        return isAcAccomodationAvailable;
    }

    public void setIsAcAccomodationAvailable(String isAcAccomodationAvailable) {
        this.isAcAccomodationAvailable = isAcAccomodationAvailable;
    }

    public Character getIsMusicSystemAvailable() {
        return isMusicSystemAvailable;
    }

    public void setIsMusicSystemAvailable(Character isMusicSystemAvailable) {
        this.isMusicSystemAvailable = isMusicSystemAvailable;
    }

    public Character getIsDanceFloorAvailable() {
        return isDanceFloorAvailable;
    }

    public void setIsDanceFloorAvailable(Character isDanceFloorAvailable) {
        this.isDanceFloorAvailable = isDanceFloorAvailable;
    }

    public Character getIsMusicDjAvialable() {
        return isMusicDjAvialable;
    }

    public void setIsMusicDjAvialable(Character isMusicDjAvialable) {
        this.isMusicDjAvialable = isMusicDjAvialable;
    }

    public Integer getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(Integer parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public Integer getDiningCapacityNumber() {
        return diningCapacityNumber;
    }

    public void setDiningCapacityNumber(Integer diningCapacityNumber) {
        this.diningCapacityNumber = diningCapacityNumber;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
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

    public BigDecimal getVenueCost() {
        return venueCost;
    }

    public void setVenueCost(BigDecimal venueCost) {
        this.venueCost = venueCost;
    }

    public BigDecimal getFinalCOST() {
        return finalCOST;
    }

    public void setFinalCOST(BigDecimal finalCOST) {
        this.finalCOST = finalCOST;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
}
