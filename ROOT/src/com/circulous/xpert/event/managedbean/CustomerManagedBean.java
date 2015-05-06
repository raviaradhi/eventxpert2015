/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

import com.circulous.xpert.event.comparator.AreaTypeComparatoreByAreaName;
import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.CustomerInfo;

/**
 * 
 * @author SANJANA
 */
@ManagedBean
// @ViewScoped
// @RequestScoped
@SessionScoped
public class CustomerManagedBean implements Serializable {

	private final static Logger logger = Logger.getLogger(CustomerManagedBean.class);

	/**
	 * Creates a new instance of CustomerManagedBean
	 */
	public CustomerManagedBean() {
	}

	public void createList() {

		/*
		 * EntityManagerFactory emf =
		 * Persistence.createEntityManagerFactory("ROOT"); EntityManager em =
		 * getEntityManager(); // HttpServletRequest request =
		 * (HttpServletRequest)
		 * FacesContext.getCurrentInstance().getExternalContext().getRequest();
		 * // logger.debug("request "+request.getHeader("host")+" d "+request.
		 * getContextPath()+" d "+request.getRequestURI()); try { Query query =
		 * em.createNamedQuery("AreaTypeMaster.findAll"); List aList =
		 * query.getResultList();
		 * 
		 * areaList = new LinkedHashMap();
		 * 
		 * List<AreaTypeMaster> sortedAreaList = new
		 * LinkedList<AreaTypeMaster>(aList); Collections.sort(sortedAreaList,
		 * new AreaTypeComparatoreByAreaName()); Iterator<AreaTypeMaster> atr =
		 * sortedAreaList.iterator(); while (atr.hasNext()) { AreaTypeMaster atm
		 * = atr.next(); areaList.put(atm.getAreaName(), atm); //
		 * areaList.put(atm.getAreaName(), atm.getAreaId()); }
		 * 
		 * } catch(Exception e) { e.printStackTrace(); } finally { em.close(); }
		 */
		prop = new Properties();
		InputStream input = null;

		try {

			FacesContext ctx = FacesContext.getCurrentInstance();
			ExternalContext externalContext = ctx.getExternalContext();
			input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			ccemail = prop.getProperty("custSupportEmail");
			setGoogleUrl(prop.getProperty("redirectGUrl"));
			setFacebookUrl(prop.getProperty("redirectFBUrl"));

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

	private HashMap<String, AreaTypeMaster> areaList;
	private Properties prop;
	// private HashMap areaList;
	private AddressInfo addressInfo = new AddressInfo();
	private CustomerInfo customerInfo = new CustomerInfo();
	private AreaTypeMaster atMaster;
	private boolean forgotLink;
	private boolean enableBtn;
	private boolean selectBoolean;
	private boolean resendLink;
	private boolean showRegister;
	private String googleUrl;
	private String facebookUrl;
	private boolean showChangePassword;

	public AreaTypeMaster getAtMaster() {
		return atMaster;
	}

	public void setAtMaster(AreaTypeMaster atMaster) {
		this.atMaster = atMaster;
	}

	// public HashMap getAreaList() {
	// return areaList;
	// }
	//
	// public void setAreaList(HashMap areaList) {
	// this.areaList = areaList;
	// }

	public HashMap<String, AreaTypeMaster> getAreaList() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
		EntityManager em = getEntityManager();
		// HttpServletRequest request = (HttpServletRequest)
		// FacesContext.getCurrentInstance().getExternalContext().getRequest();
		// logger.debug("request "+request.getHeader("host")+" d "+request.getContextPath()+" d "+request.getRequestURI());
		try {
			Query query = em.createNamedQuery("AreaTypeMaster.findAll");
			List aList = query.getResultList();

			areaList = new LinkedHashMap();

			List<AreaTypeMaster> sortedAreaList = new LinkedList<AreaTypeMaster>(aList);
			Collections.sort(sortedAreaList, new AreaTypeComparatoreByAreaName());
			Iterator<AreaTypeMaster> atr = sortedAreaList.iterator();
			while (atr.hasNext()) {
				AreaTypeMaster atm = atr.next();
				areaList.put(atm.getAreaName(), atm);
				// areaList.put(atm.getAreaName(), atm.getAreaId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		logger.debug("areaList***********  " + areaList.size());
		return areaList;
	}

	public void setAreaList(HashMap<String, AreaTypeMaster> areaList) {
		this.areaList = areaList;
	}

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	transient EntityManagerFactory emf;

	private EntityManager getEntityManager() {

		emf = Persistence.createEntityManagerFactory("ROOT");

		EntityManager em = emf.createEntityManager();

		return em;
	}

	public String saveCustomer() {

		try {
			// //EntityManagerFactory emf =
			// Persistence.createEntityManagerFactory("ROOT");
			if (customerInfo.getAreaId() != null) {
				EntityManager em = getEntityManager();
				em.getTransaction().begin();
				Query query = em.createNamedQuery("AreaTypeMaster.findByAreaId");
				query.setParameter("areaId", customerInfo.getAreaId().getAreaId());
				List aList = query.getResultList();

				AreaTypeMaster at = (AreaTypeMaster) aList.get(0);

				addressInfo.setAddressId(54);
				addressInfo.setCityId(at.getCityId().getCityId());

				EntityManager em0 = getEntityManager();
				em0.getTransaction().begin();
				em0.persist(addressInfo);
				em0.getTransaction().commit();
				em0.refresh(addressInfo);

				customerInfo.setAddressId(addressInfo);
			}
			customerInfo.setIsActive('N');

			EncryptDecryptString eds = new EncryptDecryptString();
			String cryptPswd = eds.encrypt(customerInfo.getPassword());

			logger.debug("key password " + cryptPswd);
			customerInfo.setPassword(cryptPswd);

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update((customerInfo.getEmail() + "." + customerInfo.getPassword()).getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			String key = Base64.encodeBase64URLSafeString(byteData);
			logger.debug("key activation " + key);
			customerInfo.setActivationKey(key);

			EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em1 = emf1.createEntityManager();
			Timestamp ts = new Timestamp(new Date().getTime());
			customerInfo.setCustomerEffectiveDate(ts);
			customerInfo.setCustomerId(0);
			em1.getTransaction().begin();
			em1.persist(customerInfo);
			em1.getTransaction().commit();

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			logger.debug("request " + request.getHeader("host") + " d " + request.getContextPath() + " d "
					+ request.getRequestURI());

			// logger.debug("request "+request.getRequestURL());

			// String link =
			// "http://localhost:8084/WEB-INF/activate.jsf?key="+key;
			String link = "http://" + request.getHeader("host") + request.getContextPath() + "/activate.jsf?key=" + key
					+ "&ty=C";
			logger.debug("link " + link);

			sendEmail(customerInfo.getEmail(), customerInfo.getFirstname() + " " + customerInfo.getLastname(), link);
			// EventDetailsManagedBean eventDetailsManagedBean =
			// (EventDetailsManagedBean)
			// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("eventDetailsManagedBean");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", customerInfo);
			// eventDetailsManagedBean.getEventInfo().setCustomerId(customerInfo);
			// eventDetailsManagedBean.insertEvent();
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			registered = false;
			// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou",registered);
		} finally {
			// em.close();

		}
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("userId");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");
		registered = true;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("thankyou", registered);
		return "thankyou.xhtml?faces-redirect=true";
	}

	private boolean registered;

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public void sendEmail(String toEmail, String name, String link) {
		/*
		 * final String username = "eventxperttest@gmail.com"; final String
		 * password = "eventxpert12";
		 * 
		 * Properties props = new Properties(); props.put("mail.smtp.auth",
		 * "true"); props.put("mail.smtp.starttls.enable", "true");
		 * props.put("mail.smtp.host", "smtp.gmail.com");
		 * props.put("mail.smtp.port", "587");
		 */

		String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
		String host = "mail.eventxpert.in";
		final String username = "jagadish@eventxpert.in";
		final String password = "jagadish";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.auth", "true");

		FacesContext ctx = FacesContext.getCurrentInstance();
		// String ccemail =
		// ctx.getExternalContext().getInitParameter("custSupportEmail");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Activate your EventXpert Account");
			message.setText("Welcome "
					+ name
					+ ".,"
					+ "\n\n Thanks for registering with EventXpert."
					+ "\n\n To prevent unauthorized use of your email address, we need to make sure that you are the rightful owner of this email ID."
					+ "\n\n Please click on the link below to confirm your email address:"
					+ "\n\n "
					+ link
					+ "\n\n If you received this verification email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail. If you are unable to click on the link above, please copy and paste above URL in your browser's address bar."
					+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "
					+ ccemail + " ." + "\n\n EventXpert Team");

			Transport.send(message);

			logger.debug("Done");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void enableLogin(String email) {
		/*
		 * EntityManagerFactory emfLogin =
		 * Persistence.createEntityManagerFactory("ROOT"); EntityManager emLogin
		 * = emfLogin.createEntityManager(); if (!emLogin.isOpen()) {
		 * emLogin.getTransaction().begin(); } Query query =
		 * emLogin.createQuery(
		 * "SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email");
		 * query.setParameter("email", email); List cList =
		 * query.getResultList(); if (cList.size() > 0) { CustomerInfo c =
		 * (CustomerInfo) cList.get(0); if(null == c.getIsActive() || (null !=
		 * c.getIsActive() && !c.getIsActive().equals('Y'))){ FacesContext ctx =
		 * ExternalContext externalContext = ctx.getExternalContext();
		 * FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .put("userId", null); FacesMessage msg = new
		 * FacesMessage(FacesMessage.SEVERITY_WARN,
		 * "Please activate your account by clicking on the activation link sent to your registered email."
		 * , "Activate Account");
		 * FacesContext.getCurrentInstance().addMessage("Activate Account",
		 * msg); }
		 * FacesContext.getCurrentInstance().getExternalContext().getSessionMap
		 * ().put("userId", c); } else { return; }
		 */
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("userId");
		return "index.xhtml?faces-redirect=true";
	}

	EntityManagerFactory emfLogin = Persistence.createEntityManagerFactory("ROOT");
	EntityManager emLogin = emfLogin.createEntityManager();

	public String login() {
		boolean success = false;
		try {

			if (!emLogin.isOpen()) {
				emLogin.getTransaction().begin();
			}

			EncryptDecryptString eds = new EncryptDecryptString();
			String cryptPswd = eds.encrypt(customerInfo.getPassword());
			logger.debug("key password " + cryptPswd);
			customerInfo.setPassword(cryptPswd);

			Query query = emLogin
					.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email and ci.password = :password");
			query.setParameter("email", customerInfo.getEmail());
			query.setParameter("password", customerInfo.getPassword());
			// query.setParameter("isActive", 'Y');
			List cList = query.getResultList();
			if (cList.size() > 0) {
				Query query1 = emLogin
						.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email and ci.password = :password and ci.isActive = :isActive");
				query1.setParameter("email", customerInfo.getEmail());
				query1.setParameter("password", customerInfo.getPassword());
				query1.setParameter("isActive", 'Y');
				List cList1 = query1.getResultList();
				if (cList1.isEmpty()) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);
					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Please activate your account by clicking on the activation link sent to your registered email.",
							"Activate Account");
					FacesContext.getCurrentInstance().addMessage("Activate Account", msg);
					return null;
				}
				CustomerInfo c = (CustomerInfo) cList.get(0);
				customerInfo = c;
				success = true;
			}
			/*
			 * Query query = emLogin.createQuery(
			 * "SELECT ci FROM CustomerInfo ci  WHERE ci.userId = :userId and ci.password = :password and ci.isActive = :isActive"
			 * ); query.setParameter("userId", customerInfo.getUserId());
			 * query.setParameter("password", customerInfo.getPassword());
			 * query.setParameter("isActive", 'Y'); List cList =
			 * query.getResultList(); if(cList.size()>0) { CustomerInfo c =
			 * (CustomerInfo) cList.get(0); customerInfo = c; success = true; }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
		} finally {
			// em.close();

		}
		if (success == false) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);
			// throw new ValidatorException(new FacesMessage(
			// FacesMessage.SEVERITY_ERROR,
			// "Invalid username and password. Please try again", "logbtn"));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials",
					"Invalid credentials");
			FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);
			return null;
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", customerInfo);
			return "index?faces-redirect=true";
		}

	}

	public boolean chkReg = false;

	public boolean isChkReg() {
		return chkReg;
	}

	public void setChkReg(boolean chkReg) {
		this.chkReg = chkReg;
	}

	public String register() {
		this.chkReg = true;
		logger.debug("inside Register customermanagedbean****** " + chkReg);
		return "customerRegister";
	}

	public String cancel() {
		this.chkReg = false;
		addressInfo = new AddressInfo();
		customerInfo = new CustomerInfo();
		return "customerRegister";
	}

	public String cancelHome() {
		// this.chkReg = false;
		// addressInfo = new AddressInfo();
		// customerInfo = new CustomerInfo();
		return "index";
	}

	public String registerDlg() {
		this.chkReg = true;
		logger.debug("inside Register customermanagedbean****** " + chkReg);

		RequestContext.getCurrentInstance().execute("eventDialog.show()");

		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		logger.debug("viewId******  " + viewId);
		RequestContext.getCurrentInstance().execute("frm:eventDialog.show()");
		// RequestContext.getCurrentInstance().redirect(url);
		// return viewId;//+"?faces-redirect=true";
		return "#";
	}

	public String cancelDlg() {
		this.chkReg = false;
		addressInfo = new AddressInfo();
		customerInfo = new CustomerInfo();
		return "customerRegDlg";
	}

	public String saveCustomerDlg() {
		logger.debug("Customer dialog");
		// RequestContext.getCurrentInstance().update("panel");
		try {
			// EntityManagerFactory emf =
			// Persistence.createEntityManagerFactory("ROOT");

			if (customerInfo.getAreaId() != null) {
				EntityManager em = getEntityManager();
				em.getTransaction().begin();
				Query query = em.createNamedQuery("AreaTypeMaster.findByAreaId");
				query.setParameter("areaId", customerInfo.getAreaId().getAreaId());
				List aList = query.getResultList();

				AreaTypeMaster at = (AreaTypeMaster) aList.get(0);

				addressInfo.setAddressId(54);
				addressInfo.setCityId(at.getCityId().getCityId());

				EntityManager em0 = getEntityManager();
				em0.getTransaction().begin();
				em0.persist(addressInfo);
				em0.getTransaction().commit();
				em0.refresh(addressInfo);

				customerInfo.setAddressId(addressInfo);
			}

			EncryptDecryptString eds = new EncryptDecryptString();
			String cryptPswd = eds.encrypt(customerInfo.getPassword());

			logger.debug("key password " + cryptPswd);
			customerInfo.setPassword(cryptPswd);

			customerInfo.setIsActive('N');

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update((customerInfo.getEmail() + "." + customerInfo.getPassword()).getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			String key = Base64.encodeBase64URLSafeString(byteData);
			logger.debug("key activation " + key);
			customerInfo.setActivationKey(key);

			EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em1 = emf1.createEntityManager();
			Timestamp ts = new Timestamp(new Date().getTime());
			customerInfo.setCustomerEffectiveDate(ts);
			customerInfo.setCustomerId(0);
			em1.getTransaction().begin();
			em1.persist(customerInfo);
			em1.getTransaction().commit();
			em1.refresh(customerInfo);

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			logger.debug("request " + request.getHeader("host") + " d " + request.getContextPath() + " d "
					+ request.getRequestURI());

			// logger.debug("request "+request.getRequestURL());

			// String link =
			// "http://localhost:8084/WEB-INF/activate.jsf?key="+key;
			String link = "http://" + request.getHeader("host") + request.getContextPath() + "/activate.jsf?key=" + key;
			logger.debug("link " + link);

			sendEmail(customerInfo.getEmail(), customerInfo.getFirstname() + " " + customerInfo.getLastname(), link);

		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
		} finally {
			// em.close();

		}
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId",
		// customerInfo);
		// FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Your details are registered with us. Please check your email for the activation link and ",
		// "Profile Details Updated");
		// FacesContext.getCurrentInstance().addMessage("Details Updated", msg);
		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		logger.debug("viewId******  " + viewId);
		// RequestContext.getCurrentInstance().execute("frm:eventDialog.hide()");
		// RequestContext.getCurrentInstance().redirect(url);
		return viewId + "?faces-redirect=true";
		// RequestContext.getCurrentInstance().execute("frmBody");
		// RequestContext.getCurrentInstance().update("frm");
		// FacesContext context = FacesContext.getCurrentInstance();
		// String viewId = context.getViewRoot().getViewId();
		// ViewHandler handler = context.getApplication().getViewHandler();
		// UIViewRoot root = handler.createView(context, viewId);
		// root.setViewId(viewId);
		// context.setViewRoot(root);
		// return "index.xhtml?faces-redirect=true";
	}

	private boolean showActLink = false;

	public boolean isShowActLink() {
		return showActLink;
	}

	public void setShowActLink(boolean showActLink) {
		this.showActLink = showActLink;
	}

	public String loginDlg() {
		CustomerInfo info = validateRegisteredUser(customerInfo.getEmail());
		if (null != info) {
			boolean success = false;
			try {
				EncryptDecryptString eds = new EncryptDecryptString();
				String cryptPswd = eds.encrypt(customerInfo.getPassword());

				logger.debug("key password " + cryptPswd);
				customerInfo.setPassword(cryptPswd);

				// EntityManagerFactory emf =
				// Persistence.createEntityManagerFactory("ROOT");
				EntityManager em = getEntityManager();
				em.getTransaction().begin();
				// Query query =
				// em.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email and ci.password = :password and ci.isActive = :isActive");
				Query query = em
						.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email and ci.password = :password");
				query.setParameter("email", customerInfo.getEmail());
				query.setParameter("password", customerInfo.getPassword());
				// query.setParameter("isActive", 'Y');
				List cList = query.getResultList();
				if (cList.size() > 0) {
					Query query1 = em
							.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email and ci.password = :password and ci.isActive = :isActive");
					query1.setParameter("email", customerInfo.getEmail());
					query1.setParameter("password", customerInfo.getPassword());
					query1.setParameter("isActive", 'Y');
					List cList1 = query1.getResultList();

					if (cList1.isEmpty()) {
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);
						FacesMessage msg = new FacesMessage(
								FacesMessage.SEVERITY_WARN,
								"Please activate your account by clicking on the activation link sent to your registered email.",
								"Activate Account");
						FacesContext.getCurrentInstance().addMessage("Activate Account", msg);
						// RequestContext.getCurrentInstance().execute("frm:loginDialog.show()");
						RequestContext.getCurrentInstance().addCallbackParam("loggedIn", false);
						showActLink = true;
						return "";
					}

					CustomerInfo c = (CustomerInfo) cList.get(0);
					customerInfo = c;

					EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
					EntityManager em1 = emf1.createEntityManager();
					em1.getTransaction().begin();
					Query qu = em1
							.createQuery("UPDATE CustomerInfo c SET c.lastLogin = CURRENT_DATE WHERE c.email = :email");
					qu.setParameter("email", c.getEmail());
					qu.executeUpdate();
					em1.getTransaction().commit();
					success = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				// em.getTransaction().rollback();
			} finally {
				// em.close();

			}
			if (success == false) {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);
				// throw new ValidatorException(new FacesMessage(
				// FacesMessage.SEVERITY_ERROR,
				// "Invalid username and password. Please try again", null));
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error. Invalid credentials",
						"Invalid credentials");
				FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);
				RequestContext.getCurrentInstance().execute("frm:loginDialog.show()");
				RequestContext.getCurrentInstance().addCallbackParam("loggedIn", false);
				return "";
			} else {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", customerInfo);
				RequestContext.getCurrentInstance().execute("frm:loginDialog.hide()");
				String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
				return viewId + "?faces-redirect=true";
			}
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Please sign in using Facebook/Google",
					"Invalid credentials");
			FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);
			RequestContext.getCurrentInstance().execute("frm:loginDialog.show()");
			RequestContext.getCurrentInstance().addCallbackParam("loggedIn", false);
			return "";
		}

	}

	public CustomerInfo validateRegisteredUser(String email) {
		CustomerInfo ci = null;
		EntityManager em01 = getEntityManager();
		em01.getTransaction().begin();
		Query query = em01.createNamedQuery("CustomerInfo.findByEmail");
		query.setParameter("email", email);
		List cList = query.getResultList();
		if (cList.size() > 0) {
			ci = (CustomerInfo) cList.get(0);
			if (null == ci.getPassword())
				return null;
			return ci;
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", null);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email Id not registered with us.",
					"Invalid credentials");
			FacesContext.getCurrentInstance().addMessage("Invalid credentials", msg);
			RequestContext.getCurrentInstance().execute("frm:loginDialog.show()");
			RequestContext.getCurrentInstance().addCallbackParam("loggedIn", false);
			return null;
		}
	}

	public String aboutUs() {
		return "aboutUs.jsf?faces-redirect=true";
	}

	public String contactUs() {
		return "contact.jsf?faces-redirect=true";
	}

	public String loginForm() {
		chkReg = false;
		return "customerRegister.xhtml?faces-redirect=true";
	}

	public String goLoginPage() {
		// FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.containsKey("eventDetailsManagedBean")) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");
		}
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
			// return "eventHome.xhtml?faces-redirect=true";
			return "index.xhtml?faces-redirect=true";
		} else {
			return "index.xhtml?faces-redirect=true";
		}
		//

	}

	public String home() {
		// return "eventHome.xhtml?faces-redirect=true";
		return "index.xhtml?faces-redirect=true";
	}

	public String chngPswd() {
		// return "eventHome.xhtml?faces-redirect=true";
		return "changePswd.xhtml?faces-redirect=true";
	}

	public String history() {
		return "eventHistory.xhtml?faces-redirect=true";
	}

	public String eventSummary() {
		return "eventSummary.xhtml?faces-redirect=true";
	}

	public String newEvent() {
		return "event-details.xhtml?faces-redirect=true";
	}

	public boolean eprofile = false;

	public boolean isEprofile() {
		return eprofile;
	}

	public void setEprofile(boolean eprofile) {
		this.eprofile = eprofile;
	}

	// EntityManagerFactory emf =
	// Persistence.createEntityManagerFactory("ROOT");

	EntityManager emAddr = getEntityManager();

	boolean saveAddr = false;

	public String editProfile() {
		eprofile = true;
		try {
			// //EntityManagerFactory emf =
			// Persistence.createEntityManagerFactory("ROOT");

			// EntityManager em = getEntityManager();
			emAddr.getTransaction().begin();
			if (customerInfo.getAddressId() != null) {
				saveAddr = false;
				Query query = emAddr.createNamedQuery("AddressInfo.findByAddressId");
				query.setParameter("addressId", customerInfo.getAddressId().getAddressId());
				List aList = query.getResultList();
				logger.debug("aList a " + aList.size());
				if (aList.size() > 0) {
					AddressInfo ad = (AddressInfo) aList.get(0);
					addressInfo = ad;
					logger.debug("adressinfo a " + addressInfo.getAddressLine1());
				}
			} else {
				saveAddr = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		chkReg = true;
		return "customerRegister.xhtml?faces-redirect=true";
	}

	public void updateCustomer() {

		try {

			if (saveAddr == true) {
				addressInfo.setAddressId(54);

				EntityManager em0 = getEntityManager();
				em0.getTransaction().begin();
				em0.persist(addressInfo);
				em0.getTransaction().commit();
				em0.refresh(addressInfo);

				customerInfo.setAddressId(addressInfo);
			}

			// em0.refresh(addressInfo);
			//
			EntityManager em01 = emfLogin.createEntityManager();
			em01.getTransaction().begin();
			em01.merge(customerInfo);
			em01.getTransaction().commit();
			em01.close();
			// em0.refresh(addressInfo);

			// emLogin.getTransaction().begin();
			// emLogin.getTransaction().commit();

			// EntityManager em0 = emfLogin.createEntityManager();
			// AddressInfo addressInfo1 = em0.find(AddressInfo.class,
			// customerInfo.getAddressId().getAddressId());
			// logger.debug("adressinfo  "+addressInfo1.getAddressLine1());
			// addressInfo1 = addressInfo;
			// em0.getTransaction().begin();
			// Query query = em0.createQuery("AddressInfo.findByAddressId");
			// query.setParameter("addressId",
			// customerInfo.getAddressId().getAddressId());
			// //em0.persist(addressInfo1);
			// logger.debug("adressinfo a "+addressInfo1.getAddressLine1());
			emAddr.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profile Details are not updated",
					"Profile Details Updated");
			FacesContext.getCurrentInstance().addMessage("Details Updated", msg);
			registered = false;
		} finally {
			// em.close();

		}
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId",
		// customerInfo);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Details are updated",
				"Profile Details Updated");
		FacesContext.getCurrentInstance().addMessage("Details Updated", msg);
		registered = true;
		// return "customerRegister.xhtml?faces-redirect=true";
	}

	public String forgotPswd() {
		try {
			EntityManager em01 = emfLogin.createEntityManager();
			em01.getTransaction().begin();
			Query query = em01.createNamedQuery("CustomerInfo.findByEmail");
			query.setParameter("email", customerInfo.getEmail());
			List cList = query.getResultList();
			logger.debug("aList a " + cList.size());
			if (cList.size() > 0) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				// String ccemail =
				// ctx.getExternalContext().getInitParameter("custSupportEmail");

				CustomerInfo ci = (CustomerInfo) cList.get(0);
				EncryptDecryptString eds = new EncryptDecryptString();
				String cryptPswd = eds.decrypt(ci.getPassword());

				String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
				String host = "mail.eventxpert.in";
				final String username = "jagadish@eventxpert.in";
				final String password = "jagadish";
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.auth", "true");

				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				Message messageU = new MimeMessage(session);
				messageU.setFrom(new InternetAddress(from));
				messageU.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ci.getEmail()));
				messageU.setSubject("Your EventXpert Account Password Recovery");
				messageU.setText("Dear "
						+ ci.getFirstname()
						+ ","
						+ "\n\n You initiated a request to help with your Account Password."
						+ "\n\n We will send your temporary password via another email."
						+ "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
						+ "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
						+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "
						+ ccemail + " ." + "\n\n EventXpert Team");

				Transport.send(messageU);

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ci.getEmail()));
				message.setSubject("Your EventXpert Account Password Recovery");
				message.setText("Dear "
						+ ci.getFirstname()
						+ ","
						+ "\n\n You initiated a request to help with your Account Password."
						+ "\n\n The password for the your EventXpert account is \u0002"
						+ cryptPswd
						+ "\n\n For security reasons, we highly recommend you to change temporary password as soon as you login to www.eventxpert.in for the first time. \u0002"
						+ "\n\n If you received this recovery email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail."
						+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "
						+ ccemail + " ." + "\n\n EventXpert Team");

				Transport.send(message);

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Your password is sent to your registered email-id", "Password Sent");
				FacesContext.getCurrentInstance().addMessage("Password Sent", msg);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Entered Email is not registered with us", "Email not registered");
				FacesContext.getCurrentInstance().addMessage("Email not registered", msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is not registered",
					"Email not registered");
			FacesContext.getCurrentInstance().addMessage("Email not registered", msg);

		} finally {
			// em.close();

		}
		return "";
	}

	public String resendActLink() {
		try {
			// logger.debug("link "+customerInfo.getEmail());
			EntityManager em01 = emfLogin.createEntityManager();
			em01.getTransaction().begin();
			Query query = em01.createNamedQuery("CustomerInfo.findByEmail");
			query.setParameter("email", customerInfo.getEmail());
			List cList = query.getResultList();
			logger.debug("aList a " + cList.size());
			if (cList.size() > 0) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				// String ccemail =
				// ctx.getExternalContext().getInitParameter("custSupportEmail");

				CustomerInfo ci = (CustomerInfo) cList.get(0);
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
						.getExternalContext().getRequest();
				String link = "http://" + request.getHeader("host") + request.getContextPath() + "/activate.jsf?key="
						+ ci.getActivationKey() + "&ty=C";
				logger.debug("link " + link);

				String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
				String host = "mail.eventxpert.in";
				final String username = "jagadish@eventxpert.in";
				final String password = "jagadish";
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.auth", "true");

				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ci.getEmail()));
				message.setSubject("Activate your EventXpert Account");
				message.setText("Welcome "
						+ ci.getFirstname()
						+ " "
						+ ci.getLastname()
						+ ".,"
						+ "\n\n Thanks for registering with EventXpert."
						+ "\n\n To prevent unauthorized use of your email address, we need to make sure that you are the rightful owner of this email ID."
						+ "\n\n Please click on the link below to confirm your email address:"
						+ "\n\n "
						+ link
						+ "\n\n If you received this verification email in error, it's likely that another user entered your email address on our website. In this case, you may simply delete this mail. If you are unable to click on the link above, please copy and paste above URL in your browser's address bar."
						+ "\n\n Please do not reply to this email. If you have any questions, please don't hesitate to contact our customer service team at "
						+ ccemail + " ." + "\n\n EventXpert Team");

				Transport.send(message);

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"An Activation link is sent to your registered email-id", "Link Sent");
				FacesContext.getCurrentInstance().addMessage("Password Sent", msg);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Entered Email is not registered with us", "Email not registered");
				FacesContext.getCurrentInstance().addMessage("Email not registered", msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is not registered",
					"Email not registered");
			FacesContext.getCurrentInstance().addMessage("Email not registered", msg);

		} finally {
			// em.close();

		}
		setResendLink(false);
		setForgotLink(false);
		return "";
	}

	private String oldPassword;
	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String updatePassword() {

		try {
			// logger.debug("link "+customerInfo.getEmail());

			EncryptDecryptString eds = new EncryptDecryptString();
			String cryptPswd = eds.encrypt(oldPassword);
			logger.debug("key password " + cryptPswd);
			// oldPassword = cryptPswd;

			EntityManager em01 = emfLogin.createEntityManager();
			em01.getTransaction().begin();
			Query query = em01
					.createQuery("SELECT c FROM CustomerInfo c WHERE c.customerId = :customerId and c.password = :password");
			query.setParameter("customerId", customerInfo.getCustomerId());
			query.setParameter("password", cryptPswd);
			List cList = query.getResultList();
			logger.debug("aList a " + cList.size());
			if (cList.size() > 0) {
				if (oldPassword.equals(newPassword)) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Entered Passwords are same. Try Again", "Password Matched");
					FacesContext.getCurrentInstance().addMessage("Password Matched", msg);
					return "";
				} else {

					String cryptPswdnew = eds.encrypt(newPassword);
					logger.debug("key password " + cryptPswdnew);

					customerInfo.setPassword(cryptPswdnew);
					em01.merge(customerInfo);
					em01.getTransaction().commit();
					em01.close();

					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Successfully",
							"Password Changed");
					FacesContext.getCurrentInstance().addMessage("Password Changed", msg);
				}
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Entered Password does not match. Try Again", "Password Not Matched");
				FacesContext.getCurrentInstance().addMessage("Password Not Matched", msg);
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"There is some unexpected error. Sorry for the inconvenience, Please try again later", "Exception");
			FacesContext.getCurrentInstance().addMessage("Exception", msg);

		} finally {
			// em.close();

		}
		return "";
	}

	public String saveFBCustomer() {
		try {
			customerInfo.setIsActive('Y');
			EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em1 = emf1.createEntityManager();
			Timestamp ts = new Timestamp(new Date().getTime());
			customerInfo.setCustomerEffectiveDate(ts);
			customerInfo.setCustomerId(0);
			customerInfo.setLastLogin(new Date());
			em1.getTransaction().begin();
			em1.persist(customerInfo);
			em1.getTransaction().commit();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			logger.debug("request " + request.getHeader("host") + " d " + request.getContextPath() + " d "
					+ request.getRequestURI());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", customerInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// em.close();
		}
		return "index.xhtml?faces-redirect=true";
	}

	public void showForgot() {
		if (forgotLink) {
			setForgotLink(false);
		} else {
			setForgotLink(true);
			setShowActLink(false);
			setResendLink(false);
		}
	}

	public void showBtn() {
		if (selectBoolean) {
			setEnableBtn(true);
		} else {
			setEnableBtn(false);
		}
	}

	public void handleClose(CloseEvent event) {
		enableLogin();
		setSelectBoolean(false);
		setEnableBtn(false);
		setForgotLink(false);
		setShowActLink(false);
		customerInfo = new CustomerInfo();
	}

	public void enableResend() {
		setResendLink(true);
		setForgotLink(true);
		setShowActLink(false);
	}

	public void enableRegister() {
		setShowRegister(true);
	}

	public void enableLogin() {
		setShowRegister(false);
	}

	public String getFacebookUrlAuth() {
		setReDirectUrl();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		String sessionId = session.getId();
		String appId = "545927382195713";
		String redirectUrl = getFacebookUrl();
		String returnValue = "https://www.facebook.com/dialog/oauth?client_id=" + appId + "&redirect_uri="
				+ redirectUrl + "&scope=email,user_birthday&state=" + sessionId;
		return returnValue;
	}

	public String getGoogleUrlAuth() {
		setReDirectUrl();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		String sessionId = session.getId();
		String appId = "660040520956-nuc48ukl23to6c1sgofjljkkrjlva6kb.apps.googleusercontent.com";
		String redirectUrl = getGoogleUrl();
		String returnValue = "https://accounts.google.com/o/oauth2/auth?scope=email%20profile&state=" + sessionId
				+ "&redirect_uri=" + redirectUrl + "&response_type=code&client_id=" + appId
				+ "&access_type=online&approval_prompt=force";
		return returnValue;
	}

	private void setReDirectUrl() {
		Properties prop = new Properties();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();	
		InputStream input = null;
		input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");
		try {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		StringBuffer url = request.getRequestURL();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rUrl", url.toString());
		Map<String, String> pMap = new HashMap<String, String>();
		setFacebookUrl(prop.getProperty("redirectFBUrl"));
		setGoogleUrl(prop.getProperty("redirectGUrl"));
		pMap.put("fbUrl", facebookUrl);
		pMap.put("gUrl", googleUrl);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pMap", pMap);
	}

	public String getUserFromSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		String userName = (String) session.getAttribute("FACEBOOK_USER");
		if (userName != null) {
			return "Hello " + userName;
		} else {
			return "";
		}
	}

	/**
	 * @return the forgotLink
	 */
	public boolean isForgotLink() {
		return forgotLink;
	}

	/**
	 * @param forgotLink
	 *            the forgotLink to set
	 */
	public void setForgotLink(boolean forgotLink) {
		this.forgotLink = forgotLink;
	}

	/**
	 * @return the enableBtn
	 */
	public boolean isEnableBtn() {
		return enableBtn;
	}

	/**
	 * @param enableBtn
	 *            the enableBtn to set
	 */
	public void setEnableBtn(boolean enableBtn) {
		this.enableBtn = enableBtn;
	}

	/**
	 * @return the selectBoolean
	 */
	public boolean isSelectBoolean() {
		return selectBoolean;
	}

	/**
	 * @param selectBoolean
	 *            the selectBoolean to set
	 */
	public void setSelectBoolean(boolean selectBoolean) {
		this.selectBoolean = selectBoolean;
	}

	/**
	 * @return the resendLink
	 */
	public boolean isResendLink() {
		return resendLink;
	}

	/**
	 * @param resendLink
	 *            the resendLink to set
	 */
	public void setResendLink(boolean resendLink) {
		this.resendLink = resendLink;
	}

	/**
	 * @return the showRegister
	 */
	public boolean isShowRegister() {
		return showRegister;
	}

	/**
	 * @param showRegister
	 *            the showRegister to set
	 */
	public void setShowRegister(boolean showRegister) {
		this.showRegister = showRegister;
	}	

	/**
	 * @return the googleUrl
	 */
	public String getGoogleUrl() {
		return googleUrl;
	}

	/**
	 * @param googleUrl the googleUrl to set
	 */
	public void setGoogleUrl(String googleUrl) {
		this.googleUrl = googleUrl;
	}

	/**
	 * @return the facebookUrl
	 */
	public String getFacebookUrl() {
		return facebookUrl;
	}

	/**
	 * @param facebookUrl the facebookUrl to set
	 */
	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	/**
	 * @return the showChangePassword
	 */
	public boolean isShowChangePassword() {
		return showChangePassword;
	}

	/**
	 * @param showChangePassword the showChangePassword to set
	 */
	public void setShowChangePassword(boolean showChangePassword) {
		this.showChangePassword = showChangePassword;
	}

}
