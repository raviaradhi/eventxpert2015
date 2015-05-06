/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
/**
 *
 * @author SANJANA
 */
@ManagedBean
@SessionScoped
public class TaxManagedBean{
    
    private final static Logger logger = Logger.getLogger(TaxManagedBean.class);
    
    private HashMap servicesMap;

    public HashMap getServicesMap() {
        return servicesMap;
    }

    public void setServicesMap(HashMap servicesMap) {
        this.servicesMap = servicesMap;
    }
    
    private MenuModel model;

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }
    
    public String stypecode;

    public String getStypecode() {
        return stypecode;
    }

    public void setStypecode(String stypecode) {
        this.stypecode = stypecode;
    }
    
    EntityManagerFactory emf;
    EntityManager em;
    public TaxManagedBean() {
        
        FacesContext context = FacesContext.getCurrentInstance();
//         boolean admin = (Boolean) context.getExternalContext().getSessionMap().get("admin");
         if(context.getExternalContext().getSessionMap().get("admin") == null)
         {
            try 
            {
                ExternalContext externalContext = context.getExternalContext();
                externalContext.redirect("adminLogin.jsf");
             }
                catch(IOException ex)
                {
                    java.util.logging.Logger.getLogger(CostManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
         }
                 emf = Persistence.createEntityManagerFactory("ROOT");
		 em = emf.createEntityManager();
                try 
                {
                    Query query = em.createNamedQuery("ServiceTypeMaster.findAll");
                    List srvList = query.getResultList();
                    
                    servicesMap = new LinkedHashMap();
                    model = new DefaultMenuModel();
                    DefaultSubMenu  submenu = new DefaultSubMenu();  
                    submenu.setLabel("SERVICES"); 
                    FacesContext facesCtx = FacesContext.getCurrentInstance();
                    ELContext elCtx = facesCtx.getELContext();
                    ExpressionFactory expFact = facesCtx.getApplication().getExpressionFactory();
                    
                    Class[] args = new Class[]{String.class};
                    
                    Iterator<ServiceTypeMaster> atr = srvList.iterator();
                    int j = 0;
                    while (atr.hasNext()) 
                    {
                        
                        ServiceTypeMaster atm = atr.next();
                        
                        DefaultMenuItem item = new DefaultMenuItem(atm.getServiceName());
                        item.setId(atm.getServiceTypeCode());
                        item.setCommand("#{taxManagedBean.setCost}");
                       // item.setParam("stypecode", atm.getServiceTypeCode());
                        String code = atm.getServiceTypeCode();
                        servicesMap.put(j, code); 
                       // item.setAjax(false); 
                        //item.setUpdate("tblDetailsV, tblDetails, pkPanel");
                        item.setUpdate("@form");
                        item.setAjax(false);
                    //    item.setImmediate(true);
                        item.setCommand(expFact.createMethodExpression(elCtx, "#{taxManagedBean.setCost("+j+")}", String.class, args).getExpressionString());
                        
                        submenu.addElement(item);
                        j++;
                    }
                    model.addElement(submenu);
		
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally 
                {
			em.close();  
		}
    }
    
    List spDetails;

    public List getSpDetails() {
        return spDetails;
    }

    public void setSpDetails(ArrayList spDetails) {
        this.spDetails = spDetails;
    }
    
    
    public String setCost(Long st)
    {  
        taxMaster  = new TaxMaster();
        venueTaxMaster  = new VenueTaxMaster();
        logger.debug("setcost "+servicesMap.get(st.intValue()));
        //stypecode = (String) servicesMap.get(st);
        ServiceTypeMaster stm = new ServiceTypeMaster(servicesMap.get(st.intValue()).toString());
        
	EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
        em = emf1.createEntityManager();
        logger.debug("setcost "+stypecode);
        //spDetails = new ArrayList();        
        selProv = "";
        if(stm.getServiceTypeCode().equals("VEN"))
        {
            try 
            {
                //serviceProviderCostInfo = new ArrayList<ServiceProviderCostInfo>();
                Query query = em.createNamedQuery("VenueInfo.findAll");
                logger.debug("query "+query);
                List<VenueInfo> srvList = query.getResultList();
                spDetails =  srvList;
                stypecode = "Venue";
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally 
            {
                    em.close();  
            }
        }
        else
        {
            try 
            {
                //serviceProviderCostInfo = new ArrayList<ServiceProviderCostInfo>();
                Query query = em.createNamedQuery("ServiceProviderInfo.findByServiceTypeCode");
                query.setParameter("serviceTypeCode", stm);
                logger.debug("query "+query);
                List<ServiceProviderInfo> srvList = query.getResultList();
               spDetails =  srvList;
               stypecode = "";//srvList.get(0).getServiceTypeCode().getServiceName();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally 
            {
                    em.close();  
            }
        }
        return "hi";
    }
    
    private VenueTaxMaster venueTaxMaster = new VenueTaxMaster();

    public VenueTaxMaster getVenueTaxMaster() {
        return venueTaxMaster;
    }

    public void setVenueTaxMaster(VenueTaxMaster venueTaxMaster) {
        this.venueTaxMaster = venueTaxMaster;
    }
    
    private TaxMaster taxMaster = new TaxMaster();

    public TaxMaster getTaxMaster() {
        return taxMaster;
    }

    public void setTaxMaster(TaxMaster taxMaster) {
        this.taxMaster = taxMaster;
    }
    
    
    private String selProv;

    public String getSelProv() {
        return selProv;
    }

    public void setSelProv(String selProv) {
        this.selProv = selProv;
    }
    
    public void showPkg(ServiceProviderInfo srv)
    {        
        taxMaster = new TaxMaster();
        selProv = srv.getServiceProviderName();
        taxMaster.setVendorId(srv.getServiceProviderId());
        taxMaster.setVendorType('O');
        try 
        {
           em = emf.createEntityManager();           
           String qry = "SELECT t FROM TaxMaster t WHERE t.vendorId = :vendorId and t.vendorType = :vendorType";
            Query query = em.createQuery(qry);
            query.setParameter("vendorId", srv.getServiceProviderId());
            query.setParameter("vendorType", 'O');
            List pkgList = query.getResultList();
            if(pkgList.isEmpty())
            {
                taxMaster.setTax(new BigDecimal(0));
                taxMaster.setIsActive(' ');
            }
            else
            {
                taxMaster = (TaxMaster) pkgList.get(0);                
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
        
    }
    
    public void showVenuePkg(VenueInfo srv)
    {
        logger.debug("inside showPkg"); 
        venueTaxMaster = new VenueTaxMaster();
        selProv = srv.getVenueName();
        venueTaxMaster.setVendorId(srv.getVenueId());
        venueTaxMaster.setVendorType('V');
        
        try 
        {
            em = emf.createEntityManager();           
            String qry = "SELECT t FROM VenueTaxMaster t WHERE t.vendorId = :vendorId and t.vendorType = :vendorType";
            Query query = em.createQuery(qry);
            query.setParameter("vendorId", srv.getVenueId());
            query.setParameter("vendorType", 'V');
            logger.debug("QUERY "+query);
            List pkgList = query.getResultList();
            if(pkgList.isEmpty())
            {
                logger.debug("empty ");
//                venueTaxMaster.setTax("");
                taxMaster.setTax(new BigDecimal(0));
                venueTaxMaster.setIsActive(' ');
            }
            else
            {
                logger.debug("empty not");
                venueTaxMaster = (VenueTaxMaster) pkgList.get(0);   
                logger.debug("empty not "+venueTaxMaster.getTax());
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
        
    }
    
    public void addTax()
    {
        try
        {
            em = emf.createEntityManager();
            em.getTransaction().begin();            
            em.merge(taxMaster);
            em.getTransaction().commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally 
        {
           em.close();  
        }
        
        taxMaster =  new TaxMaster();
    }
    
     public void addVenueTax()
    {
        try
        {
            em = emf.createEntityManager();
            em.getTransaction().begin();            
            em.merge(venueTaxMaster);
            em.getTransaction().commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally 
        {
           em.close();  
        }
        
        venueTaxMaster =  new VenueTaxMaster();
    }
    
   
}
