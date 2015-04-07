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
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.util.logging.Level;

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
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.CityTypeMaster;
import com.circulous.xpert.jpa.entities.EventDates;
import com.circulous.xpert.jpa.entities.EventTypeMaster;
import com.circulous.xpert.jpa.entities.EventxpertUser;
import com.circulous.xpert.jpa.entities.ItemCategoryMaster;
import com.circulous.xpert.jpa.entities.ItemMaster;
import com.circulous.xpert.jpa.entities.PackageInfo;
import com.circulous.xpert.jpa.entities.PackageItemInfo;
import com.circulous.xpert.jpa.entities.ProviderItemInfo;
import com.circulous.xpert.jpa.entities.ServiceProviderCostInfo;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo;
import com.circulous.xpert.jpa.entities.ServiceTypeMaster;
import com.circulous.xpert.jpa.entities.UnitCosts;
import com.circulous.xpert.model.ColumnModel;
import com.circulous.xpert.model.EventScheduleInfo;
import com.circulous.xpert.model.Image;
import com.circulous.xpert.model.PackageCatgeoryItem;
import com.circulous.xpert.model.PackageDetails;
import com.circulous.xpert.model.ProviderItem;
import java.text.ParseException;
import java.util.*;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 * @author jchowhan
 * 
 */
@ManagedBean(name = "serviceProviderManagedBean")
@SessionScoped
public class ServiceProviderManagedBean implements Serializable {
	private static Logger logger = Logger.getLogger(ServiceProviderManagedBean.class);

