/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.circulous.xpert.event.managedbean;


import com.circulous.xpert.jpa.entities.AddressInfo;
import com.circulous.xpert.jpa.entities.AreaTypeMaster;
import com.circulous.xpert.jpa.entities.views.ViewServiceProvider;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import com.circulous.xpert.jpa.entities.views.ViewVenue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
//import com.google.android.gms.maps.model.LatitudeLongitude;

/**
 *
 * @author SANJANA
 */
public class PlacesLatitudes{

    /**
     * Creates a new instance of PlacesLatitudes
     */
    public PlacesLatitudes() {
    }
    
    private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String SEARCH_URL =  "http://maps.googleapis.com/maps/api/geocode/json?";
 private static final HttpTransport transport = new ApacheHttpTransport();
 private static final JacksonFactory jacksonFactory = new JacksonFactory();
private static final boolean PRINT_AS_STRING = false;
private static final String API_KEY = "AIzaSyCZlcTOnLVDuaEN9R-I9yBHFJ5pWEBx7aY";

double latitude = 17.4368;
	double longitude = 78.4439;
        
public static void main(String[] args) throws Exception {
		PlacesLatitudes sample = new PlacesLatitudes();
                
//		 sample.getLatLng("Begumpet,Hyderabad");
                 
//                 Location cL = new Location("Ameerpet,Hyderabad");
//                 System.out.println("getLat "+cL.getLatitude());
                
//                PlacesList places = response.parseAs(PlacesList.class);
//    System.out.println("STATUS = " + places.status);
//                for (Iterator it = places.results.iterator(); it.hasNext();) {
//                    Place place = (Place) it.next();
//                    System.out.println(place);
//                }
    
//    sample.getLatLng("Plot-15, Brindavan Colony,Nallakunta,Hyderabad");
//    sample.getLatLng("12/5-N/243A, Eat Street,Nagarjuna Colony,Hyderabad");
       /*   EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
            EntityManager em = emf.createEntityManager();      
            Query arQry = em.createNamedQuery("AreaTypeMaster.findAll");
            List atmList = arQry.getResultList();
            for(int j=0;j<atmList.size();j++)
            {
                
                AreaTypeMaster atm = (AreaTypeMaster) atmList.get(j);
                LatitudeLongitude l = sample.getLatLng(atm.getAreaName()+","+atm.getCityId().getCityName());
                atm.setLatitude(String.valueOf(l.getLatitude()));
                atm.setLongitude(String.valueOf(l.getLongitude()).substring(0, 6));
                
                em.getTransaction().begin();
                em.persist(atm);
                em.getTransaction().commit();
            }*/
//            em.getTransaction().commit();

	}

 public List performSearch(String address,List area) throws Exception {
//     HttpResponse response;
     TreeMap nearbyPlaces = new TreeMap();
     
  try {
   System.out.println("Perform Search ....");
   System.out.println("------------------- "+address);
   HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
   
   HttpRequest request1 = httpRequestFactory.buildGetRequest(new GenericUrl(SEARCH_URL));
   
   request1.getUrl().put("address", address);  
   request1.getUrl().put("sensor", "false");
   
   HttpResponse res  = request1.execute();
   PlacesList placesL = new Gson().fromJson( res.parseAsString() ,PlacesList.class);
//   PlacesList placesL = new Gson().fromJson( res.parseAsString() ,new TypeToken<List<PlacesList>>(){}.getType());
   System.out.println("STATUS = " + placesL.results);
   for (Iterator it = placesL.results.iterator(); it.hasNext();) 
    {
//        LinkedTreeMap p = (LinkedTreeMap) it.next();  
         LinkedHashMap p = (LinkedHashMap) it.next();  
        Map subList = (Map)((Map) p.get("geometry")).get("location");
        System.out.println("name:::::::  "+subList.get("lat")+" long:::: "+subList.get("lng"));
        latitude = Double.parseDouble(subList.get("lat").toString());
        longitude = Double.parseDouble(subList.get("lng").toString());                     
   }
   LatitudeLongitude current = new LatitudeLongitude(latitude, longitude);
   
   // request1 = httpRequestFactory.buildGetRequest(new GenericUrl(SEARCH_URL));
   for(int j=0;j<area.size();j++)
   {
       ViewVenue vv = (ViewVenue) area.get(j);
        request1.getUrl().put("address", vv.getAddress()+","+vv.getAreaName()+","+vv.getCityName());  
        request1.getUrl().put("sensor", "false");

            res  = request1.execute();
            placesL = new Gson().fromJson( res.parseAsString() ,PlacesList.class);
        //   PlacesList placesL = new Gson().fromJson( res.parseAsString() ,new TypeToken<List<PlacesList>>(){}.getType());
        System.out.println("STATUS = " + placesL.results);
        for (Iterator it = placesL.results.iterator(); it.hasNext();) 
            {
        //        LinkedTreeMap p = (LinkedTreeMap) it.next();  
                LinkedHashMap p = (LinkedHashMap) it.next();  
                Map subList = (Map)((Map) p.get("geometry")).get("location");
                System.out.println("name:::::::  "+subList.get("lat")+" long:::: "+subList.get("lng"));
                latitude = Double.parseDouble(subList.get("lat").toString());
                longitude = Double.parseDouble(subList.get("lng").toString());                     
        }
            LatitudeLongitude last = new LatitudeLongitude(latitude, longitude);
        // System.out.println(distance(current,last,'K'));
            nearbyPlaces.put(distance(current,last,'K'),area.get(j));
   }
   
 /*  
   HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
   request.getUrl().put("key", API_KEY);
   request.getUrl().put("location", latitude + "," + longitude);
   request.getUrl().put("radius", 5000);
   request.getUrl().put("sensor", "false");
   
   if (PRINT_AS_STRING) {
    System.out.println(request.execute().parseAsString());
   } else {
    
       nearbyPlaces = new ArrayList();
        response = request.execute();
//        InputStream in = response.getContent();
//       System.out.println("STATUS = " + response.parseAsString());
//    PlacesList places = response.parseAs(PlacesList.class);
        PlacesList places = new Gson().fromJson( response.parseAsString() ,PlacesList.class);
//    System.out.println("STATUS = " + places.status);
    System.out.println("STATUS = " + places.results);
                for (Iterator it = places.results.iterator(); it.hasNext();) {
//                    LinkedTreeMap p = (LinkedTreeMap) it.next();
                     LinkedHashMap p = (LinkedHashMap) it.next();
//                    System.out.println("p:::::::  "+p.get("types"));
                    List subList = (List) p.get("types");
                    if(subList.contains("sublocality"))
                    {
                        System.out.println("name:::::::  "+p.get("name"));
                        nearbyPlaces.add(p.get("name"));
                        
                    }
//                    Place place = (Place) p.get("id");
//                    System.out.println(place);
                }
   }*/

  } catch (HttpResponseException e) {
   
   throw e;
  }
  return new ArrayList<ViewVenue>(nearbyPlaces.values());
 }
 
