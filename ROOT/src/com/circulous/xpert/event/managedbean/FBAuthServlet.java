package com.circulous.xpert.event.managedbean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.circulous.xpert.jpa.entities.CustomerInfo;

@WebServlet("*.gsec")
public class FBAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 8071426090770097330L;

	public FBAuthServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		HttpSession httpSession = request.getSession();
		String faceCode = request.getParameter("code");
		String state = request.getParameter("state");
		Map<String, String> props = (Map) request.getSession().getAttribute("pMap");
		String redirectUrl = (String) request.getSession().getAttribute("rUrl");
		String accessToken = getFacebookAccessToken(faceCode, props);
		if (null == accessToken) {
			response.sendRedirect(redirectUrl);
		} else {
			request.getSession().setAttribute("fAccessCode", accessToken);
			Map<String, String> dataMap = getUserMailAddressFromJsonResponse(accessToken, httpSession);
			String sessionID = httpSession.getId();
			CustomerManagedBean bean = (CustomerManagedBean) request.getSession().getAttribute("customerManagedBean");
			if (state.equals(sessionID)) {
				CustomerInfo info = enableLogin(dataMap.get("email"));
				if (null != info) {
					request.getSession().setAttribute("userId", info);
					bean.setCustomerInfo(info);
					bean.setShowChangePassword(true);
					response.sendRedirect(redirectUrl);
				} else {
					CustomerInfo custInfo = new CustomerInfo();
					custInfo.setEmail(dataMap.get("email"));
					custInfo.setFirstname(dataMap.get("firstName"));
					custInfo.setLastname(dataMap.get("lastName"));
					bean.setCustomerInfo(custInfo);
					bean.setShowChangePassword(true);
					response.sendRedirect(request.getContextPath() + "/NewUser.jsf");
				}
			} else {
				System.err.println("CSRF protection validation");
				response.sendRedirect(redirectUrl);
			}
		}
	}

	private String getFacebookAccessToken(String faceCode, Map<String, String> props) {
		String token = null;
		if (faceCode != null && !"".equals(faceCode)) {
			try {
				String appId = "545927382195713";
				String redirectUrl = props.get("fbUrl");
				String faceAppSecret = "3d76a223fb8cd5b77ea340553c919f29";
				String newUrl = "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri="
						+ redirectUrl + "&client_secret=" + faceAppSecret + "&code=" + faceCode;
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpGet httpget = new HttpGet(newUrl);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String responseBody = httpclient.execute(httpget, responseHandler);
					token = StringUtils.removeEnd(StringUtils.removeStart(responseBody, "access_token="),
							"&expires=5180795");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					httpclient.getConnectionManager().shutdown();
				}
			} catch (Exception e) {

			}
		}
		return token;
	}

	private Map<String, String> getUserMailAddressFromJsonResponse(String accessToken, HttpSession httpSession) {
		HttpClient httpclient = new DefaultHttpClient();
		Map<String, String> dataMap = new HashMap<String, String>();
		try {
			if (accessToken != null && !"".equals(accessToken)) {
				String newUrl = "https://graph.facebook.com/me?access_token=" + accessToken;
				httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(newUrl);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httpget, responseHandler);
				JSONObject json;
				try {
					json = (JSONObject) new JSONParser().parse(responseBody);
					dataMap.put("firstName", (String) json.get("first_name"));
					dataMap.put("lastName", (String) json.get("last_name"));
					dataMap.put("id", (String) json.get("id"));
					dataMap.put("email", (String) json.get("email"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("Facebook is null");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return dataMap;
	}

	private CustomerInfo enableLogin(String email) {

		EntityManagerFactory emfLogin = Persistence.createEntityManagerFactory("ROOT");
		EntityManager emLogin = emfLogin.createEntityManager();
		if (!emLogin.isOpen()) {
			emLogin.getTransaction().begin();
		}
		Query query = emLogin.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email");
		query.setParameter("email", email);
		List cList = query.getResultList();
		if (cList.size() > 0) {
			CustomerInfo c = (CustomerInfo) cList.get(0);
			return c;
		} else {
			return null;
		}

	}

	/*
	 * private CustomerInfo enableLogin(String email) { EntityManagerFactory
	 * emfLogin = Persistence.createEntityManagerFactory("ROOT"); EntityManager
	 * emLogin = emfLogin.createEntityManager(); if (!emLogin.isOpen()) {
	 * emLogin.getTransaction().begin(); } Query query =
	 * emLogin.createQuery("SELECT ci FROM CustomerInfo ci  WHERE ci.email = :email"
	 * ); query.setParameter("email", email); List cList =
	 * query.getResultList(); if (cList.size() > 0) { CustomerInfo c =
	 * (CustomerInfo) cList.get(0); Query qu = emLogin.createQuery(
	 * "UPDATE CustomerInfo c SET c.lastLogin = CURRENT_DATE WHERE c.email = :email"
	 * ); qu.setParameter("email", email); qu.executeUpdate();
	 * emLogin.getTransaction().commit(); return c; } else { return null; } }
	 */

}
