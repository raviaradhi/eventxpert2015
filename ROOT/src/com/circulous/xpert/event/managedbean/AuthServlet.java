package com.circulous.xpert.event.managedbean;

import java.io.IOException;
import java.util.ArrayList;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.circulous.xpert.jpa.entities.CustomerInfo;
import com.google.common.collect.ImmutableMap;

@WebServlet("*.sec")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 8071426090770097330L;

	public AuthServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		HttpSession httpSession = request.getSession();
		String gCode = request.getParameter("code");
		String redirectUrl = (String) request.getSession().getAttribute("rUrl");
		if (null == gCode) {
			response.sendRedirect(redirectUrl);
		} else {
			String state = request.getParameter("state");
			Map<String, String> props = (Map) request.getSession().getAttribute("pMap");
			String accessToken = getGoogleAccessToken(gCode, props);

			request.getSession().setAttribute("gAccessCode", accessToken);
			Map<String, String> dataMap = getUserInfo(new StringBuilder(
					"https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(accessToken).toString());
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

	private String getGoogleAccessToken(String gCode, Map<String, String> props) {
		String token = null;
		String clientId = "660040520956-nuc48ukl23to6c1sgofjljkkrjlva6kb.apps.googleusercontent.com";
		String clientSecret = "V7T5pTuyqUtYNNZzMSPCbEBl";
		String redirectUrl = props.get("gUrl");
		String body = null;
		try {
			body = post(
					"https://accounts.google.com/o/oauth2/token",
					ImmutableMap.<String, String> builder().put("code", gCode).put("client_id", clientId)
							.put("client_secret", clientSecret).put("redirect_uri", redirectUrl)
							.put("grant_type", "authorization_code").build());
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) new JSONParser().parse(body);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse json " + body);
		}
		token = (String) jsonObject.get("access_token");
		return token;
	}

	public String post(String url, Map<String, String> formParameters) throws ClientProtocolException, IOException {
		HttpPost request = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : formParameters.keySet()) {
			nvps.add(new BasicNameValuePair(key, formParameters.get(key)));
		}

		request.setEntity(new UrlEncodedFormEntity(nvps));
		return execute(request);
	}

	private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode()
					+ ", with body " + body);
		}
		return body;
	}

	public Map<String, String> getUserInfo(String url) throws ClientProtocolException, IOException {
		String jsonBody = execute(new HttpGet(url));
		JSONObject json = null;
		Map<String, String> dataMap = new HashMap<String, String>();
		try {
			json = (JSONObject) new JSONParser().parse(jsonBody);
			dataMap.put("firstName", (String) json.get("given_name"));
			dataMap.put("lastName", (String) json.get("family_name"));
			dataMap.put("email", (String) json.get("email"));
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse json " + jsonBody);
		}
		return dataMap;
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
}
