package com.circulous.xpert.event.comparator;

import java.util.Comparator;

import com.circulous.xpert.jpa.entities.views.ViewVenue;

public class VenueComparatorByName  implements Comparator<ViewVenue> {

	@Override
	public int compare(ViewVenue o1, ViewVenue o2) {
		return o1.getVenueName().compareTo(o2.getVenueName());
	}





}
