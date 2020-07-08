
package com.dmat;

import java.util.Scanner;


import com.dmat.datasource.impl.DBConnectionImpl;
import com.dmat.services.impl.AdminServiceImpl;
import com.dmat.services.impl.UserServiceImpl;
import com.dmat.views.impl.AdminLoginImpl;
import com.dmat.views.impl.UserAccountImpl;
import com.dmat.views.impl.UserLoginImpl;

public class Main {
	
	static Scanner sc=new Scanner(System.in);
	
    public static void main(String[] args) 
    { 
    		// display menu for login options
            initial();
    }
    
    public static void initial()   //Megha
    {
    	// menu to login to the software
           while(true)
           {
        	   
            System.out.println(" Main Menu  ");
            System.out.println(" 1 - User Login  ");
            System.out.println(" 2 - Admin Login");
            System.out.println(" 3 - Exit");
            
           //To check for every format of data given input by user
            String ch= sc.next();
            
            switch (ch)
            {
                case "1": userLoginMenu();
                    /* User Credential check , registration and menu post authentication */
                    break;
                case "2":
                    adminLoginMenu();
                    /* Admin Credential check  and menu post authentication*/
                    break;
                case "3":
                    System.exit(1);
                    break;
                default :
                    break;
             }
           }
    }
        
     
    public static void userLoginMenu()
    {   
    	// menu for the User login section
        while(true)
        {
            System.out.println(" User Login Menu  ");
            System.out.println(" 1 - Existing User Login  ");
            System.out.println(" 2 - Create New Demat Account  ");
            System.out.println(" 3 - Delete my Demat Account  ");
            System.out.println(" 4 - Exit");
          //To check for every format of data given input by user
            String ch= sc.next();
            switch (ch)
            {
                case "1":
                	userLogin();
                	/* User Credential check */
                    break;
                case "2":
                	UserAccountImpl create = new UserAccountImpl();
                	create.createUser();
                	/* registration */
                    break;
                case "3":
                	UserAccountImpl delete = new UserAccountImpl();
                	delete.deleteUser();
                	/* delete user */
                    break;
                case "4":
                    logout();
                	System.exit(1);
                	/* logout */
                    break;
                default :
                    break;
            }
        }
    }

    public static void adminLoginMenu()    //Megha
    {
    	// menu for the User login section
        while(true)
        {
            System.out.println(" Admin Login Menu  ");
            System.out.println(" 1 - Login as Admin");
            System.out.println(" 2 - Exit");
          //To check for every format of data given input by user
            String ch= sc.next();
            switch (ch)
            {
                case "1":
                	adminLogin();   
                	/* admin Credential check */
                    break;
                case "2":
                	/* logout */
                    logout();
                	System.exit(1);
                    break;
                default :
                    break;
            }
        }
    }
    
    public static void adminLogin()     //Megha
    {
    	// menu for the admin login section
    	while(true) 
    	{
	        try 
	        {
		        System.out.println("Provide your credentials to login as Admin");
		        
		        System.out.println("Enter Admin ID");
		        int adminID;
		        adminID = sc.nextInt();
	
		        System.out.println("Enter your password");
		        char[] pwd = sc.next().toCharArray();
		        
		        AdminLoginImpl al = AdminLoginImpl.getInstance();
		        boolean res = al.adminLoginCheck(adminID, pwd);
		        
		        if(res == true)
		        { 
		    	  AdminServiceImpl as = new AdminServiceImpl();
		    	  as.adminOperations();
		        }
		        else 
		        {
		    	  System.out.println("Invalid Admin ID or Password, Please Try again");
		    	  adminLoginMenu();
		    	  continue;
		        }
	        }
	        catch(Exception e) 
	        {
	        	System.out.println("Invalid type of information");
	        	adminLoginMenu();
	        	break ;
	        }
        }
     
    }
    
    public static void userLogin()
    {
    	// user login section
    	while(true) 
    	{	
	        try 
	        {
		        Scanner obj = new Scanner(System.in);
		        System.out.println("Provide your credentials to login");
		        
		        System.out.println("Enter customer ID");
		        int customerid;
		        customerid = obj.nextInt();
		        	
	
		        System.out.println("Enter your password");
		        char[] pwd = obj.next().toCharArray();
		        
		        UserLoginImpl al = UserLoginImpl.getInstance();
		        boolean res = al.userLoginCheck(customerid, pwd);
		        
		        if(res == true)
		        { 
		          UserServiceImpl as = new UserServiceImpl();
		    	  as.UserOperations();
		        }
		        else 
		        {
		    	  System.out.println("Invalid customer ID or Password, Please Try again");
		    	  continue;
		        }
	        }
	        catch(Exception e) 
	        {
	        	System.out.println("Invalid type of information");
	        	continue;
	        }
        }
     
    }  
	public static void logout()
	{	
		//kill connections, but not exit the system
		DBConnectionImpl.killCon();
		System.out.println("Thank you for using our services.... !");
		Main.initial();
	} 
      
 }

