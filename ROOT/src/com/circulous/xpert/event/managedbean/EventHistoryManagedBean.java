/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.event.comparator.EventDateComparator;
import com.circulous.xpert.jpa.entities.*;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;
//import java.io.Serializable;

/**
 *
 * @author SANJANA
 */
@ManagedBean
@ViewScoped
//@SessionScoped
public class EventHistoryManagedBean {//implements Serializable{

    private final static Logger logger = Logger.getLogger(EventHistoryManagedBean.class);
    /**
     * Creates a new instance of EventHistoryManagedBean
     */
    public EventHistoryManagedBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        /*EventManagedBean eventManagedBean = (EventManagedBean) context.getApplication().evaluateExpressionGet(context, "#{eventManagedBean}", EventManagedBean.class);
        setEventType(eventManagedBean.getEventType());
        setEventTypeId(eventManagedBean.getEventTypeId());
        */
        
        try
        {
            userId = (CustomerInfo) context.getExternalContext().getSessionMap().get("userId");
//            logger.debug("userId*********  "+userId.getUserId());
            cid = userId.getCustomerId();
            
            eventSet = getEventInfo().entrySet();
//            eventSet = (Set<Entry<EventInfo, HashMap>>) getEventInfo();//.entrySet();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        finally
        {
             
        }
    }
    
    // Set<Map.Entry<EventInfo, HashMap>> eventSet;
     Set<Map.Entry<EventInfo, HashMap>> eventSet;
     
    int cid = 0;
    CustomerInfo userId = null ;
    
    private String eventType;
    private int eventTypeId;
    
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
    
    public String submitForm()
        {
            logger.debug("submitForm History");            
            logger.debug("submitForm***History  "+eventType);
            return "event-details";
        }
    public String submitFormListener(ActionEvent ae)
        {
            logger.debug("submitForm History Listener");            
            logger.debug("submitForm***History  "+eventType);
            return "event-details";
        }
    
    private EventInfo eventInfoSingle;
    public Map eventInfo = new HashMap();
    private ArrayList<EspPackageInfo> espInfo = new ArrayList<EspPackageInfo>();
    private ArrayList<EspPackageItemInfo> espItemInfo = new ArrayList<EspPackageItemInfo>();
    private ArrayList<EventServiceProviderInfo> espServiceInfo = new ArrayList<EventServiceProviderInfo>();
    private String area;
    private String city;
    private String eventTypeS;
    private String venueType;
    private int eventId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventTypeS() {
        return eventTypeS;
    }

    public void setEventTypeS(String eventTypeS) {
        this.eventTypeS = eventTypeS;
    }

    public CustomerInfo getUserId() {
        return userId;
    }

    public void setUserId(CustomerInfo userId) {
        this.userId = userId;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }
    
    
    
    public EventInfo getEventInfoSingle() {
        return eventInfoSingle;
    }

    public void setEventInfoSingle(EventInfo eventInfoSingle) {
        this.eventInfoSingle = eventInfoSingle;
    }
    
    
    
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
    
    
    public void getSelEvent1(int eventid)
    {
        try
        {
            eventId = eventid;
            logger.debug("Inside getSelEvent "+eventId);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
            EntityManager em = emf.createEntityManager();
            logger.debug("Inside getSelEvent after em "+eventId);
            Query query = em.createNamedQuery("EventInfo.findByEventId");
            query.setParameter("eventId", eventId);
            logger.debug("Inside getSelEvent query "+query);            
            EventInfo eventInfoSingle1 = (EventInfo) query.getResultList().get(0);
            logger.debug("Inside getSelEvent eventInfo "+eventInfoSingle);
            area = eventInfoSingle1.getAreaId().getAreaName();
            //logger.debug("eventInfo.getEventType().getEventType()  "+eventInfoSingle.getAreaId().getCityId());
            city = eventInfoSingle1.getAreaId().getCityId().getCityName();
            eventTypeS = eventInfoSingle1.getEventType().getEventType();
            venueType = eventInfoSingle1.getVenueType().getVenueType();
//            eventInfoSingle.add(eventInfoSingle1);
//            eventInfoSingle.add(getEventSpDtls(eventInfoSingle1));
            eventInfoSingle = eventInfoSingle1;
            getEventSpDtls1(eventInfoSingle);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
             
        }
//        RequestContext.getCurrentInstance().execute("eventDialog.show()");
    }
    
