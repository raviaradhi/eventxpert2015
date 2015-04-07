package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.event.comparator.AreaTypeComparatoreByAreaName;
import com.circulous.xpert.event.comparator.CityTypeComparatoreByCityName;
import com.circulous.xpert.event.comparator.ServiceProviderComparatorByAmount;
import com.circulous.xpert.event.comparator.ServiceProviderComparatorByArea;
import com.circulous.xpert.event.comparator.ServiceProviderComparatorByName;
import com.circulous.xpert.event.comparator.ServiceVOComparator;
import com.circulous.xpert.event.comparator.VenueComparatorByAmount;
import com.circulous.xpert.event.comparator.VenueComparatorByArea;
import com.circulous.xpert.event.comparator.VenueComparatorByName;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.CityTypeMaster;
import com.circulous.xpert.jpa.entities.CurrencyTypeMaster;
import com.circulous.xpert.jpa.entities.CustomerInfo;
import com.circulous.xpert.jpa.entities.EspPackageInfo;
import com.circulous.xpert.jpa.entities.EspPackageItemInfo;
import com.circulous.xpert.jpa.entities.EventDates;
import com.circulous.xpert.jpa.entities.EventDates_;
import com.circulous.xpert.jpa.entities.EventInfo;
import com.circulous.xpert.jpa.entities.EventServiceProviderInfo;
import com.circulous.xpert.jpa.entities.EventServiceProviderInfo_;
import com.circulous.xpert.jpa.entities.EventTypeMaster;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo_;
import com.circulous.xpert.jpa.entities.ServiceTypeMaster;
import com.circulous.xpert.jpa.entities.VenueHallInfo;
import com.circulous.xpert.jpa.entities.VenueInfo;
import com.circulous.xpert.jpa.entities.VenueTypeMaster;
import com.circulous.xpert.jpa.entities.views.ViewServiceProvider;
import com.circulous.xpert.jpa.entities.views.ViewServiceProviderPackage;
import com.circulous.xpert.jpa.entities.views.ViewServiceProvider_;
import com.circulous.xpert.jpa.entities.views.ViewVenue;
import com.circulous.xpert.jpa.entities.views.ViewVenuePackage;
import com.circulous.xpert.jpa.entities.views.ViewVenue_;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.log4j.Logger;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import java.util.concurrent.CopyOnWriteArrayList;

@ManagedBean
// @ViewScoped
@SessionScoped
// @ApplicationScoped
public class EventDetailsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(EventDetailsManagedBean.class);

	// @ManagedProperty("#{customerManagedBean}")
	// private CustomerManagedBean customerManagedBean;
	//
	// public CustomerManagedBean getCustomerManagedBean()
	// {
	// return customerManagedBean;
	// }

	private String adultcount = "0";

	private String amount = "0";

	private Integer area = new Integer(0);

	// private List<SelectItem> areaList = new ArrayList<SelectItem>();

	private Map areaList = new HashMap();
	private Map areaListLocal = new HashMap();

	private String childcount = "0";

	private Integer city = new Integer(0);

	// private List<SelectItem> cityList = new ArrayList<SelectItem>();

	private Map cityList = new HashMap();
	private Map cityListLocal = new HashMap();

	private Date eventDate;

	private Date eventTime;

	// private Map<String, ViewServiceProviderPackage> finalServicesMap = new
	// HashMap<String, ViewServiceProviderPackage>();
	private HashMap finalServicesMap = new HashMap();

	private HashMap finalVenueMap = new HashMap();

	// private VenuePackageInfo finalVenueInfo = null;
	private ViewVenuePackage finalVenueInfo = null;

	private List<AreaTypeMaster> lAreaList = new ArrayList<AreaTypeMaster>();

	private List<CityTypeMaster> list = new ArrayList<CityTypeMaster>();

	private String popupServiceCode = "";

	private String popupVenueId = "0";

	private String selectedBCService;

	private List<ServiceVO> selectedSrvList = new ArrayList<ServiceVO>();

	private String serviceHidden = "";

	private List<ServiceVO> serviceList = new ArrayList<ServiceVO>();

	private Map<String, String> serviceMap = new HashMap<String, String>();

	private List<ServiceTypeMaster> serviceTypeMaster = new ArrayList<ServiceTypeMaster>();

	private String sortBy = "";

