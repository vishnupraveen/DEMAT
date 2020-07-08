package com.dmat.views.impl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.dmat.Main;
import com.dmat.Validators;
import com.dmat.datasource.impl.TransactionImpl;

public class UserAccountImpl {

	public void createUser() 

    {
		//to create a new user/ user registeration form
		 Scanner sc = new Scanner(System.in);
		 TransactionImpl txn = new TransactionImpl(); 
		 long account_number;
	   	 double initial_deposit=0.00;
	   	 String fname="",lname="",email=" ";
	     String password="",confirmpassword="";
	     String phoneNumber="";
	   	 int result=0,customer_id=0;
	   	 
	         try
	         {

		    	 System.out.println("Enter the first name");// Enter the first name
		         fname=sc.nextLine();
		         while(fname.matches("^[a-zA-Z]*$")==false)
		         {
		   	     System.out.println("Enter Valid Name");
		   	     fname = sc.nextLine();
		         }
	         }
	   	 catch (InputMismatchException e) 
	         {
			     System.out.println("Enter a valid name");
			     System.out.println(e);
	         }
	   	 
	   	 try
	   	 {
	         System.out.println("Enter your last name:");//Enter the last name
			 lname=sc.nextLine();
			 while(lname.matches("^[a-zA-Z]*$")==false)
			 {
			      System.out.println("Enter Valid Name");
			      lname=sc.nextLine();
		   	 }
	   	 }	
		 catch (Exception e) 
	   	 {
		       System.out.println("Enter a valid name");
		       System.out.println(e);
		 }
	   	 
	         try
	         {

	    	 System.out.println("Enter your email address:");//Enter a valid email address
	    	 email=sc.nextLine();	
	         while(email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")==false)
	         {

	               System.out.println("Enter a valid email address");
	               email=sc.nextLine();
	         }
	         }
	         catch(Exception e)
	         {

	    	       System.out.println("Enter a valid email address");
	               System.out.println(e);
	         }
	    
	   	try
		{
		     System.out.println("Enter the phone number");
		     phoneNumber= sc.nextLine();
		     while(phoneNumber.matches("^[0][1-9]\\d{9}$|^[1-9]\\d{9}$")==false)
		{
		     System.out.println("Enter a valid phone number");
		     phoneNumber= sc.nextLine();
		}
		}
	   	catch(InputMismatchException e)
		{
			 System.out.println("Invalid value entered,Please enter a valid number");
			 System.out.println (e);
		}
	   	
	   	     System.out.println("Enter your initial deposit amount:");
	   	     initial_deposit= sc.nextDouble();

	   	     while(initial_deposit<10000)
	   	{
	   		 System.out.println("initial deposit can not be less than 10000");
	   		 System.out.println("Enter your initial deposit:");
	       	 initial_deposit= sc.nextDouble();
	   	}
	   	     System.out.println("Enter your password");
	   	     password=sc.next();
	   	     
	   	     int flag=1;
	   	     while(flag==1)
	    {	   	 
		   	 System.out.println("Enter your confirm password:");
		   	 confirmpassword= sc.next();
		   	 
		   	 if(password.equals(confirmpassword))
		{
		   	 
		     flag=0;
		     break;
		}
		   	 else
		{
		     System.out.println("Password doesnot match re enter the password");   		 
		}
	   	}
   	    	
	   	     // adding the user details into database
		     String add_cust= "INSERT ignore INTO `mst_customer` (`customer_fname`,`customer_lname`,`email`,`phoneNumer`,`initial_deposit`,`credentials`,`account_number`,`status`) "
					+ "VALUES ('"+fname+"','"+lname+"','"+email+"','"+phoneNumber+"','"+initial_deposit+"','"+password+"',date_format(now(), '%Y%m%e%k%i%s'),'Active')";
		 
		     result = txn.update(add_cust);

	         if(result==1)
	    {
			 String get_cust = "select customer_id,account_number from mst_customer where email='"+email+"'";
			 ResultSet rs = txn.read(get_cust);

	    try 
		{
			 if(rs.next())  
		{	
			 customer_id = rs.getInt(1);
			 account_number = rs.getLong(2);
		}
			 else
		{
			 System.out.println("Account creation is not sucessfull, please try again");
			 Main.userLoginMenu();
		}			
			
			 // adding data into account table, as he is joing with the deposit amount
			 String add_account = "INSERT INTO `txn_account` (customer_id,date,type,deposit,balance) VALUES ('"+customer_id+"',now(),'Deposit','"+initial_deposit+"','"+initial_deposit+"')";
			 result = txn.update(add_account);

			 if (result==1)
		{
				 //displaying the message to the user on account creation
			 System.out.println("******Account creation is sucessfull*****");
			 System.out.println("Your account number is "+rs.getString(2));
			 System.out.println("Your customer identification number is "+rs.getString(1));
			 System.out.println("Please login with the customer ID to start your transactions");
			 Main.userLoginMenu();
		}	
				
		}
		catch (SQLException e) 
		{
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		}
	    }
	         else
	    {
			 System.out.println("Account creation is not sucessfull, please try again");
			 Main.userLoginMenu();
	    }
		
	    }

	    public void deleteUser() 
	    {
	    	// if the user wants to withdraw from the firm, he can do it here
		     Scanner sc = new Scanner(System.in);
		     TransactionImpl txn = new TransactionImpl();
		     int customer_id=0;
		     String password = null;
		     String confirmpassword;	             
		     int flag=0, count=0,companyId;
		     String input;
		     
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
		   	    	System.out.println("Enter your customerID:");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isNumber(input, "Customer ID"))
				   	     {	 
					   	     customer_id = Integer.parseInt(input); 
					   	     count=0;
					   	     flag=0;
					   	     break;
				   	     }
				   	     count++;
		   	     }
		   	     else
		   	     {
		   	    	 System.out.println("Exceeded the limit, aborting operations");
		   	    	 flag=1;
		   	    	 Main.userLoginMenu();
		   	     }		 
		     }	
		     		 
		     flag=1;
		     while(flag==1)
		     {	
				 System.out.println("Enter your password");
				 password= sc.next();
			
			     System.out.println("Enter your confirmpassword:");
				 confirmpassword= sc.next();
				
				 if(password.equals(confirmpassword))
				 {						
					flag=0;
					break;
				 }
				 else
				 {
				 System.out.println("Password doesnot match re enter the password, try again");
				 }
		
		     }
		
		     // update customer table status to 'deactive'
		     String upd_cust = "UPDATE mst_customer SET `status` = 'Deactive' WHERE customer_id = "+customer_id+" and `status`<>'Deactive' and credentials='"+password+"'";
		
		     int var = txn.update(upd_cust);
		     
	         if(var == 1)
	    {		 
			 System.out.println("Deactivated the customer with ID = "+customer_id);
	    }
	         else
	    {
     	     String checkCid = "SELECT customer_id,status,credentials FROM mst_customer WHERE `customer_id`= "+customer_id;    	   
     	     ResultSet rs = txn.read(checkCid);       	  
		     	try 
		     	{
		     		 if(rs.next())
		     		 {
						 String CurStatus = rs.getString(2);
						 if(CurStatus.equals("Deactive"))
						 {
							 System.out.println("The customer is already deactivated, No Actions Taken"); 
							 Main.userLoginMenu();
						 } 
						 
						 String credentials = rs.getString(3);
						 if(credentials.equals(password))
						 {
							 System.out.println("Customer Id or password is not matching"); 
							 Main.userLoginMenu();
						 }
		     		 }
					 else
					 {
						 System.out.println("Customer ID is not available, please try again with the correct customer ID");   
						 Main.userLoginMenu();
					 }
				} 
		     	catch (SQLException e) 
		        {
						// TODO Auto-generated catch block
					 e.printStackTrace();
				}
	   }
    } 

}
