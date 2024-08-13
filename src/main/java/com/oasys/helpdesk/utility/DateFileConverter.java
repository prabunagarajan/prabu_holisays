package com.oasys.helpdesk.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFileConverter {
	 public static boolean validateInputDate(String strDate)
	   {
		 SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-mm-dd");
			sdfrmt.setLenient(false);
			try {
				Date javaDate = sdfrmt.parse(strDate);
			} catch (ParseException e) {
				return false;
			}
			return true;
	   }
	public static String getDate(Date date) 
	{
		SimpleDateFormat	formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return formatter.format(date)+"";
	}
	
	public static String getDateConvertor(Date string) 
	{
		SimpleDateFormat	formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(string)+"";
	}
	
	public static Date getDateConvertor(String strDate) throws Exception 
	{
		SimpleDateFormat	formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date=formatter.parse(strDate);  
		return date;
	}
	
	public static String getDateFromDateTime(String strDate) 
	{
		if(strDate.indexOf(" ")!=-1) {
			return strDate.substring(0, strDate.indexOf(" ")).trim();
		}
		return strDate;
	}
	public static String getTimeFromDateTime(String strDate) 
	{
		if(strDate.indexOf(" ")!=-1) {
			return strDate.substring(strDate.indexOf(" ")).trim();
		}
		return strDate;
	}
	public static String removeSpecialCharater(String strValue) {
		return strValue.replaceAll("'", "");
	}
	
	public static Date getCurrentDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date currentDate = formatter.parse(formatter.format(new Date()));
			return currentDate;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date formattedDateTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date formattedDate = formatter.parse(formatter.format(date));
			return formattedDate;
		} catch (Exception e) {
			return null;
		}
	}
	
}