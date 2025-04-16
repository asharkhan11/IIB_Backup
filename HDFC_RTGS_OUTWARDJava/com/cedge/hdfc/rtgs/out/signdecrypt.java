package com.cedge.hdfc.rtgs.out;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Base64;

public class signdecrypt {
	private static final byte[] IV = "1234567890123456".getBytes();

    public static void decrypteddata(String pubpass,String jskprivate,String pubalias,String randomdata, String clean, String[] rawdata) {
        try 
        {
    	FileInputStream is = new FileInputStream(jskprivate);
	String[] rndmencrypt = new String[1];
    	KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    	keystore.load(is, pubpass.toCharArray());
    	String alias = pubalias;
    	Key key = keystore.getKey(alias, pubpass.toCharArray());
    	if (key instanceof PrivateKey) {  	      
  	      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
  	      byte[] b3 = Base64.getDecoder().decode(randomdata.getBytes());
  	      cipher.init(Cipher.DECRYPT_MODE, key);
  	      byte [] b = cipher.doFinal(b3);
  	      String outdec = new String(b);


	      
	      SecretKeySpec secretKeySpec=new SecretKeySpec(b,"AES"); 
	      Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
	      cipher1.init(Cipher.DECRYPT_MODE,secretKeySpec, new IvParameterSpec(IV));
	      byte[] encrypted = Base64.getDecoder().decode(clean.getBytes());
	      byte[] decryptedDataWithIV = cipher1.doFinal(encrypted);	
	      byte[] decryptedData = null;
	      decryptedData = new byte[decryptedDataWithIV.length-IV.length];	
	      System.arraycopy(decryptedDataWithIV, IV.length, decryptedData, 0, decryptedData.length);
	      String responseXMLWithSignature = new String(decryptedData);
	      
	      
	      
	      
     
        rawdata[0] = responseXMLWithSignature;
    	}
      } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
