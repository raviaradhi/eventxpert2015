/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

/**
 *
 * @author SANJANA
 */

    
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.wizard.Wizard;


 
public class ExWizardRenderer extends org.primefaces.component.wizard.WizardRenderer {
	
	//private final static Logger logger = Logger.getLogger(Activation.class);
     
    
    protected void encodeStepStatus1(FacesContext context, Wizard wizard) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String currentStep = wizard.getStep();
        boolean currentFound = false;
 
        writer.startElement("ul", null);
        writer.writeAttribute("class", Wizard.STEP_STATUS_CLASS, null);
        int i = 0;
        for(UIComponent child : wizard.getChildren()) {
            if(child instanceof Tab && child.isRendered()) {
                Tab tab = (Tab) child;
                boolean active = (!currentFound) && (currentStep == null || tab.getId().equals(currentStep));
                String titleStyleClass = active ? Wizard.ACTIVE_STEP_CLASS : Wizard.STEP_CLASS;
                if(tab.getTitleStyleClass() != null) {
                    titleStyleClass = titleStyleClass + " " + tab.getTitleStyleClass();
                }
                 
                if(active) {
                    currentFound = true;
                }
 
                writer.startElement("li", null);
                writer.writeAttribute("class", titleStyleClass, null);
                if(tab.getTitleStyle() != null) writer.writeAttribute("style", tab.getTitleStyle(), null);
                 
                writer.startElement("a", null);
                final String wiz = wizard.resolveWidgetVar();
                writer.writeAttribute("href", "javascript:"+wiz+".loadStep("+wiz+".cfg.steps["+i+"], false)", null);
                if (tab.getTitletip() != null) writer.writeAttribute("title", tab.getTitletip(), null);
                writer.write(tab.getTitle());
                writer.endElement("a");
                 
                writer.endElement("li");
                i++;
            }
        }
 
        writer.endElement("ul");
    }
    
     @Override
     protected void encodeStepStatus(FacesContext context, Wizard wizard) throws IOException {
     ResponseWriter writer = context.getResponseWriter();
        String currentStep = wizard.getStep();
        boolean currentFound = false;

        writer.startElement("ul", null);
        writer.writeAttribute("class", "ui-wizard-step-titles ui-helper-reset ui-helper-clearfix", null);
        int i = 0;
        for(UIComponent child : wizard.getChildren()) {
            if(child instanceof Tab && child.isRendered()) {
                Tab tab = (Tab) child;
                String styleClass = "ui-wizard-step-title ui-state-default";
                //logger.debug("tab.getId()  "+tab.getId());
                //logger.debug("currentStep  "+currentStep);
                if((!currentFound) && (currentStep == null || tab.getId().equals(currentStep))) {
                    styleClass += " ui-state-hover";
                    currentFound = true;
                }

                styleClass += " ui-corner-all";

                writer.startElement("li", null);
                writer.writeAttribute("class", styleClass, null);
                writer.startElement("a", null);
                final String wiz = wizard.resolveWidgetVar();
//                //logger.debug("  "+wiz.g);
//                //logger.debug("currentStep  "+currentStep);
                                // Adds javascript to allow tab clicking
                                writer.writeAttribute("href", "javascript:" + "if(" + i + " <= "
                                                            + wiz + ".getStepIndex(" + wiz
                                                            + ".currentStep" + ")){" + wiz
                                                            + ".loadStep (" + wiz
                                                            + ".cfg.steps [" + i
                                                            + "], true)}", null);
                if (tab.getTitletip() != null)
                {
                   writer.writeAttribute("title", tab.getTitletip(), null);  
                }
                                   
                                writer.write(tab.getTitle());
                                writer.endElement("a");

//                if(tab.getTitletip() != null) 
//                    writer.writeAttribute("title", tab.getTitletip(), null);
//                
//                writer.write(tab.getTitle());
                writer.endElement("li");
                i++;
            }
        }

        writer.endElement("ul");
    }
}
    