	public ServiceProviderManagedBean() {
		generateMasterDataList();
		addObjects();
		addPasswordHints();
		// getEventDatesList();
		this.activeTab = 0;
		this.registered = false;
		registerDisable = true;
		this.loggedin = false;		
                Properties prop = new Properties();
                    InputStream input = null;
		dates = new HashSet<String>();
		toAddPackageDtls = new PackageDetails();

                    try {

                             FacesContext ctx = FacesContext.getCurrentInstance();
                        ExternalContext externalContext = ctx.getExternalContext();
                        input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");

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
	/*
	 * public void setCategories(List<String> categories) { this.categories =
	 * categories; }
	 */

	private List<ServiceTypeMaster> serviceTypeMasterList = new ArrayList<ServiceTypeMaster>();
	private List<AreaTypeMaster> areaTypeMasterList = new ArrayList<AreaTypeMaster>();
	private List<CityTypeMaster> cityTypeMasterList = new ArrayList<CityTypeMaster>();
	private List<EventTypeMaster> eventMasterList = new ArrayList<EventTypeMaster>();
	private List<PackageInfo> packageInfoList = new ArrayList<PackageInfo>();
	private List<PackageInfo> pInfoList = new ArrayList<PackageInfo>();
	private List<PackageDetails> pList = new ArrayList<PackageDetails>();
	private List<String> imagePaths = new ArrayList<String>();
	private List<Image> imagesPreview = new ArrayList<Image>();
	private List<ProviderItemInfo> pItemList = new ArrayList<ProviderItemInfo>();
	private List<ProviderItem> pItemsList = new ArrayList<ProviderItem>();
	private List<String> unitCosts = new ArrayList<String>();
	private List<EventScheduleInfo> esInfo = new ArrayList<EventScheduleInfo>();
	private List<String> items = new ArrayList<String>();
	private List<ItemMaster> itemMasterList = new ArrayList<ItemMaster>();

	private List<SelectItem> areaInfo = new ArrayList<SelectItem>();

	private List<SelectItem> cityInfo = new ArrayList<SelectItem>();
	private List<SelectItem> servicesInfo = new ArrayList<SelectItem>();
	private List<SelectItem> passwordHints = new ArrayList<SelectItem>();
	private List<SelectItem> eventInfo = new ArrayList<SelectItem>();
	private List<SelectItem> itemInfo = new ArrayList<SelectItem>();

	private Map<String, Integer> itemCategoryInfo = new HashMap<String, Integer>();
	private HashMap servicesInfoMap = new HashMap();

	Map<String, List<PackageCatgeoryItem>> pciMap = new LinkedHashMap<String, List<PackageCatgeoryItem>>();
	private List<SelectItem> unitCostsInfo = new ArrayList<SelectItem>();
        private List<SelectItem> eventTypeList = new ArrayList<SelectItem>();
	private List<Map<String, List<PackageCatgeoryItem>>> pciList = new ArrayList<Map<String, List<PackageCatgeoryItem>>>();
	private String regMessage;

	private List<ColumnModel> columns = new ArrayList<ColumnModel>();

	private Integer spId = new Integer(0);
	private Integer areaId = new Integer(0);
	private Integer eventId = new Integer(0);
	private Integer itemId = new Integer(0);
	private Integer item = new Integer(0);
	private Integer itemCategoryId = new Integer(0);
	private int activeTab;
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
	private String spCode;
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

	private Date pkgFromDate;
	private Date pkgToDate;
	private int eventDatessize;
	private int login = 0;
	private boolean createMyAccount = false;
	private String serviceTypeCode;
	private String vendorDescription;
	private String pNameExists;
	private PackageInfo selectedPkgDtls;
	private PackageDetails toAddPackageDtls;
	private Set<String> dates;
	private Date dateSelected;
	private int pkgid1;
    private int eventTypeId;
    private boolean admin;

    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
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

	public String getServiceProviderCode() {
		return serviceTypeCode;
	}

	public void setServiceProviderCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<ProviderItem> getpItemsList() {
		return pItemsList;
	}

	public void setpItemsList(List<ProviderItem> pItemsList) {
		this.pItemsList = pItemsList;
	}

	public boolean isCreateMyAccount() {
		return createMyAccount;
	}

	public void setCreateMyAccount(boolean createMyAccount) {
		this.createMyAccount = createMyAccount;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getEventDatessize() {
		return eventDatessize;
	}

	public void setEventDatessize(int eventDatessize) {
		this.eventDatessize = eventDatessize;
	}

	public boolean isLoggedin() {
		return loggedin;
	}

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public boolean isRegisterDisable() {
		return registerDisable;
	}

	public void setRegisterDisable(boolean registerDisable) {
		this.registerDisable = registerDisable;
	}

	public boolean getRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isAcceptTerms() {
		return acceptTerms;
	}

	public void setAcceptTerms(boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}

	public List<PackageInfo> getpInfoList() {
		return pInfoList;
	}

	public List<PackageDetails> getpList() {
		return pList;
	}

	public List<Image> getImagesPreview() {
		return imagesPreview;
	}

	public List<Map<String, List<PackageCatgeoryItem>>> getPciList() {
		return pciList;
	}

	public void setPciList(List<Map<String, List<PackageCatgeoryItem>>> pciList) {
		this.pciList = pciList;
	}

	public List<String> getUnitCosts() {
		return unitCosts;
	}

	public void setUnitCosts(List<String> unitCosts) {
		this.unitCosts = unitCosts;
	}

	public List<SelectItem> getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(List<SelectItem> eventInfo) {
		this.eventInfo = eventInfo;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
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

	public List<AreaTypeMaster> getAreaTypeMasterList() {
		return areaTypeMasterList;
	}

	public List<CityTypeMaster> getCityTypeMasterList() {
		return cityTypeMasterList;
	}

	public void setCityTypeMasterList(List<CityTypeMaster> cityTypeMasterList) {
		this.cityTypeMasterList = cityTypeMasterList;
	}

	public List<ServiceTypeMaster> getServiceTypeMaster() {
		return serviceTypeMasterList;
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

	public HashMap getServicesInfoMap() {
		return servicesInfoMap;
	}

	public void setServicesInfoMap(HashMap servicesInfoMap) {
		this.servicesInfoMap = servicesInfoMap;
	}

	public List<SelectItem> getServicesInfo() {
		return servicesInfo;
	}

	public void setServicesInfo(List<SelectItem> servicesInfo) {
		this.servicesInfo = servicesInfo;
	}

        public List<SelectItem> getEventTypeList() {
            return eventTypeList;
        }

        public void setEventTypeList(List<SelectItem> eventTypeList) {
            this.eventTypeList = eventTypeList;
        }

        
	public List<SelectItem> getUnitCostsInfo() {
		return unitCostsInfo;
	}

	public void setUnitCostsInfo(List<SelectItem> unitCostsInfo) {
		this.unitCostsInfo = unitCostsInfo;
	}

	public String getPasswordAns() {
		return passwordAns;
	}

	public void setPasswordAns(String passwordAns) {
		this.passwordAns = passwordAns;
	}

	public List<SelectItem> getPasswordHints() {
		return passwordHints;
	}

	public List<ProviderItemInfo> getpItemList() {
		return pItemList;
	}

	public void setpItemList(List<ProviderItemInfo> pItemList) {
		this.pItemList = pItemList;
	}

	public String getRegMessage() {
		return regMessage;
	}

	public void setRegMessage(String regMessage) {
		this.regMessage = regMessage;
	}

	// package details
	private Integer packageId = new Integer(0);

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
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

	public String getUnitOfCost() {
		return unitOfCost;
	}

	public void setUnitOfCost(String unitOfCost) {
		this.unitOfCost = unitOfCost;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public String getPasswordHint() {
		return passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSpCode() {		
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public Integer getSpId() {
		return spId;
	}

	public void setSpId(Integer spId) {
		this.spId = spId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
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

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
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

	public int getUnitCostId() {
		return unitCostId;
	}

	public void setUnitCostId(int unitCostId) {
		this.unitCostId = unitCostId;
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

	public boolean isEditPackage() {
		return editPackage;
	}

	public void setEditPackage(boolean editPackage) {
		this.editPackage = editPackage;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public Map<String, List<PackageCatgeoryItem>> getPciMap() {
		return pciMap;
	}

	public void setPciMap(Map<String, List<PackageCatgeoryItem>> pciMap) {
		this.pciMap = pciMap;
	}

	private List<EventDates> eventDates = new ArrayList<EventDates>();

	public List<EventDates> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<EventDates> eventDates) {
		this.eventDates = eventDates;
	}

	private String[] oldEventDates;

	public String[] getOldEventDates() {

		return oldEventDates;
	}

	public void setOldEventDates(String[] oldEventDates) {
		this.oldEventDates = oldEventDates;
	}

	private Date eventDate;

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public boolean isImageUploadable() {
		return imageUploadable;
	}

	public void setImageUploadable(boolean imageUploadable) {
		this.imageUploadable = imageUploadable;
	}

	public List<EventScheduleInfo> getEsInfo() {
		return esInfo;
	}

	public void setEsInfo(List<EventScheduleInfo> esInfo) {
		this.esInfo = esInfo;
	}

	@SuppressWarnings("unchecked")
	public boolean editProfile() {
		boolean success = false;
		try {			
			clearProfileValues();
			if (this.vName == null) {
				EntityManager em = getEntityManager();
				em.getTransaction().begin();
				Query q = em.createQuery("select spi from ServiceProviderInfo spi where spi.userId = :userId");
				q.setParameter("userId", this.loginUser);
				List<ServiceProviderInfo> object = q.getResultList();
				ServiceProviderInfo spInfo = object.get(0);
				AddressInfo adInfo = spInfo.getAddressId();
				this.spId = spInfo.getServiceProviderId();
				this.areaId = spInfo.getAreaId().getAreaId();
				this.vName = adInfo.getContactName();
				this.streetAddress = adInfo.getAddressLine1();
				this.zipcode = adInfo.getPostalCode();
				this.cityId = adInfo.getCityId();
				this.phone = adInfo.getContactPhone();
				this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();
				this.vendorDescription = spInfo.getServiceProviderDesc();				
				this.userId = spInfo.getUserId().getUserId();
				this.emailaddr = spInfo.getUserId().getEmailaddr();
				this.passwordHint = spInfo.getUserId().getPasswordHint();				
				success = true;
			}
			// registered();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	private void clearProfileValues() {
		this.spId = 0;
		this.areaId = 0;
		this.vName = null;
		this.streetAddress = null;
		this.zipcode = null;
		this.cityId = 0;
		this.phone = null;
		this.spCode = null;		
		this.userId = 0;
		this.emailaddr = null;
		this.passwordHint = null;
	}

	private boolean showActLink = false;

	public boolean isShowActLink() {
		return showActLink;
	}

	public void setShowActLink(boolean showActLink) {
		this.showActLink = showActLink;
	}

	ServiceProviderInfo spInfo = null;
	EventxpertUser exu;

	@SuppressWarnings("unchecked")
	public String checkLogin() {
		String ret = "";
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q = null;
			// if(this.spCode.equals("VEN")){
			// VenueManagedBean venueManagedBean = new VenueManagedBean();
			// venueManagedBean.setvCode(spCode);
			// boolean success = venueManagedBean.checkLogin(this.loginUser,
			// this.loginPassword);
			// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("venueManagedBean",venueManagedBean);
			// this.activeTab = 0;
			// if(success){
			// FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
			// // ret = "vendor-venue.jsf?faces-redirect";
			// } else
			// {
			// if(venueManagedBean.isShowActLink())
			// {
			// //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serviceProviderManagedBean",
			// null);
			// FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
			// "Please activate your account by clicking on the activation link sent to your registered email.",
			// "Activate Account");
			// FacesContext.getCurrentInstance().addMessage("Activate Account",
			// msg);
			// ret = "";
			// }
			// else
			// {
			// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("venueManagedBean",
			// null);
			// FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
			// "Login Error. Invalid credentials", "Invalid credentials");
			// FacesContext.getCurrentInstance().addMessage("Invalid credentials",
			// msg);
			//
			// FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			// //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
			// ret = "";
			// }
			// }
			//
			// }
			// else {

			//MessageDigest mdp = MessageDigest.getInstance("SHA-256");
			//mdp.update((loginPassword).getBytes());

			//byte byteDatap[] = mdp.digest();

			// convert the byte to hex format method 1
			/*StringBuffer sbp = new StringBuffer();
			for (int i = 0; i < byteDatap.length; i++) {
				sbp.append(Integer.toString((byteDatap[i] & 0xff) + 0x100, 16).substring(1));
			}
*/
			/*
			 * String cryptPswd = Base64.encodeBase64URLSafeString(byteDatap);
			 * logger.debug("key password "+cryptPswd); loginPassword =
			 * cryptPswd;
			 */
                        
                        EncryptDecryptString eds = new EncryptDecryptString();                
                        String cryptPswd = eds.encrypt(loginPassword);

			q = em.createQuery("SELECT spi FROM EventxpertUser spi  WHERE spi.emailaddr = :emailaddr and spi.password = :password");

			// q.setParameter("username", user);
			// //q.setParameter("password", md5(this.loginPassword));
			// q.setParameter("password", password);
			q.setParameter("emailaddr", loginUser);
			q.setParameter("password", cryptPswd);
			List<EventxpertUser> object = q.getResultList();
			if (object.size() > 0) {
				exu = (EventxpertUser) object.get(0);
				if (exu.getISActive().equals("N")) {
					// /
					// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serviceProviderManagedBean",
					// null);
					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Please activate your account by clicking on the activation link sent to your registered email.",
							"Activate Account");
					FacesContext.getCurrentInstance().addMessage("Activate Account", msg);
					// RequestContext.getCurrentInstance().execute("frm:loginDialog.show()");
					RequestContext.getCurrentInstance().addCallbackParam("loggedIn", false);
					showActLink = true;
					return "";
				}

				this.imageUploadable = true;
				login++;

				// EntityManagerFactory emf1 =
				// Persistence.createEntityManagerFactory("ROOT");
				EntityManager em1 = getEntityManager();// emf1.createEntityManager();
				em1.getTransaction().begin();
				Query qu = em1
						.createQuery("UPDATE EventxpertUser c SET c.lastLogin = CURRENT_DATE WHERE c.userId = :userId");
				qu.setParameter("userId", exu.getUserId());
				qu.executeUpdate();
				em1.getTransaction().commit();

				if (exu.getUserType().equals("V")) {
					this.spCode = "VEN";
					VenueManagedBean venueManagedBean = new VenueManagedBean();
					venueManagedBean.setvCode(spCode);
					// boolean success =
					// venueManagedBean.checkLogin(this.loginUser,
					// this.loginPassword);
					boolean success = venueManagedBean.checkLogin(exu);
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
							.put("venueManagedBean", venueManagedBean);
					this.activeTab = 0;
					if (success) {
						FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
						// ret = "vendor-venue.jsf?faces-redirect";
					} else {
						if (venueManagedBean.isShowActLink()) {
							// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serviceProviderManagedBean",
							// null);
							FacesMessage msg = new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"Please activate your account by clicking on the activation link sent to your registered email.",
									"Activate Account");
							FacesContext.getCurrentInstance().addMessage("Activate Account", msg);
							ret = "";
						} else {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.put("venueManagedBean", null);
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Login Error. Invalid credentials", "Invalid credentials");
							FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);

							FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
							// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
							ret = "";
						}
					}

				} else {

					Query q1 = em.createQuery("SELECT vi FROM ServiceProviderInfo vi  WHERE vi.userId = :userId");
					q1.setParameter("userId", exu);

					List<ServiceProviderInfo> spLogin = q1.getResultList();

					if (spLogin.size() > 0) {
						/*
						 * EntityManagerFactory emf1 =
						 * Persistence.createEntityManagerFactory("ROOT");
						 * EntityManager em1 = emf1.createEntityManager();
						 * em1.getTransaction().begin(); Query qu =
						 * em1.createQuery(
						 * "UPDATE EventxpertUser c SET c.lastLogin = CURRENT_DATE WHERE c.userId = :userId"
						 * ); qu.setParameter("userId", exu.getUserId());
						 * qu.executeUpdate(); em1.getTransaction().commit();
						 */

						clearSPValues();

						spInfo = (ServiceProviderInfo) spLogin.get(0);

						this.spId = spInfo.getServiceProviderId();
						this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();
						this.vendorDescription = spInfo.getServiceProviderDesc();
						this.serviceName = getServiceName(this.spCode);
						setSpId(this.spId);
						setServiceName(this.serviceName);
						setSpCode(this.spCode);
						setVendorDescription(this.vendorDescription);

						AddressInfo adInfo = spInfo.getAddressId();
						this.spId = spInfo.getServiceProviderId();
						this.areaId = spInfo.getAreaId().getAreaId();
						this.vName = adInfo.getContactName();
						this.streetAddress = adInfo.getAddressLine1();
						this.zipcode = adInfo.getPostalCode();
						this.cityId = adInfo.getCityId();
						this.phone = adInfo.getContactPhone();
						this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();
						this.vendorDescription = spInfo.getServiceProviderDesc();
						this.userId = spInfo.getUserId().getUserId();
						this.emailaddr = spInfo.getUserId().getEmailaddr();
						this.passwordHint = spInfo.getUserId().getPasswordHint();
						boolean success = true;

						// boolean success = editProfile();
						if (success) {
							getExistingServiceProvider();

							getServiceItems();
							getUnitCostTypes();
                                                        getEventTypes();
							getProviderItemList();
							// FacesContext ctx =
							// FacesContext.getCurrentInstance();
							// //String path =
							// ctx.getExternalContext().getInitParameter("imgpath");

							File dir = new File(path + "/vendors/" + this.spId);

							listFilesForFolder(dir, this.spId);
							if (this.pciList.size() > 0) {
								this.pciList.clear();
							}
							getEventDatesList();
							getCategories();
							if (this.pItemsList.size() == 0) {
								getBlankItems();
							}
							// to disable the registration tab uncoment below
							// line.
							// this.loggedin = true;

							// if(spInfo.getUserId().getUsername()!= null)
							{
								this.registered = true;
								this.registerDisable = false;
								ret = "admin.jsf?faces-redirect=true";
								return "admin.jsf?faces-redirect=true";
							}
						} else {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.put("serviceProviderManagedBean", null);
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Login Error. Invalid credentials", "Invalid credentials");
							FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);

							FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
							// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
							return "";
						}
					} else {
						/*
						 * FacesContext.getCurrentInstance().getExternalContext()
						 * .getSessionMap().put("serviceProviderManagedBean",
						 * null); FacesMessage msg = new
						 * FacesMessage(FacesMessage.SEVERITY_WARN,
						 * "Login Error. Invalid credentials",
						 * "Invalid credentials");
						 * FacesContext.getCurrentInstance
						 * ().addMessage("Invalid credentials", msg);
						 * 
						 * FacesContext.getCurrentInstance().getExternalContext()
						 * .invalidateSession();
						 * //FacesContext.getCurrentInstance
						 * ().getExternalContext
						 * ().getSessionMap().remove("serviceProviderManagedBean"
						 * ); return "";
						 */

						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Login Error. Entered credentials does'nt match with the selected service",
								"Mismatch credentials");
						FacesContext.getCurrentInstance().addMessage("Mismatch credentials", msg);

						FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
						// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
						return "";

					}
				}
			} else {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("serviceProviderManagedBean", null);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials",
						"Invalid credentials");
				FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);

				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
				return "";
			}

			em.close();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return ret;
	}

	public boolean checkLoginAct() {

		String ret = "";
		boolean success = false;
		try {			
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q = null;
                        

                        EncryptDecryptString eds = new EncryptDecryptString();                
                        String cryptPswd = eds.encrypt(password);
                        logger.debug("key password "+cryptPswd);
                        //vu.setPassword(cryptPswd);
            
           
                        
			q = em.createQuery("SELECT spi FROM EventxpertUser spi  WHERE spi.emailaddr = :emailaddr and spi.password = :password");

			q.setParameter("emailaddr", user);			
			q.setParameter("password", cryptPswd);
			List<EventxpertUser> object = q.getResultList();

			if (object.size() > 0) {

				exu = (EventxpertUser) object.get(0);
				this.imageUploadable = true;
				login++;

				// EntityManagerFactory emf1 =
				// Persistence.createEntityManagerFactory("ROOT");
				EntityManager em1 = getEntityManager();// emf1.createEntityManager();
				em1.getTransaction().begin();
				Query qu = em1
						.createQuery("UPDATE EventxpertUser c SET c.lastLogin = CURRENT_DATE WHERE c.userId = :userId");
				qu.setParameter("userId", exu.getUserId());
				qu.executeUpdate();
				em1.getTransaction().commit();

				if (exu.getUserType().equals("V")) {
					this.spCode = "VEN";
					VenueManagedBean venueManagedBean = new VenueManagedBean();
					venueManagedBean.setvCode(spCode);
					success = venueManagedBean.checkLogin(exu);
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
							.put("venueManagedBean", venueManagedBean);					
					this.activeTab = 0;
					// if(success){
					// //
					// FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
					// } else {
					// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("venueManagedBean",
					// null);
					// FacesMessage msg = new
					// FacesMessage(FacesMessage.SEVERITY_WARN,
					// "Login Error. Invalid credentials",
					// "Invalid credentials");
					// FacesContext.getCurrentInstance().addMessage("Invalid credentials",
					// msg);
					//
					// FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
					// //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
					// // return "";
					// }

				} else {

					Query q1 = em.createQuery("SELECT vi FROM ServiceProviderInfo vi  WHERE vi.userId = :userId");
					q1.setParameter("userId", exu);

					List<ServiceProviderInfo> spLogin = q1.getResultList();
					

					if (spLogin.size() > 0) {

						clearSPValues();

						spInfo = (ServiceProviderInfo) spLogin.get(0);
						this.spId = spInfo.getServiceProviderId();
						this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();						
						this.serviceName = getServiceName(this.spCode);
						setSpId(this.spId);
						setServiceName(this.serviceName);
						setSpCode(this.spCode);
						setVendorDescription(this.vendorDescription);

						AddressInfo adInfo = spInfo.getAddressId();
						this.spId = spInfo.getServiceProviderId();
						this.areaId = spInfo.getAreaId().getAreaId();
						this.vName = adInfo.getContactName();
						this.streetAddress = adInfo.getAddressLine1();
						this.zipcode = adInfo.getPostalCode();
						this.cityId = adInfo.getCityId();
						this.phone = adInfo.getContactPhone();
						this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();
						this.vendorDescription = spInfo.getServiceProviderDesc();
						
						this.userId = spInfo.getUserId().getUserId();
						this.emailaddr = spInfo.getUserId().getEmailaddr();
						this.passwordHint = spInfo.getUserId().getPasswordHint();
						
						success = true;

						// boolean success = editProfile();
						if (success) {
							getExistingServiceProvider();

							getServiceItems();
							getUnitCostTypes();
                                                        getEventTypes();
							getProviderItemList();
							
							FacesContext ctx = FacesContext.getCurrentInstance();
//							//String path = ctx.getExternalContext().getInitParameter("imgpath");

							File dir = new File(path + "/vendors/" + this.spId);

							listFilesForFolder(dir, this.spId);
							if (this.pciList.size() > 0) {
								this.pciList.clear();
							}
							getEventDatesList();
							getCategories();
							if (this.pItemsList.size() == 0) {
								getBlankItems();
							}
							

							if (spInfo.getUserId() != null) {
								this.registered = true;
								registerDisable = false;
								ret = "admin?faces-redirect=true";
							}
						} else {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.put("serviceProviderManagedBean", null);
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Login Error. Invalid credentials", "Invalid credentials");
							FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);

							FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
							// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");

						}
					} else {
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Login Error. Entered credentials does'nt match with the selected service",
								"Mismatch credentials");
						FacesContext.getCurrentInstance().addMessage("Mismatch credentials", msg);

						FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
						// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");

					}
				}
			} else {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("serviceProviderManagedBean", null);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials",
						"Invalid credentials");
				FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);

				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");

			}

			em.close();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return success;

	}
	
	public String checkVendorLogin(EventxpertUser exu, ServiceProviderInfo spInfo) {
		String ret = "";
		try {
			this.imageUploadable = true;
			this.setAdmin(true);
			login++;
			this.loginUser = exu.getEmailaddr();
			if (exu.getUserType().equals("V")) {
				this.spCode = "VEN";
				VenueManagedBean venueManagedBean = new VenueManagedBean();
				venueManagedBean.setvCode(spCode);
				boolean success = venueManagedBean.checkLogin(exu);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("venueManagedBean", venueManagedBean);
				this.activeTab = 0;
				if (success)
					FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
			} else {
				clearSPValues();
				this.spId = spInfo.getServiceProviderId();
				this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();
				this.vendorDescription = spInfo.getServiceProviderDesc();
				this.serviceName = getServiceName(this.spCode);
				setSpId(this.spId);
				setServiceName(this.serviceName);
				setSpCode(this.spCode);
				setVendorDescription(this.vendorDescription);
				AddressInfo adInfo = spInfo.getAddressId();
				this.spId = spInfo.getServiceProviderId();
				this.areaId = spInfo.getAreaId().getAreaId();
				this.vName = adInfo.getContactName();
				this.streetAddress = adInfo.getAddressLine1();
				this.zipcode = adInfo.getPostalCode();
				this.cityId = adInfo.getCityId();
				this.phone = adInfo.getContactPhone();
				this.spCode = spInfo.getServiceTypeCode().getServiceTypeCode();
				this.vendorDescription = spInfo.getServiceProviderDesc();
				this.userId = spInfo.getUserId().getUserId();
				this.emailaddr = spInfo.getUserId().getEmailaddr();
				this.passwordHint = spInfo.getUserId().getPasswordHint();
				boolean success = true;				
				if (success) {
					getExistingServiceProvider();
					getServiceItems();
					getUnitCostTypes();
					getEventTypes();
					getProviderItemList();
					File dir = new File(path + "/vendors/" + this.spId);
					listFilesForFolder(dir, this.spId);
					if (this.pciList.size() > 0) {
						this.pciList.clear();
					}
					getEventDatesList();
					getCategories();
					if (this.pItemsList.size() == 0) {
						getBlankItems();
					}
					this.registered = true;
					this.registerDisable = false;
					ret = "admin.jsf?faces-redirect=true";
					FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");					
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		//FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("admin");        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serviceProviderManagedBean", this);        
		return ret;
	}

	private void clearSPValues() {
		this.spId = 0;
		this.spCode = null;
		this.serviceName = null;
	}

	@SuppressWarnings("unchecked")
	public String insertVendorDetails() {
		String ret = "";
		try {
			EntityManager em = getEntityManager();

			em.getTransaction().begin();

			Query q = em
					.createQuery("select spi from ServiceProviderInfo spi where spi.serviceProviderId = :serviceProviderId");
			q.setParameter("serviceProviderId", this.spId);
			List<ServiceProviderInfo> l = q.getResultList();

			if (l.size() > 0) {

				ServiceProviderInfo s = (ServiceProviderInfo) l.get(0);
				
				em.getReference(ServiceProviderInfo.class, s.getServiceProviderId());
				AddressInfo addI = s.getAddressId();
				em.getReference(AddressInfo.class, addI.getAddressId());
				addI.setAddressLine1(streetAddress);
                                
                                AreaTypeMaster atm = s.getAreaId();
				em.getReference(AreaTypeMaster.class, atm.getAreaId());
                                
                                AreaTypeMaster am = null;
                                Query qC1 = em.createNamedQuery("AreaTypeMaster.findByAreaId");
                                qC1.setParameter("areaId", this.areaId);
                                logger.debug("query "+qC1);
                                List aList1 = qC1.getResultList();
                                if(aList1.size()>0)
                                {
                                        am = (AreaTypeMaster) aList1.get(0);
                                }
                                
//                                atm.setAreaId(this.areaId);
                                
				addI.setAddressLine2(am.getAreaName());
				addI.setContactName(vName);
				addI.setContactPhone("" + phone);
				addI.setPostalCode(zipcode);
				addI.setEmail(this.emailaddr);
				addI.setCityId(cityId);
				
                                
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
                                
                                s.setAddressId(addI);                              
                                
                                s.setAreaId(am);
                                
				s.setServiceProviderName(vName);
				// s.getServiceTypeCode().setServiceTypeCode(this.spCode);
				s.setServiceProviderDesc(vendorDescription);
				Timestamp ts = new Timestamp(new Date().getTime());
				s.setServiceProviderEffectiveDate(ts);
				s.setServiceProviderEtDate(ts);
				s.setCreatedDate(ts);
				// s.setUserId(this.userId);
				// s.setPassword(md5(this.password));
				// s.setPassword(this.password);
				s.getUserId().setEmailaddr(emailaddr);
				// s.setPasswordHint(this.passwordHint);
				// s.setPasswordHintAns(this.passwordAns);
                                EventxpertUser eu = s.getUserId();
				em.getReference(EventxpertUser.class, eu.getUserId());
                                eu.setEmailaddr(this.emailaddr);
                                eu.setPasswordAns(passwordAns);
				eu.setPasswordHint(passwordHint);
				if (this.acceptTerms)
					s.setAcceptTermsConditions('Y');
				em.persist(s);
				em.getTransaction().commit();
				this.imageUploadable = true;
				this.createMyAccount = true;

				this.spCode = s.getServiceTypeCode().getServiceTypeCode();
				this.serviceTypeCode = this.spCode;
				ret = "";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Profile Details are updated successfully.", "");
				FacesContext.getCurrentInstance().addMessage(null, msg);

			} else {			
				
				Query q1 = em.createQuery("select vu from EventxpertUser vu where vu.emailaddr= :emailaddr");
				q1.setParameter("emailaddr", this.user);
				//List<EventxpertUser> l1 = q1.getResultList();
				
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
                                        try
                                        {
					em.persist(vu);
					em.getTransaction().commit();
					em.refresh(vu);
					this.userId = vu.getUserId();

					ServiceProviderInfo serviceInfo = createSPInfo();
					AddressInfo addInfo = createAddressInfo();
					serviceInfo.setAddressId(addInfo);
					serviceInfo.setUserId(vu);

                                        try
                                        {
					em.getTransaction().begin();
					em.persist(serviceInfo);
					em.getTransaction().commit();
					em.refresh(serviceInfo);

					this.spId = serviceInfo.getServiceProviderId();
					serviceInfo.setServiceProviderCode("SP" + this.spId);
					em.getTransaction().begin();
					em.persist(serviceInfo);
					em.getTransaction().commit();
					em.refresh(serviceInfo);

					this.serviceName = getServiceName(this.spCode);
					this.createMyAccount = true;
					this.imageUploadable = true;

					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
							.getExternalContext().getRequest();
					logger.debug("request " + request.getHeader("host") + " d " + request.getContextPath()
							+ " d " + request.getRequestURI());

					
					String link = "http://" + request.getHeader("host") + request.getContextPath()
							+ "/activateVendor.jsf?key=" + key + "&ty=V";
					

					sendEmail(vu.getEmailaddr(), serviceInfo.getServiceProviderName(), link);

				
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.remove("serviceProviderManagedBean");
				registered = true;
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
				ret = "thankyou.xhtml?faces-redirect=true";
                        
                                } 
                                catch (Exception e) 
                                {			
                                    e.printStackTrace();
                                    em.getTransaction().rollback();
                                    registered = false;
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
                                    ret = "thankyou.xhtml?faces-redirect=true";
                                }
                                } 
                                catch (Exception e) 
                                {			
                                    e.printStackTrace();
                                    em.getTransaction().rollback();
                                    registered = false;
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
                                    ret = "thankyou.xhtml?faces-redirect=true";
                                }
			}
			getEventDatesList();
			getBlankItems();
			getServiceItems();
			getUnitCostTypes();
                         getEventTypes();
			if (this.pciList.size() > 0) {
				this.pciList.clear();
			}
			getCategories();
			registered();
                        return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			registered = false;
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
			ret = "errorPage.xhtml";
                        return ret;
		}

		

	}

	private EventxpertUser createVendorUser() {
        
            EventxpertUser vu = new EventxpertUser();
            vu.setUsername(this.user);
            try 
            {
                EncryptDecryptString eds = new EncryptDecryptString();
                String cryptPswd = eds.encrypt(this.password);
                vu.setPassword(cryptPswd);
                } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ServiceProviderManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
//            vu.setPassword(this.password);
            vu.setPasswordHint(this.passwordHint);
            vu.setPasswordAns(this.passwordAns);
            vu.setEmailaddr(this.emailaddr);
            vu.setISActive("N");
            vu.setUserCategory("vendor");
            vu.setUserType("O");
            /*try {
            MessageDigest mdp = MessageDigest.getInstance("SHA-256");
            mdp.update((vu.getPassword()).getBytes());

            byte byteDatap[] = mdp.digest();

            //convert the byte to hex format method 1
            StringBuffer sbp = new StringBuffer();
            for (int i = 0; i < byteDatap.length; i++) {
                sbp.append(Integer.toString((byteDatap[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            String cryptPswd = Base64.encodeBase64URLSafeString(byteDatap);
            logger.debug("key password "+cryptPswd);
            vu.setPassword(cryptPswd);
            
            } catch (Exception ex) {
            ex.printStackTrace();
        }*/
            return vu;
        
	}

	public void insertPkgDetailsh() {
		System.out.println(this.packageName + " " + this.packageName + " " + this.description + " " + this.unitCostId );
		EntityManager em = getEntityManager();
		PackageInfo pInfo = new PackageInfo();
		pInfo.setPackageName(this.packageName);
		pInfo.setDescription(this.description);
		pInfo.setMinGuests(0);

		pInfo.setPackageEffectiveDate(new Timestamp(this.pkgFromDate.getTime()));
		pInfo.setPackageEtDate(new Timestamp(this.pkgToDate.getTime()));
		pInfo.setCreatedDate(new Timestamp(this.pkgToDate.getTime()));
		pInfo.setCost(this.cost);

		pInfo.setUnitOfCost(getUnitCostType(this.unitCostId));
		pInfo.setServiceProviderId(getServiceProvider(this.spId));
		em.getTransaction().begin();
	}

	@SuppressWarnings("unchecked")
	public void insertPackageDetails() {
		try {			
			EntityManager em = getEntityManager();
			PackageInfo pInfo = new PackageInfo();
			pInfo.setPackageName(this.packageName);
			pInfo.setDescription(this.description);
			pInfo.setMinGuests(0);
                        
                        
                        pInfo.setEventType(getEventId(eventTypeId));
			
			pInfo.setPackageEffectiveDate(new Timestamp(this.pkgFromDate.getTime()));
			pInfo.setPackageEtDate(new Timestamp(this.pkgToDate.getTime()));
                        pInfo.setCreatedDate(new Timestamp(this.pkgToDate.getTime()));
			pInfo.setCost(this.cost);
			
			pInfo.setUnitOfCost(getUnitCostType(this.unitCostId));
			pInfo.setServiceProviderId(getServiceProvider(this.spId));
			em.getTransaction().begin();
			Query q = em.createQuery("select pif from PackageInfo pif where pif.packageId = :packageId");
			q.setParameter("packageId", this.packageId);
			List<PackageInfo> l = q.getResultList();
			if (l.size() > 0) {
				Query q1 = em.createQuery("delete from PackageInfo pif where pif.packageId = :packageId");
				q1.setParameter("packageId", this.packageId);
				q1.executeUpdate();
			}
			boolean packageNameExists = false;
			Query q1 = em.createNamedQuery("PackageInfo.findByServiceProviderId");
			q1.setParameter("serviceProviderId", getServiceProvider(this.spId));

			List<PackageInfo> l1 = q1.getResultList();
			if (l1.size() > 0) {

				Iterator<PackageInfo> packageIterator = l1.iterator();
				
				while (packageIterator.hasNext()) {
					PackageInfo paInfo = (PackageInfo) packageIterator.next();
					String pName = paInfo.getPackageName();
					
					if (pName.equalsIgnoreCase(this.packageName)) {
						packageNameExists = true;
					}
				}
			}
			if (!packageNameExists) {
				em.persist(pInfo);
				em.getTransaction().commit();
				packageId = pInfo.getPackageId();
				insertPackageCategoryItems(packageId);
				
				packageSize++;
				// populate the package info details
				this.packageId = 0;
				this.pList.clear();

				// RequestContext.getCurrentInstance().reset("frm:packagedet");
			} else {
				
				this.pList.clear();
				this.pNameExists = "Package with same name already exists, give different name";

			}

			this.packageInfoList = getPackageDetaills(this.spId);
			createPackageList(this.packageInfoList);
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			clearPackageValues();
		}
		clearPackageValues();
		// this.activeTab = 1;
		// return "admin.jsf";
	}

        
        public void eventChange(AjaxBehaviorEvent e) {
            SelectOneMenu inputText = (SelectOneMenu) e.getComponent();
		
		 System.out.println("AAAAAAA   "+inputText.getValue());
		eventTypeId = Integer.parseInt(inputText.getValue().toString()); 
        }
        
        public void unitCostChange(AjaxBehaviorEvent e) {
            SelectOneMenu inputText = (SelectOneMenu) e.getComponent();
		
		 System.out.println("AAAAAAA   "+inputText.getValue());
		unitCostId = Integer.parseInt(inputText.getValue().toString()); 
        }
        
	@SuppressWarnings("unchecked")
	public void savePackageInfo(PackageDetails pkgdetails) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		System.out.println("--******Inside savePackageInfo "+eventTypeId+" PPP "+unitCostId+"   pkgdetails.getPackageId() +"
                        +pkgdetails.getPackageId()+"  ::pkgdetails.eventtype:: "+pkgdetails.getEventType().getEventId()); 
                
		try {
			Query q = em.createQuery("select pif from PackageInfo pif where pif.packageId = :packageId");
			// Query q1 =
			// em.createQuery("delete from PackageInfo pif where pif.packageId = :packageId");

			q.setParameter("packageId", pkgdetails.getPackageId());
			List<PackageInfo> l = q.getResultList();
			// Iterator<PackageDetails> packageIterator = this.pList.iterator();
			PackageInfo pInfo = (PackageInfo) l.get(0);
			// while(packageIterator.hasNext())
			{
				PackageDetails psInfo = pkgdetails;// (PackageDetails)packageIterator.next();
				pInfo.setPackageName(psInfo.getPackageName());
				pInfo.setDescription(psInfo.getDescription());
				pInfo.setMinGuests(0);
                                
                                if(eventTypeId!=0)
                                {
                                pInfo.setEventType(getEventId(eventTypeId));
                                }
                                else
                                {
                                    pInfo.setEventType(psInfo.getEventType());
                                }
			
                                if(unitCostId!=0)
                                {
                                pInfo.setUnitOfCost(getUnitCostType(this.unitCostId));
                                }
                                else
                                {
                                    pInfo.setUnitOfCost(psInfo.getUnitOfCost());
                                }
				
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
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                //get current date time with Date()
                                Date date = new Date();
//                                System.out.println(dateFormat.format(date));
                                pInfo.setUpdatedDate(date);
				// pInfo.setEventType(psInfo.getEventType());
				
//				pInfo.setUnitOfCost(psInfo.getUnitOfCost());
//                                pInfo.setEventType(psInfo.getEventType());
				ServiceProviderInfo sp = getServiceProvider(this.spId);
				pInfo.setServiceProviderId(sp);

				
			}
			em.merge(pInfo);
			em.getTransaction().commit();
                        
                        Query query = em.createNamedQuery("ServiceProviderCostInfo.findByPackageId");
                        query.setParameter("packageId", pInfo);
                        List pkgList = query.getResultList();
                        if(pkgList.isEmpty())
                        {
//                            negotiatedCost.put(pi.getPackageId(), "0");
//                            customerDiscount.put(pi.getPackageId(), "0");
//                            finalAmount.put(pi.getPackageId(), "0");
                        }
                        else
                        {
                            ServiceProviderCostInfo spi = (ServiceProviderCostInfo)pkgList.get(0);
                            BigDecimal vendorCostSaved = spi.getVendorCost();                  
                   
                            BigDecimal pcost = pInfo.getCost();
//                            String dis = spi.getCustomerDiscount();
                            BigDecimal discount = spi.getCustomerDiscount();//new BigDecimal(dis);
                            BigDecimal finalamt  = pcost.subtract(discount);                            
                            em = emf.createEntityManager();
                           // ServiceProviderCostInfo serviceProviderCostInfo 
    //                        VenuePackageInfo pInfo = new VenuePackageInfo(pkgid);
                            em.getTransaction().begin();
                            //serviceProviderCostInfo = spi;//(VenueCostInfo) srvpCostV.get(pkgid);
                            spi.setVendorCost(pcost);
                            spi.setNegotiatedCost(spi.getNegotiatedCost());
                            spi.setCustomerDiscount(spi.getCustomerDiscount());
                            spi.setFinalAmount(finalamt);                
                            em.merge(spi);
                            em.getTransaction().commit();
                        }
                        
			//this.pList.clear();
			this.pList = new ArrayList<PackageDetails>();
			this.packageInfoList = getPackageDetaills(this.spId);
			createPackageList(this.packageInfoList);
		} catch (ConstraintViolationException ex) {
			logger.debug("violations " + ex.getConstraintViolations());
		} catch (Exception e) {
			logger.debug("Exception while update package details ");
			e.printStackTrace();
		}
                
                unitCostId = 0;
                eventTypeId = 0;

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
		Iterator<PackageDetails> packageIterator = this.pList.iterator();
		while (packageIterator.hasNext()) {
			index++;			
			PackageDetails psInfo = (PackageDetails) packageIterator.next();
			
			if (packageId == psInfo.getPackageId()) {
				
				this.packageId = psInfo.getPackageId();
				this.packageName = psInfo.getPackageName();
				this.description = psInfo.getDescription();
				//this.eventId = getEventId(psInfo.getEventType());
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
		Query q = em.createQuery("DELETE FROM PackageInfo p WHERE p.packageId = :packageId");
		q.setParameter("packageId", packageId);
		q.executeUpdate();
		em.getTransaction().commit();
		
		getExistingServiceProvider();
	}
	
	public void removePackage(int pkgId) {
		int packageId = new Integer(pkgId).intValue();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM PackageInfo p WHERE p.packageId = :packageId");
		q.setParameter("packageId", packageId);
		q.executeUpdate();
		em.getTransaction().commit();		
		getExistingServiceProvider();
	}

	@SuppressWarnings("unchecked")
	private EventTypeMaster getEventId(int eventType) {
//		int eId = 0;
                EventTypeMaster ETM = null;
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT etm FROM EventTypeMaster etm  WHERE etm.eventId = :eventType");
		q.setParameter("eventType", eventType);
		List<EventTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			ETM = (EventTypeMaster) object.get(0);
			//eId = new Long(ETM.getEventId()).intValue();
		}
		em.close();
		return ETM;
	}

	private void createPackageList(List<PackageInfo> list) {
		try {
			Iterator<PackageInfo> packageIterator = list.iterator();

			while (packageIterator.hasNext()) {
				packageSize++;
				PackageInfo psInfo = (PackageInfo) packageIterator.next();
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
                                //String path = ctx.getExternalContext().getInitParameter("imgpath");

                                File dir = new File(path + "/vendors/" + this.spId+"/packages/" + psInfo.getPackageId());

                                List img = listFilesForFolderPkg(dir, this.spId, psInfo.getPackageId());

				this.pList.add(new PackageDetails(psInfo.getPackageId(), psInfo.getPackageName(),
						psInfo.getEventType(), psInfo.getDescription(), psInfo.getCost(), psInfo.getUnitOfCost(),
						psInfo.getServiceProviderId(), psInfo.getPackageEffectiveDate(), psInfo.getPackageEtDate(),
						from, to,img));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void insertProviderItems() {
		
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select pif from ProviderItemInfo pif where pif.itemId = :itemId");
			Query q1 = em.createQuery("delete from ProviderItemInfo pif where pif.itemId = :itemId");
			Iterator<ProviderItem> pItemListIterator = pItemsList.iterator();
			while (pItemListIterator.hasNext()) {
				em.getTransaction().begin();
				ProviderItem pItem = (ProviderItem) pItemListIterator.next();
				ProviderItemInfo pItemInfo = new ProviderItemInfo();
				pItemInfo.setProviderItemId(pItem.getProviderItemId());
				pItemInfo.setServiceProviderId("" + this.spId);
				pItemInfo.setUnitOfCost(pItem.getUnitOfCost());
				pItemInfo.setItemCost(pItem.getItemCost());
				pItemInfo.setItemId(pItem.getItemId());
				

				q.setParameter("itemId", pItemInfo.getItemId());
				List<ProviderItemInfo> l = q.getResultList();
				if (l.size() > 0) {

					q1.setParameter("itemId", pItemInfo.getItemId());
					q1.executeUpdate();
				}

				em.persist(pItemInfo);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			logger.debug("Exception while inserting provider items " + e.getMessage());
		}

	}

	private void insertPackageCategoryItems(int packageId) {
		EntityManager em = getEntityManager();
		try {
			Iterator<Map<String, List<PackageCatgeoryItem>>> pCItemListIterator = pciList.iterator();
			while (pCItemListIterator.hasNext()) {

				Map<String, List<PackageCatgeoryItem>> pciMap1 = pCItemListIterator.next();
				Iterator<String> keyIterator = pciMap1.keySet().iterator();
				while (keyIterator.hasNext()) {
					List<PackageCatgeoryItem> li = pciMap1.get(keyIterator.next());
					Iterator<PackageCatgeoryItem> liIterator = li.iterator();
					PackageItemInfo piInfo;
					// PackageItemInfoPK piPk;
					while (liIterator.hasNext()) {
						PackageCatgeoryItem pcItem = liIterator.next();
						if (pcItem.isSelected()) {
							em.getTransaction().begin();
							piInfo = new PackageItemInfo(pcItem.getItemId(), packageId);
							/*
							 * piPk = new PackageItemInfoPK();
							 * piPk.setItemId(pcItem.getItemId());
							 * 
							 * piPk.setPackageId(packageId); piInfo.setId(piPk);
							 */
							// piInfo.setCatItemLimit(pcItem.getLimit());
							em.persist(piInfo);
							em.getTransaction().commit();
						}

					}
				}

			}
		} catch (Exception e) {
			logger.debug("Exception while insertPackageCategoryItems " + e.getMessage());
		}

	}

	public String serviceInformation() {
		
		try {
			this.activeTab = 0;
			FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
			// return "admin.jsf";
		} catch (Exception e) {
			logger.debug("In Service Information " + e.getMessage());
		}
		return "admin.jsf";
	}

	public void adminInformation(String mess) {
		this.regMessage = mess;
		try {
			this.activeTab = 0;
			FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
		} catch (Exception e) {
			logger.debug("In Service Information " + e.getMessage());
		}
	}

	public void eventInformation() {		
		try {
			this.activeTab = 2;
			FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
		} catch (Exception e) {
			logger.debug("In event Information " + e.getMessage());
		}
	}

	private ServiceProviderInfo createSPInfo() {
		ServiceProviderInfo serviceInfo = new ServiceProviderInfo();
		if (this.spId != 0) {
			serviceInfo.setServiceProviderId(this.spId);
		}
		
		serviceInfo.setAreaId(getAreaName(this.areaId));
		serviceInfo.setIsActive('Y');
		serviceInfo.setServiceProviderCode("SP");
		this.spName = this.vName;
		serviceInfo.setServiceProviderName(this.spName);
		if (spCode == null || spCode.equals(null) || spCode.equals("")) {
			serviceInfo.setServiceTypeCode(getServiceTypeMaster(this.serviceTypeCode));
		} else {
			serviceInfo.setServiceTypeCode(getServiceTypeMaster(this.spCode));
		}
		this.spCode = this.serviceTypeCode;
		serviceInfo.setServiceProviderDesc(this.vendorDescription);
		Timestamp ts = new Timestamp(new Date().getTime());
		serviceInfo.setServiceProviderEffectiveDate(ts);
		serviceInfo.setServiceProviderEtDate(ts);
		serviceInfo.setCreatedDate(ts);
		
		if (this.acceptTerms)
			serviceInfo.setAcceptTermsConditions('Y');
		return serviceInfo;
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

	@SuppressWarnings("unchecked")
	public void generateMasterDataList() {

		EntityManager em = getEntityManager();
		try {
			char isActive = 'Y';
			Query qryServiceTypeMasterList = em.createNamedQuery("ServiceTypeMaster.findByIsActive");
			qryServiceTypeMasterList.setParameter("isActive", isActive);
			this.serviceTypeMasterList = qryServiceTypeMasterList.getResultList();

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
			logger.debug(e.getMessage());
		} finally {
			em.close();
		}
	}

	public void addObjects() {
		try {
			// populate the area information
			Iterator<AreaTypeMaster> areaIterators = this.areaTypeMasterList.iterator();
			while (areaIterators.hasNext()) {
				AreaTypeMaster aMaster = (AreaTypeMaster) areaIterators.next();
				// this.areaInfo.put(aMaster.getAreaName(),""+aMaster.getAreaId());
				this.areaInfo.add(new SelectItem(aMaster.getAreaId(), aMaster.getAreaName()));
			}

			// populate the city information
			
			Iterator<CityTypeMaster> cityIterators = this.cityTypeMasterList.iterator();
			while (cityIterators.hasNext()) {
				CityTypeMaster cMaster = (CityTypeMaster) cityIterators.next();
				// this.areaInfo.put(aMaster.getAreaName(),""+aMaster.getAreaId());
				this.cityInfo.add(new SelectItem(cMaster.getCityId(), cMaster.getCityName()));
			}

			// populate the service providers information

			Iterator<ServiceTypeMaster> servIterator = this.serviceTypeMasterList.iterator();
			while (servIterator.hasNext()) {
				ServiceTypeMaster sMaster = (ServiceTypeMaster) servIterator.next();
				this.servicesInfoMap.put(sMaster.getServiceName(), sMaster.getServiceTypeCode());
				this.servicesInfo.add(new SelectItem(sMaster.getServiceTypeCode(), sMaster.getServiceName()));
			}

			// populate the service providers information
			Iterator<EventTypeMaster> eventIterator = this.eventMasterList.iterator();
			while (eventIterator.hasNext()) {
				EventTypeMaster eMaster = (EventTypeMaster) eventIterator.next();
				this.eventInfo.add(new SelectItem(eMaster.getEventId(), eMaster.getEventType()));
			}

			// populate the service providers information
			// getCategories();

			// populate the service providers information
			/*
			 * Iterator<VenueTypeMaster> venueIterator =
			 * this.venueMasterList.iterator(); while (venueIterator.hasNext())
			 * { VenueTypeMaster vMaster =
			 * (VenueTypeMaster)venueIterator.next();
			 * //this.venueInfo.put(vMaster
			 * .getVenueType(),vMaster.getVenueTypeId()); this.venueInfo.add(new
			 * SelectItem(vMaster.getVenueTypeId(),vMaster.getVenueType())); }
			 */
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private String getServiceName(String spCode) {

		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT stm FROM ServiceTypeMaster stm  WHERE stm.serviceTypeCode = :serviceTypeCode");
		q.setParameter("serviceTypeCode", spCode);
		List<ServiceTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			ServiceTypeMaster STM = (ServiceTypeMaster) object.get(0);
			return STM.getServiceName();
		}
		em.close();
		return null;
	}

	@SuppressWarnings("unchecked")
	private ServiceTypeMaster getServiceTypeMaster(String spCode) {

		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT stm FROM ServiceTypeMaster stm  WHERE stm.serviceTypeCode = :serviceTypeCode");
		q.setParameter("serviceTypeCode", spCode);
		List<ServiceTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			ServiceTypeMaster STM = (ServiceTypeMaster) object.get(0);
			return STM;
		}
		em.close();
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<PackageInfo> getPackageDetaills(int spId) {
		
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT PI FROM PackageInfo PI  WHERE PI.serviceProviderId = :serviceProviderId");
		q.setParameter("serviceProviderId", getServiceProvider(spId));
		this.pInfoList = q.getResultList();
		em.close();
		
		return pInfoList;
	}

	@SuppressWarnings("unchecked")
	public List<String> getItems() {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT ITEM.ITEM_DESCRIPTION FROM ITEM_MASTER ITEM");
		this.items = q.getResultList();
		em.close();
		return items;
	}

	@SuppressWarnings("unchecked")
	public void getServiceItems() {
		EntityManager em = getEntityManager();
		try {
			this.itemMasterList.clear();
			em.getTransaction().begin();
			Query q = em
					.createQuery("SELECT ICM FROM ItemCategoryMaster ICM WHERE ICM.serviceTypeCode = :serviceTypeCode");
			q.setParameter("serviceTypeCode", this.spCode);
			List<ItemCategoryMaster> icm = q.getResultList();
			Iterator<ItemCategoryMaster> icmIterator = icm.iterator();
			this.itemInfo.clear();
			while (icmIterator.hasNext()) {

				ItemCategoryMaster icmMaster = (ItemCategoryMaster) icmIterator.next();
				Query q1 = em.createQuery("SELECT IM FROM ItemMaster IM WHERE IM.itemCategoryId = :itemCategoryId");
				q1.setParameter("itemCategoryId", icmMaster);
				List<ItemMaster> im = q1.getResultList();
				Iterator<ItemMaster> iIterator = im.iterator();

				while (iIterator.hasNext()) {
					ItemMaster iMaster = (ItemMaster) iIterator.next();
					// this.itemInfo.put(iMaster.getItemName(),iMaster.getItemId());
					this.itemInfo.add(new SelectItem(iMaster.getItemId(), iMaster.getItemName()));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception while getting service items " + e.getMessage());
		} finally {
			em.close();
		}

	}

	@SuppressWarnings("unchecked")
	private String getEventName(Integer eventId) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT etm FROM EventTypeMaster etm  WHERE etm.eventId = :eventId");
		q.setParameter("eventId", eventId);
		List<EventTypeMaster> object = q.getResultList();
		if (object.size() > 0) {
			EventTypeMaster ETM = (EventTypeMaster) object.get(0);
			em.close();
			return ETM.getEventType();
		}
		return "";

	}

	public void categoryChangeEvent(ValueChangeEvent e) {

		if (e.getNewValue() != null) {
			Integer itemId = new Integer(e.getNewValue().toString());
			
			String category = getCategory(itemId);
			String desc = getItemDesc(itemId);
			populateProviderItems(itemId.toString(), desc, category);
		}
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

	@SuppressWarnings("unchecked")
	private String getCategory(Integer itemid) {
		String cat = null;
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q = em.createQuery("SELECT IM FROM ItemMaster IM where IM.itemId = :itemId");
			q.setParameter("itemId", itemid);
			List<ItemMaster> object = q.getResultList();
			if (object.size() > 0) {
				ItemMaster imaster = (ItemMaster) object.get(0);
				int catId = imaster.getItemCategoryId().getItemCategoryId();
				Query q1 = em
						.createQuery("SELECT ICM FROM ItemCategoryMaster ICM WHERE ICM.itemCategoryId = :itemCategoryId");
				q1.setParameter("itemCategoryId", catId);
				List<ItemCategoryMaster> object1 = q1.getResultList();
				if (object1.size() > 0) {
					ItemCategoryMaster ICM = (ItemCategoryMaster) object1.get(0);
					cat = ICM.getItemCategoryName();
				}

			}
			em.close();
		} catch (Exception e) {
			logger.debug("Exception while getting category name " + e.getMessage());
		}
		return cat;
	}

	

	@SuppressWarnings("unchecked")
	private void getCategories() {
		// String cat = null;
		// Map<String, List<PackageCatgeoryItem>> pciMap = new HashMap<String,
		// List<PackageCatgeoryItem>>();

		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q = em
					.createQuery("SELECT ICM FROM ItemCategoryMaster ICM WHERE ICM.serviceTypeCode = :serviceTypeCode");
			q.setParameter("serviceTypeCode", this.spCode);
			List<ItemCategoryMaster> icm = q.getResultList();
			Iterator<ItemCategoryMaster> icmIterator = icm.iterator();
			if (this.pciList.size() > 0) {
				this.pciList.clear();
			}
			while (icmIterator.hasNext()) {

				ItemCategoryMaster icmMaster = (ItemCategoryMaster) icmIterator.next();
				this.columns.add(new ColumnModel(icmMaster.getItemCategoryName().toUpperCase(), icmMaster
						.getItemCategoryName()));

				Query q1 = em.createQuery("SELECT IM FROM ItemMaster IM WHERE IM.itemCategoryId = :itemCategoryId");
				q1.setParameter("itemCategoryId", icmMaster);
				List<ItemMaster> im = q1.getResultList();
				Iterator<ItemMaster> iIterator = im.iterator();
				PackageCatgeoryItem pci;
				List<PackageCatgeoryItem> itemNames = new ArrayList<PackageCatgeoryItem>();

				while (iIterator.hasNext()) {
					ItemMaster iMaster = (ItemMaster) iIterator.next();
					pci = new PackageCatgeoryItem();
					pci.setCategoryId(icmMaster.getItemCategoryId());
					pci.setCategoryName(icmMaster.getItemCategoryName());
					pci.setItemId(iMaster.getItemId());
					pci.setItemName(iMaster.getItemName());

					itemNames.add(pci);
				}

				this.pciMap.put(icmMaster.getItemCategoryName(), itemNames);
				
				this.pciList.add(pciMap);

			}
			

			em.close();
		} catch (Exception e) {
			logger.debug("Exception while getting categories " + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private String getItemDesc(Integer itemid) {
		String desc = null;
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q1 = em.createQuery("SELECT IM FROM ItemMaster IM WHERE IM.itemId = :itemId");
			q1.setParameter("itemId", itemid);
			List<ItemMaster> object = q1.getResultList();
			if (object.size() > 0) {
				ItemMaster result = (ItemMaster) object.get(0);
				desc = result.getItemDescription();
				em.close();
			}
		} catch (Exception e) {
			logger.debug("Exception while getting Item Desc  " + e.getMessage());
		}
		return desc;
	}

	private void populateProviderItems(String itemId, String desc, String category) {
		try {
			
			if (this.pItemsList.size() == 1) {
				ProviderItem pi = this.pItemsList.get(0);
				if (pi.getItemId().equals("")) {
					this.pItemsList.clear();
					ProviderItem p = new ProviderItem();
					p.setItemId(itemId);
					p.setCategory(category);
					p.setItemDesc(desc);
					this.pItemsList.add(p);
				}
			} else {
				Iterator<ProviderItem> pItemListIterator = pItemsList.iterator();
				while (pItemListIterator.hasNext()) {
					ProviderItem pItem = (ProviderItem) pItemListIterator.next();
					if ((pItem.getItemId().equals(itemId)) || (pItem.getItemId().equals(""))) {
						pItem.setItemDesc(desc);
						pItem.setCategory(category);
						pItem.setItemId(pItem.getItemId());
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Exception populateProviderItems " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private AreaTypeMaster getAreaName(Integer areaid) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q1 = em.createQuery("SELECT IM FROM AreaTypeMaster IM WHERE IM.areaId = :areaId");
		q1.setParameter("areaId", areaid);
		List<AreaTypeMaster> object = q1.getResultList();
		if (object.size() > 0) {
                    System.out.println("Inside AreaName");
			AreaTypeMaster result = (AreaTypeMaster) object.get(0);
			
			em.close();
			return result;
		}
		return null;

	}

	private void getExistingServiceProvider() {
		this.pList.clear();
		this.packageInfoList = getPackageDetaills(this.spId);
		createPackageList(this.packageInfoList);
	}

	private void getExistingServiceProviderItems() {
		this.pItemsList.clear();
		getRemainingItemsList();
	}

	@SuppressWarnings("unchecked")
	private void getRemainingItemsList() {
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q1 = em
					.createQuery("SELECT PII FROM ProviderItemInfo PII WHERE PII.serviceProviderId = :serviceProviderId");
			q1.setParameter("serviceProviderId", "" + this.spId);
			List<ProviderItemInfo> object = q1.getResultList();
			Iterator<ProviderItemInfo> pItemListIterator = object.iterator();
			while (pItemListIterator.hasNext()) {
				ProviderItemInfo pInfo = (ProviderItemInfo) pItemListIterator.next();
				ProviderItem p = new ProviderItem();
				p.setProviderItemId(pInfo.getProviderItemId());
				p.setItemId(pInfo.getItemId());
				p.setServiceProviderId(pInfo.getServiceProviderId());
				p.setItemCost(pInfo.getItemCost());
				p.setUnitOfCost(pInfo.getUnitOfCost());
				p.setCategory(getCategory(new Integer(p.getItemId())));
				p.setItemDesc(getItemDesc(new Integer(p.getItemId())));
				this.pItemsList.add(p);
			}
			em.close();
		} catch (Exception e) {
			logger.debug("Exception while getRemainingItemsList  " + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private void getProviderItemList() {
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q1 = em
					.createQuery("SELECT PII FROM ProviderItemInfo PII WHERE PII.serviceProviderId = :serviceProviderId");
			
			q1.setParameter("serviceProviderId", "" + this.spId);
			if (q1.getMaxResults() <= 1) {
				
				List<ProviderItemInfo> object = q1.getResultList();
				if (object.size() > 0) {
					ProviderItemInfo pInfo = (ProviderItemInfo) object.get(0);
					ProviderItem p = new ProviderItem();
					p.setProviderItemId(pInfo.getProviderItemId());
					p.setItemId(pInfo.getItemId());
					p.setServiceProviderId(pInfo.getServiceProviderId());
					p.setItemCost(pInfo.getItemCost());
					p.setUnitOfCost(pInfo.getUnitOfCost());
					p.setCategory(getCategory(new Integer(p.getItemId())));
					p.setItemDesc(getItemDesc(new Integer(p.getItemId())));
					pItemsList.add(p);
				}

			} else {
				this.pItemList = q1.getResultList();
				
				Iterator<ProviderItemInfo> pItemListIterator = pItemList.iterator();
				this.pItemsList.clear();
				while (pItemListIterator.hasNext()) {
					ProviderItemInfo pItemInfo = (ProviderItemInfo) pItemListIterator.next();
					ProviderItem p = new ProviderItem();
					p.setProviderItemId(pItemInfo.getProviderItemId());
					p.setItemId(pItemInfo.getItemId());
					p.setServiceProviderId(pItemInfo.getServiceProviderId());
					p.setItemCost(pItemInfo.getItemCost());
					p.setUnitOfCost(pItemInfo.getUnitOfCost());
					p.setCategory(getCategory(new Integer(p.getItemId())));
					p.setItemDesc(getItemDesc(new Integer(p.getItemId())));
					pItemsList.add(p);
				}
			}

			
			em.close();
		} catch (Exception e) {
			logger.debug("Exception while getProviderItemList  " + e.getMessage());
		}

	}

	private void getBlankItems() {		
		ProviderItem p = new ProviderItem();
		p.setItemId("");
		p.setServiceProviderId("");
		p.setUnitOfCost("");
		p.setItemCost(0);
		p.setCategory("");
		p.setItemDesc("");
		this.pItemsList.add(p);
	}

	public void add() {

		getBlankItems();
	}

	public void removeProviderItem() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("providerItemId");
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("DELETE FROM ProviderItemInfo p WHERE p.providerItemId = :providerItemId");
		q.setParameter("providerItemId", new Integer(action).intValue());
		q.executeUpdate();
		em.getTransaction().commit();
		getExistingServiceProviderItems();
		itemSize--;
	}

	private void addPasswordHints() {		
		this.passwordHints.add(new SelectItem("What is your birth place", "What is your birth place"));
		this.passwordHints.add(new SelectItem("What is your first school name", "What is your first school name"));
		this.passwordHints.add(new SelectItem("What is your nick name", "What is your nick name"));
	}

	private String md5(String s) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			BigInteger i = new BigInteger(1, m.digest());
			return String.format("%1$032x", i);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void register() {		
		try {
			this.activeTab = 0;			
			this.activeTab = 0;
			FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
			
		} catch (Exception e) {
			logger.debug("In Register " + e.getMessage());
		}
	}

	public String login() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
		return "login.jsf";
	}

	public String logout() {

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");		
		return "vendorLogin.xhtml?faces-redirect=true";
	}

	public String chngPswd() {
		return "changePswdVendor.xhtml?faces-redirect=true";
	}

	private void registered() {

		try {
			this.registered = true;
			registerDisable = false;
			this.activeTab = 0;
			// FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
		} catch (Exception e) {
			logger.debug("In Service Information " + e.getMessage());
		}
	}

	

	@SuppressWarnings("unchecked")
	private List<SelectItem> getUnitCostTypes() {
		this.unitCostsInfo.clear();
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT UC FROM UnitCosts UC WHERE UC.serviceTypeCode = :serviceTypeCode");
		try {
			if (spCode != null || !spCode.equals("")) {
				q.setParameter("serviceTypeCode", "" + this.spCode);
			} else {
				q.setParameter("serviceTypeCode", "" + this.serviceTypeCode);
			}
		} catch (Exception e) {
			q.setParameter("serviceTypeCode", "" + this.serviceTypeCode);
		}
		List<UnitCosts> object = q.getResultList();
		Iterator<UnitCosts> ucIterator = object.iterator();
		while (ucIterator.hasNext()) {
			UnitCosts uc = (UnitCosts) ucIterator.next();			
			this.unitCostsInfo.add(new SelectItem(uc.getId(), uc.getUnitCostType()));
		}
		return this.unitCostsInfo;
	}
        
        
        @SuppressWarnings("unchecked")
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
        
	private ServiceProviderInfo getServiceProvider(int spId) {
		EntityManager em = getEntityManager();

		em.getTransaction().begin();

		Query q = em
				.createQuery("select spi from ServiceProviderInfo spi where spi.serviceProviderId = :serviceProviderId");
		q.setParameter("serviceProviderId", this.spId);
		List<ServiceProviderInfo> l = q.getResultList();
		if (l.size() > 0) {
			return l.get(0);
		}
		return null;
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
		//return unitCostType;
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
	public void getEventDatesList() {
		
		EntityManager em = getEntityManager();
		try {
			Query eventDatesListQry = em.createNamedQuery("EventDates.findByServiceProviderId");
			eventDatesListQry.setParameter("serviceProviderId", this.spId);
			this.eventDates = eventDatesListQry.getResultList();
			/*
			 * SimpleDateFormat readFormat = new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 * 
			 * SimpleDateFormat writeFormat = new
			 * SimpleDateFormat("yyyy-MM-dd");
			 * 
			 * java.util.Date d; oldEventDates = new String[eventDates.size()];
			 * 
			 * for (int j = 0; j < eventDates.size(); j++) { eventDatessize++;
			 * EventDates ed = eventDates.get(j);
			 * 
			 * if (ed != null && ed.getEventDate() != null) { d =
			 * readFormat.parse(ed.getEventDate().toString());
			 * 
			 * String str = writeFormat.format(d);
			 * 
			 * java.sql.Date nd = new java.sql.Date(d.getTime());
			 * 
			 * ed.setEventDate(new Timestamp(nd.getTime()));
			 * this.eventDates.set(j, ed); this.oldEventDates[j] = "'" + str +
			 * "'"; }
			 * 
			 * }
			 */
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

	private List<EventScheduleInfo> getESInfo(int spId) {

		return null;
	}

	public void insertEventDates() {
		try {

			EntityManager em = getEntityManager();
			EventDates eDate = new EventDates();
			
			eDate.setEventDate(this.eventDate);
			eDate.setServiceProviderId(this.spId);
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

	public void test() {
		logger.debug("Test called " + this.testName);
	}

	private String testName;

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	transient EntityManagerFactory emf;

	private EntityManager getEntityManager() {

		emf = Persistence.createEntityManagerFactory("ROOT");

		EntityManager em = emf.createEntityManager();

		return em;
	}

	public void handleFileUpload(FileUploadEvent event) throws URISyntaxException {

		
		FacesContext ctx = FacesContext.getCurrentInstance();
		//String path = ctx.getExternalContext().getInitParameter("imgpath");

		File dir = new File(path + "/vendors/" + this.spId);
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
		//String url = ctx.getExternalContext().getInitParameter("imgurl");
		img.setPath(url + "/vendors/" + this.spId + "/" + event.getFile().getFileName());
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

			listFilesForFolder(dir, this.spId);

			logger.info("File Description :file name: " + event.getFile().getFileName() + "file size: "
					+ event.getFile().getSize() / 1024 + " Kb content type: " + event.getFile().getContentType()
					+ "The file was uploaded.");

		} catch (Exception e) {
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
			//String url = ctx.getExternalContext().getInitParameter("imgurl");
			
			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listFilesForFolder(fileEntry, spId);
					} else {
						
						img = new Image();
						img.setImageName(fileEntry.getName());
						img.setPath("http://" + url + "/vendors/" + spId + "/" + fileEntry.getName());
						
						imagesPreview.add(img);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handlePkgFileUpload(FileUploadEvent event) throws URISyntaxException {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getInitParameter("imgpath");
		int pkgId = Integer.parseInt(event.getComponent().getAttributes().get("pkgid").toString());
		
		File dirPk = new File(path + "/vendors/" + this.spId + "/packages");
		File dir = new File(path + "/vendors/" + this.spId + "/packages/" + pkgId);
		
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
		img.setPath(url + "/vendors/" + this.spId + "/packages/" + pkgId + "/" + event.getFile().getFileName());
		System.out.println("File Upload Listener for Packages ************** " + img.getPath());
		img.setImageName(event.getFile().getFileName());
		imagesPreview.add(img);
		//imagesPreviewHall.add(img);
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
			for (PackageDetails pkgDtl:pList){
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

	private List<Image> listPkgsFilesForFolder(final File folder, Integer pkId) {
		List<Image> imagesPreviewHalla = new ArrayList<Image>();
		try {
			// imagesPreviewHall.clear();
			Image img;
			FacesContext ctx = FacesContext.getCurrentInstance();
			// String url = ctx.getExternalContext().getInitParameter("imgurl");

			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listFilesForFolderPkg(fileEntry, this.spId, pkId);
					} else {

						img = new Image();
						img.setImageName(fileEntry.getName());
						// img.setPath("http://"+url+"/vendors/"+spId+"/"+fileEntry.getName());
						img.setPath("http://" + url + "/vendors/" + this.spId + "/packages/" + pkId + "/" + fileEntry.getName());
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
        
        
        public void handleFileUploadPkg(FileUploadEvent event) throws URISyntaxException {

		
		FacesContext ctx = FacesContext.getCurrentInstance();
		// String path = ctx.getExternalContext().getInitParameter("imgpath");
		Integer pkgid = selectedPkgDtls.getPackageId();
		Integer pkgid1 = (Integer) event.getComponent().getAttributes().get("pkgid");
		// Integer rowind = (Integer)
		// event.getComponent().getAttributes().get("rowind");
		File dirPk = new File(path + "/vendors/" + this.spId + "/packages");
		File dir = new File(path + "/vendors/" + this.spId + "/packages/" + pkgid);
		/*
		 * URI uri = new URI("http://img.eventxpert.in/vendors/"+this.spId);
		 * File dir = new File(uri);
		 */
		if (!dirPk.exists()) {
			if (dirPk.mkdirs()) {
				logger.info("Packages Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
                
                if (!dir.exists()) {
			if (dir.mkdirs()) {
				logger.info("Package Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}

		}
		File result = new File(dir + "//" + event.getFile().getFileName());
		Image img = new Image();
		//String url = ctx.getExternalContext().getInitParameter("imgurl");
		img.setPath(url + "/vendors/" + this.spId +"/packages/"+pkgid+ "/" + event.getFile().getFileName());
		img.setImageName(event.getFile().getFileName());
		//imagesPreview.add(img);		
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

			// this.pList.get(rowind).setImagesPreview(listFilesForFolderPkg(dir,
			// this.spId, pkgid));
			//selectedPkgDtls.setImagesPreview(listFilesForFolderPkg(dir, this.spId, pkgid));
			logger.info("File Description :file name: " + event.getFile().getFileName() + "file size: "
					+ event.getFile().getSize() / 1024 + " Kb content type: " + event.getFile().getContentType()
					+ "The file was uploaded.");

		} catch (Exception e) {
			e.printStackTrace();

			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The files were not uploaded!", "");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}

	}

	private List<Image> listFilesForFolderPkg(final File folder, Integer spId, Integer pid) {
            List<Image> imagesPreviewPkg = new ArrayList<Image>();
		try {
			
			Image img;
			FacesContext ctx = FacesContext.getCurrentInstance();
			//String url = ctx.getExternalContext().getInitParameter("imgurl");
			
			if (folder != null & folder.exists()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listFilesForFolderPkg(fileEntry, spId, pid);
					} else {
						
						img = new Image();
						img.setImageName(fileEntry.getName());
						img.setPath("http://" + url + "/vendors/" + spId + "/packages/"+ pid + "/" + fileEntry.getName());
						
						imagesPreviewPkg.add(img);
					}
				}
			}
                        else
                        {
//                            imagesPreviewPkg.add("images/noImage.gif");
                            img = new Image();
                            img.setImageName("noImage");
                            img.setPath("images/noImage.gif");
                            imagesPreviewPkg.add(img);
                        }
		} catch (Exception e) {
			e.printStackTrace();
		}
                return imagesPreviewPkg;
	}

	public void selectedService() {

		
		try {
			this.activeTab = 0;
			FacesContext.getCurrentInstance().getExternalContext().redirect("vendorRegister.jsf");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

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
                
                 FacesContext ctx = FacesContext.getCurrentInstance();
                    //String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

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
					+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
					+ "\n\n EventXpert Team");

			Transport.send(message);		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	public void setLoginSpCode() {
		// SelectOneRadio val = (SelectOneRadio) ae.getComponent();
		// spCode = (String) val.getSubmittedValue();
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("myRadVal", spCode);
		if (this.spCode.equals("VEN")) {
			try {
				VenueManagedBean venueManagedBean = new VenueManagedBean();
				venueManagedBean.setSpCode(spCode);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("venueManagedBean", venueManagedBean);
				
				this.activeTab = 0;
				FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(ServiceProviderManagedBean.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}		
	}

	public void setLoginUsere() {
		logger.info("loginUser aja " + loginUser);
	}

	public void setLoginPswd() {
		logger.info("loginPassword aja " + loginPassword);
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
			//String path = context.getInitParameter("imgpath");			
			File deleteimage = new File(path + "/vendors/" + this.spId + "/" + fileName);			
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
			//String path = context.getInitParameter("imgpath");			
			File deleteimage = new File(path + "/vendors/" + this.spId + "/packages/" + pid + "/" + fileName);
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
                    
                    EncryptDecryptString eds = new EncryptDecryptString();                
                  String cryptPswd = eds.encrypt(oldPassword);
                    logger.debug("key password "+cryptPswd);
                    
			EntityManager em01 = getEntityManager();
			em01.getTransaction().begin();
			Query query = em01
					.createQuery("SELECT c FROM EventxpertUser c WHERE c.username = :username and c.password = :password");
			query.setParameter("username", exu.getUsername());
			query.setParameter("password", cryptPswd);
			List cList = query.getResultList();

			if (cList.size() > 0) {
				if (oldPassword.equals(newPassword)) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Both the Passwords entered are same. Try Again", "Password Matched");
					FacesContext.getCurrentInstance().addMessage("Password Matched", msg);
					return "";
				} 
                                else 
                                {
                                   
                                    String cryptPswdnew = eds.encrypt(newPassword);
                                    logger.debug("key password "+cryptPswdnew);

					exu.setPassword(cryptPswdnew);
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
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"There is some unexpected error. Sorry for the inconvenience, Please try again later", "Exception");
			FacesContext.getCurrentInstance().addMessage("Exception", msg);

		} finally {
			
		}
		return "";
	}

	public String forgotPswd() {
		try {
			EntityManager em01 = getEntityManager();
			;
			em01.getTransaction().begin();
			Query query = em01.createNamedQuery("EventxpertUser.findByEmailaddr");
			query.setParameter("emailaddr", emailaddr);
			List cList = query.getResultList();
			logger.debug("aList a " + cList.size());
			if (cList.size() > 0) {
				EventxpertUser ci = (EventxpertUser) cList.get(0);
                                EncryptDecryptString eds = new EncryptDecryptString();                
                                String cryptPswd = eds.decrypt(ci.getPassword());
                    
				String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
				String host = "mail.eventxpert.in";
				final String username = "jagadish@eventxpert.in";
				final String password = "jagadish";
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.auth", "true");
                                
                                 FacesContext ctx = FacesContext.getCurrentInstance();
                                 //String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			
				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				Message messageU = new MimeMessage(session);
				messageU.setFrom(new InternetAddress(from));
				messageU.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ci.getEmailaddr()));
				messageU.setSubject("Your EventXpert Account Password Recovery");
				messageU.setText("Dear "
						+ ci.getUsername()
						+ ","
						+ "\n\n You initiated a request to help with your Account Password."
						+ "\n\n We will send your temporary password via another email."
						+ "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
						+ "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
						+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
						+ "\n\n EventXpert Team");

				Transport.send(messageU);

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ci.getEmailaddr()));
				message.setSubject("Your EventXpert Account Password Recovery");
				message.setText("Dear "
						+ ci.getUsername()
						+ ","
						+ "\n\n You initiated a request to help with your Account Password."
						+ "\n\n The password for the your EventXpert account is \u0002"
						+ cryptPswd
						+ "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
						+ "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
						+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
						+ "\n\n EventXpert Team");

				Transport.send(message);

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Your password is sent to your registered email-id", "Password Sent");
				FacesContext.getCurrentInstance().addMessage("Password Sent", msg);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Entered Email is not registered with us", "Email not registered");
				FacesContext.getCurrentInstance().addMessage("Email not registered", msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is not registered",
					"Email not registered");
			FacesContext.getCurrentInstance().addMessage("Email not registered", msg);

		} finally {
			// em.close();
			
		}
		return "";
	}

	public String resendActLink() {
		try {
			
			EntityManager em01 = getEntityManager();
			em01.getTransaction().begin();
			Query query = em01.createNamedQuery("EventxpertUser.findByEmailaddr");
			query.setParameter("emailaddr", emailaddr);
			List cList = query.getResultList();
			logger.debug("aList a " + cList.size());
			if (cList.size() > 0) {
                             FacesContext ctx = FacesContext.getCurrentInstance();
                    //String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			
				EventxpertUser ci = (EventxpertUser) cList.get(0);
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
						.getExternalContext().getRequest();
				String link = "http://" + request.getHeader("host") + request.getContextPath()
						+ "/activateVendor.jsf?key=" + ci.getActivationKey() + "&ty=V";
				logger.debug("link " + link);

				String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
				String host = "mail.eventxpert.in";
				final String username = "jagadish@eventxpert.in";
				final String password = "jagadish";
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.auth", "true");

				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ci.getEmailaddr()));
				message.setSubject("Activate your EventXpert Account");
				message.setText("Welcome "
						+ ci.getUsername()
						+ ".,"
						+ "\n\n Thanks for registering with EventXpert."
						+ "\n\n To prevent unauthorized use of your email address, we need to make sure that you are the rightful owner of this email ID."
						+ "\n\n Please click on the link below to confirm your email address:"
						+ "\n\n "
						+ link
						+ "\n\n If you received this verification email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail. If you are unable to click on the link above, please copy and paste above URL in your browser's address bar."
						+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
						+ "\n\n EventXpert Team");

				Transport.send(message);

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"An Activation link is sent to your registered email-id", "Link Sent");
				FacesContext.getCurrentInstance().addMessage("Password Sent", msg);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Entered Email is not registered with us", "Email not registered");
				FacesContext.getCurrentInstance().addMessage("Email not registered", msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is not registered",
					"Email not registered");
			FacesContext.getCurrentInstance().addMessage("Email not registered", msg);

		} finally {
			// em.close();
			
		}
		return "";
	}
	public void handleDateSelect(SelectEvent event) {
		Date date = (Date) event.getObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dates.add(dateFormat.format(date));
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
				dateObject.setServiceProviderId(this.spId);
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

	/**
	 * @return the selectedPkgDtls
	 */
	public PackageInfo getSelectedPkgDtls() {
		return selectedPkgDtls;
	}

	/**
	 * @param selectedPkgDtls
	 *            the selectedPkgDtls to set
	 */
	public void setSelectedPkgDtls(PackageInfo selectedPkgDtls) {
		this.selectedPkgDtls = selectedPkgDtls;
	}

	/**
	 * @return the dateSelected
	 */
	public Date getDateSelected() {
		return dateSelected;
	}

	/**
	 * @param dateSelected
	 *            the dateSelected to set
	 */
	public void setDateSelected(Date dateSelected) {
		this.dateSelected = dateSelected;
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

	public void printSelected() {
		//System.out.println(selectedPkgDtls.getPackageName());
		//Integer pkgid1 = (Integer) event.getComponent().getAttributes().get("pkgid");
		System.out.println(pkgid1);
	}

	public String populateLessorData() {
		System.out.println(pkgid1);
		//System.out.println(selectedPkgDtls.getPackageName());
		return null;
	}

	/**
	 * @return the toAddPackageDtls
	 */
	public PackageDetails getToAddPackageDtls() {
		return toAddPackageDtls;
	}

	/**
	 * @param toAddPackageDtls the toAddPackageDtls to set
	 */
	public void setToAddPackageDtls(PackageDetails toAddPackageDtls) {
		this.toAddPackageDtls = toAddPackageDtls;
	}

	public void handleClose(CloseEvent event) {
		selectedPkgDtls = new PackageInfo();
	}

	/**
	 * @return the pkgid1
	 */
	public int getPkgid1() {
		return pkgid1;
	}

	/**
	 * @param pkgid1 the pkgid1 to set
	 */
	public void setPkgid1(int pkgid1) {
		this.pkgid1 = pkgid1;
	}

	/**
	 * @return the packageInfoList
	 */
	public List<PackageInfo> getPackageInfoList() {
		return packageInfoList;
	}

	/**
	 * @param packageInfoList the packageInfoList to set
	 */
	public void setPackageInfoList(List<PackageInfo> packageInfoList) {
		this.packageInfoList = packageInfoList;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
}
