/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.event.comparator.AreaTypeComparatoreByAreaName;
import com.circulous.xpert.jpa.entities.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.primefaces.context.RequestContext;
/**
 *
 * @author SANJANA
 */
@ManagedBean
//@ViewScoped
//@RequestScoped
@SessionScoped
public class ActivationManagedBean implements Serializable {
private final static Logger logger = Logger.getLogger(ActivationManagedBean.class);

//    @ManagedProperty(value="#{param.key}")
    private String key;
    //ty -- type of link C: Customer, V:Vendor
    private String ty; 
    
    private boolean valid;
    
    /**
     * Creates a new instance of ActivationManagedBean
     */
    
    public ActivationManagedBean() {
        
        logger.debug("Inside Activation");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        logger.debug("key "+request.getParameter("key"));
        key = request.getParameter("key");
        ty = request.getParameter("ty"); 
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
        EntityManager em = emf.createEntityManager();

        try 
        {
            Query query = null;
            
            if(ty.equals("C"))
            {
                query = em.createNamedQuery("CustomerInfo.findByActivationKey");
                query.setParameter("activationKey", request.getParameter("key"));
                logger.debug("query "+query);
                List aList = query.getResultList();
                if(aList.size()>0)
                {
                    this.valid = true;
                    CustomerInfo ci = (CustomerInfo) aList.get(0);
                    //customerInfo = ci;
                    EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
                    EntityManager em1 = emf1.createEntityManager();
                    em1.getTransaction().begin();
                    Query q = em1.createQuery("UPDATE CustomerInfo c SET c.activationKey = :activationKey, c.isActive = :isActive WHERE c.customerId = :customerId");
                    q.setParameter("activationKey", "");
                    q.setParameter("isActive", 'Y');
                    q.setParameter("customerId", ci.getCustomerId());
                    q.executeUpdate();
                    em1.getTransaction().commit();  

                }
                else
                {
                    this.valid = false;
                }
            }
            if(ty.equals("V"))
            {
                query = em.createNamedQuery("EventxpertUser.findByActivationKey");
                query.setParameter("activationKey", request.getParameter("key"));
                logger.debug("query "+query);
                List aList = query.getResultList();
                if(aList.size()>0)
                {
                    this.valid = true;
                    EventxpertUser ci = (EventxpertUser) aList.get(0);
                    eventxpertUserlocal = ci;
                    EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
                    EntityManager em1 = emf1.createEntityManager();
                    em1.getTransaction().begin();
                    Query q = em1.createQuery("UPDATE EventxpertUser c SET c.activationKey = :activationKey, c.iSActive = :iSActive WHERE c.userId = :userId");
                    q.setParameter("activationKey", "");
                    q.setParameter("iSActive", "Y");
                    q.setParameter("userId", ci.getUserId());
                    q.executeUpdate();
                    em1.getTransaction().commit();  

                }
                else
                {
                    this.valid = false;
                }
            }
          
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally 
        {
                em.close();
        }
        
        Properties prop = new Properties();
        InputStream input = null;

        try {

                             FacesContext ctx = FacesContext.getCurrentInstance();
                        ExternalContext externalContext = ctx.getExternalContext();
                        input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");

                            // load a properties file
                            prop.load(input);

                            // get the property value and print it out
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

    String ccemail = "";
    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
     private EventxpertUser eventxpertUser = new EventxpertUser();
      private EventxpertUser eventxpertUserlocal = new EventxpertUser();
    private CustomerInfo customerInfo = new CustomerInfo();

    public EventxpertUser getEventxpertUser() {
        return eventxpertUser;
    }

    public void setEventxpertUser(EventxpertUser eventxpertUser) {
        this.eventxpertUser = eventxpertUser;
    }

    
    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }
    
    private String user;
    private String pass;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    
    public String login()
    {       
        boolean success = false;
         EntityManagerFactory emf;
         EntityManager em = null ;
        try 
            {
                EncryptDecryptString eds = new EncryptDecryptString();
                
                String cryptPswd = eds.encrypt(customerInfo.getPassword());
                logger.debug("key password "+cryptPswd);
                customerInfo.setPassword(cryptPswd);
                
                emf = Persistence.createEntityManagerFactory("ROOT");
                em = emf.createEntityManager();
                em.getTransaction().begin(); 
                Query query = em.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email and ci.password = :password");
                query.setParameter("email", customerInfo.getEmail());
                query.setParameter("password", customerInfo.getPassword());
                List cList = query.getResultList();
                if(cList.size()>0)
                {
                    CustomerInfo c = (CustomerInfo) cList.get(0);
                    customerInfo = c;
                    success = true;
                }
                System.out.println("success:::::::  "+success);
                
            } catch (Exception e) 
            {
                e.printStackTrace();                
                //em.getTransaction().rollback();
            } 
            finally 
            {
                //em.close();
            }
        if(success == false)
        {           
             FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);            
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials", "Invalid credentials"); 
             FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg); 
                        
            return "";
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", customerInfo);
            Query query = em.createNamedQuery("EventInfo.findByCustomerId");
            query.setParameter("customerId", customerInfo);                
            List eList = query.getResultList();
             if(!eList.isEmpty())
             {
                 EventInfo ei = (EventInfo) eList.get(0);
                 EventDetailsManagedBean eventDetailsManagedBean = new EventDetailsManagedBean();
                 eventDetailsManagedBean.setCity(ei.getAreaId().getCityId().getCityId());
                 eventDetailsManagedBean.setVenueRadio(String.valueOf(ei.getVenueType().getVenueTypeId()));
                 eventDetailsManagedBean.setArea(ei.getAreaId().getAreaId());
                 eventDetailsManagedBean.setVenueType(ei.getVenueType().getVenueType());
                 eventDetailsManagedBean.nextScreen();
             }
            return "index.xhtml?faces-redirect=true";
        }
        
        
    }
    
