/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SANJANA
 */
public class NewClass {
    
    public static void main(String[] args)
    {
        try {
            EncryptDecryptString ed = new EncryptDecryptString();
            System.out.println(ed.encrypt("airlines123"));
            System.out.println(ed.decrypt("JSh0q0bknRkPEd7X+cPO4A==	"));
        } catch (Exception ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
