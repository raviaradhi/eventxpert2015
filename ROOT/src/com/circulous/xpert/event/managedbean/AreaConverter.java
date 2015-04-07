/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SANJANA
 */
@FacesConverter(value = "areaConverter")
public class AreaConverter implements Converter {

 @PersistenceContext(unitName = "ROOT")     
private transient EntityManager em;
    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {      
        if(value==null)
        {
            value = "1";
        }       
      return new AreaTypeMaster(Integer.parseInt(value)); 
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {       
        return ((AreaTypeMaster) o).getAreaId().toString(); 
    }
}