    public Map getEventInfo() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		logger.debug("EventManagedBean Constructing--------------Begin");
		EntityManager em = emf.createEntityManager();
                List eventInfolist = null;
                
		try {
                        eventInfo = new TreeMap();
                        
			Query query = em.createQuery("Select ei from EventInfo ei where ei.customerId = :customerId order by ei.eventId");
                        query.setParameter("customerId", userId);
			List eList =  query.getResultList();
			
                        for(int j=0;j<eList.size();j++)
                        {
                            EventInfo evt = (EventInfo) eList.get(j);
                            eventInfo.put(evt,getEventSpDtls(evt));
                        }
                        TreeMap t = (TreeMap) eventInfo;
                      //  eventInfo = new TreeMap(eventInfo);
                      //  TreeMap t = new TreeMap()
//                        eventInfolist = new ArrayList(eventInfo.keySet());
//                        Collections.sort(eventInfolist, EventInfo.eventDateComparator);
                        eventInfo = t.descendingMap();
                    } 
                catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                finally {
			//logger.debug("EventManagedBean Constructing--------------Begineeeee");
			em.close();  
		}
        
        
        return eventInfo;
    }
    
    public Map allEvents;

    public Map getAllEvents() {
       
       // ArrayList li = new ArrayList<Map.Entry<EventInfo, HashMap>>(eventSet);
     //   ArrayList li = (ArrayList) eventInfo.entrySet();
       // Collections.sort(li);
        return eventInfo;
    }

    public void setAllEvents(Map allEvents) {
        this.allEvents = allEvents;
    }
    
    

    public void setEventInfo(HashMap eventInfo) {
        this.eventInfo = eventInfo;
    }
    
    private HashMap services = new HashMap();    
    private HashMap venues = new HashMap();
    
    private HashMap servicesD = new HashMap();    
    private HashMap venuesD = new HashMap();
    private ArrayList<EspPackageInfo> espInfoD = new ArrayList<EspPackageInfo>();
    private ArrayList<EspPackageItemInfo> espItemInfoD = new ArrayList<EspPackageItemInfo>();
    private ArrayList<EventServiceProviderInfo> espServiceInfoD = new ArrayList<EventServiceProviderInfo>();
    

    public HashMap getServicesD() {
        logger.debug("servicesD*  "+servicesD);
        return servicesD;
    }

    public void setServicesD(HashMap servicesD) {
        this.servicesD = servicesD;
    }

    public HashMap getVenuesD() {
        return venuesD;
    }

    public void setVenuesD(HashMap venuesD) {
        this.venuesD = venuesD;
    }
    
    

    public HashMap getServices() {
        return services;
    }

    public void setServices(HashMap services) {
        this.services = services;
    }

    public HashMap getVenues() {
        return venues;
    }

    public void setVenues(HashMap venues) {
        this.venues = venues;
    }
    
            
    public ArrayList getEventSpDtls(EventInfo eId)
    {
        ArrayList allservices = new ArrayList();
        logger.debug("Inside getEventDtls venues venues "+venues);
        try 
        {
            this.services = new HashMap();    
            this.venues = new HashMap();
            this.espInfo = new ArrayList<EspPackageInfo>();
            this.espItemInfo = new ArrayList<EspPackageItemInfo>();
            this.espServiceInfo = new ArrayList<EventServiceProviderInfo>();
    
            logger.debug("Inside getEventDtls "+eId);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
            EntityManager em = emf.createEntityManager();
            logger.debug("Inside getEventDtls after em "+eId);
            
            Query queryEspi = em.createNamedQuery("EventServiceProviderInfo.findByEventId");
            queryEspi.setParameter("eventId", eId);
            
            espServiceInfo = new ArrayList<EventServiceProviderInfo>(queryEspi.getResultList());
            
            for(int j=0;j<espServiceInfo.size();j++)
            {
               EventServiceProviderInfo espi =  espServiceInfo.get(j);
               logger.debug("espi.getVenue() "+espi.getVenue());
               if(espi.getVenue().equals('Y'))
               {
                   HashMap servicesInfo = new HashMap();
                   
                   Query queryEspi1 = em.createNamedQuery("VenueInfo.findByVenueId");
                   queryEspi1.setParameter("venueId", espi.getServiceProviderId());
                   
                   VenueInfo vinfo = (VenueInfo) queryEspi1.getResultList().get(0);
                   
                   Query queryAi = em.createNamedQuery("AddressInfo.findByAddressId");
                   queryAi.setParameter("addressId", vinfo.getAddressId().getAddressId());
                   logger.debug("queryAi******** "+queryAi);
                   AddressInfo ai  = (AddressInfo) queryAi.getResultList().get(0);
                   
                  // servicesInfo.put(vinfo.getVenueName(), espi);
                    servicesInfo.put(vinfo, espi);
                   venues.put("Venue", servicesInfo);
               }
               else
               {
                   HashMap servicesInfo = new HashMap();
                   
                   Query queryEspi1 = em.createNamedQuery("ServiceProviderInfo.findByServiceProviderId");
                   queryEspi1.setParameter("serviceProviderId", espi.getServiceProviderId());
                   
                   ServiceProviderInfo spi = (ServiceProviderInfo) queryEspi1.getResultList().get(0);
                   
                   Query queryEspi11 = em.createNamedQuery("ServiceTypeMaster.findByServiceTypeCode");
                   queryEspi11.setParameter("serviceTypeCode", spi.getServiceTypeCode().getServiceTypeCode());
                   logger.debug("queryEspi11******** "+queryEspi11);
                   ServiceTypeMaster stm = (ServiceTypeMaster) queryEspi11.getResultList().get(0);
                   
                   Query queryAi = em.createNamedQuery("AddressInfo.findByAddressId");
                   queryAi.setParameter("addressId", spi.getAddressId().getAddressId());
                   logger.debug("queryAi******** "+queryAi);
                   AddressInfo ai  = (AddressInfo) queryAi.getResultList().get(0);
                   
                   
                   servicesInfo.put(spi, espi);
                   //servicesInfo.put(spi.getServiceProviderName(), espi);
                   services.put(stm.getServiceName(), servicesInfo);
               }
            }
            Set<Map.Entry<EventInfo, HashMap>> venueset = venues.entrySet();
            Set<Map.Entry<EventInfo, HashMap>> servicesset = services.entrySet();
            allservices.add(new ArrayList<Map.Entry<EventInfo, HashMap>>(venueset));
            allservices.add(new ArrayList<Map.Entry<EventInfo, HashMap>>(servicesset));
	} 
        catch(Exception e)
        { 
            e.printStackTrace();
        }
        finally 
        {
             
	}
       return allservices;
    }
    
     public void getEventSpDtls1(EventInfo eId)
    {
        //ArrayList allservices = new ArrayList();   
        List<ServiceVO> selServices = new ArrayList<ServiceVO>();   
        logger.debug("Inside getEventDtls venues venues "+venues);
        try 
        {
            this.servicesD = new HashMap();    
            this.venuesD = new HashMap();
            this.espInfoD = new ArrayList<EspPackageInfo>();
            this.espItemInfoD = new ArrayList<EspPackageItemInfo>();
            this.espServiceInfoD = new ArrayList<EventServiceProviderInfo>();
    
            logger.debug("Inside getEventDtlsSSSSSSS "+eId);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
            EntityManager em = emf.createEntityManager();
            logger.debug("Inside getEventDtlsSSSSSSSSSS after em "+eId.getEventId());
            
            Query queryEspi = em.createNamedQuery("EventServiceProviderInfo.findByEventId");
            queryEspi.setParameter("eventId", eId);
            
            espServiceInfoD = new ArrayList<EventServiceProviderInfo>(queryEspi.getResultList());
            
            for(int j=0;j<espServiceInfoD.size();j++)
            {
               EventServiceProviderInfo espi =  espServiceInfoD.get(j);
               logger.debug("espi.getVenue() "+espi.getVenue());
               if(espi.getVenue().equals('Y'))
               {
                   HashMap servicesInfo = new HashMap();
                   
                   Query queryEspi1 = em.createNamedQuery("VenueInfo.findByVenueId");
                   queryEspi1.setParameter("venueId", espi.getServiceProviderId());
                   
                   VenueInfo vinfo = (VenueInfo) queryEspi1.getResultList().get(0);
                   
                   Query queryAi = em.createNamedQuery("AddressInfo.findByAddressId");
                   queryAi.setParameter("addressId", vinfo.getAddressId().getAddressId());
                   logger.debug("queryAi******** "+queryAi);
                   AddressInfo ai  = (AddressInfo) queryAi.getResultList().get(0);
                   
                  // servicesInfo.put(vinfo.getVenueName(), espi);
                    servicesInfo.put(vinfo, espi);
                   venuesD.put("Venue", servicesInfo);
               }
               else
               {
                   HashMap servicesInfo = new HashMap();
                   
                   Query queryEspi1 = em.createNamedQuery("ServiceProviderInfo.findByServiceProviderId");
                   queryEspi1.setParameter("serviceProviderId", espi.getServiceProviderId());
                   
                   ServiceProviderInfo spi = (ServiceProviderInfo) queryEspi1.getResultList().get(0);
                   
                   Query queryEspi11 = em.createNamedQuery("ServiceTypeMaster.findByServiceTypeCode");
                   queryEspi11.setParameter("serviceTypeCode", spi.getServiceTypeCode().getServiceTypeCode());
                   logger.debug("queryEspi11******** "+queryEspi11);
                   ServiceTypeMaster stm = (ServiceTypeMaster) queryEspi11.getResultList().get(0);
                   selServices.add(new ServiceVO(String.valueOf(stm.getServiceTypeCode()), "images/event-img3.jpg", stm
						.getServiceName(), stm.getServiceDescription()));
				
                   
                   Query queryAi = em.createNamedQuery("AddressInfo.findByAddressId");
                   queryAi.setParameter("addressId", spi.getAddressId().getAddressId());
                   logger.debug("queryAi******** "+queryAi);
                   AddressInfo ai  = (AddressInfo) queryAi.getResultList().get(0);
                   
                   
                   servicesInfo.put(spi, espi);
                   //servicesInfo.put(spi.getServiceProviderName(), espi);
                   servicesD.put(stm.getServiceName(), servicesInfo);
               }
            }
            Set<Map.Entry<EventInfo, HashMap>> venueset = venuesD.entrySet();
            Set<Map.Entry<EventInfo, HashMap>> servicesset = servicesD.entrySet();
//            allservices.add(new ArrayList<Map.Entry<EventInfo, HashMap>>(venueset));
//            allservices.add(new ArrayList<Map.Entry<EventInfo, HashMap>>(servicesset));
            
            EventDetailsManagedBean edmb = new EventDetailsManagedBean();
            edmb.setFinalVenueMap(venuesD);
            edmb.setFinalServicesMap(servicesD);
            edmb.setEventDate(eId.getEventEffectiveDate());
            edmb.setAreaName(area);
            edmb.setCityName(city);
            edmb.setVenueType(eId.getVenueType().getVenueDescription());
            edmb.setTotalCount(String.valueOf(eId.getEstimatedGuests()));
            edmb.setSelectedSrvList(selServices);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("eventDetailsManagedBean",edmb);
            
	} 
        catch(Exception e)
        { 
            e.printStackTrace();
        }
        finally 
        {
             
	}
//       return allservices;
    }
     
    /* public List sortByDate()
     {
         logger.debug("allEvents*******  "+allEvents);
         Collections.sort(allEvents, new EventDateComparator());
         return allEvents;
     }*/
}
