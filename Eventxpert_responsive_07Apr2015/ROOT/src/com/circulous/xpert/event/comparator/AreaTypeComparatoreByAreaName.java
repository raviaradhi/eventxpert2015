package com.circulous.xpert.event.comparator;

import java.util.Comparator;

import com.circulous.xpert.jpa.entities.AreaTypeMaster;


public class AreaTypeComparatoreByAreaName  implements Comparator<AreaTypeMaster> {


	@Override
	public int compare(AreaTypeMaster arg0, AreaTypeMaster arg1) {
		// TODO Auto-generated method stub
		return arg0.getAreaName().compareTo(arg1.getAreaName());
	}





}
