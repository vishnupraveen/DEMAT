package com.dmat;

import java.util.ArrayList;

public class Validators {
	
	public static boolean isNumber(String input, String field)
	{
		
        try 
        { 
            // checking for valid integer using parseInt() method 
            Integer.parseInt(input); 
            return true;
        }  
        catch (NumberFormatException e)  
        { 
            System.out.println(input + " is not a valid format of Data for "+ field +", please provide the correct data"); 
        } 
		
		return false;
		
	}
	
	public static boolean isDouble(String input, String field)
	{
		
        try 
        { 
            // checking for valid double using parseDouble() method 
        	Double.parseDouble(input); 
            return true;
        }  
        catch (NumberFormatException e)  
        { 
            System.out.println(input + " is not a valid format of Data for "+ field +", please provide the correct data"); 
        } 
		
		return false;
		
	}

	static int MAX_VALID_YR = 9999; 
	static int MIN_VALID_YR = 1800; 
	  
	    // Returns true if  
	    // given year is valid. 
	    static boolean isLeap(int year) 
	    { 
	        return (((year % 4 == 0) &&  
	                 (year % 100 != 0)) ||  
	                 (year % 400 == 0)); 
	    } 
	    
	    static boolean isValidDate(int d, int m, int y) 
	    { 
	        // If year, month and day  
	        // are not in given range 
	        if (y > MAX_VALID_YR || y < MIN_VALID_YR) 
	            return false; 
	        if (m < 1 || m > 12) 
	            return false; 
	        if (d < 1 || d > 31) 
	            return false; 
	  
	        // Handle February month 
	        // with leap year 
	        if (m == 2)  
	        { 
	            if (isLeap(y)) 
	                return (d <= 29); 
	            else
	                return (d <= 28); 
	        } 
	  
	        // Months of April, June,  
	        // Sept and Nov must have  
	        // number of days less than 
	        // or equal to 30. 
	        if (m == 4 || m == 6 ||  
	            m == 9 || m == 11) 
	            return (d <= 30); 
	  
	        return true; 
	    } 
	    
	    public static boolean isDate(String input, String field) 
	    { 
	    	try
	    	{
		    	int dd,mm,yyyy;
		    	
		    	String[] ar;
		    	
		    	ar = input.split("-");	    	
		    	
		    	yyyy= Integer.parseInt(ar[0]);
		    	mm= Integer.parseInt(ar[1]);
		    	dd= Integer.parseInt(ar[2]);		    	
		    	
		    	//checking is the date is valid 
		    	
		        if (isValidDate(dd, mm, yyyy)) 
		        {
		        	return true;
		        }
		        else
		        {
		            System.out.println(input + " is not a valid format of Data for "+ field +", please provide the correct data"); 
		        	return false;
		        }
	    	}
	    	catch(Exception e)
	    	{
	            System.out.println(input + " is not a valid format of Data for "+ field +", please provide the correct data"); 
	    		return false;
	    	}

	    } 

}
