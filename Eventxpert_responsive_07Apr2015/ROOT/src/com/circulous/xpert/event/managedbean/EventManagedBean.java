package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.CustomerInfo;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;


@ManagedBean
@SessionScoped
public class EventManagedBean  implements Serializable {

    private final static Logger logger = Logger.getLogger(EventManagedBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private final static Logger logger = Logger.getLogger(EventManagedBean.class);

	private List<Event> eventList;
        
        private String eventType;
        private int eventTypeId;
        
        private String themeSel="exTheme";//"blitzer";
        private int sessionTimeOut = 20;
	@SuppressWarnings("unchecked")
	public EventManagedBean() {


	ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
	logger.debug(exContext.getApplicationMap().containsKey("eventExpertApplicationManagedBean"));
	
	EventExpertApplicationManagedBean eAppManagedBean = (EventExpertApplicationManagedBean) exContext.getApplicationMap().get("eventExpertApplicationManagedBean");
	logger.debug("EventExpertApplicationManagedBean----"+eAppManagedBean);
	logger.debug("EventManagedBean Constructing--------------Begin");
	
	if(exContext.getSessionMap().containsKey("eventDetailsManagedBean"))
    {
           	exContext.getSessionMap().remove("eventDetailsManagedBean");  
    }
                
    exContext.invalidateSession();
    exContext.getSessionMap().clear();
                
    eventList = eAppManagedBean.fetchEventList();
        
	/*	eventList = new ArrayList<Event>();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		logger.debug("EventManagedBean Constructing--------------Begin");
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createNamedQuery("EventTypeMaster.findAll");
			List<EventTypeMaster>  list = query.getResultList();
			for (Iterator<EventTypeMaster> iterator = list.iterator(); iterator.hasNext();) {
				EventTypeMaster eventTypeMaster = iterator.next();
                                int eventid = (int) eventTypeMaster.getEventId();
				String event = eventTypeMaster.getEventType();
				eventList.add(new Event(eventid, event));
			}
		} finally {
                    String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
                    HttpServletRequest  request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    //String url = request.getRequestURL();
                    logger.debug("request   "+request.getRequestURL()+"     viewId "+viewId+" viewRoot "+FacesContext.getCurrentInstance().getViewRoot().toString());
                
			logger.debug("EventManagedBean Constructing--------------Begineeeee");
			em.close(); //  
		}*/
		logger.debug("EventManagedBean Constructed--------------End");
                
//                PlacesLatitudes pl = new PlacesLatitudes();
//                pl.enterLat();
	}

	public List<Event> getEventDetails() {            
		return eventList;
	}

    public String getThemeSel() {
        return themeSel;
    }

    public void setThemeSel(String themeSel) {
        this.themeSel = themeSel;
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
    
    
        
        
        public String submitForm(int id,String type)
        {
            logger.debug("submitForm");
            setEventType(type);
            setEventTypeId(id);
            logger.debug("submitForm***  "+eventType);
            themeSel = eventType.toLowerCase();
            //return "eventHistory?faces-redirect=true";
//            return "eventHome?faces-redirect=true";
             return "event-details?faces-redirect=true";
        }
        
        public String eventForm(int id,String type)
        {
            logger.debug("submitForm");
            setEventType(type);
            setEventTypeId(id);
            logger.debug("submitForm***  "+eventType);
            //if(id==1)
            {
                themeSel = eventType.toLowerCase();//"birthday";
            }
          /*  else
            {
                themeSel = "blitzer";
            } */
            return "event-details?faces-redirect=true";
//            return "event-details.xhtml";
        }
        
        public String eventForm1()
        {
            logger.debug("submitForm");
//            setEventType(type);
//            setEventTypeId(id);
            logger.debug("submitForm***  "+eventType);
            return "event-details?faces-redirect=true";
//            return "event-details.xhtml";
        }
        
        public String loginForm()
        {
           return "customerRegister"; 
        }
        
        private CustomerInfo userId ;

        public CustomerInfo getUserId() {
            logger.debug("userId*****  "+userId);
            userId = (CustomerInfo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            logger.debug("userId*********  "+userId);
            return userId;
        }

        public void setUserId(CustomerInfo userId) {
            this.userId = userId;
        }
}
