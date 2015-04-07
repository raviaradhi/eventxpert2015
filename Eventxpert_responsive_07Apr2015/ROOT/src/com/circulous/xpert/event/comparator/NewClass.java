/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.comparator;

import com.circulous.xpert.event.managedbean.EncryptDecryptString;

/**
 *
 * @author SANJANA
 */
public class NewClass {
    
    public static void main(String args[])
    {
        try
        {
            EncryptDecryptString eds = new EncryptDecryptString();
            String cryptPswd = ""; String decrypt ="";
            
            cryptPswd = eds.encrypt("admin@123");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            cryptPswd = eds.encrypt("ravi@123");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            cryptPswd = eds.encrypt("eventxpert2012");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            cryptPswd = eds.encrypt("abhinav123");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            cryptPswd = eds.encrypt("12345678");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            cryptPswd = eds.encrypt("password19");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            cryptPswd = eds.encrypt("eventxpert123");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            System.out.println("========================VENDORS=========================================");
            System.out.println("========================VENDORS=========================================");
            System.out.println("========================VENDORS========================================= \n");
            
            cryptPswd = eds.encrypt("xpert@123");
            decrypt =eds.decrypt(cryptPswd);
            System.out.println("cryptPswd "+cryptPswd+"   decryptPswd "+decrypt);
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
