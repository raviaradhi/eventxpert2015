/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.comparator;
import java.util.Comparator;

import com.circulous.xpert.event.managedbean.ServiceVO;

/**
 *
 * @author SANJANA
 */
public class ServiceVOComparator implements Comparator<ServiceVO>{

    @Override
    public int compare(ServiceVO o1, ServiceVO o2) {
        return o1.getHeading().compareTo(o2.getHeading());
    }
    
    
    
}
