/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.EventxpertUser;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo;
import java.io.File;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author SANJANA
 */
@ManagedBean
@SessionScoped
public class AdminManagedBean {

    /**
     * Creates a new instance of AdminManagedBean
     */
    public AdminManagedBean() {
    }
    
    private static Logger logger = Logger.getLogger(AdminManagedBean.class);
    
    private String user;
    private String password;
    private String emailaddr;
    
    transient EntityManagerFactory emf;

	private EntityManager getEntityManager() {

		emf = Persistence.createEntityManagerFactory("ROOT");

		EntityManager em = emf.createEntityManager();

		return em;
	}
        
    private String insertUserDtls(){
        String ret="";
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Query q1 = em.createQuery("select vu from EventxpertUser vu where vu.emailaddr= :emailaddr and vu.userType='A'");
            q1.setParameter("emailaddr", this.user);
            List<EventxpertUser> l1 = q1.getResultList();
				

            if (l1.size() > 0) {

                    String mess = "Registration failed, User Name Already Exist";
                    //adminInformation(mess);
            }
				

            else {

                    EventxpertUser vu = createVendorUser();                                     



                    vu.setActivationKey("");

                    vu.setCreatedBy("Admin");
                    vu.setModifiedBy("Admin");
                    vu.setCreatedDate(new Date());
                    // vu.setActivationKey("Y");                    
                    em.persist(vu);
                    em.getTransaction().commit();
                    em.refresh(vu);
                    ret = "thankyou.xhtml?faces-redirect=true";
                        
                               
                                
		}
           } 
           catch (Exception e) 
            {			
                e.printStackTrace();
                em.getTransaction().rollback();
//                registered = false;
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
                ret = "thankyou.xhtml?faces-redirect=true";
            }

            return ret;
    }
    
    private EventxpertUser createVendorUser() {
        
            EventxpertUser vu = new EventxpertUser();
            vu.setUsername(this.user);
            vu.setPassword(this.password);
            vu.setPasswordHint("");
            vu.setPasswordAns("");
            vu.setEmailaddr(this.emailaddr);
            vu.setISActive("N");
            vu.setUserCategory("Admin");
            vu.setUserType("A");
            try {
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
        }
            return vu;
        
	}
    EventxpertUser exu;
    public String checkLogin() {

        System.out.println("CHECKLOGIN");
		String ret = "";
		boolean success = false;
		try {			
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			Query q = null;
                        

//                        EncryptDecryptString eds = new EncryptDecryptString();                
//                        String cryptPswd = eds.encrypt(password);
//                        logger.debug("key password "+cryptPswd);
                        //vu.setPassword(cryptPswd);
            
           
                        
			q = em.createQuery("SELECT spi FROM EventxpertUser spi  WHERE spi.emailaddr = :emailaddr and spi.password = :password and spi.userType=:utype");

			q.setParameter("emailaddr", user);			
			q.setParameter("password", password);
                        q.setParameter("utype", "A");
			List<EventxpertUser> object = q.getResultList();

			if (object.size() > 0) {

				exu = (EventxpertUser) object.get(0);

				// EntityManagerFactory emf1 =
				// Persistence.createEntityManagerFactory("ROOT");
				EntityManager em1 = getEntityManager();// emf1.createEntityManager();
				em1.getTransaction().begin();
				Query qu = em1.createQuery("UPDATE EventxpertUser c SET c.lastLogin = CURRENT_DATE WHERE c.userId = :userId");
				qu.setParameter("userId", exu.getUserId());
				qu.executeUpdate();
				em1.getTransaction().commit();
                                
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("admin",true);
				
			} else {
				
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
                        return "";
		}

		return "costAdmin.xhtml?faces-redirect=true";

	}

    public String getEmailaddr() {
        return emailaddr;
    }

    public void setEmailaddr(String emailaddr) {
        this.emailaddr = emailaddr;
    }

    public EventxpertUser getExu() {
        return exu;
    }

    public void setExu(EventxpertUser exu) {
        this.exu = exu;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String logout()
    {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("admin");
        
        return "adminLogin.jsf?faces-redirect=true";
    }
    
    public void loadLatAllLng()
    {
        PlacesLatitudes pl = new PlacesLatitudes();
        pl.enterLat(false);
    }
    
    public void loadLatLng()
    {
        PlacesLatitudes pl = new PlacesLatitudes();
        pl.enterLat(true);
    }
}
