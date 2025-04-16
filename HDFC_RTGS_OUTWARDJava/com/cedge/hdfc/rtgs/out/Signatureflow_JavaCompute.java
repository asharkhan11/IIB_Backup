package com.cedge.hdfc.rtgs.out;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;


public class Signatureflow_JavaCompute {
	private static final String SHA256_RSA_SIGNATURE_ALGORITHM = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
	private static final int a = 'a';
	private static final int z = 'z';
	private static final int A = 'A';
	private static final int Z = 'Z';
	private static final int ZERO = '0';
	private static final int NINE = '9'; 
	private static final String HASH = "#";
	public static void signaturedata(String dosigndocument, String jskprivate,String p12pass,String al,String jskfilepublic,String pubpass,String pubalias,String[] signencrypt,String[] randomencrypt,String[] OrgXml) {

        try {              
           
        	Document doc = null;
			String Id = generateAlphaNumericKey(32).trim();
			Id = Id.replaceAll("[^a-zA-Z0-9]", "");
			dosigndocument = dosigndocument.replaceAll("xsi:noNamespaceSchemaLocation", "Id").trim();
			dosigndocument = dosigndocument.replaceAll("CO_STR.xsd", Id).trim();
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	factory.setNamespaceAware(true);
        	doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(dosigndocument)));
        	FileInputStream jskfile = new FileInputStream(jskprivate);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(jskfile, p12pass.toCharArray());
           KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry)ks.getEntry      //Modified devi//
                    (al,new KeyStore.PasswordProtection(p12pass.toCharArray()));          
            X509Certificate cert =(X509Certificate) keyEntry.getCertificate() ; 

			
            if (cert == null || keyEntry == null) {
                throw new Exception("Key or Certificate not found!!");
            }
            // Create a DOM XMLSignatureFactory that will be used to generate the
            // enveloped signature
            XMLSignatureFactory fac;
            fac = XMLSignatureFactory.getInstance("DOM");

            // Create a Reference to the enveloped document (in this case we are
            // signing the whole document, so a URI of "" signifies that) and
            // also specify the SHA1 digest algorithm and the ENVELOPED Transform.
            Reference ref = fac.newReference(HASH+Id, fac.newDigestMethod(DigestMethod.SHA256, null));

            // Create the SignedInfo
            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SHA256_RSA_SIGNATURE_ALGORITHM,(SignatureMethodParameterSpec)null),
                    Collections.singletonList(ref));
          //create signature payload
            KeyInfoFactory kif = fac.getKeyInfoFactory();
            List x509DataContent = new ArrayList();
            x509DataContent.add(cert.getSubjectX500Principal().getName());
            x509DataContent.add(cert);
            X509Data xd = kif.newX509Data(x509DataContent);
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
            // Instantiate the document to be signed
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(true);

            // default false
            dbf.setIgnoringElementContentWhitespace(true);
            // default true
            dbf.setExpandEntityReferences(false);
           
            // Create a DOMSignContext and specify the RSA PrivateKey and
            // location of the resulting XMLSignature's parent element
            DOMSignContext dsc = new DOMSignContext
             	    (keyEntry.getPrivateKey(), doc.getDocumentElement());
            dsc.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);
            dsc.putNamespacePrefix(XMLSignature.XMLNS, ""); //removed ds from tag

            // Create the XMLSignature (but don't sign it yet)
            XMLSignature signature = fac.newXMLSignature(si, ki);

            // Marshal, generate (and sign) the enveloped signature
            signature.sign(dsc);
     
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer(); 
            
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            trans.transform(new DOMSource(doc), result);
            
            // Store XML data into signout string  
             String signout = writer.toString();
            signout = signout.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
          //store XML data into Signbase64 String  
            String SignBase64 = signout.trim();
            OrgXml[0] = signout;
            // Get 32 bytes string(Symmetric key) by calling the RandomString method
            byte[] randomdata = RandomString();
            // Add IV parameter to the signed payload using Symmetric key through encrypt method
           byte[] encrypted = encrypt(SignBase64,randomdata);
           //base64 encoding of the  encrypted payload
          signencrypt[0] = Base64.getEncoder().encodeToString(encrypted).trim();
          //Storing encrypted Asymmetric key value
          randomencrypt[0] = publickeyencrypt(pubpass,randomdata,jskfilepublic,pubalias).trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static byte[] encrypt(String plainText,byte[] randomdata) throws Exception {
        byte[] clean = plainText.getBytes();

        // Generating IV.
        int ivSize = 16;

          byte[] iv ="1234567890123456".getBytes();
          IvParameterSpec ivParameterSpec = new IvParameterSpec(iv); 
        // Hashing key.
       byte[] keyBytes = randomdata;
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[ivSize + clean.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(clean, 0, encryptedIVAndText, ivSize, clean.length);
        
        byte[] encrypted = cipher.doFinal(encryptedIVAndText);
        return encrypted;

    }
      private static String publickeyencrypt(String pubpass,byte[] randomdata,String jskfilepublic,String pubalias) throws Exception {
    	FileInputStream is = new FileInputStream(jskfilepublic);
    	KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    	keystore.load(is, pubpass.toCharArray());
     	String rndmencrypt = "";
    	  // Get certificate of public key
  	      Certificate cert = keystore.getCertificate(pubalias);
  	      // Get public key
	      PublicKey publicKey = cert.getPublicKey();
	      // Return a key pair
	      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	      byte[] output = cipher.doFinal(randomdata);
 	      rndmencrypt = Base64.getEncoder().encodeToString(output);
 //   	}
		return rndmencrypt;
    }
      private static byte[] RandomString() throws Exception {
    	// Get the size n 
    	  int n =32;
    	  byte[] array = new byte[256];
    	  new Random().nextBytes(array);
    	  String randomString= new String(array, Charset.forName("UTF-8"));// Create a StringBuffer to store the result
    	  StringBuffer r = new StringBuffer();// Append first 20 alphanumeric characters
    	  // from the generated random String into the result
    	  for (int k = 0; k < randomString.length(); k++) {char ch = randomString.charAt(k);
    	  if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
    	   || (ch >= '0' && ch <= '9'))&& (n> 0)) {r.append(ch);
    	  n--;
    	  }
    	 
    	  }
    	  // return the resultant string with 32 bytes
          byte[] b= String.valueOf(r).getBytes();
    	  return b;

  		}     
      
      public static String generateAlphaNumericKey(int keySize) {
 
  		Random random = new Random();
  		StringBuilder key = new StringBuilder();
  		while(key.length() < keySize) {
  			int ch = random.nextInt(128);
  			//is within range
  			if((ch <= Z && ch >= A) || (ch <= z && ch >= a) || (ch <= NINE && ch >= ZERO)) {
  				key.append((char)ch);
  			}
  		}
  		return key.toString();
  	}
}