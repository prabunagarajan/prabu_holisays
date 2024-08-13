package com.oasys.helpdesk.utility;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;


public class RandomUtil {
private static RandomUtil randomUtil = new RandomUtil();
	
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();
     RandomUtil() {}
    
    public static RandomUtil createInstance() {
        return randomUtil;
    }
    public String getTrackId( ) 
    {
       StringBuilder sb = new StringBuilder( 8 );
       for( int i = 0; i < 8; i++ ) 
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       return sb.toString();
    }
    public static String getRandomNumber() {
//        Random rnd = new Random();
//        int number = rnd.nextInt(999999);
//        return String.format("%06d", number);
    	
    	 int sixLength = RandomUtils.nextInt(100000, 1000000);
         System.out.println("random 6 digit number:" + sixLength);
         String sixRandom = RandomStringUtils.randomNumeric(6).replaceAll("0", "8");;
         return String.format(sixRandom);
    	
    }
    
    public static String generateTicketId() {
    	 int sixLengthRandomNumber = RandomUtils.nextInt(100000, 1000000);
         System.out.println("random 6 digit number:" + sixLengthRandomNumber);
         String sixRandomNumber = RandomStringUtils.randomNumeric(6).replaceAll("0", "8");;
//        Random rnd = new Random();
//        int number = rnd.nextInt(999999);
//        return String.format("%06d", number);
         
         return String.format(sixRandomNumber);
      
    }
    
    public static String generatechangereqapplnno() {
   	 int sixLengthRandomNumber = RandomUtils.nextInt(100000, 1000000);
        System.out.println("random 6 digit number:" + sixLengthRandomNumber);
        String sixRandomNumber = RandomStringUtils.randomNumeric(6).replaceAll("0", "8");;
//       Random rnd = new Random();
//       int number = rnd.nextInt(999999);
//       return String.format("%06d", number);
        
        return String.format(sixRandomNumber);
     
   }
    
    
    public static String removeZero(String str)
    {
 
        // Count leading zeros
 
        // Initially setting loop counter to 0
        int i = 0;
        while (i < str.length() && str.charAt(i) == '0')
            i++;
 
        // Converting string into StringBuffer object
        // as strings are immutable
        StringBuffer sb = new StringBuffer(str);
 
        // The StringBuffer replace function removes
        // i characters from given index (0 here)
        sb.replace(0, i, "");
 
        // Returning string after removing zeros
        return sb.toString();
    }
  
   
    public static String getinitialsequential() {
        int number = 1000001;
        return String.format("%07d",number);
  	
  }
    
   
}
