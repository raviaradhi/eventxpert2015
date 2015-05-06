/**
 * 
 */
package com.circulous.xpert.event.managedbean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.CityTypeMaster;
import com.circulous.xpert.jpa.entities.EventDates;
import com.circulous.xpert.jpa.entities.EventTypeMaster;
import com.circulous.xpert.jpa.entities.EventxpertUser;
import com.circulous.xpert.jpa.entities.ItemMaster;
import com.circulous.xpert.jpa.entities.ProviderItemInfo;
import com.circulous.xpert.jpa.entities.ServiceTypeMaster;
import com.circulous.xpert.jpa.entities.UnitCosts;
import com.circulous.xpert.jpa.entities.VenueCostInfo;
import com.circulous.xpert.jpa.entities.VenueHallInfo;
import com.circulous.xpert.jpa.entities.VenueInfo;
import com.circulous.xpert.jpa.entities.VenuePackageInfo;
import com.circulous.xpert.jpa.entities.VenueTypeMaster;
import com.circulous.xpert.model.ColumnModel;
import com.circulous.xpert.model.EventScheduleInfo;
import com.circulous.xpert.model.Image;
import com.circulous.xpert.model.PackageCatgeoryItem;
import com.circulous.xpert.model.ProviderItem;
import com.circulous.xpert.model.VenuePackageDetails;

/**
 * @author jagadish
 * 
 */
