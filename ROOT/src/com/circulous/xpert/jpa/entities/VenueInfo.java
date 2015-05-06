/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "venue_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VenueInfo.findAll", query = "SELECT v FROM VenueInfo v"),
    @NamedQuery(name = "VenueInfo.findByVenueId", query = "SELECT v FROM VenueInfo v WHERE v.venueId = :venueId"),
    @NamedQuery(name = "VenueInfo.findByVenueCode", query = "SELECT v FROM VenueInfo v WHERE v.venueCode = :venueCode"),
    @NamedQuery(name = "VenueInfo.findByVenueName", query = "SELECT v FROM VenueInfo v WHERE v.venueName = :venueName"),
    @NamedQuery(name = "VenueInfo.findByVenueDesc", query = "SELECT v FROM VenueInfo v WHERE v.venueDesc = :venueDesc"),
    @NamedQuery(name = "VenueInfo.findByVenueEffectiveDate", query = "SELECT v FROM VenueInfo v WHERE v.venueEffectiveDate = :venueEffectiveDate"),
    @NamedQuery(name = "VenueInfo.findByVenueEtDate", query = "SELECT v FROM VenueInfo v WHERE v.venueEtDate = :venueEtDate"),
    @NamedQuery(name = "VenueInfo.findByIsParkingAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isParkingAvailable = :isParkingAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsDiningAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isDiningAvailable = :isDiningAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsStewartsAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isStewartsAvailable = :isStewartsAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsAccomodationAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isAccomodationAvailable = :isAccomodationAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsAcAccomodationAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isAcAccomodationAvailable = :isAcAccomodationAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsCutleryAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isCutleryAvailable = :isCutleryAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsMusicSystemAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isMusicSystemAvailable = :isMusicSystemAvailable"),
    @NamedQuery(name = "VenueInfo.findByIsMusicDjAvialable", query = "SELECT v FROM VenueInfo v WHERE v.isMusicDjAvialable = :isMusicDjAvialable"),
    @NamedQuery(name = "VenueInfo.findByIsDanceFloorAvailable", query = "SELECT v FROM VenueInfo v WHERE v.isDanceFloorAvailable = :isDanceFloorAvailable"),
    @NamedQuery(name = "VenueInfo.findByParkingNumber", query = "SELECT v FROM VenueInfo v WHERE v.parkingNumber = :parkingNumber"),
    @NamedQuery(name = "VenueInfo.findByDiningCapacityNumber", query = "SELECT v FROM VenueInfo v WHERE v.diningCapacityNumber = :diningCapacityNumber"),
    @NamedQuery(name = "VenueInfo.findByVenueCost", query = "SELECT v FROM VenueInfo v WHERE v.venueCost = :venueCost"),
    @NamedQuery(name = "VenueInfo.findByIsActive", query = "SELECT v FROM VenueInfo v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VenueInfo.findByIsDeleted", query = "SELECT v FROM VenueInfo v WHERE v.isDeleted = :isDeleted"),
    @NamedQuery(name = "VenueInfo.findByCreatedUser", query = "SELECT v FROM VenueInfo v WHERE v.createdUser = :createdUser"),
    @NamedQuery(name = "VenueInfo.findByCreatedDate", query = "SELECT v FROM VenueInfo v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VenueInfo.findByUpdatedUser", query = "SELECT v FROM VenueInfo v WHERE v.updatedUser = :updatedUser"),
    @NamedQuery(name = "VenueInfo.findByUpdatedDate", query = "SELECT v FROM VenueInfo v WHERE v.updatedDate = :updatedDate"),
    @NamedQuery(name = "VenueInfo.findByAcceptTermsConditions", query = "SELECT v FROM VenueInfo v WHERE v.acceptTermsConditions = :acceptTermsConditions")})