//	private List<ServiceProviderInfo> spInfoList = new ArrayList<ServiceProviderInfo>();
//
//	private List<VenueInfo> venueInfoList = new ArrayList<VenueInfo>();

	private List<VenueVO> venueList = new ArrayList<VenueVO>();

	// private Map<String, String> venueMap = new HashMap<String, String>();

	private Map venueMap = new HashMap();
	private Map venueMapLocal = new HashMap();

	private String venueRadio = "";

	private List<VenueTypeMaster> venueTypeMaster = new ArrayList<VenueTypeMaster>();

	private List<ViewServiceProvider> viewServiceProviderList = new ArrayList<ViewServiceProvider>();

	private List<ViewVenue> viewVenueList = new ArrayList<ViewVenue>();

	private String packDesc;

	private int packId;

	private BigDecimal serviceCost;

	private BigDecimal serviceFinalCost;

	private ViewServiceProviderPackage srvcpkg;

	private ViewVenuePackage venuePkg;

	private String eventType;
	private int eventTypeId;
	private String cityName = "";
	private String areaName = "";
	private List<ViewVenue> allVenueList;

	public Map getAreaList() {
		return areaList;
	}

	public void setAreaList(Map areaList) {
		this.areaList = areaList;
	}

	public Map getAreaListLocal() {
		return areaListLocal;
	}

	public void setAreaListLocal(Map areaListLocal) {
		this.areaListLocal = areaListLocal;
	}

	public Map getCityList() {
		return cityList;
	}

	public void setCityList(Map cityList) {
		this.cityList = cityList;
	}

	public Map getCityListLocal() {
		return cityListLocal;
	}

	public void setCityListLocal(Map cityListLocal) {
		this.cityListLocal = cityListLocal;
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	private CustomerInfo userId;

	public CustomerInfo getUserId() {
		// logger.debug("userId*****  "+userId);
		userId = (CustomerInfo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
		// logger.debug("userId*********  "+userId);
		return userId;
	}

	public void setUserId(CustomerInfo userId) {
		this.userId = userId;
	}

	public EventDetailsManagedBean() {
		logger.debug("EventDetailsManagedBean :: Constructor Begin");
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			userId = (CustomerInfo) context.getExternalContext().getSessionMap().get("userId");
			logger.debug("userId*********  " + userId);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.containsKey("eventDetailsManagedBean")) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");
		}

		// if(userId!=null)
		// {
		// EventHistoryManagedBean eventHistoryManagedBean =
		// (EventHistoryManagedBean)
		// context.getApplication().evaluateExpressionGet(context,
		// "#{eventHistoryManagedBean}", EventHistoryManagedBean.class);
		// setEventType(eventHistoryManagedBean.getEventType());
		// setEventTypeId(eventHistoryManagedBean.getEventTypeId());
		// }
		// else
		{
			EventManagedBean eventManagedBean = (EventManagedBean) context.getApplication().evaluateExpressionGet(
					context, "#{eventManagedBean}", EventManagedBean.class);
			setEventType(eventManagedBean.getEventType());
			setEventTypeId(eventManagedBean.getEventTypeId());
		}

		logger.debug("serviceList " + serviceList);

		ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
		logger.debug("EventDetailsManagedBean ::"
				+ exContext.getApplicationMap().containsKey("eventExpertApplicationManagedBean"));
		EventExpertApplicationManagedBean eAppManagedBean = (EventExpertApplicationManagedBean) exContext
				.getApplicationMap().get("eventExpertApplicationManagedBean");
		logger.debug("EventDetailsManagedBean :: EventExpertApplicationManagedBean----" + eAppManagedBean);

		generateMasterDataList(eAppManagedBean);

		populateMasterData();

		/*
		 * hotelImages = new ArrayList<String>();
		 * 
		 * for(int i=1;i<=4;i++) { hotelImages.add("venue/hotels/hotel" + i +
		 * ".jpg"); // hotelImages.add("where-bg" + i + ".jpg"); }
		 */

		eventTypeList = eAppManagedBean.fetchEventTypeMap();

		Properties prop = new Properties();
		InputStream input = null;

		try {

			// input = new FileInputStream("eXpertConfig.properties");
			FacesContext ctx = FacesContext.getCurrentInstance();
			ExternalContext externalContext = ctx.getExternalContext();
			input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");// new
																							// FileInputStream("eXpertConfig.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			path = prop.getProperty("imgpath");
			url = prop.getProperty("imgurl");

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
	private HashMap eventTypeList;

	public HashMap getEventTypeList() {
		return eventTypeList;
	}

	public void setEventTypeList(HashMap eventTypeList) {
		this.eventTypeList = eventTypeList;
	}

	public void listenEventList(ValueChangeEvent e) {
		// logger.debug(e.getNewValue());
		eventType = (String) eventTypeList.get(e.getNewValue());
	}

	private List<String> hotelImages;
	private List<String> hotelHallImages;
	private List<String> servicesImages;
	private List<String> servicePkgImages;

	private String srvcPrvdPkgPath;
	private String srvcPrvdUrl;

	public List<String> getServicesImages() {
		return servicesImages;
	}

	public void setServicesImages(List<String> servicesImages) {
		this.servicesImages = servicesImages;
	}

	public List<String> getHotelImages() {
		logger.debug("/hotels/ " + hotelImages);
		return hotelImages;
	}

	public void setHotelImages(List<String> hotelImages) {
		this.hotelImages = hotelImages;
	}

	public List<String> getHotelHallImages() {
		return hotelHallImages;
	}

	public void setHotelHallImages(List<String> hotelHallImages) {
		this.hotelHallImages = hotelHallImages;
	}

	@SuppressWarnings("unchecked")
	public void generateMasterDataList(EventExpertApplicationManagedBean eAppManagedBean) {

		list = eAppManagedBean.fetchList();

		lAreaList = eAppManagedBean.fetchlAreaList();

		venueTypeMaster = eAppManagedBean.fetchVenueTypeMaster();

		serviceTypeMaster = eAppManagedBean.fetchServiceTypeMaster();

//		venueInfoList = eAppManagedBean.fetchVenueInfoList();
//
//		spInfoList = eAppManagedBean.fetchSpInfoList();

		viewServiceProviderList = eAppManagedBean.fetchViewServiceProviderList();

		viewVenueList = eAppManagedBean.fetchViewVenueList();

	}

	/*
	 * @SuppressWarnings("unchecked") public void generateMasterDataList() {
	 * EntityManagerFactory emf =
	 * Persistence.createEntityManagerFactory("ROOT"); EntityManager em =
	 * emf.createEntityManager();
	 * 
	 * try { Query query = em.createNamedQuery("CityTypeMaster.findAll"); list =
	 * query.getResultList(); Query areaquery =
	 * em.createNamedQuery("AreaTypeMaster.findAll"); lAreaList =
	 * areaquery.getResultList(); //Query qryVenueMasterList =
	 * em.createNamedQuery("VenueTypeMaster.findAll"); Query qryVenueMasterList
	 * = em.createNamedQuery("VenueTypeMaster.findByIsActive");
	 * qryVenueMasterList.setParameter("isActive", 'Y'); venueTypeMaster =
	 * qryVenueMasterList.getResultList();
	 * 
	 * //Query qryServiceTypeMasterList =
	 * em.createNamedQuery("ServiceTypeMaster.findAll"); Query
	 * qryServiceTypeMasterList =
	 * em.createNamedQuery("ServiceTypeMaster.findByIsActive");
	 * qryServiceTypeMasterList.setParameter("isActive", 'Y'); serviceTypeMaster
	 * = qryServiceTypeMasterList.getResultList();
	 * 
	 * Query qryVenueList = em.createNamedQuery("VenueInfo.findAll");
	 * venueInfoList = qryVenueList.getResultList(); //
	 * logger.debug("generateMasterDataList:::::  venueInfoList  --" // +
	 * venueInfoList.size());
	 * 
	 * Query qryServiceMasterList =
	 * em.createNamedQuery("ServiceProviderInfo.findAll"); spInfoList =
	 * qryServiceMasterList.getResultList(); //
	 * logger.debug("generateMasterDataList:::::  spInfoList  --" // +
	 * spInfoList.size());
	 * 
	 * Query qryViewServiceProviderList =
	 * em.createNamedQuery("ViewServiceProvider.findAll");
	 * viewServiceProviderList = qryViewServiceProviderList.getResultList(); //
	 * logger.debug("generateMasterDataList:::::  viewServiceProviderList  --"
	 * // + viewServiceProviderList.size()); //
	 * logger.debug("generateMasterDataList::::: ViewServiceProvider  -- " // +
	 * viewServiceProviderList);
	 * 
	 * Query qryViewVenueList = em.createNamedQuery("ViewVenue.findAll");
	 * viewVenueList = qryViewVenueList.getResultList(); //
	 * logger.debug("generateMasterDataList:::::  viewVenueList size  --" // +
	 * viewVenueList.size()); //
	 * logger.debug("generateMasterDataList::::: viewVenueList  -- " // +
	 * viewVenueList);
	 * 
	 * // logger.debug("###############################-- :::venueTypeMaster" //
	 * + venueTypeMaster.size()); } finally { em.close(); } }
	 */

	public String getAdultcount() {
		return adultcount;
	}

	public List<ViewServiceProvider> getServiceListDtls(ServiceVO serviceVO) {

		List<ViewServiceProvider> localVIList = new ArrayList<ViewServiceProvider>();
		try {
			// logger.debug("selectedBCService&&&&&&&&&&&&&& "
			// +selectedBCService);
			// if (selectedBCService==null || selectedBCService.equals("null")){
			//
			// // selectedSrvList = new ArrayList<ServiceVO>();
			// selectedBCService = "venue";//getNextBCService();
			// logger.debug("selectedBCService else "+selectedBCService);
			// return new ArrayList<ViewServiceProvider>();
			//
			//
			//
			// //return localVIList;
			// } else {

			ServiceVO selectedServiceVO = null;
			// logger.debug("getAllServicesList::::: selectedSrvList"
			// +selectedSrvList);
			// logger.debug("selectedBCService***********" +selectedBCService);
			// logger.debug("getNextBCService()***********"
			// +getNextBCService());
			// for (int j=0;j<selectedSrvList.size(); j++) {
			// ServiceVO serviceVO = selectedSrvList.get(j);
			logger.debug("serviceVO.getHeading::::: " + serviceVO.getHeading());
			logger.debug("serviceVO.getId::::: " + serviceVO.getId());
			// // if (null != getNextBCService() &&
			// getNextBCService().equalsIgnoreCase(serviceVO.getHeading())) {
			// if (null != selectedBCService &&
			// selectedBCService.equalsIgnoreCase(serviceVO.getHeading())) {
			selectedServiceVO = serviceVO;
			logger.debug("selectedServiceVO.getId::::: " + selectedServiceVO.getId());
			// break;
			// }
			// }
			// logger.debug(selectedServiceVO);

			long lCount = new Long(getTotalCount()).longValue();

			if (null != selectedServiceVO) {


				EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
				EntityManager em = emf.createEntityManager();
				
				ServiceTypeMaster stm = new ServiceTypeMaster(selectedServiceVO.getId());
				// SimpleDateFormat readFormat = new
				// SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
				SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat writeFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				Date d = readFormat.parse(eventDate.toString());
				logger.debug("dattteee " + d);
				String str = writeFormat.format(d);
				Date strDate = writeFormat1.parse(str);
				logger.debug("strDate " + strDate);

				/*Query qryList = em.createQuery("select  e1 from  EventDates e1, ServiceProviderInfo s where  e1.eventDate = :edate and e1.serviceProviderId = s.serviceProviderId and s.serviceTypeCode=:vt");
				qryList.setParameter("edate", strDate);
				qryList.setParameter("vt", stm);
				List espList = qryList.getResultList();*/
                                
                                CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ViewServiceProvider> criteriaQuery = cb.createQuery(ViewServiceProvider.class);
                                Root<ViewServiceProvider> vSProvider = criteriaQuery.from(ViewServiceProvider.class);                       
                                CriteriaQuery<ViewServiceProvider> select = criteriaQuery.select(vSProvider);
                                
                                

                                Subquery<EventDates> cq = criteriaQuery.subquery(EventDates.class);
                                Root<EventDates> edates = cq.from(EventDates.class);
                                Root<ServiceProviderInfo> sp = cq.from(ServiceProviderInfo.class); 
                                cq.select(edates);
                                cq.where(cb.and(cb.equal(edates.get(EventDates_.eventDate), strDate),
                                        cb.equal(sp.get(ServiceProviderInfo_.serviceProviderId), edates.get(EventDates_.serviceProviderId)),
                                        cb.equal(sp.get(ServiceProviderInfo_.serviceTypeCode), stm)));                        
                                
                                logger.debug("(SP CQ**********   " + cq);
                                
                                Expression<String> exp = vSProvider.get("serviceProviderId");
                                Predicate predicate = exp.in(cq);
                                //select.where(cb.not(predicate));  
                                select.where(cb.and(cb.equal(vSProvider.get(ViewServiceProvider_.serviceTypeCode), selectedServiceVO.getId()),cb.not(predicate)));
                               // select.where(cb.not(cb.exists(cq)));                        

                                TypedQuery<ViewServiceProvider> qryViewServiceProviderList = em.createQuery(select);

				logger.debug("(SP**********   " + select);
				//logger.debug("(((((((((((!**********  " + espList);

				/*String qry = "Select v from ViewServiceProvider v where v.serviceTypeCode='"
						+ selectedServiceVO.getId() + "'";
				for (int j = 0; j < espList.size(); j++) {
					EventDates esp = (EventDates) espList.get(j);

					if (j == 0) {
						qry = qry + " and v.serviceProviderId<>" + esp.getServiceProviderId() + " and ";
					}

					qry = qry + "v.serviceProviderId<>" + esp.getServiceProviderId() + " and ";

					if (j == (espList.size() - 1)) {
						qry = qry + "v.serviceProviderId<>" + esp.getServiceProviderId();
					}
				}

				Query qryViewServiceProviderList = em.createQuery(qry);*/
				localVIList = qryViewServiceProviderList.getResultList();

                                List<ViewServiceProvider> templist = new ArrayList<ViewServiceProvider>();
                                ArrayList indexes = new ArrayList();
                                
                               // Concurrent<ViewServiceProvider> localVIList1 = (CopyOnWriteArrayList<ViewServiceProvider>) localVIList;
                        
                        //for (Iterator<ViewServiceProvider> iterator = localVIList.iterator(); iterator.hasNext();) 
                        for (int j = 0; j<localVIList.size(); j++) 
                        {
                            //ViewServiceProvider type = (ViewServiceProvider) iterator.next();
                            System.out.println("localVIList.size()**  "+localVIList.size());
                            //System.out.println("templist.size()**  "+templist.size());
                            ViewServiceProvider type = (ViewServiceProvider) localVIList.get(j);
                            Query qryList11 = em.createNamedQuery("ViewServiceProviderPackage.findBySrvPrvdIdEType");			
                            qryList11.setParameter("serviceProviderId", type.getServiceProviderId());
                            qryList11.setParameter("eventType", eventTypeId);
                            System.out.println("qryList11***  "+qryList11);
                            List vList = qryList11.getResultList();
                            
                            if(vList.size()>0)
                            {
                                System.out.println("Records exists");
                                templist.add(type);
                            }
                            else
                            {
                                System.out.println("No Records exists");
//                                templist.remove(type);
                                //indexes.add(j);
                                 
                            }
                        }
                        
//                        for (int j = 0; j<indexes.size(); j++) 
//                        {
//                            templist.remove(j);
//                        }
                        localVIList = templist;
                        
				logger.debug("getAllServicesList::::: localVIList size -- " + localVIList.size()
						+ "  qryViewServiceProviderList " + qryViewServiceProviderList);
                                
                                Query arQry = em.createNamedQuery("AreaTypeMaster.findByAreaId");
                                arQry.setParameter("areaId", area);
                                AreaTypeMaster atm = (AreaTypeMaster) arQry.getSingleResult();

                                System.out.println("area  "+area);

                                double lat = Double.valueOf(atm.getLatitude());
                                double lng = Double.valueOf(atm.getLongitude());
                                LatitudeLongitude current = new LatitudeLongitude(lat, lng);

                                    PlacesLatitudes sample = new PlacesLatitudes();
                                    try 
                                    {
        //                                System.out.println("address "+firstAddress);
        //                                localVIList = sample.performSearch(firstAddress+","+areaName+","+cityName,localVIList);
                                        localVIList = sample.calculateDistancesServices(current,localVIList);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

			}

			logger.debug("getAllServicesList::::: localVIList  -- " + localVIList);

			// logger.debug("selected Venue");
			if ("Amount".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByAmount());
			} else if ("Area".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByArea());
			} else if ("Name".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByName());
			}
                        else{
                            
                        }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return localVIList;
	}

	public List<ViewServiceProvider> getAllServicesList() {

		List<ViewServiceProvider> localVIList = new ArrayList<ViewServiceProvider>();
		try {
			logger.debug("selectedBCService&&&&&&&&&&&&&& " + selectedBCService);
			if (selectedBCService == null || selectedBCService.equals("null")) {

				// selectedSrvList = new ArrayList<ServiceVO>();
				selectedBCService = "venue";// getNextBCService();
				logger.debug("selectedBCService else----------- " + selectedBCService);
				return new ArrayList<ViewServiceProvider>();

				// return localVIList;
			} else {

				ServiceVO selectedServiceVO = null;
				logger.debug("getAllServicesList::::: selectedSrvList" + selectedSrvList);
				logger.debug("selectedBCService***********" + selectedBCService);
				logger.debug("getNextBCService()***********" + getNextBCService());
				for (int j = 0; j < selectedSrvList.size(); j++) {
					ServiceVO serviceVO = selectedSrvList.get(j);
					logger.debug("serviceVO.getHeading::::: " + serviceVO.getHeading());
					// if (null != getNextBCService() &&
					// getNextBCService().equalsIgnoreCase(serviceVO.getHeading()))
					// {
					if (null != selectedBCService && selectedBCService.equalsIgnoreCase(serviceVO.getHeading())) {
						selectedServiceVO = serviceVO;
						break;
					}
				}
				// logger.debug(selectedServiceVO);

				long lCount = new Long(getTotalCount()).longValue();

				if (null != selectedServiceVO) {

					/*
					 * for (Iterator<ViewServiceProvider> iterator =
					 * viewServiceProviderList.iterator(); iterator.hasNext();)
					 * { ViewServiceProvider type = (ViewServiceProvider)
					 * iterator.next(); logger.debug(
					 * "************************************************");
					 * logger
					 * .debug("getAllServicesList::::: ViewServiceProvider  -- "
					 * + type); logger.debug("type.getVenueTypeId() --"
					 * +type.getServiceTypeCode());
					 * logger.debug("selectedServiceVO.getId() -- "
					 * +selectedServiceVO.getId()); if
					 * (type.getServiceTypeCode()
					 * .equalsIgnoreCase(selectedServiceVO.getId())) {
					 * //if(type.getMinGuests() <= lCount) {
					 * logger.debug("selectedServiceVO.getId()_---------"
					 * +selectedServiceVO.getId()); localVIList.add(type); } }
					 * logger.debug(
					 * "-------------------------------------------------------"
					 * ); }
					 */

					EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
					EntityManager em = emf.createEntityManager();
					Query qryViewServiceProviderList = em
							.createQuery("Select v from ViewServiceProvider v where v.serviceTypeCode='"
									+ selectedServiceVO.getId() + "'");
					localVIList = qryViewServiceProviderList.getResultList();
                                        
                                        Query arQry = em.createNamedQuery("AreaTypeMaster.findByAreaId");
                                        arQry.setParameter("areaId", area);
                                        AreaTypeMaster atm = (AreaTypeMaster) arQry.getSingleResult();

                                        System.out.println("area  "+area);

                                        double lat = Double.valueOf(atm.getLatitude());
                                        double lng = Double.valueOf(atm.getLongitude());
                                        LatitudeLongitude current = new LatitudeLongitude(lat, lng);

                                            PlacesLatitudes sample = new PlacesLatitudes();
                                            try 
                                            {
                //                                System.out.println("address "+firstAddress);
                //                                localVIList = sample.performSearch(firstAddress+","+areaName+","+cityName,localVIList);
                                                localVIList = sample.calculateDistancesServices(current,localVIList);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
				}
				logger.debug("getAllServicesList::::: localVIList size -- " + localVIList.size());
				logger.debug("getAllServicesList::::: localVIList  -- " + localVIList);

				// logger.debug("selected Venue");
				if ("Amount".equals(sortBy)) {
					Collections.sort(localVIList, new ServiceProviderComparatorByAmount());
				} else if ("Area".equals(sortBy)) {
					Collections.sort(localVIList, new ServiceProviderComparatorByArea());
				} else if ("Name".equals(sortBy)){
					Collections.sort(localVIList, new ServiceProviderComparatorByName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return localVIList;
	}

	VenueVO selectedVenueVO = null;

	public VenueVO getSelectedVenueVO() {
		return selectedVenueVO;
	}

	public void setSelectedVenueVO(VenueVO selectedVenueVO) {
		this.selectedVenueVO = selectedVenueVO;
	}

	public void setAllVenueList(List<ViewVenue> allVenueList) {
		this.allVenueList = allVenueList;
	}

	public List<ViewVenue> getAllVenueList() {
		return allVenueList;
	}

	public HashMap vPkgDtls;

	public HashMap getvPkgDtls() {
		return vPkgDtls;
	}

	public void setvPkgDtls(HashMap vPkgDtls) {
		this.vPkgDtls = vPkgDtls;
	}

        private boolean haveRecords;

    public boolean isHaveRecords() {
        return haveRecords;
    }

    public void setHaveRecords(boolean haveRecords) {
        this.haveRecords = haveRecords;
    }
        
        
        
	public List<ViewVenue> getAllVenueList1() {

		logger.debug("getAllVenueList -- selectedBCService -- " + selectedBCService);

		// if ("venue".equalsIgnoreCase(selectedBCService)) {

		List<ViewVenue> localVIList = new ArrayList<ViewVenue>();
		logger.debug("getAllVenueList -- venueType -- " + venueType);
		logger.debug("getAllVenueList -- venueRadio -- " + venueRadio);

                VenueTypeMaster vtm = new VenueTypeMaster(Integer.parseInt(venueRadio));
		vPkgDtls = new HashMap();
                
		for (Iterator<VenueVO> iterator = venueList.iterator(); iterator.hasNext();) {
			VenueVO venueVO = iterator.next();

			if (venueVO.getId().equals(venueRadio)) {
				// if (venueVO.getHeading().equals(venueType)){
				logger.debug("selected Venue  " + venueVO.getHeading());
				// logger.debug("Found --");
				selectedVenueVO = venueVO;
				break;
			}
		}

		// logger.debug(selectedVenueVO);
		if (null != selectedVenueVO) {
			logger.debug("*********########****** " + eventDate);
			// SimpleDateFormat writeFormat = new
			// SimpleDateFormat("dd-MM-YYYY");
			// String str = writeFormat.format(eventDate);

			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();                        
                       
                        
			Query qryList1 = em
					.createQuery("select  e1 from  VenueInfo e1 where  e1.venueTypeId = :vt");			
			qryList1.setParameter("vt", vtm);
			List espList = qryList1.getResultList();
                        
                        if(espList.size()==0)
                        {
                            haveRecords = false;
                        }
                        else
                        {
                             haveRecords = true;
                            CriteriaBuilder cb = em.getCriteriaBuilder();
//                        CriteriaQuery<EventServiceProviderInfo> cq = cb.createQuery(EventServiceProviderInfo.class);
//                        Root<EventServiceProviderInfo> at = cq.from(EventServiceProviderInfo.class);
//                        cq.select(at);
//                        // Person_.java is the generated canonical so we don't have to use
//                        // at.get("firstName") and at.get("lastName")
//                        cq.where(cb.and(cb.equal(at.get(EventServiceProviderInfo_.eventServiceProviderEffectiveDate), eventDate),
//                                cb.equal(at.get(EventServiceProviderInfo_.venue), 'Y')));
//                        TypedQuery<EventServiceProviderInfo> qryList = em.createQuery(cq);
//                        List<EventServiceProviderInfo> espList = qryList.getResultList();
//                        
			// Query qryList1 =
			// em.createQuery("select  e1 from  EventDates e1 where  e1.eventDate = :edate and e1.serviceProviderId = :vt");
			// qryList.setParameter("edate", eventDate);
			// qryList.setParameter("vt", 'Y');
			// List espList = qryList.getResultList();

//			logger.debug("((((   " + qryList);
//			logger.debug("(((((((((((!**********  " + espList);

                       // cb = em.getCriteriaBuilder();
                        CriteriaQuery<ViewVenue> criteriaQuery = cb.createQuery(ViewVenue.class);
                        Root<ViewVenue> vVenue = criteriaQuery.from(ViewVenue.class);                       
                        CriteriaQuery<ViewVenue> select = criteriaQuery.select(vVenue);
                        
                        
                        
                        Subquery<EventServiceProviderInfo> cq = criteriaQuery.subquery(EventServiceProviderInfo.class);
                        Root<EventServiceProviderInfo> at = cq.from(EventServiceProviderInfo.class);
                        cq.select(at);
                        cq.where(cb.and(cb.equal(at.get(EventServiceProviderInfo_.eventServiceProviderEffectiveDate), eventDate),
                                cb.equal(at.get(EventServiceProviderInfo_.venue), 'Y')));                        
                        
                        Expression<String> exp = vVenue.get("venueId");
                        Predicate predicate = exp.in(cq);
                        select.where(cb.not(predicate));                        
                       // select.where(cb.not(cb.exists(cq)));                        
                        
                        TypedQuery<ViewVenue> qryList = em.createQuery(select);
                        
                        viewVenueList = qryList.getResultList();
                        logger.debug("(((( hjjkh   " + qryList.toString());
                        
			/*String qry = "select v from ViewVenue v";
			for (int j = 0; j < espList.size(); j++) {
				EventServiceProviderInfo esp = (EventServiceProviderInfo) espList.get(j);

				if (j == 0) {
					qry = qry + " where v.venueId<>" + esp.getServiceProviderId() + " and ";
				}

				qry = qry + "v.venueId<>" + esp.getServiceProviderId() + " and ";

				if (j == (espList.size() - 1)) {
					qry = qry + "v.venueId<>" + esp.getServiceProviderId();
				}
			}

			logger.debug("((((   " + qry);
			Query qryViewVenueList = em.createQuery(qry); */
//			viewVenueList = qryViewVenueList.getResultList();
                        boolean getAddress = false;
                        String firstAddress="";
                        LatitudeLongitude current = null;
                        
                        List<ViewVenue> viewVenueList1 = viewVenueList;
                        List<ViewVenue> templist = new ArrayList<ViewVenue>();
                         
                        for (Iterator<ViewVenue> iterator = viewVenueList.iterator(); iterator.hasNext();) 
                        {
                            ViewVenue type = (ViewVenue) iterator.next();
                            Query qryList11 = em.createNamedQuery("ViewVenuePackage.findByVenueIdEType");			
                            qryList11.setParameter("venueId", type.getVenueId());
                            qryList11.setParameter("eventType", eventTypeId);
                            System.out.println("qryList11***  "+qryList11);
                            List vList = qryList11.getResultList();
                            
                            if(vList.size()>0)
                            {
                                System.out.println("Records exists");
                                templist.add(type);
                            }
                            else
                            {
                                System.out.println("No Records exists");
                                //viewVenueList1.remove(type);
                            }
                        }
                        
                        viewVenueList = templist;
                        
			for (Iterator<ViewVenue> iterator = viewVenueList.iterator(); iterator.hasNext();) 
                        {
				ViewVenue type = (ViewVenue) iterator.next();
				if (type.getVenueTypeId() == 3) {
					type.setVenueCost(type.getVenueCost().multiply(new BigDecimal(getTotalCount())));
					type.setFinalCOST(type.getFinalCOST().multiply(new BigDecimal(getTotalCount())));
				} else {
					type.setVenueCost(type.getVenueCost());
					type.setFinalCOST(type.getFinalCOST());
				}

				// logger.debug("type.getVenueTypeId() --" +
				// type.getVenueTypeId());
				// logger.debug(Integer.parseInt(selectedVenueVO.getId()));
				if (type.getVenueTypeId() == Integer.parseInt(selectedVenueVO.getId())) {

					localVIList.add(type);

				}
                                
				vPkgDtls.put(type.getVenueId(), type);
//                                if(getAddress==false && type.getAreaName().equals(areaName))
//                                {
//                                    getAddress = true;
//                                    firstAddress = type.getAddress();
//                                    double lat = Double.valueOf(type.getLatitude());
//                                    double lng = Double.valueOf(type.getLongitude());
//                                    current = new LatitudeLongitude(lat, lng);
//                                }
			}
                        
                        Query arQry = em.createNamedQuery("AreaTypeMaster.findByAreaId");
                        arQry.setParameter("areaId", area);
                        AreaTypeMaster atm = (AreaTypeMaster) arQry.getSingleResult();
                        
                        System.out.println("area  "+area);
                        
                        double lat = Double.valueOf(atm.getLatitude());
                        double lng = Double.valueOf(atm.getLongitude());
                        current = new LatitudeLongitude(lat, lng);
                        
                            PlacesLatitudes sample = new PlacesLatitudes();
                            try 
                            {
//                                System.out.println("address "+firstAddress);
//                                localVIList = sample.performSearch(firstAddress+","+areaName+","+cityName,localVIList);
                                localVIList = sample.calculateDistances(current,localVIList);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
			//Collections.sort(localVIList, new VenueComparatorByAmount());
		} //else end
                } 
		// logger.debug("localVIList size -- " + localVIList.size());

		if ("Amount".equals(sortBy)) {
			Collections.sort(localVIList, new VenueComparatorByAmount());
		} else if ("Area".equals(sortBy)) {
                    
			Collections.sort(localVIList, new VenueComparatorByArea());
		} else if ("Name".equals(sortBy)){
			Collections.sort(localVIList, new VenueComparatorByName());
		}
		return localVIList;

		// } else {
		// return new ArrayList<ViewVenue>();
		// }

	}

	public String getAmount() {
		return amount;
	}

	public Integer getArea() {
		return area;
	}

	public String getChildcount() {
		return childcount;
	}

	public Integer getCity() {
		return city;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public Map getFinalServicesMap() {
		// logger.debug("finalServicesMap ******************  "+finalServicesMap);
		return finalServicesMap;
	}

	public ViewVenuePackage getFinalVenueInfo() {
		return finalVenueInfo;
	}

	public String getPopupServiceCode() {
		return popupServiceCode;
	}

	public String getPopupVenueId() {
		return popupVenueId;
	}

	ViewServiceProvider popupViewServiceProvider;

	public ViewServiceProvider setPopupViewServiceProvider(ViewServiceProvider popupViewServiceProvider) {
		this.popupViewServiceProvider = popupViewServiceProvider;
		return popupViewServiceProvider;
	}

	public ViewServiceProvider getPopupViewServiceProvider() {

		// logger.debug("getPopupViewServiceProvider :: popupServiceCode   -- "
		// + popupServiceCode);
		for (Iterator<ViewServiceProvider> iterator = viewServiceProviderList.iterator(); iterator.hasNext();) {
			ViewServiceProvider type = (ViewServiceProvider) iterator.next();
			if (String.valueOf(type.getServiceProviderCode()).equalsIgnoreCase(popupServiceCode)) {
				// logger.debug("getPopupViewServiceProvider   -"+type);
				return type;
			}
		}

		return new ViewServiceProvider();
	}

	public ViewVenue popupViewVenue;

	public ViewVenue getPopupViewVenue() {
		return popupViewVenue;
	}

	public void setPopupViewVenue(ViewVenue popupViewVenue) {
		this.popupViewVenue = popupViewVenue;
	}

	private int popVenueId;

	public int getPopVenueId() {
		return popVenueId;
	}

	public void setPopVenueId(int popVenueId) {
		this.popVenueId = popVenueId;
	}

	public List<String> hotelImagesSmall;

	public List<String> getHotelImagesSmall() {
		return hotelImagesSmall;
	}

	public void setHotelImagesSmall(List<String> hotelImagesSmall) {
		this.hotelImagesSmall = hotelImagesSmall;
	}

	public List<String> getHotelImagesSm(String venueId) {
		hotelImagesSmall = new ArrayList<String>();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		// String path = context.getInitParameter("imgpath");
		File dir = new File(path + "/venue/" + venueId);
		logger.debug("Directory! " + dir);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			String[] listFiles = dir.list();
			int len = dir.list().length;
			if (len == 0) {
				logger.debug("No images uploaded");
				hotelImagesSmall.add("images/noImage.gif");
			} else {
				// String url = context.getInitParameter("imgurl");

				for (int i = 0; i < len; i++) {
					logger.debug("listFiles[i] " + listFiles[i]);
					logger.debug("listFiles[i] " + list[i].getAbsolutePath());
					logger.debug("listFiles[i] " + list[i].getPath());
					if (!list[i].isDirectory()) {
						try {
							String str = "http://" + url + "/venue/" + venueId + "/" + listFiles[i];
							// File f = new File(str);
							// if(f.exists())
							URL url1 = new URL(str);
							HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
							huc.setRequestMethod("HEAD");
							int responseCode = huc.getResponseCode();

							if (responseCode == HttpURLConnection.HTTP_OK) {
								hotelImagesSmall.add(str);
							} else {
								hotelImagesSmall.add("images/noImage.gif");
							}
							// hotelImagesSmall.add("http://" + url + "/venue/"
							// + venueId + "/" + listFiles[i]);
						} catch (Exception ex) {
							java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
									Level.SEVERE, null, ex);
						}
					}
				}
			}
		} else {
			logger.debug("No directory found");
			hotelImagesSmall.add("images/noImage.gif");
		}
		return hotelImagesSmall;
	}

	public List<String> serviceImagesSmall;

	public List<String> getServiceImagesSmall() {
		return serviceImagesSmall;
	}

	public void setServiceImagesSmall(List<String> serviceImagesSmall) {
		this.serviceImagesSmall = serviceImagesSmall;
	}

	public List<String> getServiceImagesSm(String id) {
		logger.debug("ID SRVICE " + id);
		serviceImagesSmall = new ArrayList<String>();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		// String path = context.getInitParameter("imgpath");

		File dir = new File(path + "/vendors/" + id);
		logger.debug("Directory! " + dir);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			String[] listFiles = dir.list();
			int len = dir.list().length;
			if (len == 0) {
				logger.debug("No images uploaded");
				serviceImagesSmall.add("images/noImage.gif");
			} else {
				// String url = context.getInitParameter("imgurl");
				for (int i = 0; i < len; i++) {
					if (!list[i].isDirectory()) {
						try {
//							logger.debug("listFiles[i] " + listFiles[i]);
							String str = "http://" + url + "/vendors/" + id + "/" + listFiles[i];
							// File f = new File(str);
							URL url1 = new URL(str);
							HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
							huc.setRequestMethod("HEAD");
							int responseCode = huc.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								serviceImagesSmall.add(str);
							} else {
								serviceImagesSmall.add("images/noImage.gif");
							}

							// serviceImagesSmall.add("http://" + url +
							// "/vendors/" + id + "/" + listFiles[i]);
						} catch (Exception ex) {
							java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
									Level.SEVERE, null, ex);
						}
					}
				}
			}
		} else {
			logger.debug("No directory found");
			serviceImagesSmall.add("images/noImage.gif");
		}
		return serviceImagesSmall;
	}

	private List<VenueHallInfo> hallList;

	public List<VenueHallInfo> getHallList() {
		return hallList;
	}

	private int selHall = 0;

	public int getSelHall() {
		return selHall;
	}

	public void setSelHall(int selHall) {
		this.selHall = selHall;
	}

	private String hallId = "";

	public String getHallId() {
		return hallId;
	}

	public void setHallId(String hallId) {
		this.hallId = hallId;
	}

	Map hallMap = new HashMap();
	Map hallLocalMap = new HashMap();

	public Map getHallLocalMap() {
		return hallLocalMap;
	}

	public void setHallLocalMap(Map hallLocalMap) {
		this.hallLocalMap = hallLocalMap;
	}

	public Map getHallMap() {
		return hallMap;
	}

	public void setHallMap(Map hallMap) {
		this.hallMap = hallMap;
	}

	public void setHallList(List<VenueHallInfo> hallList) {
		this.hallList = hallList;
	}

	private int hallIndex = 0;

	public int getHallIndex() {
		return hallIndex;
	}

	public void setHallIndex(int hallIndex) {
		this.hallIndex = hallIndex;
	}

	public String selectedVenueVals(int venueId) {

		logger.debug("getPopupViewVenue :: popupVenueId b4sssa   -- " + popupVenueId);
		setPopupVenueId(String.valueOf(venueId));
		logger.debug("getPopupViewVenue :: popupVenueId   -- " + popupVenueId);
		logger.debug("getPopupViewVenue :: venueId   -- " + venueId);
		for (int j = 0; j < viewVenueList.size(); j++) {
			ViewVenue type = (ViewVenue) viewVenueList.get(j);
			logger.debug("popupViewVenue   -" + type.getVenueName());
			if (String.valueOf(type.getVenueId()).equalsIgnoreCase(popupVenueId)) {
				logger.debug("popupViewVenue id eq  -" + type.getVenueName());
				logger.debug("popupViewVenue id eq  -" + type.getVenueName());
				popupViewVenue = type;
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
				EntityManager em = emf.createEntityManager();
				Query query = em.createNamedQuery("VenueHallInfo.findByVenueIDActive");
				query.setParameter("venueID", type.getVenueId());
				query.setParameter("active", 'Y');
				hallList = query.getResultList();
				logger.debug("halllist " + hallList.size());
				hallMap = new HashMap();
				hallLocalMap = new HashMap();
				if (!hallList.isEmpty()) {
					hallIndex = hallList.get(0).getHallID();
					hallLocalMap.put(hallList.get(0).getHallID(), hallList.get(0));
					for (int js = 0; js < hallList.size(); js++) {
						VenueHallInfo vhi = hallList.get(js);
						hallMap.put(vhi.getHallID(), vhi.getHallName());
						hallLocalMap.put(vhi.getHallID(), vhi);
					}
					hallMap = new TreeMap<Integer, String>(hallMap);
					hallLocalMap = new TreeMap<Integer, VenueHallInfo>(hallLocalMap);
					hotelImages = new ArrayList<String>();
					hotelHallImages = new ArrayList<String>();
					ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
					// String path = context.getInitParameter("imgpath");
					hallId = String.valueOf(hallList.get(0).getHallID());
					File dir = new File(path + "/venue/" + type.getVenueId());
					srvcPrvdUrl = "/venue/" + type.getVenueId() + "/packages/";
					srvcPrvdPkgPath = path + "/venue/" + type.getVenueId() + "/packages/";
					File dirHall = new File(path + "/venue/" + type.getVenueId() + "/" + hallList.get(0).getHallID());
					logger.debug("Directory! " + dir);
					if (dir.exists()) {
						File[] list = dir.listFiles();
						String[] listFiles = dir.list();
						int len = dir.list().length;
						if (len == 0) {
							logger.debug("No images uploaded");
							hotelImages.add("images/noImage.gif");
						} else {
							// String url = context.getInitParameter("imgurl");
							for (int i = 0; i < len; i++) {
								logger.debug("listFiles[i] " + listFiles[i]);
								if (!list[i].isDirectory()) {
									try {
										String str = "http://" + url + "/venue/" + type.getVenueId() + "/"
												+ listFiles[i];
										URL url1 = new URL(str);
										HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
										huc.setRequestMethod("HEAD");
										int responseCode = huc.getResponseCode();
										if (responseCode == HttpURLConnection.HTTP_OK) {
											hotelImages.add(str);
										} else {
											hotelImages.add("images/noImage.gif");
										}
										// hotelImages.add("http://" + url +
										// "/venue/" + type.getVenueId() + "/"
										// + listFiles[i]);
									} catch (Exception ex) {
										java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName())
												.log(Level.SEVERE, null, ex);
									}
								}

							}
						}
					} else {
						logger.debug("No directory found");
						hotelImages.add("images/noImage.gif");
					}

					if (dirHall.exists()) {
						File[] listHall = dirHall.listFiles();
						String[] listHallFiles = dirHall.list();
						int lenj = dirHall.list().length;
						if (lenj == 0) {
							logger.debug("No images uploaded");
							hotelHallImages.add("images/noImage.gif");
						} else {
							// String url = context.getInitParameter("imgurl");
							for (int i = 0; i < lenj; i++) {
								logger.debug("listFiles[i] " + listHallFiles[i]);
								if (!listHall[i].isDirectory()) {
									try {
										String str = "http://" + url + "/venue/" + type.getVenueId() + "/"
												+ hallList.get(0).getHallID() + "/" + listHallFiles[i];
										URL url1 = new URL(str);
										HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
										huc.setRequestMethod("HEAD");
										int responseCode = huc.getResponseCode();
										if (responseCode == HttpURLConnection.HTTP_OK) {
											hotelHallImages.add(str);
										} else {
											hotelHallImages.add("images/noImage.gif");
										}
										// hotelHallImages.add("http://" + url +
										// "/venue/" + type.getVenueId() + "/"
										// + hallList.get(0).getHallID() + "/" +
										// listHallFiles[i]);
									} catch (Exception ex) {
										java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName())
												.log(Level.SEVERE, null, ex);
									}
								}
							}
							logger.debug(hotelHallImages);
						}
					} else {
						logger.debug("No directory found");
						hotelHallImages.add("images/noImage.gif");
					}

				}

				try {
					initMap(type.getVenueName() + "," + type.getAreaName() + "," + type.getCityName(),
							type.getVenueName());
				} catch (JsonSyntaxException ex) {
					ex.printStackTrace();
					java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(Level.SEVERE, null,
							ex);
				} catch (IOException ex) {
					ex.printStackTrace();
					java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(Level.SEVERE, null,
							ex);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
                
                List<ViewVenuePackage> vnpkgs = getVenuePackages();
		if (!vnpkgs.isEmpty()) {
			setVenuePkg(getVenuePackages().get(0));
		} else {
			setVenuePkg(null);
		}

		return "newVendor";

	}

	public String getRemainAmount() {
		BigDecimal usedAmount = totAmount;
		BigDecimal actAmount = new BigDecimal(amount);
		addAmnt = actAmount.subtract(usedAmount);
		logger.debug("actAmount " + actAmount + " usedAmount " + usedAmount);
		if (addAmnt.compareTo(BigDecimal.ZERO) == 1) {
			logger.debug("addAmnt " + addAmnt);
			addAmnt = BigDecimal.ZERO;
			return String.valueOf(actAmount.subtract(usedAmount));
		} else {
			return "0";
		}
	}

	BigDecimal addAmnt;
	private String addAmount;

	public String getAddAmount() {
		addAmount = String.valueOf(addAmnt.negate());
		return addAmount;
	}

	public void setAddAmount(String addAmount) {
		this.addAmount = addAmount;
	}

	public String getSelectedBCService() {

		if (null == selectedBCService || "".equals(selectedBCService)) {
			selectedBCService = "Venue";
		}

		return selectedBCService;
	}

	public String getNextBCService() {

		boolean isServiceFound = false;
		String nxtService = getSelectedBCService();
		List<ServiceVO> srvList = getSelectedSrvList();
		if ("Venue".equalsIgnoreCase(nxtService) && !srvList.isEmpty()) {
			return srvList.get(0).getHeading();
		} else if (!srvList.isEmpty()) {

			ServiceVO serviceVO = null;

			for (Iterator<ServiceVO> iterator = srvList.iterator(); iterator.hasNext();) {
				serviceVO = iterator.next();
				if (isServiceFound) {
					isServiceFound = false;
					return serviceVO.getHeading();
				}

				if (serviceVO.getHeading().equalsIgnoreCase(nxtService)) {
					isServiceFound = true;
				}
			}

			if (isServiceFound && null != serviceVO) {
				isServiceFound = false;
				return "Next";
			}
			logger.debug("isServiceFound" + isServiceFound);
		} else {
			return "Next";
		}

		return selectedBCService;
	}

	public List<ServiceVO> getSelectedSrvList() {
		return selectedSrvList;
	}

	public String getServiceHidden() {
		return serviceHidden;
	}

	public List<ServiceVO> getServiceList() {
		return serviceList;
	}

	public Map<String, String> getServiceMap() {
		return serviceMap;
	}

	public String getSortBy() {

		if (null == sortBy || "".equalsIgnoreCase(sortBy)) {
			return selectedBCService;
		} else {
			return sortBy;
		}
	}

	Integer tCount = 0;
	public String totalCount = "0";

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalCount() {
		tCount = Integer.parseInt(totalCount);
		return totalCount;
	}

	public String getUsedAmount() {
		logger.debug("Inside usedAmount***********!!!!!!!!!!!!!!!");
		BigDecimal totAmount1 = new BigDecimal(0);
		totAmount = new BigDecimal(0);
		for (int j = 0; j < finalServicesMap.size(); j++) {
			logger.debug("finalServicesMap Inside usedAmount***********!!!!!!!!!!!!!!!");
			ViewServiceProviderPackage vsProvider = (ViewServiceProviderPackage) finalServicesMap.values().toArray()[j];
//                        HashMap vsProviderhash = (HashMap) finalServicesMap.values().toArray()[j];
//                        ViewServiceProviderPackage vsProvider = (ViewServiceProviderPackage) vsProviderhash.values().toArray()[0];
			logger.debug(vsProvider.getFinalAmount() + " :: " + vsProvider.getServiceProviderName());
			if (vsProvider.getServiceTypeCode().equals("CATR")) {
				BigDecimal cost = (vsProvider.getFinalAmount()).multiply(new BigDecimal(tCount));
				totAmount = totAmount.add(cost);
			} else {
				// BigDecimal taxc = taxAmounts.get(vsProvider);
				// if(taxc==null)
				{
					totAmount = totAmount.add(vsProvider.getFinalAmount());
					// totAmount = totAmount.add(vsProvider.getCost());
				}
			}
		}

		if (null != finalVenueInfo) {
			logger.debug("finalVenueInfo Inside usedAmount***********!!!!!!!!!!!!!!!");
			// totAmount = totAmount.add(finalVenueInfo.getCost());
			totAmount = totAmount.add((finalVenueInfo.getFinalAmount()));
		}
		// totAmount = totAmount1;
		return totAmount.toString();
		// return String.valueOf(totAmount);

	}

	BigDecimal totAmount = new BigDecimal(0);

	public BigDecimal getTotAmount() {
		return totAmount;
	}

	public void setTotAmount(BigDecimal totAmount) {
		this.totAmount = totAmount;
	}

	public BigDecimal totalCharge() {

		BigDecimal totAmount1 = new BigDecimal(0);
		// if(servSet.size()>0)
		{
			// for (@SuppressWarnings("rawtypes")

			for (int j = 0; j < finalServicesMap.size(); j++) {
				BigDecimal tAmt;
				// @SuppressWarnings("rawtypes")
				// Integer key =
				// (Integer)finalServicesMap.keySet().toArray()[j];
				logger.debug("finalServicesMap Inside usedAmount***********!!!!!!!!!!!!!!!");
				ViewServiceProviderPackage vsProvider = (ViewServiceProviderPackage) finalServicesMap.values()
						.toArray()[j];
				logger.debug(vsProvider.getFinalAmount() + " :: " + vsProvider.getServiceProviderName());
				if (vsProvider.getServiceTypeCode().equals("CATR")) {
					// BigDecimal cost = vsProvider.getCost().multiply(new
					// BigDecimal(tCount));
					BigDecimal cost = (vsProvider.getFinalAmount()).multiply(new BigDecimal(tCount));
					tAmt = cost;
				} else {
					tAmt = vsProvider.getFinalAmount();
				}
				BigDecimal taxc = taxAmounts.get(vsProvider);
				if (taxc == null) {
					taxc = new BigDecimal(0);
				}

				totAmount1 = totAmount1.add(tAmt).add(taxc);
				// totAmount = totAmount.add(vsProvider.getCost());

			}
		}

		if (null != finalVenueInfo) {
			logger.debug("finalVenueInfo Inside usedAmount***********!!!!!!!!!!!!!!!");
			// totAmount = totAmount.add(finalVenueInfo.getCost());
			BigDecimal vTax;
			if (null != venuetaxService)
				vTax = venuetaxService.add(finalVenueInfo.getFinalAmount());
			else
				vTax = finalVenueInfo.getFinalAmount();
			totAmount1 = totAmount1.add(vTax);
		}
		totAmount = totAmount1;
		return totAmount;

	}

	private BigDecimal svCh = new BigDecimal(0);
	private BigDecimal svTax = new BigDecimal(0);

	public BigDecimal getSvCh() {
		return svCh;
	}

	public void setSvCh(BigDecimal svCh) {
		this.svCh = svCh;
	}

	public BigDecimal getServiceCharges() {
		BigDecimal servCh = new BigDecimal(0);
		BigDecimal eight = new BigDecimal(8);
		BigDecimal ONEHUNDERT = new BigDecimal(100);
		servCh = totAmount.multiply(eight).divide(ONEHUNDERT);
		svCh = servCh;
		return servCh;
	}

	public BigDecimal getServiceTaxes() {
		BigDecimal servCh = new BigDecimal(0);
		BigDecimal t = new BigDecimal(12.36);
		BigDecimal ONEHUNDERT = new BigDecimal(100);
		servCh = totAmount.multiply(t).divide(ONEHUNDERT);
		svTax = servCh.setScale(2, RoundingMode.FLOOR);
		;
		return svTax;
	}

	public BigDecimal getTotalAmountCharge() {
		return totAmount.add(svCh).add(svTax);
	}

	public List<VenueVO> getVenueList() {
		// logger.debug("venueList Inside venueList***********!!!!!!!!!!!!!!! "+venueList);
		return venueList;
	}

	public Map getVenueMap() {
		return venueMap;
	}

	public String getVenueRadio() {
		return venueRadio;
	}

	public void listenAdult(AjaxBehaviorEvent e) {
		InputText inputText = (InputText) e.getComponent();
		if ("".equals(inputText.getSubmittedValue())) {
			adultcount = "";
		}
	}

	public void listenChild(AjaxBehaviorEvent e) {
		InputText inputText = (InputText) e.getComponent();
		if ("".equals(inputText.getSubmittedValue())) {
			childcount = "";
		}
	}

	public void listenCityList(ValueChangeEvent e) {
		// logger.debug(e.getNewValue());
		city = (Integer) e.getNewValue();
		populateAreaList();
	}

	public void listenServiceChange(AjaxBehaviorEvent e) {
		// public void listenServiceChange() {
		// logger.debug(e.getBehavior());
		// logger.debug(e.getClass());
		//
		// logger.debug(e.getSource());
		// logger.debug(e.getComponent());
		logger.debug("serviceList -- listenServiceChange b4 listenServiceChange " + serviceList);
		disServiceTab = new ArrayList();
		HashMap tempMap = new LinkedHashMap();
		tempMap.putAll(allSelectedServicesMap);
		//
		logger.debug("selectedSrvList -- listenServiceChange b4 listenServiceChange " + selectedSrvList);
		logger.debug("tempMap -- listenServiceChange b4 listenServiceChange " + tempMap);
		for (ServiceVO dataItem : serviceList) {
			if (dataItem.isSelected()) {
				logger.debug("dataItem -- fgfg" + dataItem);
				if (!selectedSrvList.contains(dataItem)) {
					this.selectedSrvList.add(dataItem);
					// tempMap.put(dataItem, getServiceListDtls(dataItem));
				}

			}
		}

		List<ServiceVO> sortedAreaList = new LinkedList<ServiceVO>(selectedSrvList);
		Collections.sort(sortedAreaList, new ServiceVOComparator());
		Iterator<ServiceVO> atr = sortedAreaList.iterator();
		int j = 0;
		while (atr.hasNext()) {

			ServiceVO atm = atr.next();

			tempMap.put(atm, getServiceListDtls(atm));
			if (j == 0 && allVenueList.isEmpty()) {
				disServiceTab.add(false);
			} else {
				disServiceTab.add(true);
			}
			j++;

		}

		// allSelectedServicesMap = new HashMap() ;
		allSelectedServicesMap = tempMap;
		logger.debug("selectedSrvList -- listenServiceChange " + selectedSrvList);
		logger.debug("allSelectedServicesMap -- listenServiceChange " + allSelectedServicesMap);
		// setSelectedSrvList(selectedSrvList);
		// setAllSelectedServicesMap(this.allSelectedServicesMap);

	}

	public void listenServiceRemove(ServiceVO e, String vsp) {
		// logger.debug(e.getBehavior());
		// logger.debug(e.getClass());
		// logger.debug( e.getOldValue());
		// logger.debug(e.getSource());
		// logger.debug(e.getComponent());
		HashMap tempMap = new LinkedHashMap();
		tempMap.putAll(allSelectedServicesMap);
		//
		logger.debug("Remove selectedSrvList -- listenServiceChange b4 listenServiceChange " + selectedSrvList);
		logger.debug("tempMap -- listenServiceChange b4 listenServiceChange " + tempMap);

		// ServiceVO sv = (ServiceVO) e.getBehavior();

		// selectedSrvList.remove(e);
		finalServicesMap.remove(vsp);

		List<ServiceVO> sortedAreaList = new LinkedList<ServiceVO>(selectedSrvList);
		Collections.sort(sortedAreaList, new ServiceVOComparator());
		Iterator<ServiceVO> atr = sortedAreaList.iterator();
		while (atr.hasNext()) {
			ServiceVO atm = atr.next();

			tempMap.put(atm, getServiceListDtls(atm));

		}

		// allSelectedServicesMap = new HashMap() ;
		allSelectedServicesMap = tempMap;
		logger.debug("Remove selectedSrvList -- listenServiceChange " + selectedSrvList);
		logger.debug("Remove allSelectedServicesMap -- listenServiceChange " + allSelectedServicesMap);
		// setSelectedSrvList(selectedSrvList);
		// setAllSelectedServicesMap(this.allSelectedServicesMap);

	}

	public void listenVenueRemove(String venuetype, String vsp) {
		// logger.debug(e.getBehavior());
		// logger.debug(e.getClass());
		// logger.debug( e.getOldValue());
		// logger.debug(e.getSource());
		// logger.debug(e.getComponent());
		HashMap tempMap = new LinkedHashMap();
		// tempMap.putAll(allSelectedServicesMap);
		//
		logger.debug("Remove selectedSrvList -- listenServiceChange b4 listenServiceChange " + selectedSrvList);
		logger.debug("tempMap -- listenServiceChange b4 listenServiceChange " + tempMap);

		// ServiceVO sv = (ServiceVO) e.getBehavior();

		// selectedSrvList.remove(e);
		((HashMap) finalVenueMap.get(venuetype)).remove(vsp);
		finalVenueInfo = null;
		// List<ServiceVO> sortedAreaList = new
		// LinkedList<ServiceVO>(selectedSrvList);
		// Collections.sort(sortedAreaList, new ServiceVOComparator());
		// Iterator<ServiceVO> atr = sortedAreaList.iterator();
		// while (atr.hasNext()) {
		// ServiceVO atm = atr.next();
		//
		// tempMap.put(atm, getServiceListDtls(atm));
		//
		// }
		//
		// // allSelectedServicesMap = new HashMap() ;
		// allSelectedServicesMap = tempMap;
		// logger.debug("Remove selectedSrvList -- listenServiceChange " +
		// selectedSrvList);
		// logger.debug("Remove allSelectedServicesMap -- listenServiceChange "
		// + allSelectedServicesMap);
		// setSelectedSrvList(selectedSrvList);
		// setAllSelectedServicesMap(this.allSelectedServicesMap);

	}

	/************************* added on 1-6-2013 *****************************/
	private ArrayList allSelectedServices = new ArrayList();
	private HashMap allSelectedServicesMap = new HashMap();
	private HashMap serviceVOList = new HashMap();
	private String venueType;

	public HashMap getServiceVOList() {
		return serviceVOList;
	}

	public void setServiceVOList(HashMap serviceVOList) {
		this.serviceVOList = serviceVOList;
	}

	private int allSelSrvMapSize = 0;

	public int getAllSelSrvMapSize() {
		return allSelSrvMapSize;
	}

	public void setAllSelSrvMapSize(int allSelSrvMapSize) {
		this.allSelSrvMapSize = allSelSrvMapSize;
	}

	public Map getAllSelectedServicesMap() {
		// logger.debug("allSelectedServicesMap*******////////////////!!!!!!!!!!!1  "+allSelectedServicesMap);
		// getAllSelectedServices();
		serviceVOList = new HashMap();

		List keys = new ArrayList(allSelectedServicesMap.keySet());
		for (int j = 0; j < keys.size(); j++) {
			// disServiceTab.add(true);
			Object key = keys.get(j);
			// List localVIList = (List) allSelectedServicesMap.get(key);
			ServiceVO srv = (ServiceVO) key;
			serviceVOList.put(j + 1, key);
			List localVIList = (List) allSelectedServicesMap.get(key);
			Collections.sort(localVIList, new ServiceProviderComparatorByAmount());// getServiceListDtls(srv);
			if ("Amount".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByAmount());
			} else if ("Area".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByArea());
			} else if ("Name".equals(sortBy)){
				Collections.sort(localVIList, new ServiceProviderComparatorByName());
			}
			allSelectedServicesMap.put(key, localVIList);
		}
		allSelSrvMapSize = allSelectedServicesMap.size();
		// logger.debug("disServiceTab*******////////////////!!!!!!!!!!!1  "+disServiceTab);
		return allSelectedServicesMap;
	}

	public void setAllSelectedServicesMap(HashMap allSelectedServicesMap) {
		this.allSelectedServicesMap = allSelectedServicesMap;
	}
        
	public ArrayList getAllSelectedServices() {
		logger.debug("getAllSelectedServices*******  " + allSelectedServices);
		allSelectedServices = new ArrayList();
		// allSelectedServices.add(getAllVenueList());
		// getAllVenueList();
		// selectedSrvList = new ArrayList<ServiceVO>();
		//
		// for (ServiceVO dataItem : serviceList) {
		// if (dataItem.isSelected()) {
		// logger.debug("dataItem --" + dataItem);
		// selectedSrvList.add(dataItem);
		// // allSelectedServices.add(dataItem);
		// }
		// }
		logger.debug("selectedSrvList******* alllll  " + selectedSrvList);
		allSelectedServices.add(selectedSrvList);

		// allSelectedServices.add(getAllServicesList());

		// allSelectedServicesMap.put(selectedVenueVO, getAllVenueList());
		// for(int j=0;j<selectedSrvList.size();j++)
		// {
		// this.allSelectedServicesMap.put(selectedSrvList.get(j),
		// getServiceListDtls(selectedSrvList.get(j)));
		//
		// }

		// List mapValues = new ArrayList(allSelectedServicesMap.values());
		// List mapKeys = new ArrayList(selectedSrvList.keySet());
		// Collections.sort(mapValues);
		// Collections.sort(mapKeys);
		//
		LinkedHashMap sortedMap = new LinkedHashMap();
		//
		// Collections.sort(mapKeys, new ServiceVOComparator());
		// Iterator<AreaTypeMaster> atr = mapKeys.iterator();
		// while (atr.hasNext()) {
		// AreaTypeMaster atm = atr.next();
		// if (null != city && city == atm.getCityId().getCityId()) {
		// if (areaList.isEmpty()) {
		// setArea(atm.getAreaId());
		// }
		// // areaList.add(new SelectItem(atm.getAreaId(), atm.getAreaName()));
		// areaList.put(atm.getAreaName(), atm.getAreaId());
		// areaListLocal.put(atm.getAreaId(), atm.getAreaName());
		// // logger.debug(" Area  Name:" + atm.getAreaName() +
		// // " Area  Code:" + atm.getAreaId());
		// }
		// }
		disServiceTab = new ArrayList();
		List<ServiceVO> sortedAreaList = new LinkedList<ServiceVO>(selectedSrvList);
		Collections.sort(sortedAreaList, new ServiceVOComparator());
		Iterator<ServiceVO> atr = sortedAreaList.iterator();
		int j = 0;
		while (atr.hasNext()) {

			ServiceVO atm = atr.next();

			sortedMap.put(atm, getServiceListDtls(atm));
			if (j == 0 && allVenueList.isEmpty()) {
				disServiceTab.add(false);
			} else {
				disServiceTab.add(true);
			}

			j++;
		}

		allSelectedServicesMap = sortedMap;

		logger.debug("getAllSelectedServices------------  " + allSelectedServices);
		// allSelectedServices.add(allSelectedServicesMap);
		return allSelectedServices;
	}

	public void setAllSelectedServices(ArrayList allSelectedServices) {
		this.allSelectedServices = allSelectedServices;
	}

	public String getVenueType() {
		return venueType;
	}

	public void setVenueType(String venueType) {
		this.venueType = venueType;
	}

	/***************************************************************************/

	public void dChange(AjaxBehaviorEvent event) {
		Calendar date = (Calendar) event.getComponent();
		logger.debug("File Date: " + date);
		logger.debug("Hello... I am in DateChange");
		eventDate = (Date) date.getValue();
		logger.debug("File Date: " + eventDate);
		// nextScreen();
		setAllVenueList(getAllVenueList1());
		getAllSelectedServices();
	}

	public void detailsChange() {
            String ar = String.valueOf(areaListLocal.get(area));
		setAreaName(ar);

		String ct = String.valueOf(cityListLocal.get(city));
		setCityName(ct);
//              PlacesLatitudes sample = new PlacesLatitudes();
//                    try {
//                        nearby = sample.performSearch(ar+","+ct);
//                        
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
		setAllVenueList(getAllVenueList1());
		getAllSelectedServices();
	}

	public void resetVenue() {
		setAllVenueList(getAllVenueList1());
		finalVenueMap.clear();
		finalVenueInfo = null;
		disableCat();

	}
        
        private List nearby;

    public List getNearby() {
        return nearby;
    }

    public void setNearby(List nearby) {
        this.nearby = nearby;
    }

    private int venueTypeid;

    public int getVenueTypeid() {
        return venueTypeid;
    }

    public void setVenueTypeid(int venueTypeid) {
        this.venueTypeid = venueTypeid;
    }
    
	public String nextScreen() {
		logger.debug("###############################-- :::savePerson");
		logger.debug(" City --" + this.city);
		logger.debug(" Venue selected --" + venueRadio);
		logger.debug(" Service --" + this.selectedSrvList);

		logger.debug(" Venue Type venueMapLocal --" + venueMapLocal);
		logger.debug(" Venue Type venueMapLocal.get(venueRadio) --" + venueMapLocal.get(venueRadio.trim()));
		logger.debug(" Venue Type venueMapLocal.get(venueRadioint) --"
				+ venueMapLocal.get(Integer.parseInt(venueRadio.trim())));               
               
                
		String vt = String.valueOf(venueMapLocal.get(Integer.parseInt(venueRadio.trim())));
		setVenueType(vt);

		String ar = String.valueOf(areaListLocal.get(area));
		setAreaName(ar);

		String ct = String.valueOf(cityListLocal.get(city));
		setCityName(ct);
                
                
//                 PlacesLatitudes sample = new PlacesLatitudes();
//                    try {
//                        nearby = sample.performSearch(ar+","+ct);
//                        
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                logger.debug(" nearby --" + nearby);
		logger.debug(" Venue Type selected --" + this.venueType);
		// allSelectedServices = new ArrayList();
		// allSelectedServices.add(getAllVenueList());

		selectedSrvList = new ArrayList<ServiceVO>();

		for (ServiceVO dataItem : serviceList) {
			if (dataItem.isSelected()) {
				logger.debug("dataItem --" + dataItem);
				selectedSrvList.add(dataItem);
				// allSelectedServices.add(dataItem);
			}
		}

		logger.debug(" Service selectedSrvList--" + selectedSrvList);

		if (selectedSrvList.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"Please Select atleast once service");
			FacesContext.getCurrentInstance().addMessage("frm:serviceHidden", message);
			return "event-details";
		}
		setAllVenueList(getAllVenueList1());
		getAllSelectedServices();

		return "event-details-summary";
	}

	private void populateAreaList() {
		areaList = new LinkedHashMap();

		List<AreaTypeMaster> sortedAreaList = new LinkedList<AreaTypeMaster>(lAreaList);
		Collections.sort(sortedAreaList, new AreaTypeComparatoreByAreaName());
		Iterator<AreaTypeMaster> atr = sortedAreaList.iterator();
		while (atr.hasNext()) {
			AreaTypeMaster atm = atr.next();
			if (null != city && city == atm.getCityId().getCityId()) {
				if (areaList.isEmpty()) {
					setArea(atm.getAreaId());
				}
				// areaList.add(new SelectItem(atm.getAreaId(),
				// atm.getAreaName()));
				areaList.put(atm.getAreaName(), atm.getAreaId());
				areaListLocal.put(atm.getAreaId(), atm.getAreaName());
				// logger.debug(" Area  Name:" + atm.getAreaName() +
				// " Area  Code:" + atm.getAreaId());
			}
		}
	}

	private void populateMasterData() {
		// cityList = new ArrayList<SelectItem>();
		cityList = new LinkedHashMap();

		List<CityTypeMaster> sortedCityList = new LinkedList<CityTypeMaster>(list);

		Collections.sort(sortedCityList, new CityTypeComparatoreByCityName());

		Iterator<CityTypeMaster> itr = sortedCityList.iterator();
		while (itr.hasNext()) {
			CityTypeMaster emp = itr.next();
			// cityList.add(new SelectItem(emp.getCityId(), emp.getCityName()));
			if (emp.getCityId() == 1 || emp.getCityId() == 6) // restricting
																// cities to hyd
																// and secbad
			{
				cityList.put(emp.getCityName(), emp.getCityId());
				cityListLocal.put(emp.getCityId(), emp.getCityName());
				if (null == city || city == 0) {
					city = emp.getCityId();
				}
			}
		}
		populateAreaList();
		populateVenueList();
		populateServiceList();
	}

	private void populateServiceList() {
		logger.debug("Inside populateServiceList");
		if (serviceList.isEmpty()) {
			serviceList = new ArrayList<ServiceVO>();
			Iterator<ServiceTypeMaster> atr = serviceTypeMaster.iterator();
			while (atr.hasNext()) {
				ServiceTypeMaster atm = atr.next();
				serviceList.add(new ServiceVO(String.valueOf(atm.getServiceTypeCode()), "images/event-img3.jpg", atm
						.getServiceName(), atm.getServiceDescription()));
				// logger.debug(" Service  Name:" + atm.getServiceTypeCode());
				// serviceMap.put(atm.getServiceName(), atm.getServiceName());
			}
		}
	}

	private void populateVenueList() {
		venueList = new ArrayList<VenueVO>();
		Iterator<VenueTypeMaster> atr = venueTypeMaster.iterator();
		while (atr.hasNext()) {
			VenueTypeMaster atm = atr.next();
			venueList.add(new VenueVO(String.valueOf(atm.getVenueTypeId()), "images/event-img3.jpg",
					atm.getVenueType(), atm.getVenueDescription()));
			venueMap.put(atm.getVenueType(), atm.getVenueTypeId());
			venueMapLocal.put(atm.getVenueTypeId(), atm.getVenueType());
		}

		// venueMap = new LinkedHashMap();

		List mapValues = new ArrayList(venueMap.values());
		List mapKeys = new ArrayList(venueMap.keySet());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				int comp1 = Integer.parseInt(venueMap.get(key).toString());
				int comp2 = Integer.parseInt(val.toString());
				//
				// logger.debug("omp1 "+comp1);
				// logger.debug("omp2 "+comp2);

				if (comp1 == comp2) {
					venueMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (Integer) val);
					break;
				}

			}

		}

		venueMap = sortedMap;

		logger.debug("venueList :: venueList   -- " + venueList);
		logger.debug("venueMap :: venueMap   -- " + venueMap);
	}

	public ServiceVO selService;

	public ServiceVO getSelService() {
		return selService;
	}

	public void setSelService(ServiceVO selService) {
		this.selService = selService;
	}

	private boolean chkServicePop = false;

	public boolean isChkServicePop() {
		return chkServicePop;
	}

	public void setChkServicePop(boolean chkServicePop) {
		this.chkServicePop = chkServicePop;
	}

	public String popupService(ServiceVO sv, String serviceProviderCode) {
		logger.debug("popupService :: serviceProviderCode   -- " + serviceProviderCode);
		popupServiceCode = serviceProviderCode;
		chkServicePop = true;
		setSelService(sv);
		servicePkgImages = new ArrayList<String>();
		File dirPkgSrv = null;
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		for (Iterator<ViewServiceProvider> iterator = viewServiceProviderList.iterator(); iterator.hasNext();) {
			ViewServiceProvider type = (ViewServiceProvider) iterator.next();
			if (String.valueOf(type.getServiceProviderCode()).equalsIgnoreCase(popupServiceCode)) {
				logger.debug("getPopupViewServiceProvider   -" + type);
				popupViewServiceProvider = type;
				setServiceProviderId(type.getServiceProviderId());

				servicesImages = new ArrayList<String>();

				// String path = context.getInitParameter("imgpath");

				File dir = new File(path + "/vendors/" + type.getServiceProviderId());
				srvcPrvdUrl = "/vendors/" + type.getServiceProviderId() + "/packages/";
				srvcPrvdPkgPath = path + "/vendors/" + type.getServiceProviderId() + "/packages/";
				logger.debug("Directory! " + dir);
				if (dir.exists()) {
					File[] list = dir.listFiles();
					String[] listFiles = dir.list();
					int len = dir.list().length;
					if (len == 0) {
						logger.debug("No images uploaded");
						servicesImages.add("images/noImage.gif");
					} else {
						// String url = context.getInitParameter("imgurl");
						for (int i = 0; i < len; i++) {
							if (!list[i].isDirectory()) {
								try {
									logger.debug("listFiles[i] " + listFiles[i]);
									String str = "http://" + url + "/vendors/" + type.getServiceProviderId() + "/"
											+ listFiles[i];
									// File f = new File(str);
									URL url1 = new URL(str);
									HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
									huc.setRequestMethod("HEAD");
									int responseCode = huc.getResponseCode();
									if (responseCode == HttpURLConnection.HTTP_OK) {
										servicesImages.add(str);
									} else {
										servicesImages.add("images/noImage.gif");
									}

									// serviceImagesSmall.add("http://" + url +
									// "/vendors/" + id + "/" + listFiles[i]);
								} catch (Exception ex) {
									java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
											Level.SEVERE, null, ex);
								}
								logger.debug("listFiles[i] " + listFiles[i]);
								// servicesImages.add("http://" + url +
								// "/vendors/"
								// + type.getServiceProviderId() + "/"
								// + listFiles[i]);
							}
						}
					}
				} else {
					logger.debug("No directory found");
					servicesImages.add("images/noImage.gif");
				}

				try {
					initMap(type.getServiceProviderName() + "," + type.getAreaName() + "," + type.getCityName(),
							type.getServiceProviderName());
				} catch (JsonSyntaxException ex) {
					ex.printStackTrace();
					java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(Level.SEVERE, null,
							ex);
				} catch (IOException ex) {
					ex.printStackTrace();
					java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(Level.SEVERE, null,
							ex);
				}

				break;
			}

		}
		if (!getServicePackges().isEmpty()) {
			ViewServiceProviderPackage srvProviderPkg = servicePackges.get(0);
			setSrvcpkg(srvProviderPkg);
			logger.debug("Printing the packages path:%%%%% " + srvcPrvdPkgPath);
			dirPkgSrv = new File(srvcPrvdPkgPath + srvProviderPkg.getPackageId());
			// String url = context.getInitParameter("imgurl");
			if (dirPkgSrv.exists()) {
				String[] listFiles = dirPkgSrv.list();
				File[] list = dirPkgSrv.listFiles();
				int len = dirPkgSrv.list().length;
				if (len == 0) {
					logger.debug("No images uploaded");
					servicePkgImages.add("images/noImage.gif");
				} else {
					for (int i = 0; i < len; i++) {
						if (!list[i].isDirectory()) {
							try {
								logger.debug("PkgSrvcPath: " + dirPkgSrv + " listFiles[i] " + listFiles[i]);
								String str = "http://" + url + srvcPrvdUrl + srvProviderPkg.getPackageId() + "/"
										+ listFiles[i];
								// File f = new File(str);
								URL url1 = new URL(str);
								HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
								huc.setRequestMethod("HEAD");
								int responseCode = huc.getResponseCode();
								if (responseCode == HttpURLConnection.HTTP_OK) {
									servicePkgImages.add(str);
								} else {
									servicePkgImages.add("images/noImage.gif");
								}
								// serviceImagesSmall.add("http://" + url +
								// "/vendors/" + id + "/" + listFiles[i]);
							} catch (Exception ex) {
								java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
										Level.SEVERE, null, ex);
							}
						}
						// servicePkgImages.add("http://" + url + srvcPrvdUrl +
						// srvProviderPkg.getPackageId() + "/"
						// + listFiles[i]);
					}
					logger.debug(servicePkgImages);
				}
			} else {
				logger.debug("No directory found");
				servicePkgImages.add("images/noImage.gif");
			}

		} else {
			setPackDesc(null);
		}
		return "eventService";

	}

	public void popupVenue(String venueId) {
		// logger.debug("popupVenue :: venueId   -- " + venueId);
		popupVenueId = venueId;
	}

	/*
	 * public void selectedFinalService(String serviceId) {
	 * logger.debug("selectedFinalService :: serviceId   -- " + serviceId);
	 * 
	 * for (Iterator<ViewServiceProvider> iterator =
	 * viewServiceProviderList.iterator(); iterator.hasNext();) {
	 * ViewServiceProvider type = (ViewServiceProvider) iterator.next();
	 * logger.debug("type.getVenueTypeId() --" + type.getServiceProviderCode());
	 * // logger.debug(Integer.parseInt(selectedVenueVO.getId())); if
	 * (type.getServiceProviderCode().equalsIgnoreCase(serviceId)) {
	 * logger.debug("type -- " + type);
	 * finalServicesMap.put(type.getServiceTypeCode(), type); break; } }
	 * logger.debug(finalServicesMap); }
	 */

	public String selectedFinalServicePackage(int packageId) {
		logger.debug("selectedFinalServicePackage :: packageId   -- " + packageId);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery("ViewServiceProviderPackage.findByPackageId");
		query.setParameter("packageId", new Integer(packageId).intValue());
		ViewServiceProviderPackage vsppackage = (ViewServiceProviderPackage) query.getSingleResult();
		finalServicesMap.put(vsppackage.getServiceTypeCode(), vsppackage);

		logger.debug(finalServicesMap);

		return "event-details-summary";
	}

	public void selectedFinalServicePopup(ServiceVO sv, String serviceProviderCode) {
		logger.debug("serviceProviderCode :: serviceProviderCode   -- " + serviceProviderCode);
		// logger.debug("selectedFinalVenue :: hallid   -- " + hallId);
		// selHall = Integer.parseInt(hallId);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();

		// Query query = em.createNamedQuery("VenuePackageInfo.findByPkgId");
		// Query query =
		// em.createNamedQuery("VenuePackageInfo.findByPackageId");
		// Query query =
		// em.createNamedQuery("ViewVenuePackage.findByPackageId");
		Query query = em
				.createQuery("SELECT v FROM ViewServiceProviderPackage v WHERE v.serviceProviderCode = :serviceProviderCode order by v.packageId");
		query.setParameter("serviceProviderCode", serviceProviderCode);

		// VenuePackageInfo vpInfo = (VenuePackageInfo) query.getSingleResult();
		ViewServiceProviderPackage vsppackage = (ViewServiceProviderPackage) query.getResultList().get(0);

		Query query1 = em.createNamedQuery("ServiceProviderInfo.findByServiceProviderId");
		// query1.setParameter("venueId", vpInfo.getVenueId());
		query1.setParameter("serviceProviderId", vsppackage.getServiceProviderId());
		ServiceProviderInfo vInfo = (ServiceProviderInfo) query1.getResultList().get(0);

		finalServicesMap.put(vsppackage.getServiceTypeCode(), vsppackage);

	}

	public void selectedFinalVenue(int venueId) {
		logger.debug("selectedFinalVenue :: venueId   -- " + venueId);

		// for (Iterator<ViewVenue> iterator = viewVenueList.iterator();
		// iterator.hasNext();) {
		// ViewVenue type = (ViewVenue) iterator.next();
		// logger.debug("type.getVenueTypeId() --" +
		// type.getVenueTypeId());
		// logger.debug(Integer.parseInt(selectedVenueVO.getId()));

		// if (type.getVenueId() == Integer.parseInt(venueId)) {
		// setFinalVenueInfo(type);
		// break;
		// }
		// }
	}

	public void selectFinalVenueBookNow(int venueId) {
		logger.debug("selectedFinalVenue :: venueId   -- " + venueId);
		logger.debug("selectedFinalVenue :: hallid   -- " + hallId);
		// selHall = Integer.parseInt(hallId);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();

		// Query query = em.createNamedQuery("VenuePackageInfo.findByPkgId");
		// Query query =
		// em.createNamedQuery("VenuePackageInfo.findByPackageId");
		// Query query =
		// em.createNamedQuery("ViewVenuePackage.findByPackageId");
		Query query = em
				.createQuery("SELECT v FROM ViewVenuePackage v WHERE v.venueId = :venueId order by v.packageId");
		query.setParameter("venueId", venueId);

		// VenuePackageInfo vpInfo = (VenuePackageInfo) query.getSingleResult();
		ViewVenuePackage vpInfo = (ViewVenuePackage) query.getResultList().get(0);

		Query query1 = em.createNamedQuery("VenueInfo.findByVenueId");
		// query1.setParameter("venueId", vpInfo.getVenueId());
		query1.setParameter("venueId", venueId);
		VenueInfo vInfo = (VenueInfo) query1.getResultList().get(0);

		Query query12 = em.createNamedQuery("VenueHallInfo.findByVenueID");
		// query1.setParameter("venueId", vpInfo.getVenueId());
		query12.setParameter("venueID", venueId);
		VenueHallInfo vhInfo = (VenueHallInfo) query12.getResultList().get(0);

		selHall = vhInfo.getHallID();

		// VenueInfo vInfo = vpInfo.getVenueId();
		// VenueInfo vInfo = new VenueInfo(vpInfo.getVenueId());

		// Query query2 =
		// em.createNamedQuery("VenueTypeMaster.findByVenueType");
		// query2.setParameter("venueTypeId", vInfo.getVenueTypeId());
		// Query query2 =
		// em.createNamedQuery("VenueTypeMaster.findByVenueTypeId");
		// query2.setParameter("venueTypeId",
		// vInfo.getVenueTypeId().getVenueTypeId());
		// VenueTypeMaster vtypeInfo = (VenueTypeMaster)
		// query2.getSingleResult();
		logger.debug("vInfo****** " + vInfo.getVenueTypeId());
		VenueTypeMaster vtypeInfo = vInfo.getVenueTypeId();
		if (vInfo.getVenueTypeId().getVenueTypeId() == 3) {
			vpInfo.setCost(vpInfo.getCost().multiply(new BigDecimal(getTotalCount())));
			vpInfo.setFinalAmount(vpInfo.getFinalAmount().multiply(new BigDecimal(getTotalCount())));
		}

		setFinalVenueInfo(vpInfo);
		// finalServicesMap.put(vpInfo.getVenueId(), vpInfo);

		HashMap venueDtls = new HashMap();
		venueDtls.put(vInfo.getVenueName(), vpInfo);
		finalVenueMap.put(vtypeInfo.getVenueType(), venueDtls);

		logger.debug("finalVenueMap$$$$$$$$$$$$$$$$$$$$$$$  " + finalVenueMap);

	}

	public String selectedFinalVenuePopup(int packageId) {
		logger.debug("selectedFinalVenue :: packageId   -- " + packageId);
		logger.debug("selectedFinalVenue :: hallid   -- " + hallId);
		selHall = Integer.parseInt(hallId);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();

		// Query query = em.createNamedQuery("VenuePackageInfo.findByPkgId");
		// Query query =
		// em.createNamedQuery("VenuePackageInfo.findByPackageId");
		Query query = em.createNamedQuery("ViewVenuePackage.findByPackageId");
		query.setParameter("packageId", Integer.valueOf(packageId).intValue());

		// VenuePackageInfo vpInfo = (VenuePackageInfo) query.getSingleResult();
		ViewVenuePackage vpInfo = (ViewVenuePackage) query.getSingleResult();

		Query query1 = em.createNamedQuery("VenueInfo.findByVenueId");
		// query1.setParameter("venueId", vpInfo.getVenueId());
		query1.setParameter("venueId", vpInfo.getVenueId());
		VenueInfo vInfo = (VenueInfo) query1.getSingleResult();

		// VenueInfo vInfo = vpInfo.getVenueId();
		// VenueInfo vInfo = new VenueInfo(vpInfo.getVenueId());

		// Query query2 =
		// em.createNamedQuery("VenueTypeMaster.findByVenueType");
		// query2.setParameter("venueTypeId", vInfo.getVenueTypeId());
		// Query query2 =
		// em.createNamedQuery("VenueTypeMaster.findByVenueTypeId");
		// query2.setParameter("venueTypeId",
		// vInfo.getVenueTypeId().getVenueTypeId());
		// VenueTypeMaster vtypeInfo = (VenueTypeMaster)
		// query2.getSingleResult();
		logger.debug("vInfo****** " + vInfo.getVenueTypeId());
		VenueTypeMaster vtypeInfo = vInfo.getVenueTypeId();
		if (vInfo.getVenueTypeId().getVenueTypeId() == 3) {
			vpInfo.setCost(vpInfo.getCost().multiply(new BigDecimal(getTotalCount())));
			vpInfo.setFinalAmount(vpInfo.getFinalAmount().multiply(new BigDecimal(getTotalCount())));
		}

		setFinalVenueInfo(vpInfo);
		// finalServicesMap.put(vpInfo.getVenueId(), vpInfo);

		HashMap venueDtls = new HashMap();
		venueDtls.put(vInfo.getVenueName(), vpInfo);
		finalVenueMap.put(vtypeInfo.getVenueType(), venueDtls);

		logger.debug("finalVenueMap$$$$$$$$$$$$$$$$$$$$$$$  " + finalVenueMap);
		return "event-details-summary";

	}

	public BigDecimal getVenueServiceCost(ViewVenuePackage vpin) {
		BigDecimal tax = null;

		logger.debug("UnitOfCost::::" + vpin.getVenueId());
		try {
			/*
			 * EntityManagerFactory emf =
			 * Persistence.createEntityManagerFactory("ROOT"); EntityManager em
			 * = emf.createEntityManager(); String qry =
			 * "SELECT t FROM TaxMaster t WHERE t.vendorId = :vendorId and t.vendorType = :vendorType"
			 * ; Query query = em.createQuery(qry);
			 * query.setParameter("vendorId", vpin.getVenueId());
			 * query.setParameter("vendorType", 'V'); List pkgList =
			 * query.getResultList(); if(pkgList.isEmpty()) {
			 * logger.debug("empty "); tax=new BigDecimal("0"); } else {
			 * logger.debug("empty not"); TaxMaster taxMaster = (TaxMaster)
			 * pkgList.get(0); tax = new BigDecimal(taxMaster.getTax());
			 * logger.debug("empty not "+taxMaster.getTax());
			 * 
			 * }
			 */
			if (vpin.getTax() == null) {
				logger.debug("empty ");
				tax = new BigDecimal("0");
			} else {
				logger.debug("empty not");
				//tax = new BigDecimal(vpin.getTax());
                                tax = vpin.getTax();
			}

			BigDecimal fAmt = (vpin.getFinalAmount());
			BigDecimal ONEHUNDERT = new BigDecimal(100);
			venuetaxService = fAmt.multiply(tax).divide(ONEHUNDERT);

			BigDecimal tAmt = fAmt.add(venuetaxService);
			logger.debug("fAmt:: " + fAmt + "  tAmt:: " + tAmt);
			// serviceProvider.setFinalAmount(tAmt.toString());
		} catch (Exception e) {
			logger.debug("Exception " + e.toString());
			e.printStackTrace();

		} finally {

		}

		return venuetaxService;
	}

	public HashMap getFinalVenueMap() {
		// logger.debug("finalVenueMap***************$$$$$$$$$$  "+finalVenueMap);
		return finalVenueMap;
	}

	public void setFinalVenueMap(HashMap finalVenueMap) {
		this.finalVenueMap = finalVenueMap;
	}

	private VenueInfo summaryVenueInfo;

	public VenueInfo getSummaryVenueInfo() {
		return summaryVenueInfo;
	}

	public void setSummaryVenueInfo(VenueInfo summaryVenueInfo) {
		this.summaryVenueInfo = summaryVenueInfo;
	}

	public VenueInfo getAddress(int venueId) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery("VenueInfo.findByVenueId");
		query.setParameter("venueId", venueId);
		summaryVenueInfo = (VenueInfo) query.getSingleResult();
		return summaryVenueInfo;
	}

	private ServiceProviderInfo summarySpInfo;

	public ServiceProviderInfo getSummarySpInfo() {
		return summarySpInfo;
	}

	public void setSummarySpInfo(ServiceProviderInfo summarySpInfo) {
		this.summarySpInfo = summarySpInfo;
	}

	public ServiceProviderInfo getSpAddress(int venueId) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery("ServiceProviderInfo.findByServiceProviderId");
		query.setParameter("serviceProviderId", venueId);
		summarySpInfo = (ServiceProviderInfo) query.getSingleResult();
		return summarySpInfo;
	}

	public void selectVenueBC(String inputString) {
		// logger.debug("inputString ---" + inputString);
		// if ("next".equalsIgnoreCase(inputString)) {
		// inputString = "Venue";
		// }

		this.selectedBCService = inputString;
		this.getAllVenueList();
		this.getAllServicesList();

	}

	public void setAdultcount(String adultcount) {
		this.adultcount = adultcount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public void setChildcount(String childcount) {
		this.childcount = childcount;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public void setFinalServicesMap(HashMap finalServicesMap) {
		this.finalServicesMap = finalServicesMap;
	}

	public void setFinalVenueInfo(ViewVenuePackage finalVenueInfo) {
		this.finalVenueInfo = finalVenueInfo;
	}

	public void setPopupServiceCode(String popupServiceCode) {
		this.popupServiceCode = popupServiceCode;
	}

	public void setPopupVenueId(String popupVenueId) {
		this.popupVenueId = popupVenueId;
	}

	public void setSelectedBCService(String selectedBCService) {
		this.selectedBCService = selectedBCService;
	}

	public void setSelectedSrvList(List<ServiceVO> selectedSrvList) {
		this.selectedSrvList = selectedSrvList;
	}

	public void setServiceHidden(String serviceHidden) {
		this.serviceHidden = serviceHidden;
	}

	public void setServiceList(List<ServiceVO> serviceList) {
		this.serviceList = serviceList;
	}

	public void setServiceMap(Map<String, String> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public void setVenueList(List<VenueVO> venueList) {
		this.venueList = venueList;
	}

	public void setVenueMap(Map venueMap) {
		this.venueMap = venueMap;
	}

	public void setVenueRadio(String venueRadio) {
		this.venueRadio = venueRadio;
	}

	private BigDecimal venuetaxService;

	public BigDecimal getVenuetaxService() {
		return venuetaxService;
	}

	public void setVenuetaxService(BigDecimal venuetaxService) {
		this.venuetaxService = venuetaxService;
	}

	private BigDecimal taxService;

	public BigDecimal getTaxService() {
		return taxService;
	}

	public void setTaxService(BigDecimal taxService) {
		this.taxService = taxService;
	}

	private BigDecimal pCost;

	public BigDecimal getpCost() {
		return pCost;
	}

	public void setpCost(BigDecimal pCost) {
		this.pCost = pCost;
	}

	public String getProjectedCost(BigDecimal cost, String strUnitOfCost) {
		// BigDecimal pCost;
		logger.debug("UnitOfCost::::" + strUnitOfCost);
		if ("PER PLATE".equalsIgnoreCase(strUnitOfCost.trim()) || "PER HEAD".equalsIgnoreCase(strUnitOfCost.trim())) {
			logger.debug("inside perplate::::" + cost);
			logger.debug("inside perplate:::: " + cost.multiply(new BigDecimal(getTotalCount())));
			pCost = cost.multiply(new BigDecimal(getTotalCount()));

		} else {
			logger.debug("else unite::::" + strUnitOfCost);
			pCost = cost;
		}
		// taxService =
		return pCost.stripTrailingZeros().toPlainString();
	}

	public String getCost(BigDecimal cost) {
		logger.debug("In Get Cost method.......");
		return cost.stripTrailingZeros().toPlainString();
	}

	HashMap<ViewServiceProviderPackage, BigDecimal> taxAmounts = new HashMap();

	public BigDecimal getTotalServiceCost(ViewServiceProviderPackage serviceProvider) {
		BigDecimal tax = null;
		logger.debug("UnitOfCost::::" + serviceProvider.getServiceProviderId());
		try {
			/*
			 * EntityManagerFactory emf =
			 * Persistence.createEntityManagerFactory("ROOT"); EntityManager em
			 * = emf.createEntityManager(); String qry =
			 * "SELECT t FROM TaxMaster t WHERE t.vendorId = :vendorId and t.vendorType = :vendorType"
			 * ; Query query = em.createQuery(qry);
			 * query.setParameter("vendorId",
			 * serviceProvider.getServiceProviderId());
			 * query.setParameter("vendorType", 'O'); List pkgList =
			 * query.getResultList(); if(pkgList.isEmpty()) {
			 * logger.debug("empty "); tax=new BigDecimal("0"); } else {
			 * logger.debug("empty not"); TaxMaster taxMaster = (TaxMaster)
			 * pkgList.get(0); tax = new BigDecimal(taxMaster.getTax());
			 * logger.debug("empty not "+taxMaster.getTax());
			 * 
			 * }
			 */

			// if("PER PLATE".equalsIgnoreCase(serviceProvider.getUnitOfCost().trim()))
			// {
			// logger.debug("inside perplate::::"+cost);
			// logger.debug("inside perplate:::: "+cost.multiply(new
			// BigDecimal(getTotalCount())));
			// pCost = cost.multiply(new BigDecimal(getTotalCount()));
			//
			// }
			// else
			// {
			// pCost = cost;
			// }
			if (serviceProvider.getTax() == null) {
				logger.debug("empty ");
				tax = new BigDecimal("0");
			} else {
				//tax = new BigDecimal(serviceProvider.getTax());
                            tax = serviceProvider.getTax();
			}

			BigDecimal ONEHUNDERT = new BigDecimal(100);
			taxService = pCost.multiply(tax).divide(ONEHUNDERT);

			BigDecimal fAmt = pCost;// new
									// BigDecimal(serviceProvider.getFinalAmount());
			BigDecimal tAmt = fAmt.add(taxService);
			taxAmounts.put(serviceProvider, taxService);
			logger.debug("fAmt:: " + fAmt + "  tAmt:: " + tAmt);
			// serviceProvider.setFinalAmount(tAmt.toString());
		} catch (Exception e) {
			logger.debug("Exception " + e.toString());
			e.printStackTrace();

		} finally {

		}

		return taxService;
	}

	public String onFlowProcess(FlowEvent event) {
		logger.debug("selectedBCService b4 " + selectedBCService);
		// logger.debug("getNextBCService() "+nextBCService);

		try {
			// if(event.)
			this.selectedBCService = getNextBCService();
			logger.debug("finalServicesMap---------- " + finalServicesMap);
			logger.debug("finalVenueMap$$$$$$$$$$$$$$$$$$$$$$$  " + finalVenueMap);
			// allSelectedServices.set(1, getAllServicesList());
		} catch (Exception e) {
			logger.debug("Exception " + e.toString());
			e.printStackTrace();

		}

		logger.debug("selectedBCService after " + selectedBCService);
		return event.getNewStep();
	}

	// private List<VenuePackageInfo> venuePackages;
	private List<ViewVenuePackage> venuePackages;

	public void setVenuePackages(List<ViewVenuePackage> venuePackages) {
		this.venuePackages = venuePackages;
	}

	public List<ViewVenuePackage> getVenuePackages() {

		logger.debug("getVenuePackges:1::venueId" + popupVenueId);
		// List<VenuePackageInfo> list;

		VenueInfo vi = new VenueInfo(Integer.parseInt(popupVenueId));

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			// Query query =
			// em.createNamedQuery("VenuePackageInfo.findByVenueId");
//			Query query = em.createNamedQuery("ViewVenuePackage.findByVenueId");
                        Query query = em.createNamedQuery("ViewVenuePackage.findByVenueIdEType");
			query.setParameter("venueId", Integer.parseInt(popupVenueId));
			query.setParameter("eventType", eventTypeId);

			venuePackages = query.getResultList();

		} finally {

		}

		// logger.debug("getVenuePackges:2::packageId" + packageId);
		return venuePackages;
	}

	private int serviceProviderId = 0;

	public int getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(int serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	// private List<PackageInfo> servicePackges;
	private List<ViewServiceProviderPackage> servicePackges;

	public void setServicePackges(List<ViewServiceProviderPackage> servicePackges) {
		this.servicePackges = servicePackges;
	}

	public List<ViewServiceProviderPackage> getServicePackges() {
		// serviceProviderId = "SP06";
		logger.debug("ServiceDetailsManagedBean::::serviceProviderId::::" + serviceProviderId);
		List<ViewServiceProviderPackage> list;

		// if(serviceProviderId!=0)
		{

			try {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
				EntityManager em = emf.createEntityManager();
				//Query query = em.createNamedQuery("ViewServiceProviderPackage.findByServiceProviderId");
                                Query query = em.createNamedQuery("ViewServiceProviderPackage.findBySrvPrvdIdEType");
				ServiceProviderInfo spInfo = new ServiceProviderInfo(new Integer(serviceProviderId).intValue());
				query.setParameter("serviceProviderId", serviceProviderId);
                                query.setParameter("eventType", eventTypeId);

				// query.setParameter("sProviderId", new
				// Integer(serviceProviderId).intValue());

				servicePackges = query.getResultList();
				logger.debug("ServiceDetailsManagedBean::::list::::" + servicePackges);
			} finally {

			}
		}
		return servicePackges;
	}

        private boolean show_summary = false;

    public boolean isShow_summary() {
        return show_summary;
    }

    public void setShow_summary(boolean show_summary) {
        this.show_summary = show_summary;
    }
        
	public String showSummary() {

		logger.debug("showSummary::::::::");
                show_summary = true;
		return "eventSummary";
	}

	private EventInfo eventInfo = new EventInfo();
	private ArrayList<EspPackageInfo> espInfo = new ArrayList<EspPackageInfo>();
	private ArrayList<EspPackageItemInfo> espItemInfo = new ArrayList<EspPackageItemInfo>();
	private ArrayList<EventServiceProviderInfo> espServiceInfo = new ArrayList<EventServiceProviderInfo>();

	public ArrayList<EspPackageInfo> getEspInfo() {
		return espInfo;
	}

	public void setEspInfo(ArrayList<EspPackageInfo> espInfo) {
		this.espInfo = espInfo;
	}

	public ArrayList<EspPackageItemInfo> getEspItemInfo() {
		return espItemInfo;
	}

	public void setEspItemInfo(ArrayList<EspPackageItemInfo> espItemInfo) {
		this.espItemInfo = espItemInfo;
	}

	public ArrayList<EventServiceProviderInfo> getEspServiceInfo() {
		return espServiceInfo;
	}

	public void setEspServiceInfo(ArrayList<EventServiceProviderInfo> espServiceInfo) {
		this.espServiceInfo = espServiceInfo;
	}

	public EventInfo getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}

	private int eventId;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String insertEvent() {
		eventInfo.setEventEffectiveDate(eventDate);
		AreaTypeMaster atm = new AreaTypeMaster();
		atm.setAreaId(area);
		eventInfo.setAreaId(atm);
		eventInfo.setEstimatedGuests(tCount);
		// eventInfo.setCost(BigDecimal.valueOf(Double.valueOf(amount)));getTotalAmountCharge
		eventInfo.setTotalAmount(totAmount);
		eventInfo.setCost(getTotalAmountCharge());
		eventInfo.setServiceCharges(svCh);
		eventInfo.setServiceTax(svTax);
		// eventInfo.setCost(BigDecimal.valueOf(Double.valueOf(getUsedAmount())));
		eventInfo.setCurrencyId(new CurrencyTypeMaster(1));

		FacesContext context = FacesContext.getCurrentInstance();
		try {
			CustomerInfo userId = (CustomerInfo) context.getExternalContext().getSessionMap().get("userId");
//			logger.debug("userId*********  " + userId.getUserId());
			eventInfo.setCustomerId(userId);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
                
                if(eventInfo.getCustomerId()!=null)
                {
		// eventInfo.setCustomerId(new CustomerInfo(1));
		EventTypeMaster evtTypeMaster = new EventTypeMaster();
		evtTypeMaster.setEventId(getEventTypeId());
		eventInfo.setEventType(evtTypeMaster);
		VenueTypeMaster vTypeMaster = new VenueTypeMaster();
		vTypeMaster.setVenueTypeId(Integer.parseInt(getVenueRadio()));
		eventInfo.setVenueType(vTypeMaster);
                
                if(eventStatus.equals("Cancel"))
                {
                    eventInfo.setIsActive('L');
                }
                else if(eventStatus.equals("Draft"))
                {
                    eventInfo.setIsActive('D');
                }                
                else
                {
                    eventInfo.setIsActive('B');
                }

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(eventInfo);
			em.getTransaction().commit();
			em.refresh(eventInfo);

			eventId = eventInfo.getEventId();
			logger.debug("eventInfo********  " + eventInfo.getEventId());

			EventServiceProviderInfo espi;
			// for(int j=0; j<finalVenueMap.size(); j++)
			Iterator itj = finalVenueMap.entrySet().iterator();
			while (itj.hasNext()) {

				Map.Entry srv = (Map.Entry) itj.next();
				HashMap vpInfoMap = (HashMap) srv.getValue();

				Iterator itje = vpInfoMap.entrySet().iterator();
				while (itje.hasNext()) {
					espi = new EventServiceProviderInfo();
					Map.Entry srvMap = (Map.Entry) itje.next();

					// VenuePackageInfo vpInfo = (VenuePackageInfo)
					// srvMap.getValue();
					ViewVenuePackage vpInfo = (ViewVenuePackage) srvMap.getValue();
					espi.setServiceProviderId(vpInfo.getVenueId());// .getVenueId());
					espi.setEventServiceProviderEffectiveDate(eventDate);
					espi.setTotalGuestToBeServed(tCount);
					espi.setEventServiceProviderTotamt(vpInfo.getFinalAmount());
					if (null != venuetaxService) {
						espi.setEventServiceProviderCost(vpInfo.getFinalAmount().add(venuetaxService));
						espi.setEventServiceProviderTaxamt(venuetaxService);
					} else {
						espi.setEventServiceProviderCost(vpInfo.getFinalAmount());
					}
					espi.setEventId(eventInfo);
					//espi.setEventServiceProviderTax(Integer.parseInt(vpInfo.getTax()));
                                        espi.setEventServiceProviderTax(vpInfo.getTax());
					espi.setVenue('Y');
					logger.debug("SELAHLLL*******  " + selHall);
					VenueHallInfo vif = new VenueHallInfo(selHall);
					espi.setHallId(vif);

					EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
					EntityManager em1 = emf1.createEntityManager();
					em1.getTransaction().begin();
					em1.persist(espi);
					em1.getTransaction().commit();

					EspPackageInfo epi = new EspPackageInfo();
					epi.setEspPackageEffectiveDate(eventDate);
					epi.setEventServiceProviderId(espi);
					epi.setPackageId(vpInfo.getPackageId());

					EntityManagerFactory emf11 = Persistence.createEntityManagerFactory("ROOT");
					EntityManager em11 = emf11.createEntityManager();
					em11.getTransaction().begin();
					em11.persist(epi);
					em11.getTransaction().commit();
				}
			}
			// for (Map.Entry entry : finalServicesMap.entrySet().toArray())
			Iterator it = finalServicesMap.entrySet().iterator();
			while (it.hasNext()) {
				espi = new EventServiceProviderInfo();
				Map.Entry srv = (Map.Entry) it.next();
				ViewServiceProviderPackage vsppackage = (ViewServiceProviderPackage) srv.getValue();
				espi.setServiceProviderId(vsppackage.getServiceProviderId());
				espi.setEventServiceProviderEffectiveDate(eventDate);
				espi.setTotalGuestToBeServed(tCount);
				logger.debug("vsppackage.getServiceTypeCode(): " + vsppackage.getServiceTypeCode());
				BigDecimal taxS = new BigDecimal(0);
				if (vsppackage.getTax() == null) {
					taxS = new BigDecimal(0);
				} else {
					//taxS = Integer.parseInt(vsppackage.getTax());
                                        taxS = vsppackage.getTax();
				}
				if (vsppackage.getServiceTypeCode().equals("CATR")) {
					// BigDecimal cost = vsppackage.getCost().multiply(new
					// BigDecimal(tCount));
					BigDecimal cost = vsppackage.getFinalAmount().multiply(new BigDecimal(tCount));
					espi.setEventServiceProviderTotamt(cost);
					// logger.debug("vsppackage.getServiceTypeCode(): " +
					// vsppackage.getServiceTypeCode());
					try {
						espi.setEventServiceProviderCost(cost.add(taxAmounts.get(vsppackage)));
						espi.setEventServiceProviderTax(taxS);
						espi.setEventServiceProviderTaxamt(taxAmounts.get(vsppackage));
					} catch (NullPointerException e) {
						espi.setEventServiceProviderCost(cost);// .add(taxAmounts.get(vsppackage)));
						espi.setEventServiceProviderTax(taxS);
						// espi.setEventServiceProviderTaxamt(taxAmounts.get(vsppackage));
					}
				} else {
					// espi.setEventServiceProviderCost(vsppackage.getCost());
					espi.setEventServiceProviderTax(taxS);
					espi.setEventServiceProviderTaxamt(taxAmounts.get(vsppackage));
					espi.setEventServiceProviderTotamt(vsppackage.getFinalAmount());
					if (null != taxAmounts.get(vsppackage))
						espi.setEventServiceProviderCost(vsppackage.getFinalAmount().add(taxAmounts
								.get(vsppackage)));
					else
						espi.setEventServiceProviderCost(vsppackage.getFinalAmount());

				}

				espi.setEventId(eventInfo);
				espi.setVenue('N');

				EntityManager em11 = emf.createEntityManager();
				em11.getTransaction().begin();
				em11.persist(espi);
				em11.getTransaction().commit();

				EspPackageInfo epi = new EspPackageInfo();
				epi.setEspPackageEffectiveDate(eventDate);
				epi.setEventServiceProviderId(espi);
				epi.setPackageId(vsppackage.getPackageId());

				EntityManagerFactory emf111 = Persistence.createEntityManagerFactory("ROOT");
				EntityManager em111 = emf111.createEntityManager();
				em111.getTransaction().begin();
				em111.persist(epi);
				em111.getTransaction().commit();
			}

			// sendEmail("test","test","test");

		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
                }
		return chkEvent();

	}

	public String chkEvent() {
		
                if(eventStatus.equals("B"))
                {
                    EventConfirmManagedBean e = new EventConfirmManagedBean();
                    return e.sendEmail();
                }
                else
                {
                    return returnFirstPage();
//                    return "index?faces-redirect=true";
                }
	}

	private EventDetailsManagedBean eventDetailsManagedBean;

	public EventDetailsManagedBean getEventDetailsManagedBean() {
		return eventDetailsManagedBean;
	}

	public void setEventDetailsManagedBean(EventDetailsManagedBean eventDetailsManagedBean) {
		this.eventDetailsManagedBean = eventDetailsManagedBean;
	}

	private boolean updateScenario;

	public boolean isUpdateScenario() {
		return updateScenario;
	}

	public void setUpdateScenario(boolean updateScenario) {
		this.updateScenario = updateScenario;
	}

	public long setTabIndex = 0;

	public long getSetTabIndex() {
		return setTabIndex;
	}

	private ArrayList<Boolean> disServiceTab = new ArrayList<Boolean>(selectedSrvList.size());

	public ArrayList<Boolean> getDisServiceTab() {
		return disServiceTab;
	}

	public void setDisServiceTab(ArrayList<Boolean> disServiceTab) {
		this.disServiceTab = disServiceTab;
	}

	public void setSetTabIndex(long setTabIndex) {
		logger.debug("setSetTabIndex " + setTabIndex);
		this.setTabIndex = setTabIndex;
		if (setTabIndex > 0) {
			try {
				this.setTabIndex = setTabIndex;
				// logger.debug("setSetTabIndex1 "+setTabIndex);
				int ind = (int) (setTabIndex - 1);
				logger.debug("setSetTabIndex2 " + setTabIndex);
				disServiceTab.set(ind, false);
				// logger.debug("setSetTabIndex3 "+setTabIndex);
				// FacesContext.getCurrentInstance().renderResponse();
				// logger.debug("setSetTabIndex4 "+setTabIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public String changeScreen(Object index) {
		// long index1;
		// if(venue.equals("Y"))
		// {
		// = new Long(index);
		// }
		String ind = index.toString();
		setSetTabIndex((Long.parseLong(ind)));
		// setSetTabIndex(index);
		return "event-details-summary";
	}

	public String changeScreen1(int index) {
		setSetTabIndex(index);
		return "event-details-summary";
	}

	public String returnFirstPage() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");
		return "index.xhtml?faces-redirect=true";
	}

	private boolean disCat = false;

	public boolean isDisCat() {
		return disCat;
	}

	public void setDisCat(boolean disCat) {
		this.disCat = disCat;
	}

	public void disableCat() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("myRadVal", venueRadio);
		if (venueRadio.equals("3")) {
			disCat = true;
		} else {
			disCat = false;
		}
		logger.debug("radioVal: " + venueRadio);
		// ServiceVO srv0 = new ServiceVO("CATR", "images/event-img3.jpg",
		// "Catering", "Catering Services");
		// ServiceVO srv = (ServiceVO) serviceVOList.containsValue(srv);
		for (int j = 0; j < selectedSrvList.size(); j++) {
			ServiceVO srv = selectedSrvList.get(j);

			if (srv.getId().equals("CATR") && srv.isSelected()) {
				srv.setSelected(false);
				logger.debug("serviceList: before  " + serviceList);
				int sind = serviceList.indexOf(srv);
				logger.debug("serviceList: sind  " + sind);
				srv.setSelected(false);
				logger.debug("serviceList: before  " + serviceList);
				serviceList.set(sind, srv);
				logger.debug("serviceList: after  " + serviceList);
				selectedSrvList.remove(srv);
				logger.debug("selectedSrvList: after " + selectedSrvList);
				allSelectedServicesMap.remove(srv);
				break;
			}
		}

	}

	// public String loginForm()
	// {
	// logger.debug("inside eventdetails loginform");
	// return "customerRegister";
	// }
	public void loginForm() {
		logger.debug("inside eventdetails loginform");
		// return "customerRegister";
	}

	private boolean chkfalse = false;

	public boolean isChkfalse() {
		return chkfalse;
	}

	public void setChkfalse(boolean chkfalse) {
		this.chkfalse = chkfalse;
	}

	public void onTabClose(TabCloseEvent event) {
		logger.debug("Closed tab: " + event.getTab().getTitle());
		String id = event.getTab().getId();
		// String id = event.getTab().getTitletip();
		logger.debug("ID: " + id);
		String sid = id.substring(id.length() - 1, id.length());
		logger.debug("ID: " + sid);
		int index = Integer.parseInt(sid);// + 1;
		if (index > 0) {
			logger.debug("selectedSrvList: " + selectedSrvList);
			logger.debug("serviceVOList: " + serviceVOList);
			ServiceVO srv = (ServiceVO) serviceVOList.get(index);
			logger.debug("srv: " + srv.getId());
			int sind = serviceList.indexOf(srv);
			logger.debug("serviceList: sind  " + sind);
			srv.setSelected(false);

			logger.debug("serviceList: before  " + serviceList);
			serviceList.set(sind, srv);
			logger.debug("serviceList: after  " + serviceList);
			selectedSrvList.remove(srv);
			logger.debug("selectedSrvList: after " + selectedSrvList);
			allSelectedServicesMap.remove(srv);
			if (finalServicesMap.containsKey(srv.getId())) {
				finalServicesMap.remove(srv.getId());
			}

		}
		if (index == 0) {
			venueRadio = "1";
			venueType = "Home";
			resetVenue();
		}
		// getAllSelectedServices();
		HashMap tempMap = new LinkedHashMap();
		tempMap.putAll(allSelectedServicesMap);
		allSelectedServicesMap = tempMap;
		setAllSelectedServicesMap(allSelectedServicesMap);
		logger.debug("allSelectedServicesMap: " + allSelectedServicesMap);
		logger.debug("serviceList: after  " + serviceList);
		logger.debug("selectedSrvList: after " + selectedSrvList);
		// RequestContext.getCurrentInstance().update(":frm:ap:addMoreServices,:frm:ap:selservices");

	}

	/************** gmap ******************/
	private MapModel circleModel;
	private String center;

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public MapModel getCircleModel() {
		return circleModel;
	}

	public void setCircleModel(MapModel circleModel) {
		this.circleModel = circleModel;
	}

	private void initMap(String address, String name) throws JsonSyntaxException, IOException {
		circleModel = new DefaultMapModel();
		// logger.debug("Initmap ***  "+address);
		// List<String> tmp = new ArrayList<String>();
		//
		// tmp.add("500 Eubank Boulevard Southeast Albuquerque, NM 87123");
		// tmp.add("6350 Peachtree Dunwoody Road Northeast Atlanta, GA 30328");
		// tmp.add("1701 Dallas Parkway Plano, TX 75093");
		try {
			Gson gson = new Gson();
			// for (String address : tmp) {
			GoogleGeoCodeResponse result = gson.fromJson(jsonCoord(URLEncoder.encode(address, "UTF-8")),
					GoogleGeoCodeResponse.class);
			if (result.results.length != 0) {
				double lat = Double.parseDouble(result.results[0].geometry.location.lat);
				double lng = Double.parseDouble(result.results[0].geometry.location.lng);
				center = String.valueOf(lat) + "," + String.valueOf(lng);
				// logger.debug("lat: "+lat+"  lng: "+lng);
				LatLng coord = new LatLng(lat, lng);
				// Circle circle = new Circle(coord, 100300);
				// Circle circle = new Circle(coord, 100300);
				// circle.setStrokeColor("#00ff00");
				// circle.setFillColor("#00ff00");
				// circle.setStrokeOpacity(0.7);
				// circle.setFillOpacity(0.7);
				// circleModel.addOverlay(circle);
				circleModel.addOverlay(new Marker(coord, name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// }
	}

	private String jsonCoord(String address) throws IOException {
		URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=false");
		URLConnection connection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		String jsonResult = "";
		while ((inputLine = in.readLine()) != null) {
			jsonResult += inputLine;
		}
		in.close();
		logger.debug("jsonResult " + jsonResult);
		return jsonResult;
	}

	public String returnToSummaryPage() {

		return "event-details-summary";
	}

	/**
	 * @return the packDesc
	 */
	public String getPackDesc() {
		return packDesc;
	}

	/**
	 * @param packDesc
	 *            the packDesc to set
	 */
	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	/**
	 * @return the serviceCost
	 */
	public BigDecimal getServiceCost() {
		return serviceCost;
	}

	/**
	 * @param serviceCost
	 *            the serviceCost to set
	 */
	public void setServiceCost(BigDecimal serviceCost) {
		this.serviceCost = serviceCost;
	}

	/**
	 * @return the serviceFinalCost
	 */
	public BigDecimal getServiceFinalCost() {
		return serviceFinalCost;
	}

	/**
	 * @param serviceFinalCost
	 *            the serviceFinalCost to set
	 */
	public void setServiceFinalCost(BigDecimal serviceFinalCost) {
		this.serviceFinalCost = serviceFinalCost;
	}

	public String populatePkgData() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		for (ViewServiceProviderPackage pkg : servicePackges) {
			if (pkg.getPackageId() == packId) {
				setSrvcpkg(pkg);
				break;
			}
		}
		servicePkgImages = new ArrayList<String>();
		File dirPkgSrv = new File(srvcPrvdPkgPath + getSrvcpkg().getPackageId());
		// String url = context.getInitParameter("imgurl");
		if (dirPkgSrv.exists()) {
			String[] listFiles = dirPkgSrv.list();
			int len = dirPkgSrv.list().length;
			if (len == 0) {
				logger.debug("No images uploaded");
				servicePkgImages.add("images/noImage.gif");
			} else {
				for (int i = 0; i < len; i++) {
					logger.debug("listFiles[i] " + listFiles[i]);
					servicePkgImages.add("http://" + url + srvcPrvdUrl + getSrvcpkg().getPackageId() + "/"
							+ listFiles[i]);
				}
			}
			logger.debug(servicePkgImages);
		} else {
			logger.debug("No directory found");
			servicePkgImages.add("images/noImage.gif");
		}
		return null;
	}

	public String populateServiceImgs() {
		// santosh
		return null;

	}

	public String populateVndrPkgData() {
		for (ViewVenuePackage pkg : venuePackages) {
			if (pkg.getPackageId() == packId) {
				setVenuePkg(pkg);
				break;
			}
		}
		return null;
	}

	public String populatePkgImgData() {
		for (ViewVenuePackage pkg : venuePackages) {
			if (pkg.getPackageId() == packId) {
				setVenuePkg(pkg);
				break;
			}
		}
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		// String path = context.getInitParameter("imgpath");
		servicePkgImages = new ArrayList<String>();
		File dir = new File(srvcPrvdPkgPath);
		// srvcPrvdUrl = "/venue/" + type.getVenueId() + "/packages/";
		// srvcPrvdPkgPath = path + "/venue/" + type.getVenueId() +
		// "/packages/";
		File pkgdir = new File(srvcPrvdPkgPath + getVenuePkg().getPackageId() + "/");
		logger.debug("Directory! " + dir);
		logger.debug("Package Dir" + pkgdir);
		if (dir.exists() && pkgdir.exists()) {
			String[] listFiles = pkgdir.list();
			int len = pkgdir.list().length;
			if (len == 0) {
				logger.debug("No images uploaded");
				servicePkgImages.add("images/noImage.gif");
			} else {
				// String url = context.getInitParameter("imgurl");
				for (int i = 0; i < len; i++) {
					logger.debug("listFiles[i] " + listFiles[i]);
					servicePkgImages.add("http://" + url + srvcPrvdUrl + getVenuePkg().getPackageId() + "/"
							+ listFiles[i]);
				}
			}
		} else {
			logger.debug("No directory found");
			servicePkgImages.add("images/noImage.gif");
		}
		return null;
	}

	public void clearContents(CloseEvent event) {
		servicePkgImages = new ArrayList<String>();
	}

	/**
	 * @return the srvcpkg
	 */
	public ViewServiceProviderPackage getSrvcpkg() {
		return srvcpkg;
	}

	/**
	 * @param srvcpkg
	 *            the srvcpkg to set
	 */
	public void setSrvcpkg(ViewServiceProviderPackage srvcpkg) {
		this.srvcpkg = srvcpkg;
	}

	/**
	 * @return the venuePkg
	 */
	public ViewVenuePackage getVenuePkg() {
		return venuePkg;
	}

	/**
	 * @param venuePkg
	 *            the venuePkg to set
	 */
	public void setVenuePkg(ViewVenuePackage venuePkg) {
		this.venuePkg = venuePkg;
	}

	/**
	 * @return the packId
	 */
	public int getPackId() {
		return packId;
	}

	/**
	 * @param packId
	 *            the packId to set
	 */
	public void setPackId(int packId) {
		this.packId = packId;
	}

	public void onTabChange(TabChangeEvent e) {
		logger.debug("Selected tab : " + e.getTab().getTitle());
		String id = e.getTab().getId();
		logger.debug("ID: " + id);
		String sid = id.substring(id.length() - 1, id.length());
		logger.debug("Images Hall ID: " + sid);
		for (Object key : hallMap.keySet()) {
			if (hallMap.get(key).toString() != null
					&& (hallMap.get(key).toString().equalsIgnoreCase(e.getTab().getTitle()))) {
				sid = key.toString();
				break;
			}
		}
		logger.debug("New Sid: " + sid);
		hallId = sid;
		hallMap.values();
		hotelHallImages = new ArrayList<String>();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		// String path = context.getInitParameter("imgpath");
		File dir = new File(path + "/venue/" + popupViewVenue.getVenueId() + "/" + sid);
		logger.debug("Directory! " + dir);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			String[] listFiles = dir.list();
			int len = dir.list().length;
			if (len == 0) {
				logger.debug("No images uploaded");
				hotelHallImages.add("images/noImage.gif");
			} else {
				// String url = context.getInitParameter("imgurl");
				for (int i = 0; i < len; i++) {
					logger.debug("listFiles[i] " + listFiles[i]);
					if (!list[i].isDirectory()) {
						try {
							String str = "http://" + url + "/venue/" + popupViewVenue.getVenueId() + "/" + sid + "/"
									+ listFiles[i];
							URL url1 = new URL(str);
							HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
							huc.setRequestMethod("HEAD");
							int responseCode = huc.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								hotelHallImages.add(str);
							} else {
								hotelHallImages.add("images/noImage.gif");
							}
							// hotelHallImages.add("http://" + url + "/venue/" +
							// popupViewVenue.getVenueId() + "/" + sid + "/"
							// + listFiles[i]);
						} catch (Exception ex) {
							java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
									Level.SEVERE, null, ex);
						}
					}

				}
			}
		} else {
			logger.debug("No directory found");
			hotelHallImages.add("images/noImage.gif");
		}
	}

	/**
	 * @return the servicePkgImages
	 */
	public List<String> getServicePkgImages() {
		return servicePkgImages;
	}

	/**
	 * @param servicePkgImages
	 *            the servicePkgImages to set
	 */
	public void setServicePkgImages(List<String> servicePkgImages) {
		this.servicePkgImages = servicePkgImages;
	}

	/**
	 * @return the srvcPrvdPkgPath
	 */
	public String getSrvcPrvdPkgPath() {
		return srvcPrvdPkgPath;
	}

	/**
	 * @param srvcPrvdPkgPath
	 *            the srvcPrvdPkgPath to set
	 */
	public void setSrvcPrvdPkgPath(String srvcPrvdPkgPath) {
		this.srvcPrvdPkgPath = srvcPrvdPkgPath;
	}

	/**
	 * @return the srvcPrvdUrl
	 */
	public String getSrvcPrvdUrl() {
		return srvcPrvdUrl;
	}

	/**
	 * @param srvcPrvdUrl
	 *            the srvcPrvdUrl to set
	 */
	public void setSrvcPrvdUrl(String srvcPrvdUrl) {
		this.srvcPrvdUrl = srvcPrvdUrl;
	}
        
        String eventStatus = "B";
        
        public String goLoginPage()
        {
            
            eventStatus = "Cancel";
            insertEvent();
            
       // FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("eventDetailsManagedBean"))
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");  
        }
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId"))
            {
                //return "eventHome.xhtml?faces-redirect=true"; 
                return "index.xhtml?faces-redirect=true";
            }
        else
        {
            return "index.xhtml?faces-redirect=true";
        }
//        
        
    }
        
     public String saveDraft()
        {            
            eventStatus = "Draft";
            return insertEvent();
        }

}
