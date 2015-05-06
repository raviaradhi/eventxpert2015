/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circulous.xpert.event.managedbean;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.spec.KeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.nio.charset.Charset;
/**
 *
 * @author SANJANA
 */
public class EncryptDecryptString {
    
    private  String ALGORITHM = "md5";
    private  String DIGEST_STRING = "HG58YZ3CR9";
    private  String CHARSET_UTF_8 = "utf-8";
    private  String SECRET_KEY_ALGORITHM = "DESede";
    private  String TRANSFORMATION_PADDING = "DESede/CBC/NoPadding";//"DESede/CBC/PKCS5Padding";
    
  /*  public String encrypt(String message) throws Exception 
    { 
         MessageDigest md = MessageDigest.getInstance(ALGORITHM); 
         byte[] digestOfPassword = md.digest(DIGEST_STRING.getBytes(CHARSET_UTF_8)); 
         byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24); 
        for (int j = 0, k = 16; j < 8;) { 
                keyBytes[k++] = keyBytes[j++]; 
        } 

         SecretKey key = new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM); 
         IvParameterSpec iv = new IvParameterSpec(new byte[8]); 
         Cipher cipher = Cipher.getInstance(TRANSFORMATION_PADDING); 
        cipher.init(Cipher.ENCRYPT_MODE, key, iv); 

         byte[] plainTextBytes = message.getBytes(CHARSET_UTF_8); 
         byte[] cipherText = cipher.doFinal(plainTextBytes); 

        return new String(cipherText); 
    } 
    
   /* Decryption Method */
  /*  public String decrypt(String message) throws Exception { 
         MessageDigest md = MessageDigest.getInstance(ALGORITHM); 
         byte[] digestOfPassword = md.digest(DIGEST_STRING.getBytes(CHARSET_UTF_8)); 
         byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24); 
        for (int j = 0, k = 16; j < 8;) { 
                keyBytes[k++] = keyBytes[j++]; 
        } 

         SecretKey key = new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM); 
         IvParameterSpec iv = new IvParameterSpec(new byte[8]); 
         Cipher decipher = Cipher.getInstance(TRANSFORMATION_PADDING); 
        decipher.init(Cipher.DECRYPT_MODE, key, iv); 

         byte[] plainText = decipher.doFinal(message.getBytes()); 

        return new String(plainText, CHARSET_UTF_8); 
    }
   */ 
    
    
        private String UNICODE_FORMAT = "UTF8";
	public String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private KeySpec myKeySpec;
	private SecretKeyFactory mySecretKeyFactory;
	private Cipher cipher;
	byte[] keyAsBytes;
	private String myEncryptionKey;
	private String myEncryptionScheme;
	SecretKey key;

	public EncryptDecryptString() throws Exception
	{
		myEncryptionKey = "SecretEncryptionEventXpertKey";
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
                
		keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		myKeySpec = new DESedeKeySpec(keyAsBytes);
		mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
		cipher = Cipher.getInstance(myEncryptionScheme);
		key = mySecretKeyFactory.generateSecret(myKeySpec);
	}

	/**
	 * Method To Encrypt The String
	 */
	public String encrypt(String unencryptedString) {
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			BASE64Encoder base64encoder = new BASE64Encoder();
			encryptedString = base64encoder.encode(encryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}
	/**
	 * Method To Decrypt An Ecrypted String
	 */
	public String decrypt(String encryptedString) {
		String decryptedText=null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText= bytes2String(plainText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedText;
	}
	/**
	 * Returns String From An Array Of Bytes
	 */
	private  String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}

}
