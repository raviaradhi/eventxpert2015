package com.circulous.xpert.event.managedbean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class SendEmailService
 */
public class SendEmailService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendEmailService() {
        super();
        // TODO Auto-generated constructor stub
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
	{
		doPost(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String txtdatepick1 = request.getParameter("txtdatepick1");
		String ddlTime = request.getParameter("ddlTime");
		String ddlCity1 = request.getParameter("ddlCity1");
		String ddlArea1 = request.getParameter("ddlArea1");
		String txtBudget1 = request.getParameter("txtBudget1");
		String txtChild1 = request.getParameter("txtChild1");
		String txtAdult1 = request.getParameter("txtAdult1");
		String txtGuest1 = request.getParameter("txtGuest1");
		String Name = request.getParameter("Name");
		String TelNo = request.getParameter("TelNo");
		String email = request.getParameter("email");
		String comments = request.getParameter("comments");
		String venue = request.getParameter("venue");
		String[] services = request.getParameterValues("services");		
		String ServiesSelected = null;
		if(services != null){
			for (int i = 0; i < services.length; i++) {
				String serv = services[i];
				
			    // if null, it means checkbox is not in request, so unchecked 
			    //if (myCheckBoxValue != null){
				if(ServiesSelected != null){
					ServiesSelected = ServiesSelected +"-"+ serv;
				} else {
					ServiesSelected = serv;	
				}
					
				
			    //}
			        

			}
		}
		
		
		String messageBody = "Date:"+txtdatepick1 +"\n Time:" + ddlTime +"\nCity:" + ddlCity1 +"\nArea:" + ddlArea1 +"\nBudget:" +txtBudget1+"\nChildren:" + txtChild1 +"\nAdult:" + txtAdult1 +"\nTotal Guests:" + txtGuest1 +"\nVisior Name:" 
								+ Name +"\nPhone:" + TelNo +"\nEmail:" +  email +"\nRequirements:" + comments +"\nVenue:" +venue+"\nServices selected:" +ServiesSelected;
    	// Recipient's email ID needs to be mentioned.
		//String to = ""+ccemail+"";
		 FacesContext ctx = FacesContext.getCurrentInstance();
                    //String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			
		// Sender's email ID needs to be mentioned
		String from = ""+ccemail+"";
		
		// Assuming you are sending email from localhost
		String host = "mail.eventxpert.in";
		//String host = "smtp.gmail.com";
		String username="jagadish@eventxpert.in";
		String password="jagadish";
		String[] recipients = {""+ccemail+"","raviaradhi@gmail.com","vjshankarb@gmail.com","naveeng@eventxpert.in","raviraju@eventxpert.in","sygodha@gmail.com","chjagadishsingh@gmail.com"};	
		//String[] recipients = {"chjagadishsingh@gmail.com","sygodha@gmail.com"};
		//String[] recipients = {"chjagadishsingh@gmail.com"};		
		// Get system properties
		Properties properties = System.getProperties();
		//properties.put("mail.smtp.starttls.enable", "tru5");
	    properties.put("mail.smtp.host", host);
	    properties.put("mail.smtp.port", "25");
	    properties.put("mail.smtp.auth", "true");
		// Setup mail server
		//properties.setProperty("mail.smtp.host", host);
		//properties.setProperty("mail.smtp.port", "2525");
		// Get the default Session object.
	    Session sessionMail = Session.getInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {     
                    return new PasswordAuthentication("jagadish@eventxpert.in","jagadish");
            }                                                                   
           });                                                                  
                                                                                
       sessionMail.setDebug(true);
	   // Session session = Session.getInstance(properties,null);
		/*,  new javax.mail.Authenticator() {  
			  
            protected PasswordAuthentication getPasswordAuthenticaton() {  
                return new PasswordAuthentication("event12","xpert12"); // username and password  
            }  
        });*/  
		
		// Set response content type
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try{
		 // Create a default MimeMessage object.
		 MimeMessage message = new MimeMessage(sessionMail);
		 // Set From: header field of the header.
		 message.setFrom(new InternetAddress(from));
		 // Set To: header field of the header.
		 
		 InternetAddress[] addressTo = new InternetAddress[recipients.length];
         for (int i = 0; i < recipients.length; i++)
         {
             addressTo[i] = new InternetAddress(recipients[i]);
         }
         message.setRecipients(RecipientType.TO, addressTo); 
		// message.addRecipient(Message.RecipientType.TO,
		                        //  new InternetAddress(to));
		 // Set Subject: header field
		 message.setSubject(Name +"-"+txtdatepick1+"-"+ddlTime);
		 // Now set the actual message"This is the Subject Line!"
		 message.setText(messageBody);
		 // Send message
		 Transport transport = sessionMail.getTransport("smtp");		
		 transport.connect(host,username,password);	 
		 Transport.send(message);		
		 response.sendRedirect("http://eventxpert.in/emailconfirm.htm");
		}catch (MessagingException mex) {
		 mex.printStackTrace();
		}
	}

}



