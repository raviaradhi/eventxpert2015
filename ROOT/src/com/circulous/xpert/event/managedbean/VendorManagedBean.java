/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;

import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.CityTypeMaster;
import com.circulous.xpert.jpa.entities.EventxpertUser;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo;
import com.circulous.xpert.jpa.entities.ServiceTypeMaster;
import com.circulous.xpert.jpa.entities.VenueInfo;
import com.circulous.xpert.jpa.entities.VenueTypeMaster;

/**
 * 
 * @author SANTOSH
 */
@ManagedBean
@SessionScoped
public class VendorManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(VendorManagedBean.class);

	private List<String> vendors;

	private String selectedVendor;

	private Map<String, String> vendorMap;

	private List<String> vendorList;

	private List<ServiceProviderInfo> srvProInfo;

	private ServiceProviderInfo selectedSrvProvider;

	private VenueInfo selectedVnuProvider;

	private List<ServiceProviderInfo> srvShown;

	private List<VenueInfo> vnuShown;

	private List<VenueInfo> vnuProvidersList;

	private List<VenueTypeMaster> venueMasterList = new ArrayList<VenueTypeMaster>();

	private List<SelectItem> venueTypeInfo = new ArrayList<SelectItem>();

	private List<SelectItem> cityInfo = new ArrayList<SelectItem>();

	private List<SelectItem> areaInfo = new ArrayList<SelectItem>();

	private boolean venue;

	private boolean parkingAvailable;

	private boolean diningAvailable;

	private boolean stewartsAvailable;

	private boolean accomodationAvailable;

	private int venueTypeId;

	private int areaId;

	private String venueName;

	private String emailId;

	private int cityId;

	Map<String, List<ServiceProviderInfo>> serviceMap;

	private List<CityTypeMaster> cityTypeMasterList = new ArrayList<CityTypeMaster>();

	private List<AreaTypeMaster> areaTypeMasterList = new ArrayList<AreaTypeMaster>();

	EntityManagerFactory emf;

	EntityManager em;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public VendorManagedBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getExternalContext().getSessionMap().get("admin") == null) {
			try {
				ExternalContext externalContext = context.getExternalContext();
				externalContext.redirect("adminLogin.jsf");
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(CostManagedBean.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		emf = Persistence.createEntityManagerFactory("ROOT");
		em = emf.createEntityManager();
		try {
			Query query = em.createNamedQuery("ServiceProviderInfo.findAll");
			List srvProvidersList = query.getResultList();
			serviceMap = new HashMap<String, List<ServiceProviderInfo>>();
			Iterator<ServiceProviderInfo> atr = srvProvidersList.iterator();
			ServiceProviderInfo spi;
			while (atr.hasNext()) {
				spi = atr.next();
				if (serviceMap.containsKey(spi.getServiceTypeCode().getServiceTypeCode())) {
					srvProInfo = serviceMap.get(spi.getServiceTypeCode().getServiceTypeCode());
					srvProInfo.add(spi);
					serviceMap.put(spi.getServiceTypeCode().getServiceTypeCode(), srvProInfo);
				} else {
					srvProInfo = new ArrayList<ServiceProviderInfo>();
					srvProInfo.add(spi);
					serviceMap.put(spi.getServiceTypeCode().getServiceTypeCode(), srvProInfo);
				}
			}
			Query queryOne = em.createNamedQuery("VenueInfo.findAll");
			vnuProvidersList = queryOne.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		vendorMap = new HashMap<String, String>();
		vendorMap.put("Venue", "VEN");
		vendorMap.put("Caterer", "CATR");
		vendorMap.put("Decorator", "DECO");
		vendorMap.put("Entertainment", "ETMT");
		vendorMap.put("Photographer", "PHOT");
		vendors = new ArrayList<String>(vendorMap.keySet());

		// create vendor needs below
		generateMasterDataList();
		addObjects();
	}

	private void addObjects() {
		try {
			Iterator<VenueTypeMaster> venueIterator = this.venueMasterList.iterator();
			while (venueIterator.hasNext()) {
				VenueTypeMaster vMaster = (VenueTypeMaster) venueIterator.next();
				if (!vMaster.getVenueType().contains("ome"))
					this.venueTypeInfo.add(new SelectItem(vMaster.getVenueTypeId(), vMaster.getVenueType()));
			}

			Iterator<CityTypeMaster> cityIterators = this.cityTypeMasterList.iterator();
			while (cityIterators.hasNext()) {
				CityTypeMaster cMaster = (CityTypeMaster) cityIterators.next();
				// this.areaInfo.put(aMaster.getAreaName(),""+aMaster.getAreaId());
				this.cityInfo.add(new SelectItem(cMaster.getCityId(), cMaster.getCityName()));
			}

			Iterator<AreaTypeMaster> areaIterators = this.areaTypeMasterList.iterator();
			while (areaIterators.hasNext()) {
				AreaTypeMaster aMaster = (AreaTypeMaster) areaIterators.next();
				// this.areaInfo.put(aMaster.getAreaName(),""+aMaster.getAreaId());
				this.areaInfo.add(new SelectItem(aMaster.getAreaId(), aMaster.getAreaName()));
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

			Query qryVenueTypeMasterList = em.createNamedQuery("VenueTypeMaster.findByIsActive");
			qryVenueTypeMasterList.setParameter("isActive", isActive);
			this.venueMasterList = qryVenueTypeMasterList.getResultList();

			Query qryCityTypeMasterList = em.createNamedQuery("CityTypeMaster.findByIsActive");
			qryCityTypeMasterList.setParameter("isActive", isActive);
			this.cityTypeMasterList = qryCityTypeMasterList.getResultList();

			Query qryAreaTypeMasterList = em.createNamedQuery("AreaTypeMaster.findByIsActive");
			qryAreaTypeMasterList.setParameter("isActive", isActive);
			this.areaTypeMasterList = qryAreaTypeMasterList.getResultList();

		} catch (Exception e) {
			e.getMessage();
		} finally {
			em.close();
		}
	}
	
	private void clearValues() {
		this.emailId = "";
		this.venueName = "";
		this.areaId = 0;
		this.venueTypeId = 0;
		this.cityId = 0;
		this.parkingAvailable = false;
		this.accomodationAvailable = false;
		this.diningAvailable = false;
		this.stewartsAvailable = false;
		
	}

	public void insertNewVenueDetails() {
		EntityManager em1 = getEntityManager();
		EntityManager em3 = getEntityManager();
		EntityManager em2 = getEntityManager();
		try {
			EventxpertUser vu = new EventxpertUser();
			vu.setEmailaddr(emailId);
			vu.setISActive("Y");
			vu.setUserCategory("vendor");
			vu.setUserType("V");
			vu.setActivationKey(" ");
			vu.setCreatedBy("Admin");
			vu.setModifiedBy("Admin");
			vu.setCreatedDate(new Date());
			try {
				EncryptDecryptString eds = new EncryptDecryptString();
				String cryptPswd = eds.encrypt(emailId);
				vu.setPassword(cryptPswd);
			} catch (Exception ex) {
				java.util.logging.Logger.getLogger(ServiceProviderManagedBean.class.getName()).log(Level.SEVERE, null,
						ex);
			}
			try {
				em1.getTransaction().begin();
				em1.persist(vu);
				em1.getTransaction().commit();
				em1.refresh(vu);
				vu.getUserId();
				VenueInfo venueInfo = new VenueInfo();
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
				venueInfo.setVenueName(venueName);
				Timestamp ts = new Timestamp(new Date().getTime());
				venueInfo.setVenueEffectiveDate(ts);
				venueInfo.setVenueEtDate(ts);
				venueInfo.setCreatedDate(ts);
				venueInfo.setAcceptTermsConditions('Y');
				venueInfo.setVenueTypeId(getVenueTypeMaster(this.venueTypeId));
				venueInfo.setUserId(vu);
				AddressInfo addInfo = new AddressInfo();
				addInfo.setAddressLine2(getAreaName(this.areaId).getAreaName());
				addInfo.setContactName(venueName);
				addInfo.setEmail(emailId);
				addInfo.setCityId(this.cityId);
				try {
					em3.getTransaction().begin();
					em3.persist(addInfo);
					em3.getTransaction().commit();
					em3.refresh(addInfo);
				} catch (RollbackException e) {
					e.printStackTrace();
					em1.getTransaction().rollback();
				}
				venueInfo.setAddressId(addInfo);
				try {
					em2.getTransaction().begin();
					em2.persist(venueInfo);
					em2.getTransaction().commit();
					em2.refresh(venueInfo);
					venueInfo.setVenueCode("V" + venueInfo.getVenueId());
					em2.getTransaction().begin();
					em2.persist(venueInfo);
					em2.getTransaction().commit();					
				} catch (RollbackException e) {
					e.printStackTrace();
					em1.getTransaction().rollback();
					em2.getTransaction().rollback();
					em3.getTransaction().rollback();
				}
				vnuProvidersList.add(venueInfo);
				clearValues();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Vendor created successfully!!",
						"Vendor created successfully!!");
				FacesContext.getCurrentInstance().addMessage("Vendor created successfully!!", msg);
			} catch (RollbackException e) {
				e.printStackTrace();
				em1.getTransaction().rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em1.close();
			em3.close();
			em2.close();
		}
	}

	public void insertNewServiceDetails() {
		EntityManager em1 = getEntityManager();
		EntityManager em3 = getEntityManager();
		EntityManager em2 = getEntityManager();
		try {
			EventxpertUser vu = new EventxpertUser();
			vu.setEmailaddr(emailId);
			vu.setISActive("Y");
			vu.setUserCategory("vendor");
			vu.setUserType("O");
			vu.setActivationKey(" ");
			vu.setCreatedBy("Admin");
			vu.setModifiedBy("Admin");
			vu.setCreatedDate(new Date());
			try {
				EncryptDecryptString eds = new EncryptDecryptString();
				String cryptPswd = eds.encrypt(emailId);
				vu.setPassword(cryptPswd);
			} catch (Exception ex) {
				java.util.logging.Logger.getLogger(ServiceProviderManagedBean.class.getName()).log(Level.SEVERE, null,
						ex);
			}
			try {
				em1.getTransaction().begin();
				em1.persist(vu);
				em1.getTransaction().commit();
				em1.refresh(vu);
				vu.getUserId();
				ServiceProviderInfo serviceInfo = new ServiceProviderInfo();
				serviceInfo.setAreaId(getAreaName(this.areaId));
				serviceInfo.setIsActive('Y');
				serviceInfo.setServiceProviderCode("SP");
				serviceInfo.setServiceProviderName(venueName);
				serviceInfo.setAcceptTermsConditions('Y');
				serviceInfo.setServiceProviderDesc(venueName);
				serviceInfo.setServiceTypeCode(getServiceTypeMaster(vendorMap.get(selectedVendor)));
				Timestamp ts = new Timestamp(new Date().getTime());
				serviceInfo.setServiceProviderEffectiveDate(ts);
				serviceInfo.setServiceProviderEtDate(ts);
				serviceInfo.setCreatedDate(ts);
				AddressInfo addInfo = new AddressInfo();
				addInfo.setAddressLine2(getAreaName(this.areaId).getAreaName());
				addInfo.setContactName(venueName);
				addInfo.setEmail(emailId);
				addInfo.setCityId(this.cityId);
				try {
					em3.getTransaction().begin();
					em3.persist(addInfo);
					em3.getTransaction().commit();
					em3.refresh(addInfo);
				} catch (RollbackException e) {
					e.printStackTrace();
					em1.getTransaction().rollback();
				}
				serviceInfo.setAddressId(addInfo);
				serviceInfo.setUserId(vu);
				try {
					em2.getTransaction().begin();
					em2.persist(serviceInfo);
					em2.getTransaction().commit();
					em2.refresh(serviceInfo);
					serviceInfo.setServiceProviderCode("SP" + serviceInfo.getServiceProviderId());
					em2.getTransaction().begin();
					em2.persist(serviceInfo);
					em2.getTransaction().commit();
				} catch (RollbackException e) {
					e.printStackTrace();
					em1.getTransaction().rollback();
					em2.getTransaction().rollback();
					em3.getTransaction().rollback();
				}
				srvProInfo = serviceMap.get(vendorMap.get(selectedVendor));
				if(srvProInfo.isEmpty())
					srvProInfo = new ArrayList<ServiceProviderInfo>();
				srvProInfo.add(serviceInfo);
				serviceMap.put(selectedVendor, srvProInfo);
				clearValues();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Vendor created successfully!!",
						"Vendor created successfully!!");
				FacesContext.getCurrentInstance().addMessage("Vendor created successfully!!", msg);
			} catch (RollbackException e) {
				e.printStackTrace();
				em1.getTransaction().rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em1.close();
			em3.close();
			em2.close();
		}
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

	public void populateVendor() {
		if (vendorMap.get(selectedVendor).equals("VEN")) {
			setVnuShown(vnuProvidersList);
			setVenue(true);
		} else {
			setSrvShown(serviceMap.get(vendorMap.get(selectedVendor)));
			setVenue(false);
		}
	}

	public void reDirectUser() {
		EventxpertUser usr = selectedSrvProvider.getUserId();
		ServiceProviderManagedBean srvManagedBean = new ServiceProviderManagedBean();
		srvManagedBean.checkVendorLogin(usr, selectedSrvProvider);
	}

	public void reDirectVUser() {
		EventxpertUser usr = selectedVnuProvider.getUserId();
		ServiceProviderManagedBean srvManagedBean = new ServiceProviderManagedBean();
		srvManagedBean.checkVendorLogin(usr, null);
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();
		return em;
	}
	
	public String redirectAllVendors() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("admin",true);
		return "allVendors.xhtml?faces-redirect=true";
	}

	/**
	 * @return the vendors
	 */
	public List<String> getVendors() {
		return vendors;
	}

	/**
	 * @param vendors
	 *            the vendors to set
	 */
	public void setVendors(List<String> vendors) {
		this.vendors = vendors;
	}

	/**
	 * @return the selectedVendor
	 */
	public String getSelectedVendor() {
		return selectedVendor;
	}

	/**
	 * @param selectedVendor
	 *            the selectedVendor to set
	 */
	public void setSelectedVendor(String selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	/**
	 * @return the vendorList
	 */
	public List<String> getVendorList() {
		return vendorList;
	}

	/**
	 * @param vendorList
	 *            the vendorList to set
	 */
	public void setVendorList(List<String> vendorList) {
		this.vendorList = vendorList;
	}

	/**
	 * @return the srvShown
	 */
	public List<ServiceProviderInfo> getSrvShown() {
		return srvShown;
	}

	/**
	 * @param srvShown
	 *            the srvShown to set
	 */
	public void setSrvShown(List<ServiceProviderInfo> srvShown) {
		this.srvShown = srvShown;
	}

	/**
	 * @return the selectedSrvProvider
	 */
	public ServiceProviderInfo getSelectedSrvProvider() {
		return selectedSrvProvider;
	}

	/**
	 * @param selectedSrvProvider
	 *            the selectedSrvProvider to set
	 */
	public void setSelectedSrvProvider(ServiceProviderInfo selectedSrvProvider) {
		this.selectedSrvProvider = selectedSrvProvider;
	}

	/**
	 * @return the vnuShown
	 */
	public List<VenueInfo> getVnuShown() {
		return vnuShown;
	}

	/**
	 * @param vnuShown
	 *            the vnuShown to set
	 */
	public void setVnuShown(List<VenueInfo> vnuShown) {
		this.vnuShown = vnuShown;
	}

	/**
	 * @return the selectedVnuProvider
	 */
	public VenueInfo getSelectedVnuProvider() {
		return selectedVnuProvider;
	}

	/**
	 * @param selectedVnuProvider
	 *            the selectedVnuProvider to set
	 */
	public void setSelectedVnuProvider(VenueInfo selectedVnuProvider) {
		this.selectedVnuProvider = selectedVnuProvider;
	}

	/**
	 * @return the venue
	 */
	public boolean isVenue() {
		return venue;
	}

	/**
	 * @param venue
	 *            the venue to set
	 */
	public void setVenue(boolean venue) {
		this.venue = venue;
	}

	/**
	 * @return the parkingAvailable
	 */
	public boolean isParkingAvailable() {
		return parkingAvailable;
	}

	/**
	 * @param parkingAvailable
	 *            the parkingAvailable to set
	 */
	public void setParkingAvailable(boolean parkingAvailable) {
		this.parkingAvailable = parkingAvailable;
	}

	/**
	 * @return the diningAvailable
	 */
	public boolean isDiningAvailable() {
		return diningAvailable;
	}

	/**
	 * @param diningAvailable
	 *            the diningAvailable to set
	 */
	public void setDiningAvailable(boolean diningAvailable) {
		this.diningAvailable = diningAvailable;
	}

	/**
	 * @return the stewartsAvailable
	 */
	public boolean isStewartsAvailable() {
		return stewartsAvailable;
	}

	/**
	 * @param stewartsAvailable
	 *            the stewartsAvailable to set
	 */
	public void setStewartsAvailable(boolean stewartsAvailable) {
		this.stewartsAvailable = stewartsAvailable;
	}

	/**
	 * @return the accomodationAvailable
	 */
	public boolean isAccomodationAvailable() {
		return accomodationAvailable;
	}

	/**
	 * @param accomodationAvailable
	 *            the accomodationAvailable to set
	 */
	public void setAccomodationAvailable(boolean accomodationAvailable) {
		this.accomodationAvailable = accomodationAvailable;
	}

	/**
	 * @return the venueName
	 */
	public String getVenueName() {
		return venueName;
	}

	/**
	 * @param venueName
	 *            the venueName to set
	 */
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the cityInfo
	 */
	public List<SelectItem> getCityInfo() {
		return cityInfo;
	}

	/**
	 * @param cityInfo
	 *            the cityInfo to set
	 */
	public void setCityInfo(List<SelectItem> cityInfo) {
		this.cityInfo = cityInfo;
	}

	/**
	 * @return the areaInfo
	 */
	public List<SelectItem> getAreaInfo() {
		return areaInfo;
	}

	/**
	 * @param areaInfo
	 *            the areaInfo to set
	 */
	public void setAreaInfo(List<SelectItem> areaInfo) {
		this.areaInfo = areaInfo;
	}

	/**
	 * @return the areaId
	 */
	public int getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the venueTypeId
	 */
	public int getVenueTypeId() {
		return venueTypeId;
	}

	/**
	 * @param venueTypeId
	 *            the venueTypeId to set
	 */
	public void setVenueTypeId(int venueTypeId) {
		this.venueTypeId = venueTypeId;
	}

	/**
	 * @return the venueTypeInfo
	 */
	public List<SelectItem> getVenueTypeInfo() {
		return venueTypeInfo;
	}

	/**
	 * @param venueTypeInfo
	 *            the venueTypeInfo to set
	 */
	public void setVenueTypeInfo(List<SelectItem> venueTypeInfo) {
		this.venueTypeInfo = venueTypeInfo;
	}

}
