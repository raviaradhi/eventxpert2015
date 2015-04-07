package com.circulous.xpert.event.comparator;

import java.util.Comparator;

import com.circulous.xpert.jpa.entities.CityTypeMaster;


public class CityTypeComparatoreByCityName  implements Comparator<CityTypeMaster> {


	@Override
	public int compare(CityTypeMaster arg0, CityTypeMaster arg1) {
		// TODO Auto-generated method stub
		return arg0.getCityName().compareTo(arg1.getCityName());
	}





}