public class VenueInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "VENUE_ID")
    private Integer venueId;
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
    @Column(name = "VENUE_EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date venueEffectiveDate;
    @Column(name = "VENUE_ET_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date venueEtDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_PARKING_AVAILABLE")
    private char isParkingAvailable;
    @Column(name = "IS_DINING_AVAILABLE")
    private Character isDiningAvailable;
    @Column(name = "IS_STEWARTS_AVAILABLE")
    private Character isStewartsAvailable;
    @Column(name = "IS_ACCOMODATION_AVAILABLE")
    private Character isAccomodationAvailable;
    @Size(max = 1)
    @Column(name = "IS_AC_ACCOMODATION_AVAILABLE")
    private String isAcAccomodationAvailable;
    @Column(name = "IS_CUTLERY_AVAILABLE")
    private Character isCutleryAvailable;
    @Column(name = "IS_MUSIC_SYSTEM_AVAILABLE")
    private Character isMusicSystemAvailable;
    @Column(name = "IS_MUSIC_DJ_AVIALABLE")
    private Character isMusicDjAvialable;
    @Column(name = "IS_DANCE_FLOOR_AVAILABLE")
    private Character isDanceFloorAvailable;
    @Column(name = "PARKING_NUMBER")
    private Integer parkingNumber;
    @Column(name = "DINING_CAPACITY_NUMBER")
    private Integer diningCapacityNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VENUE_COST")
    private BigDecimal venueCost;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
    @Size(max = 50)
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 50)
    @Column(name = "UPDATED_USER")
    private String updatedUser;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "ACCEPT_TERMS_CONDITIONS")
    private Character acceptTermsConditions;
    @JoinColumn(name = "USER_ID", referencedColumnName = "user_id")
    @ManyToOne
    private EventxpertUser userId;
    @JoinColumn(name = "VENUE_TYPE_ID", referencedColumnName = "VENUE_TYPE_ID")
    @ManyToOne(optional = false)
    private VenueTypeMaster venueTypeId;
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "CURRENCY_ID")
    @ManyToOne
    private CurrencyTypeMaster currencyId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
    @ManyToOne(optional = false)
    private AreaTypeMaster areaId;
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @ManyToOne(optional = false)
    private AddressInfo addressId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venueId")
    private Collection<VenuePackageInfo> venuePackageInfoCollection;

    public VenueInfo() {
    }

    public VenueInfo(Integer venueId) {
        this.venueId = venueId;
    }

    public VenueInfo(Integer venueId, String venueCode, String venueName, String venueDesc, char isParkingAvailable) {
        this.venueId = venueId;
        this.venueCode = venueCode;
        this.venueName = venueName;
        this.venueDesc = venueDesc;
        this.isParkingAvailable = isParkingAvailable;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
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

    public Date getVenueEffectiveDate() {
        return venueEffectiveDate;
    }

    public void setVenueEffectiveDate(Date venueEffectiveDate) {
        this.venueEffectiveDate = venueEffectiveDate;
    }

    public Date getVenueEtDate() {
        return venueEtDate;
    }

    public void setVenueEtDate(Date venueEtDate) {
        this.venueEtDate = venueEtDate;
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

    public Character getIsStewartsAvailable() {
        return isStewartsAvailable;
    }

    public void setIsStewartsAvailable(Character isStewartsAvailable) {
        this.isStewartsAvailable = isStewartsAvailable;
    }

    public Character getIsAccomodationAvailable() {
        return isAccomodationAvailable;
    }

    public void setIsAccomodationAvailable(Character isAccomodationAvailable) {
        this.isAccomodationAvailable = isAccomodationAvailable;
    }

    public String getIsAcAccomodationAvailable() {
        return isAcAccomodationAvailable;
    }

    public void setIsAcAccomodationAvailable(String isAcAccomodationAvailable) {
        this.isAcAccomodationAvailable = isAcAccomodationAvailable;
    }

    public Character getIsCutleryAvailable() {
        return isCutleryAvailable;
    }

    public void setIsCutleryAvailable(Character isCutleryAvailable) {
        this.isCutleryAvailable = isCutleryAvailable;
    }

    public Character getIsMusicSystemAvailable() {
        return isMusicSystemAvailable;
    }

    public void setIsMusicSystemAvailable(Character isMusicSystemAvailable) {
        this.isMusicSystemAvailable = isMusicSystemAvailable;
    }

    public Character getIsMusicDjAvialable() {
        return isMusicDjAvialable;
    }

    public void setIsMusicDjAvialable(Character isMusicDjAvialable) {
        this.isMusicDjAvialable = isMusicDjAvialable;
    }

    public Character getIsDanceFloorAvailable() {
        return isDanceFloorAvailable;
    }

    public void setIsDanceFloorAvailable(Character isDanceFloorAvailable) {
        this.isDanceFloorAvailable = isDanceFloorAvailable;
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

    public BigDecimal getVenueCost() {
        return venueCost;
    }

    public void setVenueCost(BigDecimal venueCost) {
        this.venueCost = venueCost;
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

    public Character getAcceptTermsConditions() {
        return acceptTermsConditions;
    }

    public void setAcceptTermsConditions(Character acceptTermsConditions) {
        this.acceptTermsConditions = acceptTermsConditions;
    }

    public EventxpertUser getUserId() {
        return userId;
    }

    public void setUserId(EventxpertUser userId) {
        this.userId = userId;
    }

    public VenueTypeMaster getVenueTypeId() {
        return venueTypeId;
    }

    public void setVenueTypeId(VenueTypeMaster venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public CurrencyTypeMaster getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyTypeMaster currencyId) {
        this.currencyId = currencyId;
    }

    public AreaTypeMaster getAreaId() {
        return areaId;
    }

    public void setAreaId(AreaTypeMaster areaId) {
        this.areaId = areaId;
    }

    public AddressInfo getAddressId() {
        return addressId;
    }

    public void setAddressId(AddressInfo addressId) {
        this.addressId = addressId;
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
        hash += (venueId != null ? venueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenueInfo)) {
            return false;
        }
        VenueInfo other = (VenueInfo) object;
        if ((this.venueId == null && other.venueId != null) || (this.venueId != null && !this.venueId.equals(other.venueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.circulous.xpert.jpa.entities.VenueInfo[ venueId=" + venueId + " ]";
    }
    
}
