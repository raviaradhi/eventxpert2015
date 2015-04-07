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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import com.circulous.xpert.event.comparator.VenueComparatorByAmount;
import com.circulous.xpert.jpa.entities.EventServiceProviderInfo;
import com.circulous.xpert.jpa.entities.EventServiceProviderInfo_;
import com.circulous.xpert.jpa.entities.EventTypeMaster;
import com.circulous.xpert.jpa.entities.ServiceTypeMaster;
import com.circulous.xpert.jpa.entities.VenueHallInfo;
import com.circulous.xpert.jpa.entities.VenueInfo;
import com.circulous.xpert.jpa.entities.VenueTypeMaster;
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

	private List<String> services;

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

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
	EntityManager em = emf.createEntityManager();

	public EventsManagedBean() {

		populateVenueList();

		services = new ArrayList<String>();
		services.add("Catering");
		services.add("Decorator");
		services.add("Entertainment");
		services.add("Photography");

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
		return totalCount;
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
		List<ServiceTypeMaster> sList = queryService.getResultList();
		for (Iterator<ServiceTypeMaster> iterator = sList.iterator(); iterator.hasNext();) {
			ServiceTypeMaster srvTypeMaster = iterator.next();
			serviceList.add(srvTypeMaster.getServiceName());
		}

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

		setAllVenueList(getAllVenueList1());

		return "newResults";

	}

	public String getCost(BigDecimal cost) {
		return cost.stripTrailingZeros().toPlainString();
	}

	public String selectedVenueVals(int venueId) {

		setPopupVenueId(String.valueOf(2));
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

	public List<ViewVenuePackage> getVenuePackages() {

		logger.debug("getVenuePackges:1::venueId" + popupVenueId);
		// List<VenuePackageInfo> list;

		VenueInfo vi = new VenueInfo(Integer.parseInt(popupVenueId));

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			// Query query =
			// em.createNamedQuery("VenuePackageInfo.findByVenueId");
			// Query query =
			// em.createNamedQuery("ViewVenuePackage.findByVenueId");
			Query query = em.createNamedQuery("ViewVenuePackage.findByVenueIdEType");
			query.setParameter("venueId", Integer.parseInt(popupVenueId));
			query.setParameter("eventType", eventTypeId);

			venuePackages = query.getResultList();

		} finally {

		}

		// logger.debug("getVenuePackges:2::packageId" + packageId);
		return venuePackages;
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
	 * @return the services
	 */
	public List<String> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<String> services) {
		this.services = services;
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

}
