/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
//import javax.jdo.JDOHelper;
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
public class CostManagedBean  implements Serializable{
    
    private final static Logger logger = Logger.getLogger(CostManagedBean.class);
    
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
    public CostManagedBean() {
          
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
                    item.setCommand("#{costManagedBean.setCost}");
                   // item.setParam("stypecode", atm.getServiceTypeCode());
                    String code = atm.getServiceTypeCode();
                    servicesMap.put(j, code); 
                   // item.setAjax(false); 
                    //item.setUpdate("tblDetailsV, tblDetails, pkPanel");
                    item.setUpdate("@form");
                    item.setCommand(expFact.createMethodExpression(elCtx, "#{costManagedBean.setCost("+j+")}", String.class, args).getExpressionString());
                    
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
    
    HashMap spDetails = new HashMap();

    public HashMap getSpDetails() {
        return spDetails;
    }

    public void setSpDetails(HashMap spDetails) {
        this.spDetails = spDetails;
    }
    
    
    public String setCost(Long st)
    {       
        logger.debug("setcost "+servicesMap.get(st.intValue()));
        //stypecode = (String) servicesMap.get(st);
        ServiceTypeMaster stm = new ServiceTypeMaster(servicesMap.get(st.intValue()).toString());
        em = emf.createEntityManager();
        logger.debug("setcost "+stypecode);
        spDetails = new HashMap();
        pkgDtls = null;
        selProv = "";
        if(stm.getServiceTypeCode().equals("VEN"))
        {
            try 
            {
                //serviceProviderCostInfo = new ArrayList<ServiceProviderCostInfo>();
               // Query query = em.createNamedQuery("VenueInfo.findAll");
                 Query query = em.createNamedQuery("VenueInfo.findByIsActive");
                 query.setParameter("isActive", 'Y');
                logger.debug("query "+query);
                List srvList = query.getResultList();
                Query query1;
                for(int j=0;j<srvList.size();j++)
                {
                    //serviceProviderCostInfo.add(new ServiceProviderCostInfo(0)); 
                    VenueInfo srv = (VenueInfo) srvList.get(j);
                    stypecode = "Venue";
                    query1 = em.createNamedQuery("VenuePackageInfo.findByVenueId");
                    query1.setParameter("venueId", srv);
                    logger.debug("query1 "+query1);
                    List pkgList = query1.getResultList();                
                    spDetails.put(srv, pkgList);
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
        else
        {
            try 
            {
                //serviceProviderCostInfo = new ArrayList<ServiceProviderCostInfo>();
//                Query query = em.createNamedQuery("ServiceProviderInfo.findByServiceTypeCode");
                 Query query = em.createNamedQuery("ServiceProviderInfo.findByServiceTypeCodeActive");
                 query.setParameter("isActive", 'Y');
                query.setParameter("serviceTypeCode", stm);
                logger.debug("query "+query);
                List srvList = query.getResultList();
                Query query1;
                for(int j=0;j<srvList.size();j++)
                {
                    //serviceProviderCostInfo.add(new ServiceProviderCostInfo(0)); 
                    ServiceProviderInfo srv = (ServiceProviderInfo) srvList.get(j);
                    stypecode = srv.getServiceTypeCode().getServiceName();
                    query1 = em.createNamedQuery("PackageInfo.findByServiceProviderId");
                    query1.setParameter("serviceProviderId", srv);
                    List pkgList = query1.getResultList();                
                    spDetails.put(srv, pkgList);
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
        return "hi";
    }
    
    private VenueCostInfo venueCostInfo;

    public VenueCostInfo getVenueCostInfo() {
        return venueCostInfo;
    }

    public void setVenueCostInfo(VenueCostInfo venueCostInfo) {
        this.venueCostInfo = venueCostInfo;
    }
    
    private ServiceProviderCostInfo serviceProviderCostInfo;

    public ServiceProviderCostInfo getServiceProviderCostInfo() {
        return serviceProviderCostInfo;
    }

    public void setServiceProviderCostInfo(ServiceProviderCostInfo serviceProviderCostInfo) {
        this.serviceProviderCostInfo = serviceProviderCostInfo;
    }
    
    
    private HashMap negotiatedCost = new HashMap();
    
    private HashMap customerDiscount = new HashMap();
    
    private HashMap finalAmount = new HashMap();

    public HashMap getCustomerDiscount() {
        return customerDiscount;
    }

    public void setCustomerDiscount(HashMap customerDiscount) {
        this.customerDiscount = customerDiscount;
    }

    public HashMap getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(HashMap finalAmount) {
        this.finalAmount = finalAmount;
    }

    public HashMap getNegotiatedCost() {
        return negotiatedCost;
    }

    public void setNegotiatedCost(HashMap negotiatedCost) {
        this.negotiatedCost = negotiatedCost;
    }
    
    private HashMap negotiatedCostV = new HashMap();
    
    private HashMap customerDiscountV = new HashMap();
    
    private HashMap finalAmountV = new HashMap();

    public HashMap getCustomerDiscountV() {
        return customerDiscountV;
    }

    public void setCustomerDiscountV(HashMap customerDiscountV) {
        this.customerDiscountV = customerDiscountV;
    }

    public HashMap getFinalAmountV() {
        return finalAmountV;
    }

    public void setFinalAmountV(HashMap finalAmountV) {
        this.finalAmountV = finalAmountV;
    }

    public HashMap getNegotiatedCostV() {
        return negotiatedCostV;
    }

    public void setNegotiatedCostV(HashMap negotiatedCostV) {
        this.negotiatedCostV = negotiatedCostV;
    }
    
    private List pkgDtls;

    public List getPkgDtls() {
        return pkgDtls;
    }

    public void setPkgDtls(List pkgDtls) {
        this.pkgDtls = pkgDtls;
    }
    
    private HashMap srvpCost = new HashMap();
    private HashMap srvpCostV = new HashMap();
    private String selProv;

    public String getSelProv() {
        return selProv;
    }

    public void setSelProv(String selProv) {
        this.selProv = selProv;
    }
    
    public void showPkg(ServiceProviderInfo srv)
    {
        logger.debug("inside showPkg");
        
        pkgDtls = (List) spDetails.get(srv);
        selProv = srv.getServiceProviderName();
        try 
        {
           em = emf.createEntityManager();
            for(int j=0;j<pkgDtls.size();j++)
            {
                PackageInfo pi = (PackageInfo) pkgDtls.get(j);
                Query query = em.createNamedQuery("ServiceProviderCostInfo.findByPackageId");
                query.setParameter("packageId", pi);
                List pkgList = query.getResultList();
                if(pkgList.isEmpty())
                {
                    negotiatedCost.put(pi.getPackageId(), "0");
                    customerDiscount.put(pi.getPackageId(), "0");
                    finalAmount.put(pi.getPackageId(), "0");
                }
                else
                {
                    ServiceProviderCostInfo spi = (ServiceProviderCostInfo)pkgList.get(0);
                     BigDecimal vendorCostSaved = spi.getVendorCost();
                    
                    if(vendorCostSaved!=pi.getCost())
                    {
                        BigDecimal pcost = pi.getCost();
//                        String dis = spi.getCustomerDiscount();
                        BigDecimal discount = spi.getCustomerDiscount();//new BigDecimal(dis);
                        BigDecimal finalamt  = pcost.subtract(discount);
                        finalAmount.put(pi.getPackageId(), finalamt);
                         em = emf.createEntityManager();
//                        VenuePackageInfo pInfo = new VenuePackageInfo(pkgid);
                        em.getTransaction().begin();
                        serviceProviderCostInfo = spi;//(VenueCostInfo) srvpCostV.get(pkgid);
                        serviceProviderCostInfo.setVendorCost(pcost);
                        serviceProviderCostInfo.setNegotiatedCost(spi.getNegotiatedCost());
                        serviceProviderCostInfo.setCustomerDiscount(spi.getCustomerDiscount());
                        serviceProviderCostInfo.setFinalAmount(finalamt);                
                        em.merge(serviceProviderCostInfo);
                        em.getTransaction().commit();
                    }
                    else
                    {
                       finalAmount.put(pi.getPackageId(), spi.getFinalAmount());                    
                    }
                     negotiatedCost.put(pi.getPackageId(), spi.getNegotiatedCost());
                     customerDiscount.put(pi.getPackageId(), spi.getCustomerDiscount());                        
                    srvpCost.put(pi.getPackageId(), spi);
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
    }
    
    public void showVenuePkg(VenueInfo srv)
    {
        logger.debug("inside showPkg");
        
        pkgDtls = (List) spDetails.get(srv);
        selProv = srv.getVenueName();
        
        try 
        {
           em = emf.createEntityManager();
            for(int j=0;j<pkgDtls.size();j++)
            {
                VenuePackageInfo pi = (VenuePackageInfo) pkgDtls.get(j);
                Query query = em.createNamedQuery("VenueCostInfo.findByPackageId");
                query.setParameter("packageId", pi);
                List pkgList = query.getResultList();
                if(pkgList.isEmpty())
                {
                    negotiatedCostV.put(pi.getPackageId(), "0");
                    customerDiscountV.put(pi.getPackageId(), "0");
                    finalAmountV.put(pi.getPackageId(), "0");
                }
                else
                {
                    VenueCostInfo spi = (VenueCostInfo)pkgList.get(0);
                    
                    BigDecimal vendorCostSaved = spi.getVendorCost();
                    
                    if(vendorCostSaved!=pi.getCost())
                    {
                        BigDecimal pcost = pi.getCost();
//                        String dis = spi.getCustomerDiscount();
                        BigDecimal discount = spi.getCustomerDiscount();//new BigDecimal(dis);
                        BigDecimal finalamt  = pcost.subtract(discount); 
                        finalAmountV.put(pi.getPackageId(), finalamt);
                        
                        em = emf.createEntityManager();
//                        VenuePackageInfo pInfo = new VenuePackageInfo(pkgid);
                        em.getTransaction().begin();
                        venueCostInfo = spi;//(VenueCostInfo) srvpCostV.get(pkgid);
                        venueCostInfo.setVendorCost(pcost);
                        venueCostInfo.setNegotiatedCost(spi.getNegotiatedCost());
                        venueCostInfo.setCustomerDiscount(spi.getCustomerDiscount());
                        venueCostInfo.setFinalAmount(finalamt);                
                        em.merge(venueCostInfo);
                        em.getTransaction().commit();
                    }
                    else
                    {                       
                        finalAmountV.put(pi.getPackageId(), spi.getFinalAmount());
                    }
                     negotiatedCostV.put(pi.getPackageId(), spi.getNegotiatedCost());
                     customerDiscountV.put(pi.getPackageId(), spi.getCustomerDiscount());
                    srvpCostV.put(pi.getPackageId(), spi);
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
    }
    
    public void addCostDtls(int pkgid, BigDecimal vendorCost)
    {
        logger.debug("size  "+serviceProviderCostInfo);
        logger.debug("pj "+negotiatedCost);
        
        try
        {
            em = emf.createEntityManager();
            PackageInfo pInfo = new PackageInfo(pkgid);
            em.getTransaction().begin();
            if(srvpCost.containsKey(pkgid))
            {
                serviceProviderCostInfo = (ServiceProviderCostInfo) srvpCost.get(pkgid);
                serviceProviderCostInfo.setNegotiatedCost(new BigDecimal(negotiatedCost.get(pkgid).toString()));
                serviceProviderCostInfo.setCustomerDiscount(new BigDecimal(customerDiscount.get(pkgid).toString()));
                serviceProviderCostInfo.setFinalAmount(new BigDecimal(finalAmount.get(pkgid).toString()));                
                em.merge(serviceProviderCostInfo);
            }
            else
            {
                serviceProviderCostInfo = new ServiceProviderCostInfo();
                serviceProviderCostInfo.setPackageId(pInfo);
                serviceProviderCostInfo.setVendorCost(vendorCost); 
                serviceProviderCostInfo.setNegotiatedCost(new BigDecimal(negotiatedCost.get(pkgid).toString()));
                serviceProviderCostInfo.setCustomerDiscount(new BigDecimal(customerDiscount.get(pkgid).toString()));
                serviceProviderCostInfo.setFinalAmount(new BigDecimal(finalAmount.get(pkgid).toString()));
                em.persist(serviceProviderCostInfo);
//                em.refresh(serviceProviderCostInfo);
                srvpCost.put(pkgid, serviceProviderCostInfo);
            }
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
    }
    
    public void addVenueCostDtls(int pkgid, BigDecimal vendorCost)
    {        
        try
        {
            em = emf.createEntityManager();
            VenuePackageInfo pInfo = new VenuePackageInfo(pkgid);
            em.getTransaction().begin();
            if(srvpCostV.containsKey(pkgid))
            {
                venueCostInfo = (VenueCostInfo) srvpCostV.get(pkgid);
                venueCostInfo.setNegotiatedCost(new BigDecimal(negotiatedCostV.get(pkgid).toString()));
                venueCostInfo.setCustomerDiscount(new BigDecimal(customerDiscountV.get(pkgid).toString()));
                venueCostInfo.setFinalAmount(new BigDecimal(finalAmountV.get(pkgid).toString()));                
                em.merge(venueCostInfo);
            }
            else
            {
                venueCostInfo = new VenueCostInfo();
                venueCostInfo.setPackageId(pInfo);
                venueCostInfo.setVendorCost(vendorCost); 
                venueCostInfo.setNegotiatedCost(new BigDecimal(negotiatedCostV.get(pkgid).toString()));
                venueCostInfo.setCustomerDiscount(new BigDecimal(customerDiscountV.get(pkgid).toString()));
                venueCostInfo.setFinalAmount(new BigDecimal(finalAmountV.get(pkgid).toString()));
                em.persist(venueCostInfo);
//                em.refresh(venueCostInfo);
                srvpCostV.put(pkgid, venueCostInfo);
            }
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
    }
    
    public void setFinalAmount(int pid,BigDecimal vcost, String dis)
    {
        BigDecimal discount = new BigDecimal(dis);
//        BigDecimal ONEHUNDERD = new BigDecimal(100);
//        BigDecimal disamt = (vcost.multiply(discount)).divide(ONEHUNDERD);
//        BigDecimal finalamt  = vcost.subtract(disamt);
        BigDecimal finalamt  = vcost.subtract(discount);
        finalAmount.put(pid, finalamt);
    }
    
    public void setFinalAmountV(int pid,BigDecimal vcost, String dis)
    {
        BigDecimal discount = new BigDecimal(dis);
//        BigDecimal ONEHUNDERD = new BigDecimal(100);
//        BigDecimal disamt = (vcost.multiply(discount)).divide(ONEHUNDERD);
//        BigDecimal finalamt  = vcost.subtract(disamt);
        BigDecimal finalamt  = vcost.subtract(discount);
        finalAmountV.put(pid, finalamt);
    }
}
