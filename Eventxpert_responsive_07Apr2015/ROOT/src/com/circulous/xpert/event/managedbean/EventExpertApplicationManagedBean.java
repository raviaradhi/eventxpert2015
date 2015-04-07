package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.circulous.xpert.jpa.entities.views.ViewServiceProvider;
import com.circulous.xpert.jpa.entities.views.ViewServiceProvider_;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo_;
import com.circulous.xpert.jpa.entities.views.ViewVenue;
import javax.persistence.*;
import javax.persistence.criteria.*;

@ManagedBean(eager = true)
@ApplicationScoped
public class EventExpertApplicationManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2027481617101628258L;

	private List<Event> eventList;
	private HashMap eventTypeMap;

	private final static Logger logger = Logger.getLogger(EventExpertApplicationManagedBean.class);

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
	EntityManager em = emf.createEntityManager();
	
	
	private List<AreaTypeMaster> lAreaList = new ArrayList<AreaTypeMaster>();

	private List<CityTypeMaster> list = new ArrayList<CityTypeMaster>();
	
	private List<ServiceTypeMaster> serviceTypeMaster = new ArrayList<ServiceTypeMaster>();
	
	private List<VenueTypeMaster> venueTypeMaster = new ArrayList<VenueTypeMaster>();
	
	
