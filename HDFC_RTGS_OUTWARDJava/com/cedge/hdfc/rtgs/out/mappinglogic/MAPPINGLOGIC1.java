package com.cedge.hdfc.rtgs.out.mappinglogic;

public class MAPPINGLOGIC1 {
	public static java.lang.String getMsgId(String mmbId,String utrNumber,String oDate) 
	{

        return mmbId.substring(0, 4)+oDate+"5000"+utrNumber.substring(utrNumber.length()-6, utrNumber.length());
    }
	public static java.lang.String getAmount(String amount) 
	{
		String value = amount.replaceFirst ("^0*", "");
		 value = value.replace(',', '.');
        return value;
    }
    public static java.lang.String getCreDt(String originatingTime,String originatingDate) 
    {
      String odate = originatingDate.substring(0,4)+'-'+originatingDate.substring(4,6)+'-'+originatingDate.substring(6,8);
      String otime = originatingTime.substring(0,2)+':'+originatingTime.substring(2,4)+":00Z";
      return odate+'T'+otime;
    }
    public static java.lang.String getCreDtTm(String originatingTime,String originatingDate) 
    {
     String odate = originatingDate.substring(0,4)+'-'+originatingDate.substring(4,6)+'-'+originatingDate.substring(6,8);
     String otime = originatingTime.substring(0,2)+':'+originatingTime.substring(2,4)+":00";
     return odate+'T'+otime;
    }
    public static java.lang.String getIntrBkSttlmDt(String valuedate)
    {
	String formatedDate = valuedate.substring(0, 4)+'-'+valuedate.substring(4, 6)+'-'+valuedate.substring(6, 8);
	return formatedDate;
    }
    public static java.lang.String getTxId(String utrNumber,String oDate) 
    {

    return utrNumber.substring(0, 4)+"R5"+oDate+"00"+utrNumber.substring(utrNumber.length()-6, utrNumber.length());
    }

}
