package com.dmat.views.impl;

import java.util.*;

import com.dmat.Main;
import com.dmat.pojo.Admin;
import com.dmat.services.impl.AdminServiceImpl;
import com.dmat.views.api.AdminLogin;

public class AdminLoginImpl extends Admin implements AdminLogin {

	private static AdminLoginImpl instance = new AdminLoginImpl();  	// Singleton Design Pattern rule for lazy-initialisation
	 
	private AdminLoginImpl()
		  {
			 
		  }

	 public static AdminLoginImpl getInstance()    // Singleton Design Pattern rule for lazy-initialisation
	 { 
	   if (instance == null)  
	   { 
	     // if instance is null, initialize 
	     instance = new AdminLoginImpl(); 
	   } 
	   return instance; 
	 } 
	
	 public boolean adminLoginCheck(int adminID,char[] pwd)
	 {		 
		  if(adminID == Admin.getAdminID() )
		  {

			  if (Arrays.equals(Admin.getPassword(),pwd))
			  {  
				  return true;
		      }
			  else
			  {
				  return false;
			  }
		   }
		   else
		
		return false; 
		  
	 }
   
}
