package com.dmat.views.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import com.dmat.Main;
import com.dmat.datasource.impl.DBConnectionImpl;
import com.dmat.datasource.impl.TransactionImpl;
import com.dmat.pojo.Admin;
import com.dmat.pojo.User;
import com.dmat.services.impl.UserServiceImpl;
import com.dmat.views.api.UserLogin;

public class UserLoginImpl implements UserLogin {
	
	Scanner sc = new Scanner(System.in);
	TransactionImpl txn = new TransactionImpl();
	
	public int customerID;
	
	private static UserLoginImpl instance = new UserLoginImpl();  	// Singleton Design Pattern rule for lazy-initialisation
	 
	private UserLoginImpl()
	{
			 
	}

	 public static UserLoginImpl getInstance()    // Singleton Design Pattern rule for lazy-initialisation
	 { 
	   if (instance == null)  
	   { 
	     // if instance is null, initialize 
	     instance = new UserLoginImpl(); 
	   } 
	   return instance; 
	 } 
	
    @Override
    public boolean userLoginCheck(int customerID,char[] pwd)
    {
    	
	    try 
	    {	 

	         String queryString = "SELECT * FROM mst_customer Where customer_id ="+customerID +" ";
	         ResultSet results = txn.read(queryString);
	         char[] password = {};
	         String fname = null,lname = null,email = null,status=null;
	         long phoneNumber=0,accountNummber=0;
 	         
	         while(results.next())
 	         {
	             password =  results.getString("credentials").toCharArray();	             
             }
 	         if(Arrays.equals(password, pwd))
             {
 	        	 //setting values to the user attribute this will be used across in the project
 	        	 this.customerID = customerID;  	 
 	        	 return true;
             }
             else
             {
                 System.out.println("Incorrect customer ID or Password, please try again");
                 Main.userLogin();
             }
         }
	     catch (SQLException sql)
         {
              sql.printStackTrace();
         }
	    
	     return false;

    }
        


}


