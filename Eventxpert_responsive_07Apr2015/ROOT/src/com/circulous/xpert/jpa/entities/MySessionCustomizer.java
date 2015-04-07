/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.jpa.entities;

import javax.naming.Context;
import javax.naming.InitialContext;
 
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;


/**
 *
 * @author SANJANA
 */
public class MySessionCustomizer implements SessionCustomizer {
  public void customize(Session session) {
    session.getLogin().setConnectionHealthValidatedOnError(false);
    JNDIConnector connector = null;
    Context context = null;
    try {
      context = new InitialContext();
      if(null != context) {
        connector = (JNDIConnector)session.getLogin().getConnector(); // possible CCE
        // Change from COMPOSITE_NAME_LOOKUP to STRING_LOOKUP
        // Note: if both jta and non-jta elements exist this will only change the first one - and may still result in
        // the COMPOSITE_NAME_LOOKUP being set
        // Make sure only jta-data-source is in persistence.xml with no non-jta-data-source property set
        connector.setLookupType(JNDIConnector.STRING_LOOKUP);
 
        // Or, if you are specifying both JTA and non-JTA in your persistence.xml then set both connectors to be safe
        JNDIConnector writeConnector = (JNDIConnector)session.getLogin().getConnector();
        writeConnector.setLookupType(JNDIConnector.STRING_LOOKUP);
        JNDIConnector readConnector =
            (JNDIConnector)((DatabaseLogin)((ServerSession)session).getReadConnectionPool().getLogin()).getConnector();
        readConnector.setLookupType(JNDIConnector.STRING_LOOKUP);
 
        System.out.println("_JPAEclipseLinkSessionCustomizer: configured " + connector.getName());
      }
      else {
        throw new Exception("_JPAEclipseLinkSessionCustomizer: Context is null");
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}