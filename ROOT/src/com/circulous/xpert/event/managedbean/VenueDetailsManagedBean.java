package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.VenueInfo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.circulous.xpert.jpa.entities.VenuePackageInfo;
import com.circulous.xpert.jpa.entities.views.ViewVenuePackage;

@ManagedBean
@ViewScoped
public class VenueDetailsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850917453343704209L;

	private List<SelectItem> cityList = new ArrayList<SelectItem>();

	private VenuePackageInfo pkgDetails = new VenuePackageInfo();

	private Integer packageId = null;

	private Integer intVenueId = null;

	private String venue = "";

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public void setPkgDetails(VenuePackageInfo pkgDetails) {
		this.pkgDetails = pkgDetails;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public List<SelectItem> getVenuePackges(String venueId) {

		cityList = new ArrayList<SelectItem>();

		if (null == intVenueId) {
			intVenueId = new Integer(venueId);
			packageId = null;
		} else if (intVenueId.intValue() != (new Integer(venueId)).intValue()) {
			intVenueId = new Integer(venueId);
			packageId = null;
		}

		if (null != packageId && 0 == packageId) {

			FacesContext context = FacesContext.getCurrentInstance();
			EventDetailsManagedBean bean = (EventDetailsManagedBean) context.getApplication().evaluateExpressionGet(
					context, "#{eventDetailsManagedBean}", EventDetailsManagedBean.class);

			if (null != bean) {
				// VenuePackageInfo venuePackageInfo = bean.getFinalVenueInfo();
				ViewVenuePackage venuePackageInfo = bean.getFinalVenueInfo();
				if (null != venuePackageInfo) {
					if (null != intVenueId && intVenueId == venuePackageInfo.getVenueId()) {
						// if (null != intVenueId && intVenueId ==
						// venuePackageInfo.getVenueId().getVenueId()) {
						packageId = venuePackageInfo.getPackageId();
					} else {
						packageId = null;
					}
				}
			}
		}

		try {
			VenueInfo vi = new VenueInfo(intVenueId.intValue());
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			Query query = em.createNamedQuery("VenuePackageInfo.findByVenueId");
			query.setParameter("venueId", vi);

			List list = query.getResultList();
			Iterator<VenuePackageInfo> itr = list.iterator();
			int count = 0;
			BigDecimal pkgamount = new BigDecimal(0);
			Integer localpackageId = null;
			while (itr.hasNext()) {
				VenuePackageInfo emp = itr.next();
				cityList.add(new SelectItem(emp.getPackageId(), emp.getPackageName()));

				if (null == packageId) {
					if (count == 0) {
						localpackageId = emp.getPackageId();
						pkgamount = emp.getCost();
					} else {
						if (pkgamount.compareTo(emp.getCost()) != -1) {
							localpackageId = emp.getPackageId();
							pkgamount = emp.getCost();
						}

					}
					count++;
				}
			}
			if (null != localpackageId) {
				this.setPackageId(localpackageId);
			}
		} finally {

		}
		return cityList;
	}

	public void listenPackage(ValueChangeEvent e) {
		if (null != e.getNewValue())
			packageId = new Integer(e.getNewValue().toString());
	}

	public List getVenuePackages(String venueId) {

		List<VenuePackageInfo> list;

		try {
			VenueInfo vi = new VenueInfo(Integer.parseInt(venueId));
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			Query query = em.createNamedQuery("VenuePackageInfo.findByVenueId");
			query.setParameter("venueId", vi);

			list = query.getResultList();
		} finally {

		}

		return list;
	}

	public ViewVenuePackage getPkgDetails() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery("VenuePackageInfo.findByPkgId");
		if (0 == packageId) {
			FacesContext context = FacesContext.getCurrentInstance();
			EventDetailsManagedBean bean = (EventDetailsManagedBean) context.getApplication().evaluateExpressionGet(
					context, "#{eventDetailsManagedBean}", EventDetailsManagedBean.class);

			return bean.getFinalVenueInfo();
		} else if (null != packageId) {
			query.setParameter("packageId", packageId.intValue());
		} else {
			query.setParameter("packageId", 1);
		}

		ViewVenuePackage vInfo = (ViewVenuePackage) query.getSingleResult();

		return vInfo;
	}

}
