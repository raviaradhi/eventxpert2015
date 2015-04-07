/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.circulous.xpert.jpa.entities.CustomerInfo;
import java.math.BigDecimal;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * 
 * @author SANJANA
 */
@FacesValidator("costValidator")
public class CostValidator implements Validator {

	// private CustomerInfo customerInfo;
	@Override
	public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
		if (o == null || o.equals("")) {
			return; // Let required="true" handle.
		}
                
                String costStr = (String) o;

		BigDecimal cost = new BigDecimal(costStr);		

		if (cost.longValueExact() < 50000 || cost.longValueExact() > 10000000) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Amount should be a Number greater than 50000 and less than 10000000(1CRORE)", null));
		}
	}

}
