/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

/**
 *
 * @author SANJANA
 */
public class LatitudeLongitude {
    
     public  double latitude;
    public  double longitude;
   

    public LatitudeLongitude(double d, double d1) {
        
      this.latitude = d;
      this.longitude = d1;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    
    
}