    public String forgotPswd()
    {
         try 
            {   
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
                EntityManager em01 = emf.createEntityManager();            
                em01.getTransaction().begin();
                Query query = em01.createNamedQuery("CustomerInfo.findByEmail");
                query.setParameter("email", customerInfo.getEmail());
                List cList = query.getResultList();
                logger.debug("aList a "+cList.size());
                
                if(cList.size()>0)
                {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ////String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			
                    CustomerInfo ci = (CustomerInfo) cList.get(0);
                    
                     EncryptDecryptString eds = new EncryptDecryptString();
                
                    String cryptPswd = eds.decrypt(ci.getPassword());
                    
                    String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
                    String host = "mail.eventxpert.in";
                    final String username="jagadish@eventxpert.in";
                    final String password="jagadish";
                        Properties properties = System.getProperties();
                        properties.put("mail.smtp.host", host);
                        properties.put("mail.smtp.port", "25");
                        properties.put("mail.smtp.auth", "true");

                    Session session = Session.getInstance(properties,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                            }
                        });
       
                       Message messageU = new MimeMessage(session);
                        messageU.setFrom(new InternetAddress(from));
                        messageU.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(ci.getEmail()));
                        messageU.setSubject("Your EventXpert Account Password Recovery");
                        messageU.setText("Dear "+ci.getFirstname()+","
                                + "\n\n You initiated a request to help with your Account Password."
                                + "\n\n We will send your temporary password via another email."
                                + "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
                                + "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
                                + "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
                                + "\n\n EventXpert Team");

