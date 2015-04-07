package com.circulous.xpert.event.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.circulous.xpert.jpa.entities.PackageInfo;

@ManagedBean
@ViewScoped
public class ServiceDetailsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850917453343704209L;

	@SuppressWarnings("unchecked")
	public List<PackageInfo> getServicePackges(String serviceProviderId) {
		
		List<PackageInfo> list = new ArrayList<PackageInfo>();

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			Query query = em.createNamedQuery("PackageInfo.findByServiceProviderId");
			query.setParameter("serviceProviderId", new Integer(serviceProviderId).intValue());
			list =  query.getResultList();
		} finally {

		}
		return list;
	}
}
