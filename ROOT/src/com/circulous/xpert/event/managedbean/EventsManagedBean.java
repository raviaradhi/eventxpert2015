package com.circulous.xpert.event.managedbean;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
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
import java.util.logging.Level;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.log4j.Logger;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.circulous.xpert.event.comparator.ServiceProviderComparatorByAmount;
import com.circulous.xpert.event.comparator.ServiceProviderComparatorByArea;
import com.circulous.xpert.event.comparator.ServiceProviderComparatorByName;
import com.circulous.xpert.event.comparator.ServiceVOComparator;
import com.circulous.xpert.event.comparator.VenueComparatorByAmount;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.EventDates;
import com.circulous.xpert.jpa.entities.EventDates_;
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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@ManagedBean
@SessionScoped
public class EventsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(EventsManagedBean.class);

	private List<VenueVO> venueListt = new ArrayList<VenueVO>();

	private List<VenueTypeMaster> venueTypeMaster = new ArrayList<VenueTypeMaster>();

	private Map venueMap = new HashMap();

	private Map venueMapLocal = new HashMap();

	private String venueRadio = "";

	private String[] selectedServices;

	private List<String> guests;

	private List<String> budgets;

	private String selectedBudget;

	private String selectedGuest;

	private List<String> eventList = new ArrayList<String>();

	private List<String> venueList = new ArrayList<String>();

	private List<String> serviceList = new ArrayList<String>();

	private String selectedEvent;

	private Date eventDate;

	private boolean haveRecords;

	public HashMap vPkgDtls;

	private List<ViewVenue> allVenueList;

	private List<ViewVenue> viewVenueList = new ArrayList<ViewVenue>();

	private ViewVenuePackage finalVenueInfo = null;

	private String popupVenueId = "0";

	public ViewVenue popupViewVenue;

	private List<VenueHallInfo> hallList;

	private Map hallMap;

	private Map hallLocalMap;

	private int hallIndex;

	private List<String> hotelImages;

	private List<String> hotelHallImages;

	private MapModel circleModel;

	private String hallId = "";

	private String srvcPrvdPkgPath;

	private String srvcPrvdUrl;

	private String path;

	private String url;

	private int eventTypeId;

	private ViewVenuePackage venuePkg;

	private List<ViewVenuePackage> venuePackages;

	private String center;

	private ViewServiceProvider popupViewServiceProvider;

	private ArrayList allSelectedServices;

	private List<ServiceVO> selectedSrvList;

	private ArrayList<Boolean> disServiceTab = new ArrayList<Boolean>();

	private HashMap allSelectedServicesMap = new HashMap();

	private Integer area = new Integer(0);

	private String sortBy = "";

	private List<ViewServiceProvider> viewServiceProviderList = new ArrayList<ViewServiceProvider>();

	private String popupServiceCode = "";

	private List<ServiceTypeMaster> srvList;

	public List<String> hotelImagesSmall;

	public List<String> serviceImagesSmall;

	private BigDecimal pCost;

	private List<ViewServiceProviderPackage> servicePackges;

	private boolean chkServicePop = false;

	public ServiceVO selService;

	private List<String> servicesImages;

	private List<String> servicePkgImages;

	private int serviceProviderId = 0;

	private ViewServiceProviderPackage srvcpkg;

	private String packDesc;

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
	EntityManager em = emf.createEntityManager();

	public EventsManagedBean() {

		populateVenueList();

		guests = new ArrayList<String>();
		guests.add("0-50");
		guests.add("50-100");
		guests.add("100-300");
		guests.add("300-500");
		guests.add("500-1000");
		guests.add("1000 +");

		budgets = new ArrayList<String>();
		budgets.add("Rs 1,00,000 - 2,00,000");
		budgets.add("Rs 2 lakh");
		budgets.add("Rs 3 lakh");
		budgets.add("Rs 5 lakh");
		budgets.add("Rs 7 lakh");
		budgets.add("Rs 10 lakh");

		hallMap = new HashMap();
		hallLocalMap = new HashMap();
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

	Integer tCount = 0;
	public String totalCount = "0";

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalCount() {
		tCount = Integer.parseInt(totalCount);
		//return totalCount;
		return "300";
	}

	private void populateVenueList() {
		venueListt = new ArrayList<VenueVO>();
		Iterator<VenueTypeMaster> atr = venueTypeMaster.iterator();
		while (atr.hasNext()) {
			VenueTypeMaster atm = atr.next();
			venueListt.add(new VenueVO(String.valueOf(atm.getVenueTypeId()), "images/event-img3.jpg", atm
					.getVenueType(), atm.getVenueDescription()));
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
				if (comp1 == comp2) {
					venueMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (Integer) val);
					break;
				}
			}
		}
		venueMap = sortedMap;

		Query query = em.createNamedQuery("EventTypeMaster.findByIsActive");
		query.setParameter("isActive", 'Y');
		List<EventTypeMaster> etmList = query.getResultList();
		for (Iterator<EventTypeMaster> iterator = etmList.iterator(); iterator.hasNext();) {
			EventTypeMaster eventTypeMaster = iterator.next();
			eventList.add(eventTypeMaster.getEventType());
		}

		Query queryVenue = em.createNamedQuery("VenueTypeMaster.findByIsActive");
		queryVenue.setParameter("isActive", 'Y');
		List<VenueTypeMaster> vList = queryVenue.getResultList();
		for (Iterator<VenueTypeMaster> iterator = vList.iterator(); iterator.hasNext();) {
			VenueTypeMaster venueTypeMaster = iterator.next();
			venueList.add(venueTypeMaster.getVenueType());
		}

		Query queryService = em.createNamedQuery("ServiceTypeMaster.findByIsActive");
		queryService.setParameter("isActive", 'Y');
		srvList = queryService.getResultList();
		for (Iterator<ServiceTypeMaster> iterator = srvList.iterator(); iterator.hasNext();) {
			ServiceTypeMaster srvTypeMaster = iterator.next();
			serviceList.add(srvTypeMaster.getServiceName());
		}
		
		Query qryViewServiceProviderList = em.createNamedQuery("ViewServiceProvider.findAll");
		viewServiceProviderList = qryViewServiceProviderList.getResultList();
	}

	public List<ViewVenue> getAllVenueList1() {
		List<ViewVenue> localVIList = new ArrayList<ViewVenue>();

		vPkgDtls = new HashMap();
		int selectedVenueVO = 0;
		for (int i = 0; i < venueList.size(); i++) {
			if (venueList.get(i).equalsIgnoreCase(venueRadio)) {
				selectedVenueVO = i + 1;
				break;
			}
		}
		VenueTypeMaster vtm = new VenueTypeMaster(selectedVenueVO);
		if (0 != selectedVenueVO) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			Query qryList1 = em.createQuery("select  e1 from  VenueInfo e1 where  e1.venueTypeId = :vt");
			qryList1.setParameter("vt", vtm);
			List espList = qryList1.getResultList();
			if (espList.size() == 0) {
				haveRecords = false;
			} else {
				haveRecords = true;
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ViewVenue> criteriaQuery = cb.createQuery(ViewVenue.class);
				Root<ViewVenue> vVenue = criteriaQuery.from(ViewVenue.class);
				CriteriaQuery<ViewVenue> select = criteriaQuery.select(vVenue);
				Subquery<EventServiceProviderInfo> cq = criteriaQuery.subquery(EventServiceProviderInfo.class);
				Root<EventServiceProviderInfo> at = cq.from(EventServiceProviderInfo.class);
				cq.select(at);
				cq.where(cb.and(
						cb.equal(at.get(EventServiceProviderInfo_.eventServiceProviderEffectiveDate), eventDate),
						cb.equal(at.get(EventServiceProviderInfo_.venue), 'Y')));
				Expression<String> exp = vVenue.get("venueId");
				Predicate predicate = exp.in(cq);
				select.where(cb.not(predicate));

				TypedQuery<ViewVenue> qryList = em.createQuery(select);
				viewVenueList = qryList.getResultList();

				boolean getAddress = false;
				String firstAddress = "";
				LatitudeLongitude current = null;

				List<ViewVenue> viewVenueList1 = viewVenueList;
				List<ViewVenue> templist = new ArrayList<ViewVenue>();

				for (Iterator<ViewVenue> iterator = viewVenueList.iterator(); iterator.hasNext();) {
					ViewVenue type = (ViewVenue) iterator.next();
					Query qryList11 = em.createNamedQuery("ViewVenuePackage.findByVenueIdEType");
					qryList11.setParameter("venueId", type.getVenueId());
					// hardcoded
					qryList11.setParameter("eventType", 1);
					System.out.println("qryList11***  " + qryList11);
					List vList = qryList11.getResultList();

					if (vList.size() > 0) {
						System.out.println("Records exists");
						templist.add(type);
					} else {
						System.out.println("No Records exists");
					}
				}

				viewVenueList = templist;

				for (Iterator<ViewVenue> iterator = viewVenueList.iterator(); iterator.hasNext();) {
					ViewVenue type = (ViewVenue) iterator.next();
					if (type.getVenueTypeId() == 3) {
						type.setVenueCost(type.getVenueCost().multiply(new BigDecimal(getTotalCount())));
						type.setFinalCOST(type.getFinalCOST().multiply(new BigDecimal(getTotalCount())));
					} else {
						type.setVenueCost(type.getVenueCost());
						type.setFinalCOST(type.getFinalCOST());
					}
					if (type.getVenueTypeId() == selectedVenueVO) {
						localVIList.add(type);
					}
					vPkgDtls.put(type.getVenueId(), type);
				}

				/*
				 * Query arQry =
				 * em.createNamedQuery("AreaTypeMaster.findByAreaId");
				 * arQry.setParameter("areaId", area); AreaTypeMaster atm =
				 * (AreaTypeMaster) arQry.getSingleResult();
				 * System.out.println("area  " + area); double lat =
				 * Double.valueOf(atm.getLatitude()); double lng =
				 * Double.valueOf(atm.getLongitude()); current = new
				 * LatitudeLongitude(lat, lng);
				 * 
				 * PlacesLatitudes sample = new PlacesLatitudes(); try {
				 * localVIList = sample.calculateDistances(current,
				 * localVIList); } catch (Exception ex) { ex.printStackTrace();
				 * }
				 */
			}
		}
		// if ("Amount".equals(sortBy)) {
		Collections.sort(localVIList, new VenueComparatorByAmount());
		/*
		 * } else if ("Area".equals(sortBy)) { Collections.sort(localVIList, new
		 * VenueComparatorByArea()); } else if ("Name".equals(sortBy)) {
		 * Collections.sort(localVIList, new VenueComparatorByName()); }
		 */
		return localVIList;
	}

	public String getResults() {

		System.out.println(selectedServices);

		System.out.println(selectedGuest);

		selectedSrvList = new ArrayList<ServiceVO>();

		ServiceVO srv;

		for (String item : selectedServices) {
			for (ServiceTypeMaster stm : srvList) {
				if (item.equalsIgnoreCase(stm.getServiceName())) {
					srv = new ServiceVO(stm.getServiceTypeCode(), null, stm.getServiceName(),
							stm.getServiceDescription());
					selectedSrvList.add(srv);
					break;
				}
			}
		}

		setAllVenueList(getAllVenueList1());
		getAllSelectedServices();
		return "newResults";
	}

	public ArrayList getAllSelectedServices() {
		logger.debug("getAllSelectedServices*******  " + allSelectedServices);
		allSelectedServices = new ArrayList();
		logger.debug("selectedSrvList******* alllll  " + selectedSrvList);
		allSelectedServices.add(selectedSrvList);
		LinkedHashMap sortedMap = new LinkedHashMap();
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
		return allSelectedServices;
	}

	public List<ViewServiceProvider> getServiceListDtls(ServiceVO serviceVO) {

		List<ViewServiceProvider> localVIList = new ArrayList<ViewServiceProvider>();
		try {
			ServiceVO selectedServiceVO = null;
			logger.debug("serviceVO.getHeading::::: " + serviceVO.getHeading());
			logger.debug("serviceVO.getId::::: " + serviceVO.getId());
			selectedServiceVO = serviceVO;
			logger.debug("selectedServiceVO.getId::::: " + selectedServiceVO.getId());
			long lCount = new Long(getTotalCount()).longValue();

			if (null != selectedServiceVO) {

				EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
				EntityManager em = emf.createEntityManager();

				ServiceTypeMaster stm = new ServiceTypeMaster(selectedServiceVO.getId());
				SimpleDateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
				SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat writeFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				Date d = readFormat.parse(eventDate.toString());
				logger.debug("dattteee " + d);
				String str = writeFormat.format(d);
				Date strDate = writeFormat1.parse(str);
				logger.debug("strDate " + strDate);
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ViewServiceProvider> criteriaQuery = cb.createQuery(ViewServiceProvider.class);
				Root<ViewServiceProvider> vSProvider = criteriaQuery.from(ViewServiceProvider.class);
				CriteriaQuery<ViewServiceProvider> select = criteriaQuery.select(vSProvider);
				Subquery<EventDates> cq = criteriaQuery.subquery(EventDates.class);
				Root<EventDates> edates = cq.from(EventDates.class);
				Root<ServiceProviderInfo> sp = cq.from(ServiceProviderInfo.class);
				cq.select(edates);
				cq.where(cb.and(
						cb.equal(edates.get(EventDates_.eventDate), strDate),
						cb.equal(sp.get(ServiceProviderInfo_.serviceProviderId),
								edates.get(EventDates_.serviceProviderId)),
						cb.equal(sp.get(ServiceProviderInfo_.serviceTypeCode), stm)));
				logger.debug("(SP CQ**********   " + cq);

				Expression<String> exp = vSProvider.get("serviceProviderId");
				Predicate predicate = exp.in(cq);
				select.where(cb.and(
						cb.equal(vSProvider.get(ViewServiceProvider_.serviceTypeCode), selectedServiceVO.getId()),
						cb.not(predicate)));

				TypedQuery<ViewServiceProvider> qryViewServiceProviderList = em.createQuery(select);

				logger.debug("(SP**********   " + select);

				localVIList = qryViewServiceProviderList.getResultList();

				List<ViewServiceProvider> templist = new ArrayList<ViewServiceProvider>();
				ArrayList indexes = new ArrayList();

				for (int j = 0; j < localVIList.size(); j++) {
					System.out.println("localVIList.size()**  " + localVIList.size());
					ViewServiceProvider type = (ViewServiceProvider) localVIList.get(j);
					Query qryList11 = em.createNamedQuery("ViewServiceProviderPackage.findBySrvPrvdIdEType");
					qryList11.setParameter("serviceProviderId", type.getServiceProviderId());
					qryList11.setParameter("eventType", eventTypeId);
					System.out.println("qryList11***  " + qryList11);
					List vList = qryList11.getResultList();

					if (vList.size() > 0) {
						System.out.println("Records exists");
						templist.add(type);
					} else {
						System.out.println("No Records exists");
					}
				}
				localVIList = templist;

				logger.debug("getAllServicesList::::: localVIList size -- " + localVIList.size()
						+ "  qryViewServiceProviderList " + qryViewServiceProviderList);

				Query arQry = em.createNamedQuery("AreaTypeMaster.findByAreaId");
				arQry.setParameter("areaId", area);
				AreaTypeMaster atm = (AreaTypeMaster) arQry.getSingleResult();

				System.out.println("area  " + area);

				double lat = Double.valueOf(atm.getLatitude());
				double lng = Double.valueOf(atm.getLongitude());
				LatitudeLongitude current = new LatitudeLongitude(lat, lng);

				PlacesLatitudes sample = new PlacesLatitudes();
				try {
					localVIList = sample.calculateDistancesServices(current, localVIList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			logger.debug("getAllServicesList::::: localVIList  -- " + localVIList);

			if ("Amount".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByAmount());
			} else if ("Area".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByArea());
			} else if ("Name".equals(sortBy)) {
				Collections.sort(localVIList, new ServiceProviderComparatorByName());
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return localVIList;
	}

	public String getCost(BigDecimal cost) {
		return cost.stripTrailingZeros().toPlainString();
	}

	public String selectedVenueVals(int venueId) {

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

	public List<String> getHotelImagesSm(String venueId) {
		hotelImagesSmall = new ArrayList<String>();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
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
				for (int i = 0; i < len; i++) {
					logger.debug("listFiles[i] " + listFiles[i]);
					logger.debug("listFiles[i] " + list[i].getAbsolutePath());
					logger.debug("listFiles[i] " + list[i].getPath());
					if (!list[i].isDirectory()) {
						try {
							String str = "http://" + url + "/venue/" + venueId + "/" + listFiles[i];
							URL url1 = new URL(str);
							HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
							huc.setRequestMethod("HEAD");
							int responseCode = huc.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								hotelImagesSmall.add(str);
							} else {
								hotelImagesSmall.add("images/noImage.gif");
							}
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

	public List<String> getServiceImagesSm(String id) {
		logger.debug("ID SRVICE " + id);
		serviceImagesSmall = new ArrayList<String>();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
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
				for (int i = 0; i < len; i++) {
					if (!list[i].isDirectory()) {
						try {//
							String str = "http://" + url + "/vendors/" + id + "/" + listFiles[i];
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

	public String getProjectedCost(BigDecimal cost, String strUnitOfCost) {
		logger.debug("UnitOfCost::::" + strUnitOfCost);
		if ("PER PLATE".equalsIgnoreCase(strUnitOfCost.trim()) || "PER HEAD".equalsIgnoreCase(strUnitOfCost.trim())) {
			logger.debug("inside perplate::::" + cost);
			logger.debug("inside perplate:::: " + cost.multiply(new BigDecimal(getTotalCount())));
			pCost = cost.multiply(new BigDecimal(getTotalCount()));
		} else {
			logger.debug("else unite::::" + strUnitOfCost);
			pCost = cost;
		}
		return pCost.stripTrailingZeros().toPlainString();
	}

	public List<ViewVenuePackage> getVenuePackages() {

		logger.debug("getVenuePackges:1::venueId" + popupVenueId);
		VenueInfo vi = new VenueInfo(Integer.parseInt(popupVenueId));
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			Query query = em.createNamedQuery("ViewVenuePackage.findByVenueIdEType");
			query.setParameter("venueId", Integer.parseInt(popupVenueId));
			query.setParameter("eventType", eventTypeId);
			venuePackages = query.getResultList();
		} finally {

		}
		return venuePackages;
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
						for (int i = 0; i < len; i++) {
							if (!list[i].isDirectory()) {
								try {
									logger.debug("listFiles[i] " + listFiles[i]);
									String str = "http://" + url + "/vendors/" + type.getServiceProviderId() + "/"
											+ listFiles[i];
									URL url1 = new URL(str);
									HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
									huc.setRequestMethod("HEAD");
									int responseCode = huc.getResponseCode();
									if (responseCode == HttpURLConnection.HTTP_OK) {
										servicesImages.add(str);
									} else {
										servicesImages.add("images/noImage.gif");
									}
								} catch (Exception ex) {
									java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
											Level.SEVERE, null, ex);
								}
								logger.debug("listFiles[i] " + listFiles[i]);
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
							} catch (Exception ex) {
								java.util.logging.Logger.getLogger(EventDetailsManagedBean.class.getName()).log(
										Level.SEVERE, null, ex);
							}
						}
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
		return "newServiceDetail";
	}

	private void initMap(String address, String name) throws JsonSyntaxException, IOException {
		circleModel = new DefaultMapModel();
		try {
			Gson gson = new Gson();
			GoogleGeoCodeResponse result = gson.fromJson(jsonCoord(URLEncoder.encode(address, "UTF-8")),
					GoogleGeoCodeResponse.class);
			if (result.results.length != 0) {
				double lat = Double.parseDouble(result.results[0].geometry.location.lat);
				double lng = Double.parseDouble(result.results[0].geometry.location.lng);
				center = String.valueOf(lat) + "," + String.valueOf(lng);
				LatLng coord = new LatLng(lat, lng);
				circleModel.addOverlay(new Marker(coord, name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public List<ViewVenue> getAllVenueList2() {
		return null;
		/*
		 * List<ViewVenue> localVIList = new ArrayList<ViewVenue>();
		 * logger.debug("getAllVenueList -- venueType -- " + venueType);
		 * logger.debug("getAllVenueList -- venueRadio -- " + venueRadio);
		 * 
		 * VenueTypeMaster vtm = new
		 * VenueTypeMaster(Integer.parseInt(venueRadio)); vPkgDtls = new
		 * HashMap();
		 * 
		 * for (Iterator<VenueVO> iterator = venueList.iterator();
		 * iterator.hasNext();) { VenueVO venueVO = iterator.next();
		 * 
		 * if (venueVO.getId().equals(venueRadio)) { // if
		 * (venueVO.getHeading().equals(venueType)){
		 * logger.debug("selected Venue  " + venueVO.getHeading()); //
		 * logger.debug("Found --"); selectedVenueVO = venueVO; break; } }
		 * 
		 * // logger.debug(selectedVenueVO); if (null != selectedVenueVO) {
		 * logger.debug("*********########****** " + eventDate); //
		 * SimpleDateFormat writeFormat = new // SimpleDateFormat("dd-MM-YYYY");
		 * // String str = writeFormat.format(eventDate);
		 * 
		 * EntityManagerFactory emf =
		 * Persistence.createEntityManagerFactory("ROOT"); EntityManager em =
		 * emf.createEntityManager();
		 * 
		 * Query qryList1 = em.createQuery(
		 * "select  e1 from  VenueInfo e1 where  e1.venueTypeId = :vt");
		 * qryList1.setParameter("vt", vtm); List espList =
		 * qryList1.getResultList();
		 * 
		 * if (espList.size() == 0) { haveRecords = false; } else { haveRecords
		 * = true; CriteriaBuilder cb = em.getCriteriaBuilder(); //
		 * CriteriaQuery<EventServiceProviderInfo> cq = //
		 * cb.createQuery(EventServiceProviderInfo.class); //
		 * Root<EventServiceProviderInfo> at = //
		 * cq.from(EventServiceProviderInfo.class); // cq.select(at); // //
		 * Person_.java is the generated canonical so we don't have // to use //
		 * // at.get("firstName") and at.get("lastName") //
		 * cq.where(cb.and(cb.equal
		 * (at.get(EventServiceProviderInfo_.eventServiceProviderEffectiveDate),
		 * // eventDate), // cb.equal(at.get(EventServiceProviderInfo_.venue),
		 * 'Y'))); // TypedQuery<EventServiceProviderInfo> qryList = //
		 * em.createQuery(cq); // List<EventServiceProviderInfo> espList = //
		 * qryList.getResultList(); // // Query qryList1 = // em.createQuery(
		 * "select  e1 from  EventDates e1 where  e1.eventDate = :edate and e1.serviceProviderId = :vt"
		 * ); // qryList.setParameter("edate", eventDate); //
		 * qryList.setParameter("vt", 'Y'); // List espList =
		 * qryList.getResultList();
		 * 
		 * // logger.debug("((((   " + qryList); //
		 * logger.debug("(((((((((((!**********  " + espList);
		 * 
		 * // cb = em.getCriteriaBuilder(); CriteriaQuery<ViewVenue>
		 * criteriaQuery = cb.createQuery(ViewVenue.class); Root<ViewVenue>
		 * vVenue = criteriaQuery.from(ViewVenue.class);
		 * CriteriaQuery<ViewVenue> select = criteriaQuery.select(vVenue);
		 * 
		 * Subquery<EventServiceProviderInfo> cq =
		 * criteriaQuery.subquery(EventServiceProviderInfo.class);
		 * Root<EventServiceProviderInfo> at =
		 * cq.from(EventServiceProviderInfo.class); cq.select(at);
		 * cq.where(cb.and( cb.equal(at.get(EventServiceProviderInfo_.
		 * eventServiceProviderEffectiveDate), eventDate),
		 * cb.equal(at.get(EventServiceProviderInfo_.venue), 'Y')));
		 * 
		 * Expression<String> exp = vVenue.get("venueId"); Predicate predicate =
		 * exp.in(cq); select.where(cb.not(predicate)); //
		 * select.where(cb.not(cb.exists(cq)));
		 * 
		 * TypedQuery<ViewVenue> qryList = em.createQuery(select);
		 * 
		 * viewVenueList = qryList.getResultList(); logger.debug("(((( hjjkh   "
		 * + qryList.toString());
		 * 
		 * 
		 * String qry = "select v from ViewVenue v"; for (int j = 0; j <
		 * espList.size(); j++) { EventServiceProviderInfo esp =
		 * (EventServiceProviderInfo) espList.get(j);
		 * 
		 * if (j == 0) { qry = qry + " where v.venueId<>" +
		 * esp.getServiceProviderId() + " and "; }
		 * 
		 * qry = qry + "v.venueId<>" + esp.getServiceProviderId() + " and ";
		 * 
		 * if (j == (espList.size() - 1)) { qry = qry + "v.venueId<>" +
		 * esp.getServiceProviderId(); } }
		 * 
		 * logger.debug("((((   " + qry); Query qryViewVenueList =
		 * em.createQuery(qry);
		 * 
		 * // viewVenueList = qryViewVenueList.getResultList(); boolean
		 * getAddress = false; String firstAddress = ""; LatitudeLongitude
		 * current = null;
		 * 
		 * List<ViewVenue> viewVenueList1 = viewVenueList; List<ViewVenue>
		 * templist = new ArrayList<ViewVenue>();
		 * 
		 * for (Iterator<ViewVenue> iterator = viewVenueList.iterator();
		 * iterator.hasNext();) { ViewVenue type = (ViewVenue) iterator.next();
		 * Query qryList11 =
		 * em.createNamedQuery("ViewVenuePackage.findByVenueIdEType");
		 * qryList11.setParameter("venueId", type.getVenueId());
		 * qryList11.setParameter("eventType", eventTypeId);
		 * System.out.println("qryList11***  " + qryList11); List vList =
		 * qryList11.getResultList();
		 * 
		 * if (vList.size() > 0) { System.out.println("Records exists");
		 * templist.add(type); } else { System.out.println("No Records exists");
		 * // viewVenueList1.remove(type); } }
		 * 
		 * viewVenueList = templist;
		 * 
		 * for (Iterator<ViewVenue> iterator = viewVenueList.iterator();
		 * iterator.hasNext();) { ViewVenue type = (ViewVenue) iterator.next();
		 * if (type.getVenueTypeId() == 3) {
		 * type.setVenueCost(type.getVenueCost().multiply(new
		 * BigDecimal(getTotalCount())));
		 * type.setFinalCOST(type.getFinalCOST().multiply(new
		 * BigDecimal(getTotalCount()))); } else {
		 * type.setVenueCost(type.getVenueCost());
		 * type.setFinalCOST(type.getFinalCOST()); }
		 * 
		 * // logger.debug("type.getVenueTypeId() --" + //
		 * type.getVenueTypeId()); //
		 * logger.debug(Integer.parseInt(selectedVenueVO.getId())); if
		 * (type.getVenueTypeId() == Integer.parseInt(selectedVenueVO.getId()))
		 * {
		 * 
		 * localVIList.add(type);
		 * 
		 * }
		 * 
		 * vPkgDtls.put(type.getVenueId(), type); // if(getAddress==false && //
		 * type.getAreaName().equals(areaName)) // { // getAddress = true; //
		 * firstAddress = type.getAddress(); // double lat =
		 * Double.valueOf(type.getLatitude()); // double lng =
		 * Double.valueOf(type.getLongitude()); // current = new
		 * LatitudeLongitude(lat, lng); // } }
		 * 
		 * Query arQry = em.createNamedQuery("AreaTypeMaster.findByAreaId");
		 * arQry.setParameter("areaId", area); AreaTypeMaster atm =
		 * (AreaTypeMaster) arQry.getSingleResult();
		 * 
		 * System.out.println("area  " + area);
		 * 
		 * double lat = Double.valueOf(atm.getLatitude()); double lng =
		 * Double.valueOf(atm.getLongitude()); current = new
		 * LatitudeLongitude(lat, lng);
		 * 
		 * PlacesLatitudes sample = new PlacesLatitudes(); try { //
		 * System.out.println("address "+firstAddress); // localVIList = //
		 * sample
		 * .performSearch(firstAddress+","+areaName+","+cityName,localVIList);
		 * localVIList = sample.calculateDistances(current, localVIList); }
		 * catch (Exception ex) { ex.printStackTrace(); } //
		 * Collections.sort(localVIList, new VenueComparatorByAmount()); } //
		 * else end } // logger.debug("localVIList size -- " +
		 * localVIList.size());
		 * 
		 * if ("Amount".equals(sortBy)) { Collections.sort(localVIList, new
		 * VenueComparatorByAmount()); } else if ("Area".equals(sortBy)) {
		 * 
		 * Collections.sort(localVIList, new VenueComparatorByArea()); } else if
		 * ("Name".equals(sortBy)) { Collections.sort(localVIList, new
		 * VenueComparatorByName()); } return localVIList;
		 * 
		 * // } else { // return new ArrayList<ViewVenue>(); // }
		 */
	}

	/**
	 * @return the venueList
	 */
	public List<String> getVenueList() {
		return venueList;
	}

	/**
	 * @param venueList
	 *            the venueList to set
	 */
	public void setVenueList(List<String> venueList) {
		this.venueList = venueList;
	}

	/**
	 * @return the venueTypeMaster
	 */
	public List<VenueTypeMaster> getVenueTypeMaster() {
		return venueTypeMaster;
	}

	/**
	 * @param venueTypeMaster
	 *            the venueTypeMaster to set
	 */
	public void setVenueTypeMaster(List<VenueTypeMaster> venueTypeMaster) {
		this.venueTypeMaster = venueTypeMaster;
	}

	/**
	 * @return the venueMap
	 */
	public Map getVenueMap() {
		return venueMap;
	}

	/**
	 * @param venueMap
	 *            the venueMap to set
	 */
	public void setVenueMap(Map venueMap) {
		this.venueMap = venueMap;
	}

	/**
	 * @return the venueMapLocal
	 */
	public Map getVenueMapLocal() {
		return venueMapLocal;
	}

	/**
	 * @param venueMapLocal
	 *            the venueMapLocal to set
	 */
	public void setVenueMapLocal(Map venueMapLocal) {
		this.venueMapLocal = venueMapLocal;
	}

	/**
	 * @return the venueRadio
	 */
	public String getVenueRadio() {
		return venueRadio;
	}

	/**
	 * @param venueRadio
	 *            the venueRadio to set
	 */
	public void setVenueRadio(String venueRadio) {
		this.venueRadio = venueRadio;
	}

	/**
	 * @return the selectedServices
	 */
	public String[] getSelectedServices() {
		return selectedServices;
	}

	/**
	 * @param selectedServices
	 *            the selectedServices to set
	 */
	public void setSelectedServices(String[] selectedServices) {
		this.selectedServices = selectedServices;
	}

	/**
	 * @return the guests
	 */
	public List<String> getGuests() {
		return guests;
	}

	/**
	 * @param guests
	 *            the guests to set
	 */
	public void setGuests(List<String> guests) {
		this.guests = guests;
	}

	/**
	 * @return the selectedGuest
	 */
	public String getSelectedGuest() {
		return selectedGuest;
	}

	/**
	 * @param selectedGuest
	 *            the selectedGuest to set
	 */
	public void setSelectedGuest(String selectedGuest) {
		this.selectedGuest = selectedGuest;
	}

	/**
	 * @return the eventList
	 */
	public List<String> getEventList() {
		return eventList;
	}

	/**
	 * @param eventList
	 *            the eventList to set
	 */
	public void setEventList(List<String> eventList) {
		this.eventList = eventList;
	}

	/**
	 * @return the selectedEvent
	 */
	public String getSelectedEvent() {
		return selectedEvent;
	}

	/**
	 * @param selectedEvent
	 *            the selectedEvent to set
	 */
	public void setSelectedEvent(String selectedEvent) {
		this.selectedEvent = selectedEvent;
	}

	/**
	 * @return the serviceList
	 */
	public List<String> getServiceList() {
		return serviceList;
	}

	/**
	 * @param serviceList
	 *            the serviceList to set
	 */
	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

	/**
	 * @return the budgets
	 */
	public List<String> getBudgets() {
		return budgets;
	}

	/**
	 * @param budgets
	 *            the budgets to set
	 */
	public void setBudgets(List<String> budgets) {
		this.budgets = budgets;
	}

	/**
	 * @return the selectedBudget
	 */
	public String getSelectedBudget() {
		return selectedBudget;
	}

	/**
	 * @param selectedBudget
	 *            the selectedBudget to set
	 */
	public void setSelectedBudget(String selectedBudget) {
		this.selectedBudget = selectedBudget;
	}

	/**
	 * @return the haveRecords
	 */
	public boolean isHaveRecords() {
		return haveRecords;
	}

	/**
	 * @param haveRecords
	 *            the haveRecords to set
	 */
	public void setHaveRecords(boolean haveRecords) {
		this.haveRecords = haveRecords;
	}

	/**
	 * @return the vPkgDtls
	 */
	public HashMap getvPkgDtls() {
		return vPkgDtls;
	}

	/**
	 * @param vPkgDtls
	 *            the vPkgDtls to set
	 */
	public void setvPkgDtls(HashMap vPkgDtls) {
		this.vPkgDtls = vPkgDtls;
	}

	/**
	 * @return the venueListt
	 */
	public List<VenueVO> getVenueListt() {
		return venueListt;
	}

	/**
	 * @param venueListt
	 *            the venueListt to set
	 */
	public void setVenueListt(List<VenueVO> venueListt) {
		this.venueListt = venueListt;
	}

	/**
	 * @return the eventDate
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate
	 *            the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the viewVenueList
	 */
	public List<ViewVenue> getViewVenueList() {
		return viewVenueList;
	}

	/**
	 * @param viewVenueList
	 *            the viewVenueList to set
	 */
	public void setViewVenueList(List<ViewVenue> viewVenueList) {
		this.viewVenueList = viewVenueList;
	}

	/**
	 * @return the tCount
	 */
	public Integer gettCount() {
		return tCount;
	}

	/**
	 * @param tCount
	 *            the tCount to set
	 */
	public void settCount(Integer tCount) {
		this.tCount = tCount;
	}

	/**
	 * @return the allVenueList
	 */
	public List<ViewVenue> getAllVenueList() {
		return allVenueList;
	}

	/**
	 * @param allVenueList
	 *            the allVenueList to set
	 */
	public void setAllVenueList(List<ViewVenue> allVenueList) {
		this.allVenueList = allVenueList;
	}

	/**
	 * @return the finalVenueInfo
	 */
	public ViewVenuePackage getFinalVenueInfo() {
		return finalVenueInfo;
	}

	/**
	 * @param finalVenueInfo
	 *            the finalVenueInfo to set
	 */
	public void setFinalVenueInfo(ViewVenuePackage finalVenueInfo) {
		this.finalVenueInfo = finalVenueInfo;
	}

	/**
	 * @return the popupVenueId
	 */
	public String getPopupVenueId() {
		return popupVenueId;
	}

	/**
	 * @param popupVenueId
	 *            the popupVenueId to set
	 */
	public void setPopupVenueId(String popupVenueId) {
		this.popupVenueId = popupVenueId;
	}

	/**
	 * @return the popupViewVenue
	 */
	public ViewVenue getPopupViewVenue() {
		return popupViewVenue;
	}

	/**
	 * @param popupViewVenue
	 *            the popupViewVenue to set
	 */
	public void setPopupViewVenue(ViewVenue popupViewVenue) {
		this.popupViewVenue = popupViewVenue;
	}

	/**
	 * @return the hallList
	 */
	public List<VenueHallInfo> getHallList() {
		return hallList;
	}

	/**
	 * @param hallList
	 *            the hallList to set
	 */
	public void setHallList(List<VenueHallInfo> hallList) {
		this.hallList = hallList;
	}

	/**
	 * @return the hallMap
	 */
	public Map getHallMap() {
		return hallMap;
	}

	/**
	 * @param hallMap
	 *            the hallMap to set
	 */
	public void setHallMap(Map hallMap) {
		this.hallMap = hallMap;
	}

	/**
	 * @return the hallLocalMap
	 */
	public Map getHallLocalMap() {
		return hallLocalMap;
	}

	/**
	 * @param hallLocalMap
	 *            the hallLocalMap to set
	 */
	public void setHallLocalMap(Map hallLocalMap) {
		this.hallLocalMap = hallLocalMap;
	}

	/**
	 * @return the hallIndex
	 */
	public int getHallIndex() {
		return hallIndex;
	}

	/**
	 * @param hallIndex
	 *            the hallIndex to set
	 */
	public void setHallIndex(int hallIndex) {
		this.hallIndex = hallIndex;
	}

	/**
	 * @return the hotelImages
	 */
	public List<String> getHotelImages() {
		return hotelImages;
	}

	/**
	 * @param hotelImages
	 *            the hotelImages to set
	 */
	public void setHotelImages(List<String> hotelImages) {
		this.hotelImages = hotelImages;
	}

	/**
	 * @return the hotelHallImages
	 */
	public List<String> getHotelHallImages() {
		return hotelHallImages;
	}

	/**
	 * @param hotelHallImages
	 *            the hotelHallImages to set
	 */
	public void setHotelHallImages(List<String> hotelHallImages) {
		this.hotelHallImages = hotelHallImages;
	}

	/**
	 * @return the circleModel
	 */
	public MapModel getCircleModel() {
		return circleModel;
	}

	/**
	 * @param circleModel
	 *            the circleModel to set
	 */
	public void setCircleModel(MapModel circleModel) {
		this.circleModel = circleModel;
	}

	/**
	 * @return the hallId
	 */
	public String getHallId() {
		return hallId;
	}

	/**
	 * @param hallId
	 *            the hallId to set
	 */
	public void setHallId(String hallId) {
		this.hallId = hallId;
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

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @return the eventTypeId
	 */
	public int getEventTypeId() {
		return eventTypeId;
	}

	/**
	 * @param eventTypeId
	 *            the eventTypeId to set
	 */
	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	/**
	 * @return the center
	 */
	public String getCenter() {
		return center;
	}

	/**
	 * @param center
	 *            the center to set
	 */
	public void setCenter(String center) {
		this.center = center;
	}

	/**
	 * @param venuePackages
	 *            the venuePackages to set
	 */
	public void setVenuePackages(List<ViewVenuePackage> venuePackages) {
		this.venuePackages = venuePackages;
	}

	/**
	 * @return the popupViewServiceProvider
	 */
	public ViewServiceProvider getPopupViewServiceProvider() {
		for (Iterator<ViewServiceProvider> iterator = viewServiceProviderList.iterator(); iterator.hasNext();) {
			ViewServiceProvider type = (ViewServiceProvider) iterator.next();
			if (String.valueOf(type.getServiceProviderCode()).equalsIgnoreCase(popupServiceCode)) {
				return type;
			}
		}
		return new ViewServiceProvider();
	}

	/**
	 * @param popupViewServiceProvider
	 *            the popupViewServiceProvider to set
	 */
	public ViewServiceProvider setPopupViewServiceProvider(ViewServiceProvider popupViewServiceProvider) {
		this.popupViewServiceProvider = popupViewServiceProvider;
		return popupViewServiceProvider;
	}

	/**
	 * @return the selectedSrvList
	 */
	public List<ServiceVO> getSelectedSrvList() {
		return selectedSrvList;
	}

	/**
	 * @param selectedSrvList
	 *            the selectedSrvList to set
	 */
	public void setSelectedSrvList(List<ServiceVO> selectedSrvList) {
		this.selectedSrvList = selectedSrvList;
	}

	/**
	 * @return the disServiceTab
	 */
	public ArrayList<Boolean> getDisServiceTab() {
		return disServiceTab;
	}

	/**
	 * @param disServiceTab
	 *            the disServiceTab to set
	 */
	public void setDisServiceTab(ArrayList<Boolean> disServiceTab) {
		this.disServiceTab = disServiceTab;
	}

	/**
	 * @return the allSelectedServicesMap
	 */
	public HashMap getAllSelectedServicesMap() {
		return allSelectedServicesMap;
	}

	/**
	 * @param allSelectedServicesMap
	 *            the allSelectedServicesMap to set
	 */
	public void setAllSelectedServicesMap(HashMap allSelectedServicesMap) {
		this.allSelectedServicesMap = allSelectedServicesMap;
	}

	/**
	 * @return the area
	 */
	public Integer getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(Integer area) {
		this.area = area;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy
	 *            the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * @return the viewServiceProviderList
	 */
	public List<ViewServiceProvider> getViewServiceProviderList() {
		return viewServiceProviderList;
	}

	/**
	 * @param viewServiceProviderList
	 *            the viewServiceProviderList to set
	 */
	public void setViewServiceProviderList(List<ViewServiceProvider> viewServiceProviderList) {
		this.viewServiceProviderList = viewServiceProviderList;
	}

	/**
	 * @return the popupServiceCode
	 */
	public String getPopupServiceCode() {
		return popupServiceCode;
	}

	/**
	 * @param popupServiceCode
	 *            the popupServiceCode to set
	 */
	public void setPopupServiceCode(String popupServiceCode) {
		this.popupServiceCode = popupServiceCode;
	}

	/**
	 * @param allSelectedServices
	 *            the allSelectedServices to set
	 */
	public void setAllSelectedServices(ArrayList allSelectedServices) {
		this.allSelectedServices = allSelectedServices;
	}

	/**
	 * @return the srvList
	 */
	public List<ServiceTypeMaster> getSrvList() {
		return srvList;
	}

	/**
	 * @param srvList
	 *            the srvList to set
	 */
	public void setSrvList(List<ServiceTypeMaster> srvList) {
		this.srvList = srvList;
	}

	/**
	 * @return the hotelImagesSmall
	 */
	public List<String> getHotelImagesSmall() {
		return hotelImagesSmall;
	}

	/**
	 * @param hotelImagesSmall
	 *            the hotelImagesSmall to set
	 */
	public void setHotelImagesSmall(List<String> hotelImagesSmall) {
		this.hotelImagesSmall = hotelImagesSmall;
	}

	/**
	 * @return the serviceImagesSmall
	 */
	public List<String> getServiceImagesSmall() {
		return serviceImagesSmall;
	}

	/**
	 * @param serviceImagesSmall
	 *            the serviceImagesSmall to set
	 */
	public void setServiceImagesSmall(List<String> serviceImagesSmall) {
		this.serviceImagesSmall = serviceImagesSmall;
	}

	/**
	 * @return the pCost
	 */
	public BigDecimal getpCost() {
		return pCost;
	}

	/**
	 * @param pCost
	 *            the pCost to set
	 */
	public void setpCost(BigDecimal pCost) {
		this.pCost = pCost;
	}

	/**
	 * @return the servicePackges
	 */
	public List<ViewServiceProviderPackage> getServicePackges() {
		logger.debug("ServiceDetailsManagedBean::::serviceProviderId::::" + serviceProviderId);
		List<ViewServiceProviderPackage> list;
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			Query query = em.createNamedQuery("ViewServiceProviderPackage.findBySrvPrvdIdEType");
			ServiceProviderInfo spInfo = new ServiceProviderInfo(new Integer(serviceProviderId).intValue());
			query.setParameter("serviceProviderId", serviceProviderId);
			query.setParameter("eventType", eventTypeId);
			servicePackges = query.getResultList();
			logger.debug("ServiceDetailsManagedBean::::list::::" + servicePackges);
		} finally {

		}
		return servicePackges;
	}

	/**
	 * @param servicePackges
	 *            the servicePackges to set
	 */
	public void setServicePackges(List<ViewServiceProviderPackage> servicePackges) {
		this.servicePackges = servicePackges;
	}

	/**
	 * @return the chkServicePop
	 */
	public boolean isChkServicePop() {
		return chkServicePop;
	}

	/**
	 * @param chkServicePop
	 *            the chkServicePop to set
	 */
	public void setChkServicePop(boolean chkServicePop) {
		this.chkServicePop = chkServicePop;
	}

	/**
	 * @return the selService
	 */
	public ServiceVO getSelService() {
		return selService;
	}

	/**
	 * @param selService
	 *            the selService to set
	 */
	public void setSelService(ServiceVO selService) {
		this.selService = selService;
	}

	/**
	 * @return the servicesImages
	 */
	public List<String> getServicesImages() {
		return servicesImages;
	}

	/**
	 * @param servicesImages
	 *            the servicesImages to set
	 */
	public void setServicesImages(List<String> servicesImages) {
		this.servicesImages = servicesImages;
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
	 * @return the serviceProviderId
	 */
	public int getServiceProviderId() {
		return serviceProviderId;
	}

	/**
	 * @param serviceProviderId
	 *            the serviceProviderId to set
	 */
	public void setServiceProviderId(int serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
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

}