                        Transport.send(messageU);

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(from));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(ci.getEmail()));
                        message.setSubject("Your EventXpert Account Password Recovery");
                        message.setText("Dear "+ci.getFirstname()+","
                                + "\n\n You initiated a request to help with your Account Password."
                                + "\n\n The password for the your EventXpert account is \u0002"+cryptPswd
                                + "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
                                + "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
                                + "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
                                + "\n\n EventXpert Team");

                        Transport.send(message);
                    
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password is sent to your registered email-id", "Password Sent");
                    FacesContext.getCurrentInstance().addMessage("Password Sent", msg);                
                }
                else
                {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Entered Email is not registered with us", "Email not registered");
                    FacesContext.getCurrentInstance().addMessage("Email not registered", msg);                 
                }
            } catch (Exception e) 
            {
                e.printStackTrace();                
                //em.getTransaction().rollback();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is not registered", "Email not registered");
                FacesContext.getCurrentInstance().addMessage("Email not registered", msg); 
                
            } 
            finally 
            {
//                em.close(); 
                
            }
        return "";
    }
    
    public String forgotPswdVendor()
    {
         try 
            {   
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
                EntityManager em01 = emf.createEntityManager();            
                em01.getTransaction().begin();
                Query query = em01.createNamedQuery("EventxpertUser.findByEmailaddr");
                query.setParameter("emailaddr", eventxpertUser.getEmailaddr());
                List cList = query.getResultList();
                logger.debug("aList a "+cList.size());
                if(cList.size()>0)
                {
                    
                    
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			
                    EventxpertUser ci = (EventxpertUser) cList.get(0);
                    
                    EncryptDecryptString eds = new EncryptDecryptString();
                
                    String cryptPswd = eds.decrypt(ci.getPassword());
                    
                    
                    
                    String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
                    String host = "mail.eventxpert.in";
                    final String username="jagadish@eventxpert.in";
                    final String password="jagadish";
                        Properties properties = System.getProperties();
                        properties.put("mail.smtp.host", host);
                        properties.put("mail.smtp.port", "25");
                        properties.put("mail.smtp.auth", "true");

                    Session session = Session.getInstance(properties,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                            }
                        });
       
                       Message messageU = new MimeMessage(session);
                        messageU.setFrom(new InternetAddress(from));
                        messageU.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(ci.getEmailaddr()));
                        messageU.setSubject("Your EventXpert Account Password Recovery");
                        messageU.setText("Dear "+ci.getUsername()+","
                                + "\n\n You initiated a request to help with your Account Password."
                                + "\n\n We will send your temporary password via another email."
                                + "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
                                + "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
                                + "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
                                + "\n\n EventXpert Team");

                        Transport.send(messageU);

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(from));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(ci.getEmailaddr()));
                        message.setSubject("Your EventXpert Account Password Recovery");
                        message.setText("Dear "+ci.getUsername()+","
                                + "\n\n You initiated a request to help with your Account Password."
                                + "\n\n The password for the your EventXpert account is \u0002"+cryptPswd
                                + "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
                                + "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
                                + "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "+ccemail+" ."
                                + "\n\n EventXpert Team");

                        Transport.send(message);
                    
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password is sent to your registered email-id", "Password Sent");
                    FacesContext.getCurrentInstance().addMessage("Password Sent", msg);                
                }
                else
                {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Entered Email is not registered with us", "Email not registered");
                    FacesContext.getCurrentInstance().addMessage("Email not registered", msg);                 
                }
            } catch (Exception e) 
            {
                e.printStackTrace();                
                //em.getTransaction().rollback();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is not registered", "Email not registered");
                FacesContext.getCurrentInstance().addMessage("Email not registered", msg); 
                
            } 
            finally 
            {
//                em.close(); 
                
            }
        return "";
    }
    
    public String checkLogin()
        {
		EntityManagerFactory emf;
                EntityManager em = null ;
                try 
                    {
                        ServiceProviderManagedBean sp = new ServiceProviderManagedBean();
                        emf = Persistence.createEntityManagerFactory("ROOT");
                        em = emf.createEntityManager();
                        em.getTransaction().begin(); 
                        
                        if(eventxpertUserlocal.getUserType().equals("O"))
                        {
                            Query query = em.createNamedQuery("ServiceProviderInfo.findByUserId");
                            query.setParameter("userId", eventxpertUserlocal);
                            List cList = query.getResultList();
                            if(cList.size()>0)
                            {
                                ServiceProviderInfo c = (ServiceProviderInfo) cList.get(0);
                                sp.setSpCode(c.getServiceTypeCode().getServiceTypeCode());
                            }
//                            sp.setUser(eventxpertUser.getUsername());
//                             sp.setPassword(eventxpertUser.getPassword());  
//                            
//                         sp.setLoginUser(eventxpertUser.getUsername());
//                             sp.setLoginPassword(eventxpertUser.getPassword()); 
                            
                              sp.setUser(eventxpertUser.getEmailaddr());
                             sp.setPassword(eventxpertUser.getPassword());  
                            
                         sp.setLoginUser(eventxpertUser.getEmailaddr());
                             sp.setLoginPassword(eventxpertUser.getPassword());
               
                        boolean success =  sp.checkLoginAct();
                        
                         if(success)
                         {
                             FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serviceProviderManagedBean", sp);
                             FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
				 
                         } 
                         else 
                         {
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serviceProviderManagedBean", null);            
                            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials", "Invalid credentials"); 
                            FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg); 

                                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                                //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
                                return "";
                            }
                        }
                        if(eventxpertUserlocal.getUserType().equals("V"))
                        {
                            Query query = em.createNamedQuery("VenueInfo.findByUserId");
                            query.setParameter("userId", eventxpertUserlocal);
                            List cList = query.getResultList();
                            if(cList.size()>0)
                            {
                                VenueInfo c = (VenueInfo) cList.get(0);
                                sp.setSpCode("VEN");
                            }
//                             
//                             sp.setUser(eventxpertUser.getUsername());
//                             sp.setPassword(eventxpertUser.getPassword());  
//                            
//                         sp.setLoginUser(eventxpertUser.getUsername());
//                             sp.setLoginPassword(eventxpertUser.getPassword());  
                              sp.setUser(eventxpertUser.getEmailaddr());
                             sp.setPassword(eventxpertUser.getPassword());  
                            
                         sp.setLoginUser(eventxpertUser.getEmailaddr());
                             sp.setLoginPassword(eventxpertUser.getPassword());
               
                        boolean success =  sp.checkLoginAct();
                        
                         if(success)
                         {
                            // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("venueManagedBean", sp);
                             FacesContext.getCurrentInstance().getExternalContext().redirect("vendor-venue.jsf");
				 
                         } 
                         else 
                         {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("venueManagedBean", null);            
                                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials", "Invalid credentials"); 
                                    FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg); 

					FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
					//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("serviceProviderManagedBean");
                                        return "";
				 }
                        }
                        
                        
                    } catch (Exception e) 
                    {
                        e.printStackTrace();                
                        //em.getTransaction().rollback();
                    } 
                    finally 
                    {
                        //em.close();
                    }
            
		return "";
	}
}
