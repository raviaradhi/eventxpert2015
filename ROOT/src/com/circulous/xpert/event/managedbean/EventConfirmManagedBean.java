/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.EspPackageInfo;
import com.circulous.xpert.jpa.entities.EspPackageItemInfo;
import com.circulous.xpert.jpa.entities.EventInfo;
import com.circulous.xpert.jpa.entities.EventServiceProviderInfo;
import com.circulous.xpert.jpa.entities.ServiceProviderInfo;
import com.circulous.xpert.jpa.entities.ServiceTypeMaster;
import com.circulous.xpert.jpa.entities.VenueInfo;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 
 * @author SANJANA
 */
@ManagedBean
// @ViewScoped
// @RequestScoped
@SessionScoped
public class EventConfirmManagedBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(EventConfirmManagedBean.class);
	// private EventDetailsManagedBean eventDetailsManagedBean;
	private int eventId;
	private EventInfo eventInfo = new EventInfo();
	private ArrayList<EspPackageInfo> espInfo = new ArrayList<EspPackageInfo>();
	private ArrayList<EspPackageItemInfo> espItemInfo = new ArrayList<EspPackageItemInfo>();
	private ArrayList<EventServiceProviderInfo> espServiceInfo = new ArrayList<EventServiceProviderInfo>();

	private HashMap services = new HashMap();
	private HashMap venues = new HashMap();

	private String area;
	private String city;
	private String eventType;
	private String venueType;

	private boolean stopPoll = false;

	public boolean isStopPoll() {
		return stopPoll;
	}

	public void setStopPoll(boolean stopPoll) {
		this.stopPoll = stopPoll;
	}

	/**
	 * Creates a new instance of EventConfirmManagedBean
	 */
	public EventConfirmManagedBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		EventDetailsManagedBean eventDetailsManagedBean = (EventDetailsManagedBean) context.getApplication()
				.evaluateExpressionGet(context, "#{eventDetailsManagedBean}", EventDetailsManagedBean.class);
		logger.debug("EventConfirmManagedBean*****************  " + eventDetailsManagedBean.getEventId());
		getEventDtls(eventDetailsManagedBean.getEventId());
                Properties prop = new Properties();
                    InputStream input = null;

                    try {

                             FacesContext ctx = FacesContext.getCurrentInstance();
                        ExternalContext externalContext = ctx.getExternalContext();
                        input = externalContext.getResourceAsStream("/WEB-INF/eXpertConfig.properties");

                            // load a properties file
                            prop.load(input);

                            // get the property value and print it out
                            path = prop.getProperty("imgpath");
                            url = prop.getProperty("imgurl");
                            

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

        String path = "";
        String url = "";

	public String sendEmail() {

		String from = "EventXpert<eventxpert@eventxpert.in>/ Event2013";
		String host = "mail.eventxpert.in";
		final String username = "jagadish@eventxpert.in";
		final String password = "jagadish";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			HttpSession session1 = (HttpSession) externalContext.getSession(true);
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			//String path = context.getInitParameter("imgpath");			
			File dir = new File(path+ "/files");
			String titleFile = path + "/files/Title.png";
			String filename = path + "/files/eventNo_" + eventInfo.getEventId() + ".pdf";
			File eventFile = new File(filename);
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					logger.info("Directory is created!");
				} else {
					logger.info("Failed to create directory!!");
				}
			}
			String url = request.getRequestURL().append(";jsessionid=").append(session1.getId().toString()).toString();
			try {

				OutputStream file = new FileOutputStream(new File(filename));
				Document document = new Document();
				PdfWriter.getInstance(document, file);
				Image image = Image.getInstance(titleFile);
				image.scaleAbsolute(500f, 125f);
				Date eventDate = eventInfo.getEventEffectiveDate();
				SimpleDateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
				SimpleDateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date d = readFormat.parse(eventDate.toString());
				logger.debug("dattteee " + d);
				String edate = writeFormat.format(d);
				Font font1 = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
				Font font3 = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
				document.open();
				document.add(image);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				Paragraph line = new Paragraph("Date of Booking : " + edate, font3);
				line.setAlignment(Element.ALIGN_RIGHT);
				Paragraph line1 = new Paragraph("Quote # :" + eventInfo.getEventId().toString(), font3);
				line1.setAlignment(Element.ALIGN_RIGHT);
				Paragraph line2 = new Paragraph("From : EventXpert", font3);
				line2.setAlignment(Element.ALIGN_LEFT);
				Paragraph line3 = new Paragraph("To : " + eventInfo.getCustomerId().getFirstname(), font3);
				line3.setAlignment(Element.ALIGN_LEFT);
				document.add(line);
				document.add(line1);
				document.add(line2);
				document.add(line3);
				document.add(Chunk.NEWLINE);
				document.add(generateTable());
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(new Chunk("All payments to be made in the name of ï¿½", font3));
				document.add(new Chunk("M/S CIRCULOS TECHNOLOGIES PVT LTD", font1));
				document.add(new Chunk("ï¿½, payable at Hyderabad.", font3));
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(new Paragraph("Terms and Conditions :", font1));
				document.add(new Paragraph("1. All quotes are valid for the period stated on the quotation.", font3));
				document.add(new Paragraph(
						"2. The price in the final quote may vary from the original request if there are any prices or service changes requested by the customers. Circulos reserves the right to alter the prices in the quote, as long as the quote has not been confirmed with the customer.",
						font3));
				document.add(new Paragraph(
						"3. Quotes shall be deemed to correctly interpret the original requirement and are based on the cost at the time the quote is given. If you later require any changes to the quote, and we agree to the changes, these changes will be charged at our prevailing rate.",
						font3));
				document.add(new Paragraph(
						"4. Any discounts given on the quote are usually worked out based on the overall quote amount, if there's any significant changes to the overall quote amount, the discount may also be changed to reflect it.",
						font3));
				document.add(new Paragraph(
						"5. In case of event cancellation, 48 hrs before the event,  50%  cancellation  charges are levied.",
						font3));
				document.add(new Paragraph(
						"6. In case of event cancellation 24 hrs before the event 100%  cancellation charges are levied.",
						font3));
				document.add(new Paragraph(
						"7. Confirmation advance of 80% of the estimated event charge, and the balance must be settled One day before the event date.",
						font3));
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(new Paragraph(
						"To accept this quotation, sign here and return:____________________________", font3));
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				Paragraph business = new Paragraph("Thank you for your business!", font1);
				business.setAlignment(Element.ALIGN_CENTER);
				document.add(business);
				document.close();
				file.close();
				logger.info("Pdf created successfully..");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			facesContext.responseComplete();
                         FacesContext ctx = FacesContext.getCurrentInstance();
                    String ccemail = ctx.getExternalContext().getInitParameter("custSupportEmail");
			
			Date eventDate = eventInfo.getEventEffectiveDate();
			SimpleDateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			SimpleDateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date d = readFormat.parse(eventDate.toString());
			logger.debug("dattteee " + d);
			String edate = writeFormat.format(d);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(eventInfo.getCustomerId().getEmail()));
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(""+ccemail+""));

			message.setSubject("EventXpert booking for " + eventInfo.getEventType().getEventType() + " event on "
					+ edate + " - Confirmation #" + eventInfo.getEventId());
			message.setHeader("<h1>Event Confirmation</h1>", "text/html");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setHeader("<h1>Event Confirmation</h1>", "text/html");
			textPart.setText("Dear "
					+ eventInfo.getCustomerId().getFirstname()
					+ ","
					+ "\n\nThank you for booking your Event through EventXpert !"
					+ "\n\nYour Event has been registered with Confirmation #"
					+ eventInfo.getEventId()
					+ ", please refer to this number while talking to the Customer Support team about "
					+ "\nthe Event."
					+ "\n\nThank you for your Business! "
					+ "\n\nCONTACT US "
					+ "\n\nPlease do not reply directly to this email. It is sent from a notification-only address, and is not monitored for any incoming emails. For further assistance, contact us at <phone#> or email: "+ccemail+". We would love to hear your feedback about your recent experience with us. ");
			MimeBodyPart attachFilePart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(filename);
			attachFilePart.setDataHandler(new DataHandler(fds));
			attachFilePart.setFileName(fds.getName());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(textPart);
			mp.addBodyPart(attachFilePart);

			message.setContent(mp, "text/html");

			message.saveChanges();

			Transport.send(message);
			stopPoll = true;
			logger.debug("Done");

			eventFile.delete();

			HashMap servicesInfoV = (HashMap) venues.get("Venue");
			if (servicesInfoV != null) {
				for (int j = 0; j < servicesInfoV.keySet().toArray().length; j++) {
					VenueInfo vinfo = (VenueInfo) servicesInfoV.keySet().toArray()[j];
					if (vinfo.getVenueTypeId().getVenueTypeId() != 1) {
						Message message1 = new MimeMessage(session);
						message1.setFrom(new InternetAddress(from));
						message1.setRecipients(Message.RecipientType.TO,
								InternetAddress.parse(vinfo.getAddressId().getEmail()));
						message1.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(""+ccemail+""));
						// Ravi Start
						// message1.setSubject(" New Event booking for "+eventInfo.getEventType().getEventType()+" on "+edate);
						message1.setSubject("EventXpert " + eventInfo.getEventType().getEventType()
								+ " Event booking for " + vinfo.getVenueName() + " on " + edate);
						// Ravi End
						message1.setHeader("Event Confirmation", "text/html");
						message1.setText("Hello " + vinfo.getVenueName() + ","
								+ "\n\nYou have a new booking from EventXpert Customer for a "
								+ eventInfo.getEventType().getEventType() + " on " + edate + " for "
								+ eventInfo.getEstimatedGuests() + " Guests."
								+ "\n\nPlease contact EventXpert team to confirm your availability and other details."
								+ "\n\nThanks," + "\n\nEventXpert Team");
						Transport.send(message1);
					}
				}
			}

			if (services != null) {
				for (int j = 0; j < services.keySet().toArray().length; j++) {
					HashMap servicesInfo = (HashMap) services.get(services.keySet().toArray()[j]);
					if (servicesInfo != null) {
						for (int j1 = 0; j1 < servicesInfo.keySet().toArray().length; j1++) {
							ServiceProviderInfo sinfo = (ServiceProviderInfo) servicesInfo.keySet().toArray()[j1];
							message = new MimeMessage(session);
							message.setFrom(new InternetAddress(from));
							message.setRecipients(Message.RecipientType.TO,
									InternetAddress.parse(sinfo.getAddressId().getEmail()));
							message.setRecipients(Message.RecipientType.BCC,
									InternetAddress.parse(""+ccemail+""));
							message.setSubject(" New Event booking for " + eventInfo.getEventType().getEventType()
									+ " on " + edate);
							message.setHeader("Event Confirmation", "text/html");
							message.setText("Hello "
									+ sinfo.getServiceProviderName()
									+ ","
									+ "\n\nYou have a new booking from EventXpert Customer for a "
									+ eventInfo.getEventType().getEventType()
									+ " on "
									+ edate
									+ " for "
									+ eventInfo.getEstimatedGuests()
									+ " Guests."
									+ "\n\nPlease contact EventXpert team to confirm your availability and other details."
									+ "\n\nThanks," + "\n\nEventXpert Team");
							Transport.send(message);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		// return "eventConfirm";
		logger.debug("retet");

		return returnFirstPage();

		// }
	}

	public PdfPTable generateTable() {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(90);
		table.setSpacingBefore(2.0f);
		table.setSpacingAfter(30.0f);
		PdfPTable lTable = new PdfPTable(1);
		PdfPTable rTable = new PdfPTable(3);

		lTable.getDefaultCell().setBorder(0);
		rTable.getDefaultCell().setBorder(0);

		Font fontAll = new Font(Font.HELVETICA, 10, Font.NORMAL);
		Font fontHeads = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.WHITE);

		Date eventDate = eventInfo.getEventEffectiveDate();
		SimpleDateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		SimpleDateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date d = null;
		try {
			d = readFormat.parse(eventDate.toString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String edate = writeFormat.format(d);

		PdfPTable table1 = new PdfPTable(2);
		PdfPCell cell = new PdfPCell(new Paragraph("Event Details", fontHeads));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(10.0f);
		cell.setBackgroundColor(WebColors.getRGBColor("#980000"));
		cell.setBorderWidth(0);
		table1.addCell(cell);
		table1.getDefaultCell().setBorder(0);

		Font font = new Font(Font.HELVETICA, 10, Font.BOLD);
		table1.addCell(new Paragraph("Event Date:", font));
		table1.addCell(new Paragraph(edate, fontAll));
		table1.addCell(new Paragraph("Location:", font));
		table1.addCell(new Paragraph(area + ", " + city, fontAll));
		table1.addCell(new Paragraph("Venue Type:", font));
		table1.addCell(new Paragraph(eventInfo.getVenueType().getVenueDescription(), fontAll));
		table1.addCell(new Paragraph("Guests:", font));
		table1.addCell(new Paragraph(Integer.toString(eventInfo.getEstimatedGuests()), fontAll));
		table1.addCell("");
		table1.addCell("");

		cell = new PdfPCell(new Paragraph("Service Requests", fontHeads));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(10.0f);
		cell.setBackgroundColor(WebColors.getRGBColor("#980000"));
		cell.setBorderWidth(0);
		table1.addCell(cell);
		lTable.addCell(table1);

		PdfPTable sTable, sdTable;

		List<PdfPTable> ltables = new ArrayList<PdfPTable>();

		List<PdfPTable> rTables = new ArrayList<PdfPTable>();

		Iterator iter = (Iterator) venues.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			sTable = new PdfPTable(2);
			sTable.getDefaultCell().setBorder(0);
			sdTable = new PdfPTable(3);
			sdTable.getDefaultCell().setBorder(0);
			PdfPCell sCell = new PdfPCell(new Paragraph((String) entry.getKey()));
			sCell.setBackgroundColor(Color.LIGHT_GRAY);
			sTable.getDefaultCell().setBorder(0);
			sCell.setColspan(2);
			sCell.setBorderWidth(0);
			sCell.setFixedHeight(20);
			sTable.addCell(sCell);
			sdTable.addCell("");
			sdTable.addCell("");
			sdTable.addCell("");
			sCell = new PdfPCell(new Paragraph((String) entry.getKey(), font));
			sCell.setBackgroundColor(Color.LIGHT_GRAY);
			sTable.getDefaultCell().setBorder(0);
			sCell.setColspan(3);
			sCell.setBorderWidth(0);
			sCell.setFixedHeight(20);
			sdTable.addCell(sCell);

			Iterator iterSub = (Iterator) ((HashMap<Object, Object>) entry.getValue()).entrySet().iterator();
			while (iterSub.hasNext()) {
				Map.Entry entryItr = (Map.Entry) iterSub.next();
				VenueInfo vi = (VenueInfo) entryItr.getKey();
				sCell = new PdfPCell(new Paragraph(vi.getVenueName(), fontAll));
				EventServiceProviderInfo esp = (EventServiceProviderInfo) entryItr.getValue();
				sCell.setColspan(2);
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sCell = new PdfPCell(new Paragraph("Amount:", fontAll));
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sTable.addCell(new Paragraph("Rs. " + esp.getEventServiceProviderTotamt(), font));
				sCell = new PdfPCell(new Paragraph("Tax(" + esp.getEventServiceProviderTax() + "%):", fontAll));
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sTable.addCell(new Paragraph("Rs. " + esp.getEventServiceProviderTaxamt(), font));
				sCell = new PdfPCell(new PdfPCell(new Paragraph("Sub Total:", fontAll)));
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sTable.addCell(new Paragraph("Rs. " + esp.getEventServiceProviderCost(), font));

				PdfPCell nameCell = new PdfPCell(new Paragraph(vi.getVenueName(), fontAll));
				nameCell.setColspan(3);
				nameCell.setBorderWidth(0);
				sdTable.addCell(nameCell);
				PdfPCell tempCell = new PdfPCell(new Paragraph("Address: ", font));
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);
				tempCell = new PdfPCell(new Paragraph(vi.getAddressId().getAddressLine1() + ", "
						+ vi.getAddressId().getAddressLine2() + ", " + vi.getAddressId().getCityId(), fontAll));
				tempCell.setColspan(2);
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);
				tempCell = new PdfPCell(new Paragraph("Hall: ", font));
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);
				tempCell = new PdfPCell(new Paragraph(esp.getHallId().getHallName(), fontAll));
				tempCell.setBorderWidth(0);
				tempCell.setColspan(2);
				sdTable.addCell(tempCell);
				sdTable.addCell("");
				sdTable.addCell("");
				sdTable.addCell("");
			}
			ltables.add(sTable);
			rTables.add(sdTable);
		}

		Iterator iterSrvc = (Iterator) services.entrySet().iterator();

		while (iterSrvc.hasNext()) {
			Map.Entry entrySrv = (Map.Entry) iterSrvc.next();
			sTable = new PdfPTable(2);
			sTable.getDefaultCell().setBorder(0);
			sdTable = new PdfPTable(3);
			sdTable.getDefaultCell().setBorder(0);
			PdfPCell sCell = new PdfPCell(new Paragraph((String) entrySrv.getKey()));
			sCell.setBackgroundColor(Color.LIGHT_GRAY);
			sTable.getDefaultCell().setBorder(0);
			sCell.setColspan(2);
			sCell.setBorderWidth(0);
			sCell.setFixedHeight(20);
			sTable.addCell(sCell);
			sdTable.addCell("");
			sdTable.addCell("");
			sdTable.addCell("");
			sCell = new PdfPCell(new Paragraph((String) entrySrv.getKey(), font));
			sCell.setBackgroundColor(Color.LIGHT_GRAY);
			sTable.getDefaultCell().setBorder(0);
			sCell.setColspan(3);
			sCell.setBorderWidth(0);
			sCell.setFixedHeight(20);
			sdTable.addCell(sCell);

			Iterator iterSubSrv = (Iterator) ((HashMap<Object, Object>) entrySrv.getValue()).entrySet().iterator();
			while (iterSubSrv.hasNext()) {
				Map.Entry entryItrSrv = (Map.Entry) iterSubSrv.next();
				ServiceProviderInfo vi = (ServiceProviderInfo) entryItrSrv.getKey();
				sCell = new PdfPCell(new Paragraph(vi.getServiceProviderName(), fontAll));
				EventServiceProviderInfo esp = (EventServiceProviderInfo) entryItrSrv.getValue();
				sCell.setColspan(2);
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);

				sCell = new PdfPCell(new Paragraph("Amount:", fontAll));
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sTable.addCell(new Paragraph("Rs. " + esp.getEventServiceProviderTotamt(), font));
				sCell = new PdfPCell(new Paragraph("Tax(" + esp.getEventServiceProviderTax() + "%):", fontAll));
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sTable.addCell(new Paragraph("Rs. " + esp.getEventServiceProviderTaxamt(), font));
				sCell = new PdfPCell(new PdfPCell(new Paragraph("Sub Total:", fontAll)));
				sCell.setBorderWidth(0);
				sTable.addCell(sCell);
				sTable.addCell(new Paragraph("Rs. " + esp.getEventServiceProviderCost(), font));

				PdfPCell nameCell = new PdfPCell(new Paragraph(vi.getServiceProviderName(), fontAll));
				nameCell.setColspan(3);
				nameCell.setBorderWidth(0);
				sdTable.addCell(nameCell);
				PdfPCell tempCell = new PdfPCell(new Paragraph("Address: ", font));
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);
				tempCell = new PdfPCell(new Paragraph(vi.getAddressId().getAddressLine1() + ", "
						+ vi.getAddressId().getAddressLine2() + ", " + vi.getAddressId().getCityId(), fontAll));
				tempCell.setColspan(2);
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);
				/*tempCell = new PdfPCell(new Paragraph("Package: ", font));
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);
				tempCell = new PdfPCell(new Paragraph("#PackageName Here", fontAll));
				tempCell.setColspan(2);
				tempCell.setBorderWidth(0);
				sdTable.addCell(tempCell);*/
				sdTable.addCell("");
				sdTable.addCell("");
				sdTable.addCell("");
			}
			ltables.add(sTable);
			rTables.add(sdTable);
		}

		for (PdfPTable pfdt : ltables)
			lTable.addCell(pfdt);

		Font fontHead = new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE);
		PdfPCell hdrCell = new PdfPCell(new Paragraph(eventType + " event Package Details", fontHead));
		hdrCell.setColspan(3);
		hdrCell.setBackgroundColor(WebColors.getRGBColor("#980000"));
		hdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hdrCell.setPadding(10.0f);
		rTable.addCell(hdrCell);

		PdfPCell tcell;
		for (PdfPTable pfdt : rTables) {
			tcell = new PdfPCell(pfdt);
			tcell.setColspan(3);
			tcell.setBorderWidth(0);
			rTable.addCell(tcell);
		}

		cell = new PdfPCell(new Paragraph("Cost Details", fontHeads));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(10.0f);
		cell.setBackgroundColor(WebColors.getRGBColor("#980000"));
		cell.setBorderWidth(0);
		table1 = new PdfPTable(2);
		table1.getDefaultCell().setBorder(0);
		table1.addCell(cell);
		table1.addCell(new Paragraph("Total Charges:", fontAll));
		table1.addCell(new Paragraph("Rs. " + eventInfo.getTotalAmount(), font));
		table1.addCell(new Paragraph("Service Charges(8%):", fontAll));
		table1.addCell(new Paragraph("Rs. " + eventInfo.getServiceCharges(), font));
		table1.addCell(new Paragraph("Service Tax(12.5%):", fontAll));
		table1.addCell(new Paragraph("Rs. " + eventInfo.getServiceTax(), font));
		table1.addCell(new Paragraph("Total Amount:", fontAll));
		table1.addCell(new Paragraph("Rs. " + eventInfo.getCost(), font));

		lTable.addCell(table1);

		PdfPCell rTableCell = new PdfPCell(rTable);
		rTableCell.setColspan(2);

		table.addCell(lTable);
		table.addCell(rTableCell);

		return table;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public ArrayList<EspPackageInfo> getEspInfo() {
		return espInfo;
	}

	public void setEspInfo(ArrayList<EspPackageInfo> espInfo) {
		this.espInfo = espInfo;
	}

	public ArrayList<EspPackageItemInfo> getEspItemInfo() {
		return espItemInfo;
	}

	public void setEspItemInfo(ArrayList<EspPackageItemInfo> espItemInfo) {
		this.espItemInfo = espItemInfo;
	}

	public ArrayList<EventServiceProviderInfo> getEspServiceInfo() {
		return espServiceInfo;
	}

	public void setEspServiceInfo(ArrayList<EventServiceProviderInfo> espServiceInfo) {
		this.espServiceInfo = espServiceInfo;
	}

	public EventInfo getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}

	public HashMap getServices() {
		return services;
	}

	public void setServices(HashMap services) {
		this.services = services;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public HashMap getVenues() {
		return venues;
	}

	public void setVenues(HashMap venues) {
		this.venues = venues;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getVenueType() {
		return venueType;
	}

	public void setVenueType(String venueType) {
		this.venueType = venueType;
	}

	public void getEventDtls(int eventId) {
		try {
			logger.debug("Inside getEventDtls " + eventId);
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
			EntityManager em = emf.createEntityManager();
			logger.debug("Inside getEventDtls after em " + eventId);
			Query query = em.createNamedQuery("EventInfo.findByEventId");
			query.setParameter("eventId", eventId);
			logger.debug("Inside getEventDtls query " + query);
			eventInfo = (EventInfo) query.getResultList().get(0);
			logger.debug("Inside getEventDtls eventInfo " + eventInfo);
			area = eventInfo.getAreaId().getAreaName();
			logger.debug("eventInfo.getEventType().getEventType()  " + eventInfo.getAreaId().getCityId());
			city = eventInfo.getAreaId().getCityId().getCityName();
			eventType = eventInfo.getEventType().getEventType();
			venueType = eventInfo.getVenueType().getVenueType();
			logger.debug("eventInfo.getEventType().getEventType() " + eventInfo.getEventType().getEventType());
			// EntityManagerFactory emf =
			// Persistence.createEntityManagerFactory("ROOT");
			// EntityManager em = emf.createEntityManager();
			Query queryEspi = em.createNamedQuery("EventServiceProviderInfo.findByEventId");
			queryEspi.setParameter("eventId", eventInfo);

			espServiceInfo = new ArrayList<EventServiceProviderInfo>(queryEspi.getResultList());

			for (int j = 0; j < espServiceInfo.size(); j++) {
				EventServiceProviderInfo espi = espServiceInfo.get(j);
				logger.debug("espi.getVenue() " + espi.getVenue());
				if (espi.getVenue().equals('Y')) {
					HashMap servicesInfo = new HashMap();

					Query queryEspi1 = em.createNamedQuery("VenueInfo.findByVenueId");
					queryEspi1.setParameter("venueId", espi.getServiceProviderId());

					VenueInfo vinfo = (VenueInfo) queryEspi1.getResultList().get(0);

					Query queryAi = em.createNamedQuery("AddressInfo.findByAddressId");
					queryAi.setParameter("addressId", vinfo.getAddressId().getAddressId());
					logger.debug("queryAi******** " + queryAi);
					AddressInfo ai = (AddressInfo) queryAi.getResultList().get(0);

					// servicesInfo.put(vinfo.getVenueName(), espi);
					servicesInfo.put(vinfo, espi);
					venues.put("Venue", servicesInfo);
				} else {
					HashMap servicesInfo = new HashMap();

					Query queryEspi1 = em.createNamedQuery("ServiceProviderInfo.findByServiceProviderId");
					queryEspi1.setParameter("serviceProviderId", espi.getServiceProviderId());

					ServiceProviderInfo spi = (ServiceProviderInfo) queryEspi1.getResultList().get(0);

					Query queryEspi11 = em.createNamedQuery("ServiceTypeMaster.findByServiceTypeCode");
					queryEspi11.setParameter("serviceTypeCode", spi.getServiceTypeCode().getServiceTypeCode());
					logger.debug("queryEspi11******** " + queryEspi11);
					ServiceTypeMaster stm = (ServiceTypeMaster) queryEspi11.getResultList().get(0);

					Query queryAi = em.createNamedQuery("AddressInfo.findByAddressId");
					queryAi.setParameter("addressId", spi.getAddressId().getAddressId());
					logger.debug("queryAi******** " + queryAi);
					AddressInfo ai = (AddressInfo) queryAi.getResultList().get(0);

					servicesInfo.put(spi, espi);
					// servicesInfo.put(spi.getServiceProviderName(), espi);
					services.put(stm.getServiceName(), servicesInfo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		// RequestContext.getCurrentInstance().execute("frm:eventDialog.show()");
		// return "eventConfirm";
	}

	public List<String> hotelImagesSmall;

	public List<String> getHotelImagesSmall() {
		return hotelImagesSmall;
	}

	public void setHotelImagesSmall(List<String> hotelImagesSmall) {
		this.hotelImagesSmall = hotelImagesSmall;
	}

	public List<String> getHotelImagesSm(String venueId) {
		hotelImagesSmall = new ArrayList<String>();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		//String path = context.getInitParameter("imgpath");
		File dir = new File(path + "/venue/" + venueId);
		logger.debug("Directory! " + dir);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			String[] listFiles = dir.list();
			int len = dir.list().length;
			if (len == 0) {
				logger.debug("No images uploaded");
				hotelImagesSmall.add("images/noImage.gif");
			} else {
				//String url = context.getInitParameter("imgurl");

				for (int i = 0; i < len; i++) {
					logger.debug("listFiles[i] " + listFiles[i]);
					logger.debug("listFiles[i] " + list[i].getAbsolutePath());
					logger.debug("listFiles[i] " + list[i].getPath());
					if (!list[i].isDirectory()) {
						hotelImagesSmall.add("http://" + url + "/venue/" + venueId + "/" + listFiles[i]);
					}
				}
			}
		} else {
			logger.debug("No directory found");
			hotelImagesSmall.add("images/noImage.gif");
		}
		return hotelImagesSmall;
	}

	public String returnFirstPage() {
            String r = "";
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventConfirmManagedBean");
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.containsKey("eventDetailsManagedBean")) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");
		}
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
			// return "eventHome.xhtml?faces-redirect=true";
			//return null;
                    r = "eventConfirm?faces-redirect=true";
		} else {
			return null;
		}
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventDetailsManagedBean");
		// return "index.xhtml?faces-redirect=true";
                return r;
	}
}
