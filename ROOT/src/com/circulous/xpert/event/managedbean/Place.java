/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import com.google.api.client.util.Key;
/**
 *
 * @author SANJANA
 */
public class Place {
    
    @Key
 public String id;
 
 @Key
 public String name;
 
 @Key
 public String reference;

 @Override
 public String toString() {
  return name + " - " + id + " - " + reference;
 }
    
}
