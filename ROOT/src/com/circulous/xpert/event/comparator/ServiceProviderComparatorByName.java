package com.circulous.xpert.event.comparator;

import java.util.Comparator;

import com.circulous.xpert.jpa.entities.views.ViewServiceProvider;

public class ServiceProviderComparatorByName  implements Comparator<ViewServiceProvider> {

	@Override
	public int compare(ViewServiceProvider o1, ViewServiceProvider o2) {
		return o1.getServiceProviderName().compareTo(o2.getServiceProviderName());
	}





}
