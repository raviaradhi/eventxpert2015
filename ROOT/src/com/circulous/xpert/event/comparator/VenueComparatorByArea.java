package com.circulous.xpert.event.comparator;

import java.util.Comparator;
import java.util.List;

import com.circulous.xpert.jpa.entities.views.ViewVenue;

public class VenueComparatorByArea  implements Comparator<ViewVenue> {
    
    private String area;
    private List nearby;

    public List getNearby() {
        return nearby;
    }

    public void setNearby(List nearby) {
        this.nearby = nearby;
    }

    
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }    

	@Override
	public int compare(ViewVenue o1, ViewVenue o2) {
            System.out.println("nearby "+getNearby());
//                if(o1.getAreaName().equals(area))
//                {
//                   return 0;//o1.getAreaName().compareToIgnoreCase(area);
//                }
                
                if(nearby.contains(o1.getAreaName()))
                {
                    return o1.getAreaName().compareToIgnoreCase(area);
                }
                else if(nearby.contains(o2.getAreaName()))
                {
                    return o2.getAreaName().compareToIgnoreCase(area);
                }
                else
                {
                    return o1.getAreaName().compareToIgnoreCase(o2.getAreaName());
                }
		//return o1.getAreaName().compareTo(o2.getAreaName());
	}





}
