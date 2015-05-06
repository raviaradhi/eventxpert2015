package com.circulous.xpert.event.comparator;

import java.util.Comparator;

import com.circulous.xpert.jpa.entities.views.ViewServiceProvider;

public class ServiceProviderComparatorByAmount  implements Comparator<ViewServiceProvider> {

	@Override
	public int compare(ViewServiceProvider o1, ViewServiceProvider o2) {
		return o1.getCost().compareTo(o2.getCost());
	}





}