//	private List<ServiceProviderInfo> spInfoList = new ArrayList<ServiceProviderInfo>();
//
//	private List<VenueInfo> venueInfoList = new ArrayList<VenueInfo>();
//	
	private List<ViewServiceProvider> viewServiceProviderList = new ArrayList<ViewServiceProvider>();

	private List<ViewVenue> viewVenueList = new ArrayList<ViewVenue>();
	
	
	public EventExpertApplicationManagedBean() {
		logger.debug("::::::::::::::::  Constructor of EventExpertApplicationManagedBean");
	}

	@PostConstruct
	public void createEventList() {

		logger.debug("EventExpertApplicationManagedBean:: createEventList ::");
		eventList = new ArrayList<Event>();
		eventTypeMap = new HashMap();
		
		Query query = em.createNamedQuery("EventTypeMaster.findByIsActive");
                query.setParameter("isActive", 'Y');
		List<EventTypeMaster> etmList = query.getResultList();
		for (Iterator<EventTypeMaster> iterator = etmList.iterator(); iterator.hasNext();) {
			EventTypeMaster eventTypeMaster = iterator.next();
			int eventid = eventTypeMaster.getEventId();
			String event = eventTypeMaster.getEventType();
                        String color = "";
                        if(eventid==1)
                        {
                            color = "#38a7de";//"#f9ca00";//"#A87878";
                        }
                        if(eventid==2)
                        {
                            color = "#a62890";//"#eb8800";//#e8b319";
                        }
                        if(eventid==3)
                        {
                            color = "#e7258f";//"#d52d5b";//#e5715d";
                        }
                        if(eventid==4)
                        {
                            color = "#f39320";//"#07b5c0";//73b6c1";//#3fc6db";
                        }
                        if(eventid==5)
                        {
                            color = "#999933";//"#f8ef1f";//"#8865a8";//#db3f58";
                        }
                        if(eventid==6)
                        {
                            color = "#7dbf42";//"#86c222";//#8ccc44";
                        }
                        
			eventList.add(new Event(eventid, event,color, eventTypeMaster.getEventDescription()));
			eventTypeMap.put(eventid, event);
		}
		
		
		Query qryCityMaster = em.createNamedQuery("CityTypeMaster.findAll");
		list = qryCityMaster.getResultList();
		
//		Query areaquery = em.createNamedQuery("AreaTypeMaster.findAll");
//                Query areaquery = em.createQuery("SELECT a FROM AreaTypeMaster a, ServiceProviderInfo s, VenueInfo v where  a.areaId = v.areaId or a.areaId = s.areaId");
//                Query areaquery = em.createNativeQuery("SELECT distinct a.area_Id FROM Area_Type_Master a, Service_Provider_Info s, Venue_Info v where  a.area_Id = v.area_Id or a.area_Id = s.area_Id");
                
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<AreaTypeMaster> criteriaQuery = cb.createQuery(AreaTypeMaster.class);
                Root<AreaTypeMaster> atmaster = criteriaQuery.from(AreaTypeMaster.class);                       
                CriteriaQuery<AreaTypeMaster> select = criteriaQuery.select(atmaster);                
                Root<ServiceProviderInfo> sp = select.from(ServiceProviderInfo.class);
                Root<VenueInfo> vi = select.from(VenueInfo.class); 
                select.where(cb.or(cb.equal(sp.get(ServiceProviderInfo_.areaId), atmaster),
                        cb.equal(vi.get(VenueInfo_.areaId), atmaster)));
                TypedQuery<AreaTypeMaster> areaquery = em.createQuery(select);
		lAreaList = areaquery.getResultList();

                /************************************/
		Query qryVenueMasterList = em.createNamedQuery("VenueTypeMaster.findByIsActive");
		qryVenueMasterList.setParameter("isActive", 'Y');
		venueTypeMaster = qryVenueMasterList.getResultList();
//                
           /*     CriteriaQuery<VenueTypeMaster> criteriaQueryV = cb.createQuery(VenueTypeMaster.class);
                Root<VenueTypeMaster> vtmaster = criteriaQueryV.from(VenueTypeMaster.class);                       
                CriteriaQuery<VenueTypeMaster> selectV = criteriaQueryV.select(vtmaster);                
//                selectV.where(cb.equal(vtmaster.get(VenueTypeMaster_.isActive), 'Y'));
                Subquery<VenueInfo> cqV = criteriaQueryV.subquery(VenueInfo.class);
                Root<VenueInfo> viT = selectV.from(VenueInfo.class); 
//                Root<VenueTypeMaster> vTp = selectV.from(VenueInfo.class); 
                Subquery<VenueInfo> selectVCq = cqV.select(viT); 
//                cqV.select(viT);
                selectVCq.where(cb.equal(viT.get(VenueInfo_.venueTypeId), vtmaster));                
//                 selectV.where(cb.and(cb.equal(vtmaster.get(VenueTypeMaster_.isActive), 'Y'),
//                        cb.exists(selectVCq)));
                 selectV.where(cb.or(cb.or(cb.equal(vtmaster.get(VenueTypeMaster_.venueTypeId), 1)),cb.and(cb.equal(vtmaster.get(VenueTypeMaster_.isActive), 'Y'),
                        cb.exists(selectVCq))));
                System.out.println("selectV "+selectV.toString());
                TypedQuery<VenueTypeMaster> qryVenueMasterList = em.createQuery(selectV);
                venueTypeMaster = qryVenueMasterList.getResultList();
                System.out.println("selectV "+qryVenueMasterList.toString()); */

                /************************************************************************/
                
		//Query qryServiceTypeMasterList = em.createNamedQuery("ServiceTypeMaster.findByIsActive");
                Query qryServiceTypeMasterList = em.createQuery("SELECT s FROM ServiceTypeMaster s WHERE s.isActive = :isActive and s.serviceTypeCode <> :serviceTypeCode");
		qryServiceTypeMasterList.setParameter("isActive", 'Y');
                qryServiceTypeMasterList.setParameter("serviceTypeCode", "VEN");
		serviceTypeMaster = qryServiceTypeMasterList.getResultList();
                
               /* CriteriaQuery<ServiceTypeMaster> criteriaQueryS = cb.createQuery(ServiceTypeMaster.class);
                Root<ServiceTypeMaster> stmaster = criteriaQueryS.from(ServiceTypeMaster.class);                       
                CriteriaQuery<ServiceTypeMaster> selectS = criteriaQueryS.select(stmaster);                
                
                Subquery<ServiceProviderInfo> cqS = criteriaQueryS.subquery(ServiceProviderInfo.class);
                Root<ServiceProviderInfo> siT = selectS.from(ServiceProviderInfo.class); 
                cqS.select(siT);
                cqS.where(cb.equal(siT.get(ServiceProviderInfo_.serviceTypeCode), stmaster));  
                selectS.where(cb.and(cb.equal(stmaster.get(ServiceTypeMaster_.isActive), 'Y'),
                        cb.notEqual(stmaster.get(ServiceTypeMaster_.serviceTypeCode), "VEN"),cb.exists(cqS)));
//                selectS.where(cb.exists(cqS));
                System.out.println("selectS "+selectS.toString());
                TypedQuery<ServiceTypeMaster> qryServiceTypeMasterList = em.createQuery(selectS);
                serviceTypeMaster = qryServiceTypeMasterList.getResultList();
                System.out.println("selectS "+qryServiceTypeMasterList.toString());
                */
                
		/*Query qryVenueList = em.createNamedQuery("VenueInfo.findAll");
		venueInfoList = qryVenueList.getResultList();

		Query qryServiceMasterList = em.createNamedQuery("ServiceProviderInfo.findAll");
		spInfoList = qryServiceMasterList.getResultList(); */

		Query qryViewServiceProviderList = em.createNamedQuery("ViewServiceProvider.findAll");
		viewServiceProviderList = qryViewServiceProviderList.getResultList();

		Query qryViewVenueList = em.createNamedQuery("ViewVenue.findAll");
		viewVenueList = qryViewVenueList.getResultList();
		
	}

	public List<AreaTypeMaster> fetchlAreaList() {
		return lAreaList;
	}

	public List<CityTypeMaster> fetchList() {
		return list;
	}

	public List<ServiceTypeMaster> fetchServiceTypeMaster() {
		return serviceTypeMaster;
	}

	public List<VenueTypeMaster> fetchVenueTypeMaster() {
		return venueTypeMaster;
	}

//	public List<ServiceProviderInfo> fetchSpInfoList() {
//		return spInfoList;
//	}
//
//	public List<VenueInfo> fetchVenueInfoList() {
//		return venueInfoList;
//	}
//
	public List<ViewServiceProvider> fetchViewServiceProviderList() {
		return viewServiceProviderList;
	}

	public List<ViewVenue> fetchViewVenueList() {
		return viewVenueList;
	}

	public List<Event> fetchEventList() {
		logger.debug("EventExpertApplicationManagedBean:: fetchEventList ::");
		return eventList;
	}
	
	public HashMap fetchEventTypeMap() {
		logger.debug("EventExpertApplicationManagedBean:: fetchEventTypeMap ::");
		return eventTypeMap;
	}

	@PreDestroy
	public void cleanup() {
		logger.debug("EventExpertApplicationManagedBean:: cleanup ::");
		em.close();
	}

}
