package com.cedge.hdfc.rtgs.out;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class HDFC_RTGS_OUTWARD_JavaCompute {
	public static void getProperties( String propLoc, String key,String bank, String[] fileLoc,
			String[] ftpProv)
	{	
		Properties properties = new Properties();
		try {
			File propFile = new File(propLoc);
			FileInputStream fileInput = new FileInputStream(propFile);
			properties.load(fileInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLoc[0] = properties.getProperty(key);
		ftpProv[0] = properties.getProperty(bank+"_CBS_SFTP");
//		queue[0] = properties.getProperty("MQ_Out_Qname_"+bank);
//		appID[0] = properties.getProperty("AppID_"+bank);
	}
	
	public static void getsftpProperties( String propLoc, String key,String bank, String[] fileLoc,String[] sftpProv)
	{	
		Properties properties = new Properties();
		try {
			File propFile = new File(propLoc);
			FileInputStream fileInput = new FileInputStream(propFile);
			properties.load(fileInput);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		fileLoc[0] = properties.getProperty(key);
		sftpProv[0] = properties.getProperty(bank+"_HDFC_SFTP");
//		
	}
	
	public static void getMailProp( String propLoc,String[] toEmail, String[] ccEmail,
			String[] fromEmail, String[] smtpServer)
	{	
		Properties properties = new Properties();
		try {
			File propFile = new File(propLoc);
			FileInputStream fileInput = new FileInputStream(propFile);
			properties.load(fileInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		toEmail[0] = properties.getProperty("MailTo");
		ccEmail[0] = properties.getProperty("MailCc");
		fromEmail[0] = properties.getProperty("MailFrom");
		smtpServer[0] = properties.getProperty("MailHost");
		
	}
	
	public static void getSignData( String propLoc, String bank, String[] jskprivate, 
			String[] p12pass,String[] al,String[] jskfilepublic,
			String[] pubpass,String[] pubalias)
    {	
		Properties properties = new Properties();
		try {
			File propFile = new File(propLoc);
			FileInputStream fileInput = new FileInputStream(propFile);
			properties.load(fileInput);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		jskprivate[0] = properties.getProperty(bank+"_jskprivate");
		p12pass[0] = properties.getProperty(bank+"_p12pass");
		al[0] = properties.getProperty(bank+"_al");
		jskfilepublic[0] = properties.getProperty(bank+"_jskfilepublic");
		pubpass[0] = properties.getProperty(bank+"_pubpass");
		pubalias[0] = properties.getProperty(bank+"_pubalias");		
	}	

}