 public List calculateDistances(LatitudeLongitude current,List area)
 {
     TreeMap nearbyPlaces = new TreeMap();
     try
     {
      for(int j=0;j<area.size();j++)
        {
            ViewVenue vv = (ViewVenue) area.get(j);
            double lat = Double.valueOf(vv.getLatitude());
            double lng = Double.valueOf(vv.getLongitude());
            LatitudeLongitude last = new LatitudeLongitude(lat, lng);  
            double dis = distance(current,last,'K');
            System.out.println("area "+vv.getAddress()+","+vv.getAreaName());
            nearbyPlaces.put(dis,vv);
        }
      
      System.out.println("NEARBYLIST "+nearbyPlaces);
      
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
     return new ArrayList<ViewVenue>(nearbyPlaces.values());
 }
 
  public List calculateDistancesServices(LatitudeLongitude current,List area)
 {
     TreeMap nearbyPlaces = new TreeMap();
     try
     {
      for(int j=0;j<area.size();j++)
        {
            ViewServiceProvider vv = (ViewServiceProvider) area.get(j);
            double lat = Double.valueOf(vv.getLatitude());
            double lng = Double.valueOf(vv.getLongitude());
            LatitudeLongitude last = new LatitudeLongitude(lat, lng);  
            double dis = distance(current,last,'K');
            System.out.println("area "+vv.getAddress()+","+vv.getAreaName());
            nearbyPlaces.put(dis,vv);
        }
      
      System.out.println("NEARBYLIST "+nearbyPlaces);
      
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
     return new ArrayList<ViewVenue>(nearbyPlaces.values());
 }
 
   public LatitudeLongitude getLatLng(String address)
   {
       LatitudeLongitude laln = new LatitudeLongitude(0,0);
       try
       {
           HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
   
            HttpRequest request1 = httpRequestFactory.buildGetRequest(new GenericUrl(SEARCH_URL));

            request1.getUrl().put("address", address);  
            request1.getUrl().put("sensor", "false");

            HttpResponse res  = request1.execute();
            PlacesList placesL = new Gson().fromJson( res.parseAsString() ,PlacesList.class);
            //   PlacesList placesL = new Gson().fromJson( res.parseAsString() ,new TypeToken<List<PlacesList>>(){}.getType());
            System.out.println("STATUS = " + placesL.results);
            for (Iterator it = placesL.results.iterator(); it.hasNext();) 
                {
//                    LinkedTreeMap p = (LinkedTreeMap) it.next();  
                    LinkedHashMap p = (LinkedHashMap) it.next();  
                    Map subList = (Map)((Map) p.get("geometry")).get("location");
                    System.out.println("name:::::::  "+subList.get("lat")+" long:::: "+subList.get("lng"));
                    latitude = Double.parseDouble(subList.get("lat").toString());
                    longitude = Double.parseDouble(subList.get("lng").toString().substring(0, 6));                     
            }
            laln = new LatitudeLongitude(latitude, longitude);
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
       return laln;
   }
 
 public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {
			   
		  return transport.createRequestFactory(new HttpRequestInitializer() {
		   public void initialize(HttpRequest request) {
		    JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
		    request.setParser(parser);
		   }
		});
	}

    
private double distance(LatitudeLongitude current, LatitudeLongitude last, char unit) {
    System.out.println("distance");
  double lat1 =current.latitude;
  double lon1 = current.longitude; 
  double lat2 = last.latitude; 
  double lon2 = last.longitude;
  double theta = lon1 - lon2;
  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
  dist = Math.acos(dist);
  dist = rad2deg(dist);
  dist = dist * 60 * 1.1515;
  if (unit == 'K') {
    dist = dist * 1.609344;
  } else if (unit == 'N') {
  	dist = dist * 0.8684;
    }
  System.out.println("distance**** "+dist);
  return (dist);
}

/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::  This function converts decimal degrees to radians             :*/
/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
private double deg2rad(double deg) {
  return (deg * Math.PI / 180.0);
}

/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::  This function converts radians to decimal degrees             :*/
/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
private double rad2deg(double rad) {
  return (rad * 180 / Math.PI);
}
  

public LatitudeLongitude getLatLngOffline(String address,HttpRequest request1)
   {
       LatitudeLongitude laln = new LatitudeLongitude(0,0);
       try
       {
          latitude = 0.0;
          longitude = 0.0;
            request1.getUrl().put("address", address);  
            request1.getUrl().put("sensor", "false");

            HttpResponse res  = request1.execute();
            PlacesList placesL = new Gson().fromJson( res.parseAsString() ,PlacesList.class);
            //   PlacesList placesL = new Gson().fromJson( res.parseAsString() ,new TypeToken<List<PlacesList>>(){}.getType());
            System.out.println("STATUS = " + placesL.results);
            for (Iterator it = placesL.results.iterator(); it.hasNext();) 
                {
//                    LinkedTreeMap p = (LinkedTreeMap) it.next();  
                    LinkedHashMap p = (LinkedHashMap) it.next();  
                    Map subList = (Map)((Map) p.get("geometry")).get("location");
                    System.out.println("name:::::::  "+subList.get("lat")+" long:::: "+subList.get("lng"));
                    latitude = Double.parseDouble(subList.get("lat").toString());
                    longitude = Double.parseDouble(subList.get("lng").toString().substring(0, 6));                     
            }
            laln = new LatitudeLongitude(latitude, longitude);
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
       return laln;
   }


public void enterLat(boolean condition)
{
    try
    {
    HttpRequestFactory httpRequestFactory = createRequestFactory(transport);   
    HttpRequest request1 = httpRequestFactory.buildGetRequest(new GenericUrl(SEARCH_URL));
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ROOT");
    EntityManager em = emf.createEntityManager();
    Query arQry;
    if(condition==false)
    {
        arQry = em.createNamedQuery("AreaTypeMaster.findAll");
    }
    else
    {
        arQry = em.createNamedQuery("AreaTypeMaster.findByLatitude");
        arQry.setParameter("latitude", "0.0");
    }
    List atmList = arQry.getResultList();
    for(int j=0;j<atmList.size();j++)
    {                
        AreaTypeMaster atm = (AreaTypeMaster) atmList.get(j);
        LatitudeLongitude l = getLatLngOffline(atm.getAreaName()+","+atm.getCityId().getCityName(),request1);
        atm.setLatitude(String.valueOf(l.getLatitude()));
//        atm.setLongitude(String.valueOf(l.getLongitude()).substring(0, 6));
        atm.setLongitude(String.valueOf(l.getLongitude()));

        em.getTransaction().begin();
        em.persist(atm);
        em.getTransaction().commit();
    }
    
    Query arQry1;
    if(condition==false)
    {
         arQry1 = em.createNamedQuery("AddressInfo.findAll");
    }
    else
    {
         arQry1 = em.createNamedQuery("AddressInfo.findByLatitude");
         arQry1.setParameter("latitude", "0.0");
    }
    List atmList1 = arQry1.getResultList();
    for(int j=0;j<atmList1.size();j++)
    {                
        AddressInfo atm = (AddressInfo) atmList1.get(j);
        LatitudeLongitude l = getLatLngOffline(atm.getAddressLine1()+","+atm.getAddressLine2()+",Hyderabad",request1);
        atm.setLatitude(String.valueOf(l.getLatitude()));
        atm.setLongitude(String.valueOf(l.getLongitude()));
//        atm.setLongitude(String.valueOf(l.getLongitude()).substring(0, 6));
        
        em.getTransaction().begin();
        em.persist(atm);
        em.getTransaction().commit();
    }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}


   
}