@ManagedBean(name = "venueManagedBean")
@SessionScoped
public class VenueManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(VenueManagedBean.class);

	public VenueManagedBean() {

		generateMasterDataList();
		addObjects();
		addPasswordHints();
		this.activeTab = 0;
		this.registered = false;
		registerDisable = true;
		this.loggedin = false;
		getEventDatesList();
		dates = new HashSet<String>();
		// this.imageUploadable = true;
		Properties prop = new Properties();
		InputStream input = null;

		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ExternalContext externalContext = ctx.getExternalContext();
			input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");// new
																							// FileInputStream("eXpertConfig.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			path = prop.getProperty("imgpath");
			url = prop.getProperty("imgurl");
			ccemail = prop.getProperty("custSupportEmail");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	String path = "";
	String url = "";
	String ccemail = "";

	private String venueCode;
	private String venueName;
	private String venueDesc;
	private Date venueFromDate;
	private Date venueToDate;
	private Integer venueTypeId = new Integer(0);
	private Integer venueAreaId = new Integer(0);
	private Integer venueAddressId = new Integer(0);
	private boolean parkingAvailable;
	private boolean diningAvailable;
	private boolean stewartsAvailable;
	private boolean accomodationAvailable;
	private boolean stageAvailable;
	private String aC;
	private boolean cutleryAvailable;
	private boolean musicSystemAvailable;
	private boolean musicDJAvailable;
	private boolean danceFloorAvailable;

	private Integer parkingNumber = new Integer(0);
	private Integer diningCapacityNumber = new Integer(0);
	private Integer currencyId = new Integer(0);
	private Integer venueCost = new Integer(0);
	private List<VenueTypeMaster> venueMasterList = new ArrayList<VenueTypeMaster>();
	private List<SelectItem> venueTypeInfo = new ArrayList<SelectItem>();

	private List<ServiceTypeMaster> serviceTypeMasterList = new ArrayList<ServiceTypeMaster>();
	private List<AreaTypeMaster> areaTypeMasterList = new ArrayList<AreaTypeMaster>();
	private List<CityTypeMaster> cityTypeMasterList = new ArrayList<CityTypeMaster>();
	private List<EventTypeMaster> eventMasterList = new ArrayList<EventTypeMaster>();
	private List<VenuePackageInfo> packageInfoList = new ArrayList<VenuePackageInfo>();
	private List<VenuePackageInfo> pInfoList = new ArrayList<VenuePackageInfo>();
	private List<VenuePackageDetails> pList = new ArrayList<VenuePackageDetails>();
	// private List<VenueHallInfo> vhList = new ArrayList<VenueHallInfo>();
	private HashMap<VenueHallInfo, List<Image>> vhList = new HashMap<VenueHallInfo, List<Image>>();
	private List<String> imagePaths = new ArrayList<String>();
	private List<Image> imagesPreview = new ArrayList<Image>();
	private List<Image> imagesPreviewHall = new ArrayList<Image>();
	private List<ProviderItemInfo> pItemList = new ArrayList<ProviderItemInfo>();
	private List<ProviderItem> pItemsList = new ArrayList<ProviderItem>();
	private List<String> unitCosts = new ArrayList<String>();
	private List<EventScheduleInfo> esInfo = new ArrayList<EventScheduleInfo>();
	private List<String> items = new ArrayList<String>();
	private List<ItemMaster> itemMasterList = new ArrayList<ItemMaster>();
	private List<SelectItem> areaInfo = new ArrayList<SelectItem>();
	private List<SelectItem> cityInfo = new ArrayList<SelectItem>();
	private List<SelectItem> passwordHints = new ArrayList<SelectItem>();
	private List<SelectItem> eventInfo = new ArrayList<SelectItem>();
	private List<SelectItem> itemInfo = new ArrayList<SelectItem>();
	private Map<String, Integer> itemCategoryInfo = new HashMap<String, Integer>();
	Map<String, List<PackageCatgeoryItem>> pciMap = new LinkedHashMap<String, List<PackageCatgeoryItem>>();
	private List<SelectItem> unitCostsInfo = new ArrayList<SelectItem>();
        private List<SelectItem> eventTypeList = new ArrayList<SelectItem>();
	private List<Map<String, List<PackageCatgeoryItem>>> pciList = new ArrayList<Map<String, List<PackageCatgeoryItem>>>();
	private String regMessage;
	private List<ColumnModel> columns = new ArrayList<ColumnModel>();
	private Integer venueId = new Integer(0);
	private Integer areaId = new Integer(0);
	private Integer eventId = new Integer(0);
	private Integer itemId = new Integer(0);
	private Integer item = new Integer(0);
	private Integer itemCategoryId = new Integer(0);
	private Integer activeTab = new Integer(0);
	private String quantity;
	private double rate;
	private int packageSize = 0;
	private int itemSize;
	// vendor registration
	private String vName;
	private String streetAddress;
	private String zipcode;
	private String cityState;
	private int cityId;
	private String phone;
	private String vCode;
	private String spName;
	private int userId;
	private String user;
	private String password;
	private String emailaddr;

	private String eventType;
	private String packageName;
	private BigDecimal cost;
	private String description;
	private String unitOfCost;
	private int unitCostId;
	private String itemDescription;

	private String itemCategory;
	private String itemName;

	private String loginUser;
	private String loginPassword;
	private boolean acceptTerms;
	private String passwordHint;
	private String passwordAns;
	private boolean registered = true;
	private boolean registerDisable = true;
	private boolean loggedin = false;
	private boolean editPackage = false;
	private boolean imageUploadable = false;
	private boolean imageUploadableHall = false;
	private Date pkgFromDate;
	private Date pkgToDate;
	private Date hallFromDate;
	private int eventDatessize;
	private int login = 0;
	private boolean createMyAccount = false;
	private Date eventDate;
	private List<EventDates> eventDates = new ArrayList<EventDates>();
	private String[] oldEventDates;

	private int hallId;
	private int capacity;
	private int minCapacity;
	private String hallName;
	private char airConditioned;
	private String hallDescription;

	private String pNameExists;
	private String vendorDescription;
	private Set<String> dates;

	private HashMap servicesInfoMap = new HashMap();
                private int eventTypeId;

    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
    
     public List<SelectItem> getEventTypeList() {
            return eventTypeList;
        }

        public void setEventTypeList(List<SelectItem> eventTypeList) {
            this.eventTypeList = eventTypeList;
        }

	public List<ServiceTypeMaster> getServiceTypeMasterList() {
		return serviceTypeMasterList;
	}

	public void setServiceTypeMasterList(List<ServiceTypeMaster> serviceTypeMasterList) {
		this.serviceTypeMasterList = serviceTypeMasterList;
	}

	public HashMap getServicesInfoMap() {
		return servicesInfoMap;
	}

	public void setServicesInfoMap(HashMap servicesInfoMap) {
		this.servicesInfoMap = servicesInfoMap;
	}

	public String getVendorDescription() {
		return vendorDescription;
	}

	public void setVendorDescription(String vendorDescription) {
		this.vendorDescription = vendorDescription;
	}

	public String getpNameExists() {
		return pNameExists;
	}

	public void setpNameExists(String pNameExists) {
		this.pNameExists = pNameExists;
	}

	// package details
	private Integer packageId = new Integer(0);

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getVenueId() {
		return venueId;
	}

	public void setVenueId(Integer venueId) {
		this.venueId = venueId;
	}

	public List<EventDates> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<EventDates> eventDates) {
		this.eventDates = eventDates;
	}

	public String[] getOldEventDates() {

		return oldEventDates;
	}

	public void setOldEventDates(String[] oldEventDates) {
		this.oldEventDates = oldEventDates;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Integer getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
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

	public Date getVenueFromDate() {
		return venueFromDate;
	}

	public void setVenueFromDate(Date venueFromDate) {
		this.venueFromDate = venueFromDate;
	}

	public Date getVenueToDate() {
		return venueToDate;
	}

	public void setVenueToDate(Date venueToDate) {
		this.venueToDate = venueToDate;
	}

	public Integer getVenueTypeId() {
		return venueTypeId;
	}

	public void setVenueTypeId(Integer venueTypeId) {
		this.venueTypeId = venueTypeId;
	}

	public Integer getVenueAreaId() {
		return venueAreaId;
	}

	public void setVenueAreaId(Integer venueAreaId) {
		this.venueAreaId = venueAreaId;
	}

	public Integer getVenueAddressId() {
		return venueAddressId;
	}

	public void setVenueAddressId(Integer venueAddressId) {
		this.venueAddressId = venueAddressId;
	}

	public boolean isParkingAvailable() {
		return parkingAvailable;
	}

	public void setParkingAvailable(boolean parkingAvailable) {
		this.parkingAvailable = parkingAvailable;
	}

	public boolean isStageAvailable() {
		return stageAvailable;
	}

	public void setStageAvailable(boolean stageAvailable) {
		this.stageAvailable = stageAvailable;
	}

	public boolean isDiningAvailable() {
		return diningAvailable;
	}

	public void setDiningAvailable(boolean diningAvailable) {
		this.diningAvailable = diningAvailable;
	}

	public boolean isStewartsAvailable() {
		return stewartsAvailable;
	}

	public void setStewartsAvailable(boolean stewartsAvailable) {
		this.stewartsAvailable = stewartsAvailable;
	}

	public boolean isAccomodationAvailable() {
		return accomodationAvailable;
	}

	public void setAccomodationAvailable(boolean accomodationAvailable) {
		this.accomodationAvailable = accomodationAvailable;
	}

	public String getaC() {
		return aC;
	}

	public void setaC(String aC) {
		this.aC = aC;
	}

	public boolean isCutleryAvailable() {
		return cutleryAvailable;
	}

	public void setCutleryAvailable(boolean cutleryAvailable) {
		this.cutleryAvailable = cutleryAvailable;
	}

	public boolean isMusicSystemAvailable() {
		return musicSystemAvailable;
	}

	public void setMusicSystemAvailable(boolean musicSystemAvailable) {
		this.musicSystemAvailable = musicSystemAvailable;
	}

	public boolean isMusicDJAvailable() {
		return musicDJAvailable;
	}

	public void setMusicDJAvailable(boolean musicDJAvailable) {
		this.musicDJAvailable = musicDJAvailable;
	}

	public boolean isDanceFloorAvailable() {
		return danceFloorAvailable;
	}

	public void setDanceFloorAvailable(boolean danceFloorAvailable) {
		this.danceFloorAvailable = danceFloorAvailable;
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

	public Integer getVenueCost() {
		return venueCost;
	}

	public void setVenueCost(Integer venueCost) {
		this.venueCost = venueCost;
	}

	public List<VenueTypeMaster> getVenueMasterList() {
		return venueMasterList;
	}

	public void setVenueMasterList(List<VenueTypeMaster> venueMasterList) {
		this.venueMasterList = venueMasterList;
	}

	public List<SelectItem> getVenueTypeInfo() {
		return venueTypeInfo;
	}

	public void setVenueTypeInfo(List<SelectItem> venueTypeInfo) {
		this.venueTypeInfo = venueTypeInfo;
	}

	public List<AreaTypeMaster> getAreaTypeMasterList() {
		return areaTypeMasterList;
	}

	public void setAreaTypeMasterList(List<AreaTypeMaster> areaTypeMasterList) {
		this.areaTypeMasterList = areaTypeMasterList;
	}

	public List<CityTypeMaster> getCityTypeMasterList() {
		return cityTypeMasterList;
	}

	public void setCityTypeMasterList(List<CityTypeMaster> cityTypeMasterList) {
		this.cityTypeMasterList = cityTypeMasterList;
	}

	public List<EventTypeMaster> getEventMasterList() {
		return eventMasterList;
	}

	public void setEventMasterList(List<EventTypeMaster> eventMasterList) {
		this.eventMasterList = eventMasterList;
	}

	public List<VenuePackageInfo> getPackageInfoList() {
		return packageInfoList;
	}

	public void setPackageInfoList(List<VenuePackageInfo> packageInfoList) {
		this.packageInfoList = packageInfoList;
	}

	public List<VenuePackageInfo> getpInfoList() {
		return pInfoList;
	}

	public void setpInfoList(List<VenuePackageInfo> pInfoList) {
		this.pInfoList = pInfoList;
	}

	public List<VenuePackageDetails> getpList() {
		return pList;
	}

	public void setpList(List<VenuePackageDetails> pList) {
		this.pList = pList;
	}

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public List<Image> getImagesPreviewHall() {
		return imagesPreviewHall;
	}

	public void setImagesPreviewHall(List<Image> imagesPreviewHall) {
		this.imagesPreviewHall = imagesPreviewHall;
	}

	public List<Image> getImagesPreview() {
		return imagesPreview;
	}

	public void setImagesPreview(List<Image> imagesPreview) {
		this.imagesPreview = imagesPreview;
	}

	public List<ProviderItemInfo> getpItemList() {
		return pItemList;
	}

	public void setpItemList(List<ProviderItemInfo> pItemList) {
		this.pItemList = pItemList;
	}

	public List<ProviderItem> getpItemsList() {
		return pItemsList;
	}

	public void setpItemsList(List<ProviderItem> pItemsList) {
		this.pItemsList = pItemsList;
	}

	public List<String> getUnitCosts() {
		return unitCosts;
	}

	public void setUnitCosts(List<String> unitCosts) {
		this.unitCosts = unitCosts;
	}

	public List<EventScheduleInfo> getEsInfo() {
		return esInfo;
	}

	public void setEsInfo(List<EventScheduleInfo> esInfo) {
		this.esInfo = esInfo;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public List<ItemMaster> getItemMasterList() {
		return itemMasterList;
	}

	public void setItemMasterList(List<ItemMaster> itemMasterList) {
		this.itemMasterList = itemMasterList;
	}

	public List<SelectItem> getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(List<SelectItem> areaInfo) {
		this.areaInfo = areaInfo;
	}

	public List<SelectItem> getCityInfo() {
		return cityInfo;
	}

	public void setCityInfo(List<SelectItem> cityInfo) {
		this.cityInfo = cityInfo;
	}

	public List<SelectItem> getPasswordHints() {
		return passwordHints;
	}

	public void setPasswordHints(List<SelectItem> passwordHints) {
		this.passwordHints = passwordHints;
	}

	public List<SelectItem> getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(List<SelectItem> eventInfo) {
		this.eventInfo = eventInfo;
	}

	public List<SelectItem> getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(List<SelectItem> itemInfo) {
		this.itemInfo = itemInfo;
	}

	public Map<String, Integer> getItemCategoryInfo() {
		return itemCategoryInfo;
	}

	public void setItemCategoryInfo(Map<String, Integer> itemCategoryInfo) {
		this.itemCategoryInfo = itemCategoryInfo;
	}

	public Map<String, List<PackageCatgeoryItem>> getPciMap() {
		return pciMap;
	}

	public void setPciMap(Map<String, List<PackageCatgeoryItem>> pciMap) {
		this.pciMap = pciMap;
	}

	public List<SelectItem> getUnitCostsInfo() {
		return unitCostsInfo;
	}

	public void setUnitCostsInfo(List<SelectItem> unitCostsInfo) {
		this.unitCostsInfo = unitCostsInfo;
	}

	public List<Map<String, List<PackageCatgeoryItem>>> getPciList() {
		return pciList;
	}

	public void setPciList(List<Map<String, List<PackageCatgeoryItem>>> pciList) {
		this.pciList = pciList;
	}

	public String getRegMessage() {
		return regMessage;
	}

	public void setRegMessage(String regMessage) {
		this.regMessage = regMessage;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(int packageSize) {
		this.packageSize = packageSize;
	}

	public int getItemSize() {
		return itemSize;
	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

	public String getvName() {
		return vName;
	}

	public void setvName(String vName) {
		this.vName = vName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCityState() {
		return cityState;
	}

	public void setCityState(String cityState) {
		this.cityState = cityState;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getvCode() {
		return vCode;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailaddr() {
		return emailaddr;
	}

	public void setEmailaddr(String emailaddr) {
		this.emailaddr = emailaddr;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitOfCost() {
		return unitOfCost;
	}

	public void setUnitOfCost(String unitOfCost) {
		this.unitOfCost = unitOfCost;
	}

	public int getUnitCostId() {
		return unitCostId;
	}

	public void setUnitCostId(int unitCostId) {
		this.unitCostId = unitCostId;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public boolean isAcceptTerms() {
		return acceptTerms;
	}

	public void setAcceptTerms(boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}

	public String getPasswordHint() {
		return passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public String getPasswordAns() {
		return passwordAns;
	}

	public void setPasswordAns(String passwordAns) {
		this.passwordAns = passwordAns;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isLoggedin() {
		return loggedin;
	}

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

	public boolean isEditPackage() {
		return editPackage;
	}

	public void setEditPackage(boolean editPackage) {
		this.editPackage = editPackage;
	}

	public boolean isImageUploadable() {
		return imageUploadable;
	}

	public void setImageUploadable(boolean imageUploadable) {
		this.imageUploadable = imageUploadable;
	}

	public boolean isImageUploadableHall() {
		return imageUploadableHall;
	}

	public void setImageUploadableHall(boolean imageUploadableHall) {
		this.imageUploadableHall = imageUploadableHall;
	}

	public Date getPkgFromDate() {
		return pkgFromDate;
	}

	public void setPkgFromDate(Date pkgFromDate) {
		this.pkgFromDate = pkgFromDate;
	}

	public Date getPkgToDate() {
		return pkgToDate;
	}

	public void setPkgToDate(Date pkgToDate) {
		this.pkgToDate = pkgToDate;
	}

	public HashMap<VenueHallInfo, List<Image>> getVhList() {
		return vhList;
	}

	public void setVhList(HashMap<VenueHallInfo, List<Image>> vhList) {
		this.vhList = vhList;
	}

	public Date getHallFromDate() {
		return hallFromDate;
	}

	public void setHallFromDate(Date hallFromDate) {
		this.hallFromDate = hallFromDate;
	}

	public int getEventDatessize() {
		return eventDatessize;
	}

	public void setEventDatessize(int eventDatessize) {
		this.eventDatessize = eventDatessize;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public boolean isCreateMyAccount() {
		return createMyAccount;
	}

	public void setCreateMyAccount(boolean createMyAccount) {
		this.createMyAccount = createMyAccount;
	}

	public int getMinCapacity() {
		return minCapacity;
	}

	public void setMinCapacity(int minCapacity) {
		this.minCapacity = minCapacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public boolean isRegisterDisable() {
		return registerDisable;
	}

	public void setRegisterDisable(boolean registerDisable) {
		this.registerDisable = registerDisable;
	}

	public int getHallId() {
		return hallId;
	}

	public void setHallId(int hallId) {
		this.hallId = hallId;
	}

	public char getAirConditioned() {
		return airConditioned;
	}

	public void setAirConditioned(char airConditioned) {
		this.airConditioned = airConditioned;
	}

	public String getHallDescription() {
		return hallDescription;
	}

	public void setHallDescription(String hallDescription) {
		this.hallDescription = hallDescription;
	}

	public void alterVenueInfo() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addObjects() {
		try {
			Iterator<VenueTypeMaster> venueIterator = this.venueMasterList.iterator();
			while (venueIterator.hasNext()) {
				VenueTypeMaster vMaster = (VenueTypeMaster) venueIterator.next();
				if (!vMaster.getVenueType().contains("ome"))
					this.venueTypeInfo.add(new SelectItem(vMaster.getVenueTypeId(), vMaster.getVenueType()));
			}

			// populate the area information
			Iterator<AreaTypeMaster> areaIterators = this.areaTypeMasterList.iterator();
			while (areaIterators.hasNext()) {
				AreaTypeMaster aMaster = (AreaTypeMaster) areaIterators.next();
				// this.areaInfo.put(aMaster.getAreaName(),""+aMaster.getAreaId());
				this.areaInfo.add(new SelectItem(aMaster.getAreaId(), aMaster.getAreaName()));
			}

			Iterator<CityTypeMaster> cityIterators = this.cityTypeMasterList.iterator();
			while (cityIterators.hasNext()) {
				CityTypeMaster cMaster = (CityTypeMaster) cityIterators.next();
				// this.areaInfo.put(aMaster.getAreaName(),""+aMaster.getAreaId());
				this.cityInfo.add(new SelectItem(cMaster.getCityId(), cMaster.getCityName()));
			}

			// populate the service providers information
			Iterator<EventTypeMaster> eventIterator = this.eventMasterList.iterator();
			while (eventIterator.hasNext()) {
				EventTypeMaster eMaster = (EventTypeMaster) eventIterator.next();
				this.eventInfo.add(new SelectItem(eMaster.getEventId(), eMaster.getEventType()));
			}

			Iterator<ServiceTypeMaster> servIterator = this.serviceTypeMasterList.iterator();
			while (servIterator.hasNext()) {
				ServiceTypeMaster sMaster = (ServiceTypeMaster) servIterator.next();
				this.servicesInfoMap.put(sMaster.getServiceName(), sMaster.getServiceTypeCode());
				// this.servicesInfo.add(new
				// SelectItem(sMaster.getServiceTypeCode(),sMaster.getServiceName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void generateMasterDataList() {

		EntityManager em = getEntityManager();
		try {
			char isActive = 'Y';

			Query qryServiceTypeMasterList = em.createNamedQuery("ServiceTypeMaster.findByIsActive");
			qryServiceTypeMasterList.setParameter("isActive", isActive);
			this.serviceTypeMasterList = qryServiceTypeMasterList.getResultList();

			Query qryVenueTypeMasterList = em.createNamedQuery("VenueTypeMaster.findByIsActive");
			qryVenueTypeMasterList.setParameter("isActive", isActive);
			this.venueMasterList = qryVenueTypeMasterList.getResultList();

			Query qryAreaTypeMasterList = em.createNamedQuery("AreaTypeMaster.findByIsActive");
			qryAreaTypeMasterList.setParameter("isActive", isActive);
			this.areaTypeMasterList = qryAreaTypeMasterList.getResultList();

			Query qryCityTypeMasterList = em.createNamedQuery("CityTypeMaster.findByIsActive");
			qryCityTypeMasterList.setParameter("isActive", isActive);
			this.cityTypeMasterList = qryCityTypeMasterList.getResultList();

			Query qryEventTypeMasterList = em.createNamedQuery("EventTypeMaster.findByIsActive");
			qryEventTypeMasterList.setParameter("isActive", isActive);
			this.eventMasterList = qryEventTypeMasterList.getResultList();

		} catch (Exception e) {
			e.getMessage();
		} finally {
			em.close();
		}
	}

	VenueInfo vinfo;
	EventxpertUser exu;
	private boolean showActLink = false;

	public boolean isShowActLink() {
		return showActLink;
	}

	public void setShowActLink(boolean showActLink) {
		this.showActLink = showActLink;
	}

	@SuppressWarnings("unchecked")
	// public boolean checkLogin(String user, String password){
	public boolean checkLogin(EventxpertUser exuser) {
		try {
			boolean loggedin = false;
			/*
			 * logout(); init();
			 */

			EntityManager em = getEntityManager();
			em.getTransaction().begin();

			this.imageUploadable = true;

			login++;
			exu = exuser;

			Query q1 = em.createQuery("SELECT vi FROM VenueInfo vi  WHERE vi.userId = :userId");
			q1.setParameter("userId", exu);

			List<VenueInfo> venueLogin = q1.getResultList();

			if (venueLogin.size() > 0) {
				vinfo = venueLogin.get(0);
				AddressInfo adInfo = vinfo.getAddressId();
				this.venueId = vinfo.getVenueId();
				this.venueTypeId = vinfo.getVenueTypeId().getVenueTypeId();
				this.areaId = vinfo.getAreaId().getAreaId();
				this.vName = adInfo.getContactName();
				this.streetAddress = adInfo.getAddressLine1();
				this.zipcode = adInfo.getPostalCode();
				this.cityId = adInfo.getCityId();
				this.phone = adInfo.getContactPhone();
				this.userId = vinfo.getUserId().getUserId();
				this.emailaddr = exu.getEmailaddr();
				this.passwordHint = exu.getPasswordHint();
				this.vendorDescription = vinfo.getVenueDesc();
				getExistingVendor(vinfo.getVenueId());
                                getEventDatesList();
			}

			FacesContext ctx = FacesContext.getCurrentInstance();
			String path = ctx.getExternalContext().getInitParameter("imgpath");

			// File dir = new File(path+"/vendors/"+this.venueId);
			File dir = new File(path + "/venue/" + this.venueId);

			listFilesForFolder(dir, this.venueId);
			this.registered = true;
			this.registerDisable = false;
			return true;

		} catch (Exception e) {
			e.getMessage();
		}
		return loggedin;
	}

	private void getExistingVendor(int venueId) {
		getHallDetails(venueId);
		this.packageInfoList = getPackageDetaills(venueId);
		createPackageList(this.packageInfoList);
		getUnitCostTypes(); 
                getEventTypes();
	}

	private String spCode;

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public void setLoginSpCode() {
		// SelectOneRadio val = (SelectOneRadio) ae.getComponent();
		// spCode = (String) val.getSubmittedValue();
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("myRadVal", spCode);
		if (!this.spCode.equals("VEN")) {
			try {
				ServiceProviderManagedBean serviceProviderManagedBean = new ServiceProviderManagedBean();
				serviceProviderManagedBean.setSpCode(spCode);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("serviceProviderManagedBean", serviceProviderManagedBean);
				this.activeTab = 0;
				FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private void getHallDetails(int venueId) {
		EntityManager em = getEntityManager();
		Query qryVenueHallList = em.createNamedQuery("VenueHallInfo.findByVenueID");
		qryVenueHallList.setParameter("venueID", venueId);
		this.vhList.clear();
		List<VenueHallInfo> vhListL = qryVenueHallList.getResultList();
		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getInitParameter("imgpath");
		List<Image> imagesPreviewHall;
		for (int j = 0; j < vhListL.size(); j++) {
			VenueHallInfo vh = vhListL.get(j);
			imagesPreviewHall = new ArrayList<Image>();
			File dir = new File(path + "/venue/" + venueId + "/" + vh.getHallID());
			imagesPreviewHall = listHallFilesForFolder(dir, vh.getHallID());
			vhList.put(vh, imagesPreviewHall);
		}

	}

	@SuppressWarnings("unchecked")
	public String insertVenueDetails() {
		String ret = "";
		try {
			EntityManager em = getEntityManager();

			em.getTransaction().begin();
			Query q = em.createQuery("select vi from VenueInfo vi where vi.venueId = :venueId");
			q.setParameter("venueId", this.venueId);
			List<VenueInfo> l = q.getResultList();

			if (l.size() > 0) {

				VenueInfo s = (VenueInfo) l.get(0);

				em.getReference(VenueInfo.class, s.getVenueId());
				AddressInfo addI = s.getAddressId();
				em.getReference(AddressInfo.class, addI.getAddressId());
				addI.setAddressLine1(this.streetAddress);
                                
                                AreaTypeMaster atm = s.getAreaId();
				em.getReference(AreaTypeMaster.class, atm.getAreaId());
                                
                                AreaTypeMaster am = null;
                                Query qC1 = em.createNamedQuery("AreaTypeMaster.findByAreaId");
                                qC1.setParameter("areaId", this.areaId);
                                logger.debug("this.areaId "+this.areaId);
                                logger.debug("query "+qC1);
                                List aList1 = qC1.getResultList();
                                if(aList1.size()>0)
                                {
                                        am = (AreaTypeMaster) aList1.get(0);
                                }
                                
//                                atm.setAreaId(this.areaId);
                                
				addI.setAddressLine2(am.getAreaName());
                                
				//addI.setAddressLine2(getAreaName(this.areaId).getAreaName());
				addI.setContactName(this.vName);
				addI.setContactPhone("" + this.phone);
				addI.setPostalCode(this.zipcode);
				addI.setEmail(this.emailaddr);
				addI.setCityId(this.cityId);
                                
                                //if(addI.getLatitude()==null || addI.getLatitude().equals(null))
                                {
                                CityTypeMaster cm = null;
                                Query qC = em.createNamedQuery("CityTypeMaster.findByCityId");
                                qC.setParameter("cityId", this.cityId);
                                logger.debug("query "+qC);
                                List aList = qC.getResultList();
                                if(aList.size()>0)
                                {
                                     cm = (CityTypeMaster) aList.get(0);
                                }
                                
                                String addr = addI.getAddressLine1() + "," +addI.getAddressLine2() + "," +addI.getPostalCode() + "," +cm.getCityName();
                                
                                PlacesLatitudes pl = new PlacesLatitudes();
                                LatitudeLongitude lng = pl.getLatLng(addr);
                                
                                addI.setLatitude(String.valueOf(lng.getLatitude()));
                                addI.setLongitude(String.valueOf(lng.getLongitude()));
                                }
				s.setAddressId(addI);
                                s.setAreaId(am);
				s.setVenueName(this.vName);

				EventxpertUser eu = s.getUserId();
				em.getReference(EventxpertUser.class, eu.getUserId());
				eu.setEmailaddr(this.emailaddr);
				eu.setPasswordAns(passwordAns);
				eu.setPasswordHint(passwordHint);

				s.setVenueDesc(this.vendorDescription);
				Timestamp ts = new Timestamp(new Date().getTime());
				s.setVenueEffectiveDate(ts);
				s.setVenueEtDate(ts);
				s.setCreatedDate(ts);

				em.getTransaction().commit();
				this.imageUploadable = true;
				this.createMyAccount = true;
				ret = "";
			} else {

				Query q1 = em.createQuery("select vu from EventxpertUser vu where vu.username= :username");
				q1.setParameter("username", this.user);
				List<EventxpertUser> l1 = q1.getResultList();

				if (l1.size() > 0) {
					String mess = "Registration failed, User Name Already Exist";

					adminInformation(mess);
				} else {

					EventxpertUser vu = createVendorUser();
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					md.update((vu.getEmailaddr() + "." + vu.getPassword()).getBytes());

					byte byteData[] = md.digest();

					// convert the byte to hex format method 1
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < byteData.length; i++) {
						sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
					}

					String key = Base64.encodeBase64URLSafeString(byteData);

					vu.setActivationKey(key);

					vu.setCreatedBy("Admin");
					vu.setModifiedBy("Admin");
					vu.setCreatedDate(new Date());
					// vu.setActivationKey("Y");
					EntityManager em1 = getEntityManager();
					try {
						em1.getTransaction().begin();
						em1.persist(vu);
						em1.getTransaction().commit();
						em1.refresh(vu);
						this.userId = vu.getUserId();

						Query q2 = em.createQuery("select vi from EventxpertUser vi where vi.emailaddr= :emailaddr");
						q2.setParameter("emailaddr", this.emailaddr);
						List<EventxpertUser> l2 = q2.getResultList();
						if (l2.size() > 0) {
							String mess = "Registration failed, Email already Exist";
							adminInformation(mess);
						}

						VenueInfo venueInfo = createVenueInfo();
						venueInfo.setVenueTypeId(getVenueTypeMaster(this.venueTypeId));
						venueInfo.setUserId(vu);
						AddressInfo addInfo = createAddressInfo();
						venueInfo.setAddressId(addInfo);
						EntityManager em2 = getEntityManager();
						try {
							em2.getTransaction().begin();
							em2.persist(venueInfo);
							em2.getTransaction().commit();
							em2.refresh(venueInfo);

							this.venueId = venueInfo.getVenueId();
							venueInfo.setVenueCode("V" + this.venueId);
							em2.getTransaction().begin();
							em2.persist(venueInfo);
							em2.getTransaction().commit();
							// this.serviceName = getServiceName(this.spCode);
							this.createMyAccount = true;
							this.imageUploadable = true;
							HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
									.getExternalContext().getRequest();

							String link = "http://" + request.getHeader("host") + request.getContextPath()
									+ "/activateVendor.jsf?key=" + key + "&ty=V";

							sendEmail(vu.getEmailaddr(), venueInfo.getVenueName(), link);

							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.remove("venueProviderManagedBean");
							registered = true;
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.put("thankyou", registered);
							ret = "thankyou.xhtml?faces-redirect=true";
							em2.close();
						} catch (RollbackException e) {
							e.printStackTrace();
							em1.getTransaction().rollback();
							em2.getTransaction().rollback();
							registered = false;
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.put("thankyou", registered);
							ret = "thankyou.xhtml?faces-redirect=true";
						}
						em1.close();
					} catch (RollbackException e) {
						e.printStackTrace();
						em1.getTransaction().rollback();
						registered = false;
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
								.put("thankyou", registered);
						ret = "thankyou.xhtml?faces-redirect=true";
					}
				}

			}
			getEventDatesList();
			// getBlankItems();
			// getServiceItems();
			getUnitCostTypes();                                                         
                        getEventTypes();
			if (this.pciList.size() > 0) {
				this.pciList.clear();
			}
			// getCategories();
			registered();
			// getProviderItemList();

		} catch (Exception e) {

			e.printStackTrace();
			registered = false;
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
			ret = "thankyou.xhtml?faces-redirect=true";
		}
		return ret;
	}

	public void sendEmail(String toEmail, String name, String link) {

		String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
		String host = "mail.eventxpert.in";
		final String username = "jagadish@eventxpert.in";
		final String password = "jagadish";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.auth", "true");
//
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			// String ccemail =
			// ctx.getExternalContext().getInitParameter("custSupportEmail");

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Activate your EventXpert Account");
			message.setText("Welcome "
					+ name
					+ ".,"
					+ "\n\n Thanks for registering with EventXpert."
					+ "\n\n To prevent unauthorized use of your email address, we need to make sure that you are the rightful owner of this email ID."
					+ "\n\n Please click on the link below to confirm your email address:"
					+ "\n\n "
					+ link
					+ "\n\n If you received this verification email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail. If you are unable to click on the link above, please copy and paste above URL in your browser's address bar."
					+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "
					+ ccemail + " ." + "\n\n EventXpert Team");

			Transport.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private VenueInfo createVenueInfo() {
		VenueInfo venueInfo = new VenueInfo();

		if (this.venueId != 0) {
			venueInfo.setVenueId(this.venueId);
		}

		venueInfo.setVenueTypeId(getVenueTypeMaster(this.venueTypeId));
		venueInfo.setVenueDesc("Test");
		if (this.parkingAvailable)
			venueInfo.setIsParkingAvailable('Y');
		if (this.isAccomodationAvailable())
			venueInfo.setIsAccomodationAvailable('Y');
		if (this.isDiningAvailable())
			venueInfo.setIsDiningAvailable('Y');
		if (this.isStewartsAvailable())
			venueInfo.setIsDiningAvailable('Y');
		venueInfo.setAreaId(getAreaName(this.areaId));
		venueInfo.setIsActive('Y');

		venueInfo.setVenueCode("V");
		this.spName = this.vName;
		venueInfo.setVenueName(this.vName);
		venueInfo.setVenueDesc(this.vendorDescription);
		// serviceInfo.setServiceTypeCode(getServiceTypeMaster(this.spCode));
		Timestamp ts = new Timestamp(new Date().getTime());
		venueInfo.setVenueEffectiveDate(ts);
		venueInfo.setVenueEtDate(ts);
		venueInfo.setCreatedDate(ts);
		if (this.acceptTerms)
			venueInfo.setAcceptTermsConditions('Y');

		return venueInfo;
	}

	private AddressInfo createAddressInfo() {
		AddressInfo addInfo = new AddressInfo();
		addInfo.setAddressLine1(this.streetAddress);
		addInfo.setAddressLine2(getAreaName(this.areaId).getAreaName());
		addInfo.setContactName(this.vName);
		addInfo.setContactPhone("" + this.phone);
		addInfo.setPostalCode(this.zipcode);
		addInfo.setEmail(this.emailaddr);
		addInfo.setCityId(this.cityId);
                
                EntityManager em = getEntityManager();
                em.getTransaction().begin();
                CityTypeMaster cm = null;
                Query qC = em.createNamedQuery("CityTypeMaster.findByCityId");
                qC.setParameter("cityId", this.cityId);
                logger.debug("query "+qC);
                List aList = qC.getResultList();
                if(aList.size()>0)
                {
                        cm = (CityTypeMaster) aList.get(0);
                }

                String addr = addInfo.getAddressLine1() + "," +addInfo.getAddressLine2() + "," +addInfo.getPostalCode() + "," +cm.getCityName();

                PlacesLatitudes pl = new PlacesLatitudes();
                LatitudeLongitude lng = pl.getLatLng(addr);

                addInfo.setLatitude(String.valueOf(lng.getLatitude()));
                addInfo.setLongitude(String.valueOf(lng.getLongitude()));
		
		return addInfo;
	}

	private EventxpertUser createVendorUser() {
		EventxpertUser vu = new EventxpertUser();
		vu.setUsername(this.user);
		vu.setPassword(this.password);
		vu.setPasswordHint(this.passwordHint);
		vu.setPasswordAns(this.passwordAns);
		vu.setEmailaddr(this.emailaddr);
		vu.setISActive("N");
		vu.setUserCategory("vendor");
		vu.setUserType("V");

		return vu;
	}

	@SuppressWarnings("unchecked")
	public void insertHallDetails() {
		try {

			EntityManager em = getEntityManager();

			VenueHallInfo vhl = new VenueHallInfo();
			vhl.setVenueID(this.venueId);
			vhl.setHallName(this.hallName);
			vhl.setCapacity(this.capacity);
			vhl.setMinCapacity(this.minCapacity);
			vhl.setDescription(this.hallDescription);
			vhl.setAirConditioned(this.airConditioned);
			vhl.setHallEffectiveDate(this.hallFromDate);
			vhl.setImages("imagesurl");
			vhl.setCreatedBy("Admin");
			vhl.setActive('Y');
			vhl.setCreatedDate(new Date());
			vhl.setModifiedDate(new Date());
			if (stageAvailable) {
				vhl.setStage('Y');
			} else {
				vhl.setStage('N');
			}
			em.getTransaction().begin();
			em.persist(vhl);
			em.getTransaction().commit();

			vhList.put(vhl, imagesPreviewHall);

			this.hallId = vhl.getHallID();
			imageUploadableHall = true;
			clearHallInfo();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void saveHallInfo(VenueHallInfo vhInfo) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();

		try {
			// Query q =
			// em.createQuery("select vh from VenueHallInfo vh where vh.hallID = :hallID");
			// Query q1 =
			// em.createQuery("delete from VenueHallInfo vh where vh.hallID = :hallID");
			// Iterator<VenueHallInfo> hallIterator = this.vhList.iterator();
			// VenueHallInfo vhInfo = new VenueHallInfo();
			// while(hallIterator.hasNext()){
			// VenueHallInfo vInfo = (VenueHallInfo)hallIterator.next();
			// vhInfo.setHallID(vInfo.getHallID());
			// vhInfo.setHallName(vInfo.getHallName());
			// vhInfo.setCapacity(vInfo.getCapacity());
			// vhInfo.setDescription(vInfo.getDescription());
			// vhInfo.setAirConditioned(vInfo.getAirConditioned());
			// vhInfo.setStage(vInfo.getStage());
			// vhInfo.setVenueID(vInfo.getVenueID());
			// vhInfo.setHallEffectiveDate(vInfo.getHallEffectiveDate());
			// vhInfo.setCreatedBy(vInfo.getCreatedBy());
			// vhInfo.setCreatedDate(vInfo.getCreatedDate());
			// vhInfo.setModifiedDate(new Date());
			// vhInfo.setImages(vInfo.getImages());
			// vhInfo.setActive('Y');
			//
			//
			// q.setParameter("hallID",vInfo.getHallID());
			// List<ProviderItemInfo> l = q.getResultList();
			// if(l.size() > 0){
			//
			// q1.setParameter("hallID",vInfo.getHallID());
			// q1.executeUpdate();
			// }
			// }
			em.merge(vhInfo);
			em.getTransaction().commit();
			clearHallInfo();
			// this.vhList.clear();
			// list hall details
			// Query qryHallInfoList =
			// em.createNamedQuery("VenueHallInfo.findByVenueID").setParameter("venueID",
			// this.venueId);
			// this.vhList = qryHallInfoList.getResultList();
			imageUploadableHall = true;
		} catch (ConstraintViolationException ex) {
			ex.getConstraintViolations();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void clearHallInfo() {
		setHallName(null);
		setCapacity(0);
		setAirConditioned('N');
		setStageAvailable(false);
		setHallFromDate(null);
		setDescription("");
	}

	public void removeHall() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("hallId");

		int hallId = new Integer(action).intValue();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM VenueHallInfo p WHERE p.hallID = :hallID");
		q.setParameter("hallID", hallId);
		q.executeUpdate();
		em.getTransaction().commit();

	}

	public void removeHall(int halId) {
		int hallId = new Integer(halId).intValue();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM VenueHallInfo p WHERE p.hallID = :hallID");
		q.setParameter("hallID", hallId);
		q.executeUpdate();
		em.getTransaction().commit();

	}

	private EntityManager getEntityManager() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");

		EntityManager em = emf.createEntityManager();

		return em;
	}

	@SuppressWarnings("unchecked")
	private UnitCosts getUnitCostType(int unitCostId) {
		String unitCostType = "";
                UnitCosts uc = null;
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT UC FROM UnitCosts UC WHERE UC.id = :id");
		q.setParameter("id", unitCostId);
		List<UnitCosts> object = q.getResultList();
		if (object.size() > 0) {
                        uc = object.get(0);
			unitCostType = uc.getUnitCostType();
		}
		return uc;
	}

	@SuppressWarnings("unchecked")
	private int getUnitCostTypeId(String costType) {
		int unitCostId = 0;
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT UC FROM UnitCosts UC WHERE UC.unitCostType = :unitCostType");
		q.setParameter("unitCostType", costType);
		List<UnitCosts> object = q.getResultList();
		if (object.size() > 0) {
			UnitCosts uc = object.get(0);
			unitCostId = uc.getId();
		}
		return unitCostId;
	}

	@SuppressWarnings("unchecked")
	private AreaTypeMaster getAreaName(Integer areaid) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q1 = em.createQuery("SELECT IM FROM AreaTypeMaster IM WHERE IM.areaId = :areaId");
		q1.setParameter("areaId", areaid);
		List<AreaTypeMaster> object = q1.getResultList();
		if (object.size() > 0) {
			AreaTypeMaster result = (AreaTypeMaster) object.get(0);

			em.close();
			return result;
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	private VenueTypeMaster getVenueTypeMaster(Integer venueTypeId) {
		VenueTypeMaster VTM = null;
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT vtm FROM VenueTypeMaster vtm  WHERE vtm.venueTypeId = :venueTypeId");
		q.setParameter("venueTypeId", this.venueTypeId);
		List<VenueTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			VTM = (VenueTypeMaster) object.get(0);
			em.close();
			return VTM;
		}
		return VTM;

	}

	private void addPasswordHints() {

		this.passwordHints.add(new SelectItem("What is your birth place", "What is your birth place"));
		this.passwordHints.add(new SelectItem("What is your first school name", "What is your first school name"));
		this.passwordHints.add(new SelectItem("What is your nick name", "What is your nick name"));
	}

	@SuppressWarnings("unchecked")
	public void getEventDatesList() {

		EntityManager em = getEntityManager();
		try {
			Query eventDatesListQry = em.createNamedQuery("EventDates.findByServiceProviderId");
			eventDatesListQry.setParameter("serviceProviderId", this.venueId);
			this.eventDates = eventDatesListQry.getResultList();

			SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd");

			java.util.Date d = null;
			oldEventDates = new String[eventDates.size()];

			for (int j = 0; j < eventDates.size(); j++) {
				eventDatessize++;
				EventDates ed = eventDates.get(j);

				if (ed != null && ed.getEventDate() != null) {
                                    try
                                    {
					d = readFormat.parse(ed.getEventDate().toString());
                                        //d = readFormat.parse("Fri Nov 21 00:00:00 CST 2014");
                                    }
                                    catch(ParseException e)
                                    {
                                        System.out.println("Unparseble date");
                                        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                                            Locale.ENGLISH);
                                        
                                        d = sdf.parse(ed.getEventDate().toString());
                                        
                                    }

					String str = writeFormat.format(d);
                                        System.out.println("str "+str+ "  "+d);
                                        
					java.sql.Date nd = new java.sql.Date(d.getTime());

					ed.setEventDate(new Timestamp(nd.getTime()));
					this.eventDates.set(j, ed);
					this.oldEventDates[j] = "'" + str + "'";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void insertEventDates() {
		try {

			EntityManager em = getEntityManager();
			EventDates eDate = new EventDates();

			eDate.setEventDate(this.eventDate);
			eDate.setServiceProviderId(this.venueId);
			em.getTransaction().begin();
			em.persist(eDate);
			em.getTransaction().commit();
			getEventDatesList();
			setEventDate(null);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void removeEventDate() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("eventDateId");
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM EventDates p WHERE p.dateId = :dateId");
		q.setParameter("dateId", new Integer(action).intValue());
		q.executeUpdate();
		em.getTransaction().commit();
		getEventDatesList();
	}

	public void adminInformation(String mess) {
		this.regMessage = mess;

		try {
			this.activeTab = 0;

		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void registered() {

		try {
			this.registered = true;
			registerDisable = false;
			this.activeTab = 0;
			// FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
		} catch (Exception e) {
			e.getMessage();

		}
	}

	@SuppressWarnings("unchecked")
	private List<SelectItem> getUnitCostTypes() {
		this.unitCostsInfo.clear();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT UC FROM UnitCosts UC WHERE UC.serviceTypeCode = :serviceTypeCode");
		// q.setParameter("serviceTypeCode", ""+this.vCode);
		q.setParameter("serviceTypeCode", "VEN");
		List<UnitCosts> object = q.getResultList();
		Iterator<UnitCosts> ucIterator = object.iterator();
		while (ucIterator.hasNext()) {
			UnitCosts uc = (UnitCosts) ucIterator.next();
			// unitCosts.add(uc.getUnitCostType());
			// this.unitCostsInfo.put(uc.getUnitCostType(), uc.getId());
			this.unitCostsInfo.add(new SelectItem(uc.getId(), uc.getUnitCostType()));
		}
		return this.unitCostsInfo;
	}

//         @SuppressWarnings("unchecked")
	private List<SelectItem> getEventTypes() {
		this.eventTypeList.clear();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT UC FROM EventTypeMaster UC");
		
		List<EventTypeMaster> object = q.getResultList();
		Iterator<EventTypeMaster> ucIterator = object.iterator();
		while (ucIterator.hasNext()) {
			EventTypeMaster uc = (EventTypeMaster) ucIterator.next();			
			this.eventTypeList.add(new SelectItem(uc.getEventId(), uc.getEventType()));
		}
		return this.eventTypeList;
	}
         
	// venue packages code starts here

	@SuppressWarnings("unchecked")
	public String insertVenuePackageDetails() {
		try {

			EntityManager em = getEntityManager();
			VenuePackageInfo pInfo = new VenuePackageInfo();
			pInfo.setPackageName(this.packageName);
			pInfo.setDescription(this.description);
			// pInfo.setMinGuests(0);
//                        pInfo.setEventType(new EventTypeMaster(eventTypeId));
                        pInfo.setEventType(getEventId(eventTypeId));
			pInfo.setPackageEffectiveDate(new Timestamp(this.pkgFromDate.getTime()));
			pInfo.setPackageEtDate(new Timestamp(this.pkgToDate.getTime()));
			pInfo.setCreatedDate(new Timestamp(this.pkgToDate.getTime()));
			pInfo.setCost(this.cost);
			// pInfo.setEventType(getEventName(this.eventId));
			pInfo.setUnitOfCost(getUnitCostType(this.unitCostId));
			// pInfo.setServiceProviderId(getServiceProvider(this.spId));
			pInfo.setVenueId(getVenueInfo(this.venueId));
			em.getTransaction().begin();
			Query q = em.createQuery("select pif from VenuePackageInfo pif where pif.packageId = :packageId");
			q.setParameter("packageId", this.packageId);
			List<VenuePackageInfo> l = q.getResultList();
			if (l.size() > 0) {
				Query q1 = em.createQuery("delete from PackageInfo pif where pif.packageId = :packageId");
				q1.setParameter("packageId", this.packageId);
				q1.executeUpdate();
			}
			boolean packageNameExists = false;

			Query q1 = em.createNamedQuery("VenuePackageInfo.findByVenueId");
			q1.setParameter("venueId", getVenueInfo(this.venueId));
			List<VenuePackageInfo> l1 = q1.getResultList();
			if (l1.size() > 0) {

				Iterator<VenuePackageInfo> packageIterator = l1.iterator();

				while (packageIterator.hasNext()) {
					VenuePackageInfo vpInfo = (VenuePackageInfo) packageIterator.next();
					String pName = vpInfo.getPackageName();
					if (pName.equalsIgnoreCase(this.packageName)) {
						packageNameExists = true;
					}
				}
			}
			if (!packageNameExists) {
				em.persist(pInfo);
				em.getTransaction().commit();
				packageId = pInfo.getPackageId();

				packageSize++;
				// populate the package info details
				this.packageId = 0;
				this.pList.clear();

				// RequestContext.getCurrentInstance().reset("frm:packagedet");
			} else {
				this.pList.clear();
				this.pNameExists = "Package with same name already exists, give different name";

			}

			this.packageInfoList = getPackageDetaills(this.venueId);
			createPackageList(this.packageInfoList);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			clearPackageValues();
		}
		clearPackageValues();
		// this.activeTab = 1;
		return "vendor-venue.jsf";
	}

	@SuppressWarnings("unchecked")
	private VenueInfo getVenueInfo(int venueId) {
		EntityManager em = getEntityManager();

		em.getTransaction().begin();

		Query q = em.createQuery("select spi from VenueInfo spi where spi.venueId = :venueId");
		q.setParameter("venueId", this.venueId);
		List<VenueInfo> l = q.getResultList();
		if (l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void savePackageInfo(VenuePackageDetails pkgdetails) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();

		try {
			Query q = em.createQuery("select pif from VenuePackageInfo pif where pif.packageId = :packageId");
			// Query q1 =
			// em.createQuery("delete from VenuePackageInfo pif where pif.packageId = :packageId");
			q.setParameter("packageId", pkgdetails.getPackageId());
			List<VenuePackageInfo> l = q.getResultList();
			// Iterator<PackageDetails> packageIterator = this.pList.iterator();
			VenuePackageInfo pInfo = (VenuePackageInfo) l.get(0);
			// Iterator<VenuePackageDetails> packageIterator =
			// this.pList.iterator();
			// VenuePackageInfo pInfo = new VenuePackageInfo();
			// while(packageIterator.hasNext())
			{
				VenuePackageDetails psInfo = pkgdetails;// (VenuePackageDetails)packageIterator.next();
				pInfo.setPackageName(psInfo.getPackageName());
				pInfo.setDescription(psInfo.getDescription());
				// pInfo.setMinGuests(0);

				List<String> fromDate = new ArrayList<String>();
				if (psInfo.getStrFrom() != null) {
					String from = psInfo.getStrFrom();

					StringTokenizer st2 = new StringTokenizer(from, "-");
					while (st2.hasMoreElements()) {
						fromDate.add(st2.nextToken());
					}
					int year = new Integer(fromDate.get(2)).intValue();
					int month = new Integer(fromDate.get(1)).intValue() - 1;
					int day = new Integer(fromDate.get(0)).intValue();
					Calendar c = Calendar.getInstance();
					c.set(year, month, day);
					pInfo.setPackageEffectiveDate(new Timestamp(c.getTimeInMillis()));
				}

				List<String> toDate = new ArrayList<String>();
				if (psInfo.getStrTo() != null) {
					String to = psInfo.getStrTo();

					StringTokenizer st2 = new StringTokenizer(to, "-");
					while (st2.hasMoreElements()) {
						toDate.add(st2.nextToken());
					}
					int year = new Integer(toDate.get(2)).intValue();
					int month = new Integer(toDate.get(1)).intValue() - 1;
					int day = new Integer(toDate.get(0)).intValue();
					Calendar c = Calendar.getInstance();
					c.set(year, month, day);
					pInfo.setPackageEtDate(new Timestamp(c.getTimeInMillis()));
				}
				pInfo.setCost(psInfo.getCost());
				// pInfo.setEventType(psInfo.getEventType());
				pInfo.setUnitOfCost(psInfo.getUnitOfCost());
                                pInfo.setEventType(psInfo.getEventType());
				VenueInfo sp = getVenueInfo(this.venueId);
				pInfo.setVenueId(sp);

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// get current date time with Date()
				Date date = new Date();
				// System.out.println(dateFormat.format(date));
				// pInfo.setUpdatedDate(new Date(dateFormat.format(date)));
				pInfo.setUpdatedDate(date);
				/*
				 * q.setParameter("packageId",psInfo.getPackageId());
				 * List<ProviderItemInfo> l = q.getResultList(); if(l.size() >
				 * 0){
				 * 
				 * q1.setParameter("packageId",psInfo.getPackageId());
				 * q1.executeUpdate(); }
				 */
			}
			em.merge(pInfo);
			em.getTransaction().commit();

			Query query = em.createNamedQuery("VenueCostInfo.findByPackageId");
			query.setParameter("packageId", pInfo);
			List pkgList = query.getResultList();
			if (pkgList.isEmpty()) {
				// negotiatedCost.put(pi.getPackageId(), "0");
				// customerDiscount.put(pi.getPackageId(), "0");
				// finalAmount.put(pi.getPackageId(), "0");
			} else {
				VenueCostInfo spi = (VenueCostInfo) pkgList.get(0);
				BigDecimal vendorCostSaved = spi.getVendorCost();
				// EntityManagerFactory emf;
				BigDecimal pcost = pInfo.getCost();
				// String dis = spi.getCustomerDiscount();
				BigDecimal discount = spi.getCustomerDiscount();// new
																// BigDecimal(dis);
				BigDecimal finalamt = pcost.subtract(discount);
				em = getEntityManager();
				// ServiceProviderCostInfo serviceProviderCostInfo
				// VenuePackageInfo pInfo = new VenuePackageInfo(pkgid);
				em.getTransaction().begin();
				// serviceProviderCostInfo = spi;//(VenueCostInfo)
				// srvpCostV.get(pkgid);
				spi.setVendorCost(pcost);
				spi.setNegotiatedCost(spi.getNegotiatedCost());
				spi.setCustomerDiscount(spi.getCustomerDiscount());
				spi.setFinalAmount(finalamt);
				em.merge(spi);
				em.getTransaction().commit();
			}

			this.pList.clear();
			this.packageInfoList = getPackageDetaills(this.venueId);
			createPackageList(this.packageInfoList);
		} catch (ConstraintViolationException ex) {
			ex.getConstraintViolations();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void clearPackageValues() {
		setPackageName(null);
		setDescription(null);
		setCost(null);
		// setEventType(null);
		setUnitOfCost(null);
		// setEventId(0);
		setUnitCostId(0);
		setPkgFromDate(null);
		setPkgToDate(null);

	}

	public void modifyPackage() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("packageId");
		int packageId = new Integer(action).intValue();
		int index = -1;
		Iterator<VenuePackageDetails> packageIterator = this.pList.iterator();
		while (packageIterator.hasNext()) {
			index++;

			VenuePackageDetails psInfo = (VenuePackageDetails) packageIterator.next();

			if (packageId == psInfo.getPackageId()) {

				this.packageId = psInfo.getPackageId();
				this.packageName = psInfo.getPackageName();
				this.description = psInfo.getDescription();
//				this.eventId = getEventId(psInfo.getEventType());
                                this.eventId = psInfo.getEventType().getEventId();
				this.cost = psInfo.getCost();
				this.unitCostId = (psInfo.getUnitOfCost().getId());
				this.pkgFromDate = psInfo.getPkgFromDt();
				this.pkgToDate = psInfo.getPkgToDt();
			}
		}

		editPackage = true;
	}

	public void removePackage() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("packageId");

		int packageId = new Integer(action).intValue();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM VenuePackageInfo p WHERE p.packageId = :packageId");
		q.setParameter("packageId", packageId);
		q.executeUpdate();
		em.getTransaction().commit();

		getExistingServiceProvider();
	}

	public void removePackage(int pkgId) {
		int packageId = new Integer(pkgId).intValue();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM VenuePackageInfo p WHERE p.packageId = :packageId");
		q.setParameter("packageId", packageId);
		q.executeUpdate();
		em.getTransaction().commit();

		getExistingServiceProvider();
	}

	/*@SuppressWarnings("unchecked")
	private int getEventId(String eventType) {
		int eId = 0;
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT etm FROM EventTypeMaster etm  WHERE etm.eventType = :eventType");
		q.setParameter("eventType", eventType);
		List<EventTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			EventTypeMaster ETM = (EventTypeMaster) object.get(0);
			eId = new Long(ETM.getEventId()).intValue();
		}
		em.close();
		return eId;
	} */
        
        @SuppressWarnings("unchecked")
	private EventTypeMaster getEventId(int eventType) {
		int eId = 0;
                EventTypeMaster ETM = null;
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT etm FROM EventTypeMaster etm  WHERE etm.eventId = :eventType");
		q.setParameter("eventType", eventType);
		List<EventTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			ETM = (EventTypeMaster) object.get(0);
			eId = new Long(ETM.getEventId()).intValue();
		}
		em.close();
		return ETM;
	}

	private void getExistingServiceProvider() {
		this.pList.clear();

		this.packageInfoList = getPackageDetaills(this.venueId);
		createPackageList(this.packageInfoList);
	}

	private void createPackageList(List<VenuePackageInfo> list) {
		try {
			Iterator<VenuePackageInfo> packageIterator = list.iterator();

			while (packageIterator.hasNext()) {
				packageSize++;
				VenuePackageInfo psInfo = (VenuePackageInfo) packageIterator.next();
				Date tsFrom = psInfo.getPackageEffectiveDate();

				String from = null;
				if (tsFrom != null) {
					from = tsFrom.getDate() + "-" + (tsFrom.getMonth() + 1) + "-" + (1900 + tsFrom.getYear());
				} else {
					from = "Enter From Date";
				}

				String to = null;
				Date tsTo = psInfo.getPackageEtDate();

				if (tsTo != null) {
					to = tsTo.getDate() + "-" + (tsTo.getMonth() + 1) + "-" + (1900 + tsTo.getYear());
				} else {
					to = "Enter To Date";
				}

				FacesContext ctx = FacesContext.getCurrentInstance();
				String path = ctx.getExternalContext().getInitParameter("imgpath");

				File dir = new File(path + "/venue/" + this.venueId + "/packages/" + psInfo.getPackageId());

				List img = listPkgsFilesForFolder(dir, psInfo.getPackageId());

				this.pList.add(new VenuePackageDetails(psInfo.getPackageId(), psInfo.getPackageName(), psInfo
						.getEventType(), psInfo.getDescription(), psInfo.getCost(), psInfo.getUnitOfCost(), psInfo
						.getVenueId(), psInfo.getPackageEffectiveDate(), psInfo.getPackageEtDate(), from, to, img));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private List<VenuePackageInfo> getPackageDetaills(int spId) {

		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT PI FROM VenuePackageInfo PI  WHERE PI.venueId = :venueId");
		q.setParameter("venueId", getVenueInfo(spId));
		this.pInfoList = q.getResultList();
		em.close();

		return pInfoList;
	}

	public void areaChangeEvent(ValueChangeEvent e) {

		if (e.getNewValue() != null) {
			Integer areaId = new Integer(e.getNewValue().toString());

			Iterator<AreaTypeMaster> areaIterator = this.areaTypeMasterList.iterator();
			while (areaIterator.hasNext()) {
				AreaTypeMaster areaTypeMaster = areaIterator.next();
				if (areaId == areaTypeMaster.getAreaId()) {
					CityTypeMaster city = areaTypeMaster.getCityId();
					if (cityInfo != null)
						this.cityInfo.clear();
					this.cityInfo.add(new SelectItem(city.getCityId(), city.getCityName()));
				}
			}
		}
	}

	public void handleFileUpload(FileUploadEvent event) throws URISyntaxException {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getInitParameter("imgpath");

		// File dir = new File(path+"/vendors/"+this.venueId);
		File dir = new File(path + "/venue/" + this.venueId);
		/*
		 * URI uri = new URI("http://img.eventxpert.in/vendors/"+this.spId);
		 * File dir = new File(uri);
		 */
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		File result = new File(dir + "//" + event.getFile().getFileName());
		Image img = new Image();
		// String url = ctx.getExternalContext().getInitParameter("imgurl");
		img.setPath(url + "/venue/" + this.venueId + "/" + event.getFile().getFileName());
		img.setImageName(event.getFile().getFileName());
		imagesPreview.add(img);

		int BUFFER_SIZE = 6124;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(result);

			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();
			listFilesForFolder(dir, this.venueId);
			logger.info("File Description :file name: " + event.getFile().getFileName() + "file size: "
					+ event.getFile().getSize() / 1024 + " Kb content type: " + event.getFile().getContentType()
					+ "The file was uploaded.");

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The files were not uploaded!", "");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}

	}

	private void listFilesForFolder(final File folder, Integer spId) {
		try {
			imagesPreview.clear();
			Image img;
			FacesContext ctx = FacesContext.getCurrentInstance();
			// String url = ctx.getExternalContext().getInitParameter("imgurl");
			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						// listFilesForFolder(fileEntry,spId);
					} else {

						img = new Image();
						img.setImageName(fileEntry.getName());
						// img.setPath("http://"+url+"/vendors/"+spId+"/"+fileEntry.getName());
						img.setPath("http://" + url + "/venue/" + spId + "/" + fileEntry.getName());

						imagesPreview.add(img);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void handleFileUploadPkg(FileUploadEvent event) throws URISyntaxException {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getInitParameter("imgpath");
		Integer pkgid = (Integer) event.getComponent().getAttributes().get("pkgid");
		Integer rowind = (Integer) event.getComponent().getAttributes().get("rowind");
		File dirPk = new File(path + "/venue/" + this.venueId + "/packages");
		File dir = new File(path + "/venue/" + this.venueId + "/packages/" + pkgid);
		
		 * URI uri = new URI("http://img.eventxpert.in/vendors/"+this.spId);
		 * File dir = new File(uri);
		 
		if (!dirPk.exists()) {
			if (dirPk.mkdirs()) {
				logger.info("Packages Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		File result = new File(dir + "//" + event.getFile().getFileName());
		Image img = new Image();
		// String url = ctx.getExternalContext().getInitParameter("imgurl");
		img.setPath(url + "/venue/" + this.venueId + "/packages/" + pkgid + "/" + event.getFile().getFileName());
		System.out.println("File Upload Listener for Packages ************** " + img.getPath());
		img.setImageName(event.getFile().getFileName());
		// imagesPreview.add(img);
		int BUFFER_SIZE = 6124;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(result);

			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();

			this.pList.get(pkgid).setImagesPreview(listPkgsFilesForFolder(dir, pkgid));
			logger.info("File Description :file name: " + event.getFile().getFileName() + "file size: "
					+ event.getFile().getSize() / 1024 + " Kb content type: " + event.getFile().getContentType()
					+ "The file was uploaded.");

		} catch (Exception e) {
			e.printStackTrace();

			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The files were not uploaded!", "");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}

	}*/

	/*private List<Image> listFilesForFolderPkg(final File folder, Integer pid) {
		List<Image> imagesPreviewPkg = new ArrayList<Image>();
		try {

			Image img;
			FacesContext ctx = FacesContext.getCurrentInstance();
			// String url = ctx.getExternalContext().getInitParameter("imgurl");

			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listFilesForFolderPkg(fileEntry, pid);
					} else {

						img = new Image();
						img.setImageName(fileEntry.getName());
						img.setPath("http://" + url + "/venue/" + this.venueId + "/packages/" + pid + "/" + fileEntry.getName());
						System.out.println("List packages here ############# " + img.getPath());
						imagesPreviewPkg.add(img);
					}
				}
			} else {
				// imagesPreviewPkg.add("images/noImage.gif");
				img = new Image();
				img.setImageName("noImage");
				img.setPath("images/noImage.gif");
				imagesPreviewPkg.add(img);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagesPreviewPkg;
	}*/

	public void handlePkgFileUpload(FileUploadEvent event) throws URISyntaxException {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getInitParameter("imgpath");
		int pkgId = Integer.parseInt(event.getComponent().getAttributes().get("pkgid").toString());
		
		File dirPk = new File(path + "/venue/" + this.venueId + "/packages");
		File dir = new File(path + "/venue/" + this.venueId + "/packages/" + pkgId);
		
		if (!dirPk.exists()) {
			if (dirPk.mkdirs()) {
				logger.info("Packages Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		File result = new File(dir + "//" + event.getFile().getFileName());
		Image img = new Image();
		// String url = ctx.getExternalContext().getInitParameter("imgurl");
		// img.setPath(url+"/vendors/"+this.hallId+"/"+event.getFile().getFileName());
		img.setPath(url + "/venue/" + this.venueId + "/packages/" + pkgId + "/" + event.getFile().getFileName());
		System.out.println("File Upload Listener for Packages ************** " + img.getPath());
		img.setImageName(event.getFile().getFileName());
		imagesPreviewHall.add(img);
		int BUFFER_SIZE = 6124;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(result);

			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();
			for (VenuePackageDetails pkgDtl:pList){
				if (pkgDtl.getPackageId() == pkgId)
					pkgDtl.setImagesPreview(listPkgsFilesForFolder(dir, pkgId));
			}
			
			
			logger.info("File Description :file name: " + event.getFile().getFileName() + "file size: "
					+ event.getFile().getSize() / 1024 + " Kb content type: " + event.getFile().getContentType()
					+ "The file was uploaded.");
			
		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The files were not uploaded!", "");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}

	}

	private List<Image> listPkgsFilesForFolder(final File folder, Integer spId) {
		List<Image> imagesPreviewHalla = new ArrayList<Image>();
		try {
			// imagesPreviewHall.clear();
			Image img;
			FacesContext ctx = FacesContext.getCurrentInstance();
			// String url = ctx.getExternalContext().getInitParameter("imgurl");

			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listHallFilesForFolder(fileEntry, spId);
					} else {

						img = new Image();
						img.setImageName(fileEntry.getName());
						// img.setPath("http://"+url+"/vendors/"+spId+"/"+fileEntry.getName());
						img.setPath("http://" + url + "/venue/" + this.venueId + "/packages/" + spId + "/" + fileEntry.getName());
						System.out.println("List Packages here ############# " + img.getPath());
						imagesPreviewHalla.add(img);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagesPreviewHalla;
	}

	public void handleHallFileUpload(FileUploadEvent event) throws URISyntaxException {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getInitParameter("imgpath");
		int hallId = Integer.parseInt(event.getComponent().getAttributes().get("hallId").toString());
		VenueHallInfo vh = (VenueHallInfo) event.getComponent().getAttributes().get("vhInfo");
		// File dir = new File(path+"/vendors/"+this.hallId);
		File dir = new File(path + "/venue/" + this.venueId + "/" + hallId);
		/*
		 * URI uri = new URI("http://img.eventxpert.in/vendors/"+this.spId);
		 * File dir = new File(uri);
		 */
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		File result = new File(dir + "//" + event.getFile().getFileName());
		Image img = new Image();
		// String url = ctx.getExternalContext().getInitParameter("imgurl");
		// img.setPath(url+"/vendors/"+this.hallId+"/"+event.getFile().getFileName());
		img.setPath(url + "/venue/" + this.venueId + "/" + vh.getHallID() + "/" + event.getFile().getFileName());
		System.out.println("File Upload Listener for Halls ************** " + img.getPath());
		img.setImageName(event.getFile().getFileName());
		imagesPreviewHall.add(img);
		int BUFFER_SIZE = 6124;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(result);

			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();
			imagesPreviewHall = listHallFilesForFolder(dir, hallId);
			logger.info("File Description :file name: " + event.getFile().getFileName() + "file size: "
					+ event.getFile().getSize() / 1024 + " Kb content type: " + event.getFile().getContentType()
					+ "The file was uploaded.");

			vhList.put(vh, imagesPreviewHall);
		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The files were not uploaded!", "");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}

	}

	private List<Image> listHallFilesForFolder(final File folder, Integer spId) {
		List<Image> imagesPreviewHalla = new ArrayList<Image>();
		try {
			// imagesPreviewHall.clear();
			Image img;
			FacesContext ctx = FacesContext.getCurrentInstance();
			// String url = ctx.getExternalContext().getInitParameter("imgurl");

			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listHallFilesForFolder(fileEntry, spId);
					} else {

						img = new Image();
						img.setImageName(fileEntry.getName());
						// img.setPath("http://"+url+"/vendors/"+spId+"/"+fileEntry.getName());
						img.setPath("http://" + url + "/venue/" + this.venueId + "/" + spId + "/" + fileEntry.getName());
						System.out.println("List halls here ############# " + img.getPath());
						imagesPreviewHalla.add(img);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagesPreviewHalla;
	}

	public void deleteImage() {
		try {
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			String imagepath = params.get("imagepath");
			URL fileUri = new URL(imagepath);
			int startIndex = fileUri.toString().lastIndexOf('/');
			String fileName = fileUri.toString().substring(startIndex + 1);
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			// String path = context.getInitParameter("imgpath");
			File deleteimage = new File(path + "/venue/" + this.venueId + "/" + fileName);
			boolean val = deleteimage.delete();
			removeImage(imagepath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteImagePkg(int pid) {
		try {
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			String imagepath = params.get("imagepath");
			URL fileUri = new URL(imagepath);
			int startIndex = fileUri.toString().lastIndexOf('/');
			String fileName = fileUri.toString().substring(startIndex + 1);
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			// String path = context.getInitParameter("imgpath");
			File deleteimage = new File(path + "/venue/" + this.venueId + "/" + pid + "/" + fileName);
			boolean val = deleteimage.delete();
			removeImage(imagepath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void removeImage(String path) {
		for (int i = 0; i < this.imagesPreview.size(); i++) {
			Image p = (Image) imagesPreview.get(i);
			if (p.getPath().equalsIgnoreCase(path)) {
				imagesPreview.remove(i);
			}
		}
	}

	public void deleteHallImage(VenueHallInfo vh) {
		try {
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			String imagepath = params.get("imagepath");
			URL fileUri = new URL(imagepath);
			int startIndex = fileUri.toString().lastIndexOf('/');
			String fileName = fileUri.toString().substring(startIndex + 1);
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			// String path = context.getInitParameter("imgpath");
			File deleteimage = new File(path + "/venue/" + this.venueId + "/" + vh.getHallID() + "/" + fileName);
			boolean val = deleteimage.delete();
			removeHallImage(imagepath, vh);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeHallImage(String path, VenueHallInfo vh) {
		List<Image> imageshall = vhList.get(vh);
		for (int i = 0; i < imageshall.size(); i++) {
			Image p = (Image) imageshall.get(i);
			if (p.getPath().equalsIgnoreCase(path)) {
				imageshall.remove(i);
			}
		}
		vhList.put(vh, imageshall);
	}

	private String oldPassword;
	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String updatePassword() {

		try {
			// logger.debug("link "+customerInfo.getEmail());
			EntityManager em01 = getEntityManager();
			em01.getTransaction().begin();
			Query query = em01
					.createQuery("SELECT c FROM EventxpertUser c WHERE c.username = :username and c.password = :password");
			query.setParameter("username", exu.getUsername());
			query.setParameter("password", oldPassword);
			List cList = query.getResultList();

			if (cList.size() > 0) {
				if (oldPassword.equals(newPassword)) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Both the Passwords entered are same. Try Again", "Password Matched");
					FacesContext.getCurrentInstance().addMessage("Password Matched", msg);
					return "";
				} else {
					exu.setPassword(newPassword);
					em01.merge(exu);
					em01.getTransaction().commit();
					em01.close();
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Successfully",
							"Password Changed");
					FacesContext.getCurrentInstance().addMessage("Password Changed", msg);
				}
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Entered Password does not match. Try Again", "Password Not Matched");
				FacesContext.getCurrentInstance().addMessage("Password Not Matched", msg);
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"There is some unexpected error. Sorry for the inconvenience, Please try again later", "Exception");
			FacesContext.getCurrentInstance().addMessage("Exception", msg);

		} finally {
			// em.close();

		}
		return "";
	}

	public void insertMultipleEventDates() {
		try {

			EntityManager em = getEntityManager();
			EventDates eDate = new EventDates();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<EventDates> eDates = new ArrayList<EventDates>();
			EventDates dateObject;
			for (String date : dates) {
				dateObject = new EventDates();
				dateObject.setServiceProviderId(this.venueId);
				dateObject.setEventDate(dateFormat.parse(date));
				eDates.add(dateObject);
			}

			em.getTransaction().begin();
			for (EventDates saveDate : eDates) {
				em.persist(saveDate);
			}
			em.getTransaction().commit();
			getEventDatesList();
			setDates(new HashSet<String>());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleDateSelect(SelectEvent event) {
		Date date = (Date) event.getObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dates.add(dateFormat.format(date));
	}

	/**
	 * @return the dates
	 */
	public Set<String> getDates() {
		return dates;
	}

	/**
	 * @param dates
	 *            the dates to set
	 */
	public void setDates(Set<String> dates) {
		this.dates = dates;
	}

}
