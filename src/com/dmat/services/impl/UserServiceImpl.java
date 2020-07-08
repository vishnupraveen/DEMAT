package com.dmat.services.impl;
import com.dmat.Main;
import com.dmat.Validators;
import com.dmat.datasource.api.Transaction;
import com.dmat.datasource.impl.DBConnectionImpl;
import com.dmat.datasource.impl.TransactionImpl;
import com.dmat.exception.NoAvailableSharesException;
import com.dmat.services.api.UserService;
import com.dmat.views.impl.UserLoginImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

@SuppressWarnings("resource")
public class UserServiceImpl implements UserService 
{	
	UserLoginImpl al = UserLoginImpl.getInstance();
	int customerID = al.customerID;
	
	double charges=0;
	double t_stt=0;
	double min_charges=0;
	
	final int firmID = 122345;
	
    public void UserOperations() {
    	
    	UserTransactionServices transactions = new UserTransactionServices();
    	UserAccountServices services = new UserAccountServices();
    	Scanner sc = new Scanner(System.in);  
    	
        // give the menu here
    while(true)                                          
    {    
    	 System.out.println("");
         System.out.println("Enter your choice");                                              
         System.out.println("1.Route to home page \r\n2.Deposit Money \r\n3.Withdraw Money \r\n4.Buy Shares \r\n5.Sell Shares \r\n6.Shares Report \r\n7.Account Report \r\n8.Logout \r\n9.Quit ");                                        
                                                    
          try 
          {                                               
	          String choice = sc.next();                                                    
	          switch(choice)                                                   
	          {                                                   
	               case "1": 
	            	   	   System.out.println("                   ************My home page************");                                            
	                       displayDemat();                                           
	                       break;                                              
	               case "2": 
	            	   	   System.out.println("                   ************Deposit Amount************ ");      
	            	   	   services.depositAmount();                                          
	                       break;                                              
	               case "3": 
	            	   	    System.out.println("                   ************Withdraw Amount************");                                              
	            	   	    services.withdrawAmount();                                              
	                        break;                                             
	               case "4": 
	            	   	   System.out.println("                   ************Buy Share************"); 
	            	   	   transactions.buyShare();                                         
	                       break;                                                                                           
	               case "5": 
	            	       System.out.println("                    ************Sell Share************");       
	            	       transactions.sellShare();                                              
	                       break;                                             
	               case "6": 
	            	   	   System.out.println("                    ************Report************");
	            	   	   generateShareReport();
	                       break;
	               case "7": 
	            	   	   System.out.println("                   ************Report************");
	            	   	   generateAccountReport();
	                       break;
	               case "8": 
		                   Main.logout();
		                   break;
	               case "9": 
		                   DBConnectionImpl.killCon();
		                   System.exit(1);
		                   break;
		           default: 
			        	   UserOperations();
			        	   break;
	          }                                                   
          } 
        
          catch(Exception e)                                                   
          {                                                   
               System.out.println("Select a valid option");                                          
          }                                                  
      }                        
  }

    @Override
    public void displayDemat() {
    	
    	// display the user's account details
    	
    	Transaction txnimp = new TransactionImpl();
        String queryString = "SELECT * FROM mst_customer Where customer_id ="+customerID +" ";
        ResultSet results = txnimp.read(queryString);

        String fname = null,lname = null,email = null,status=null;
        long phoneNumber=0,accountNummber=0;
        
        try 
        { 
	        while(results.next())
	         {
	        	
	            fname = results.getString("customer_fname");
	            lname = results.getString("customer_lname");
	            email = results.getString("email");
	            status = results.getString("status");
	            phoneNumber = results.getLong("phoneNumer");   
	            accountNummber = results.getLong("account_number");            
	        }
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        	 System.out.println("");
	         System.out.println("Customer Identification number: "+customerID);
	         		         
	         System.out.println("Account Number: "+accountNummber);
	              
	         System.out.println("First Name: "+fname);
         
	         System.out.println("Last Name: "+lname);
         
	         System.out.println("email address: "+email);        
		         
	         System.out.println("Phone Number: "+phoneNumber);
		         
	         System.out.println("Status : "+status);	
 
    	int count=0;
    	ResultSet rs;
    	
    	try 
    	{    	
    		// displaying user money related details
    	   	String userDetails= "SELECT round(balance,2) as balance FROM `txn_account` where customer_id='"+customerID+"' order by date desc limit 1";
          
	    	rs = txnimp.read(userDetails);
	    		System.out.println("");
	    		
		        while(rs.next())
		        {
			         String balance = rs.getString("balance");
			         System.out.println("Current Balance: "+balance);			        
		        }
		       
		        //dispay user stock related details
	        userDetails= "SELECT a.company_id as cid,b.company_name as cname,sum(if(a.type='buy',a.units,0)) - sum(if(a.type='sell',a.units,0)) as availalbe_units,c.price as current_price"+ 
						" FROM txn_stock as a, mst_company as b, current_stock as c where a.customer_id='"+customerID+"' and a.company_id=b.company_id and a.company_id=c.company_id group by a.company_id,b.company_name having availalbe_units>0";

		    rs = txnimp.read(userDetails);
		    System.out.println();
		    System.out.println("CompanyId   	  company Name	  	Units      current_price");
	
				while(rs.next())
			    {
					int companyID = rs.getInt(1);
					String companyName = rs.getString(2);
					int units = rs.getInt(3);
					double price = rs.getDouble(4);
					
					System.out.printf("%-15d  |%-20s |%-10d |%-10f ",companyID,companyName,units,price);
			        System.out.println();
					count++;
			    }
				
				if (count==0)
				{
					throw new NoAvailableSharesException("You currently do not own any Shares");
				}
						        		       
        }

    	catch (NoAvailableSharesException e)
    	{
             
             System.err.println(e.getReason());
            
        } 
    	catch (Exception e)
    	{
             System.err.println(e.getMessage());
             e.printStackTrace();             
        } 
    	finally
    	{
    		UserOperations();
    	}

     }

    public boolean generateShareReport()
    {
    	// generate report of the transactions he made for a particular date range
    	Transaction txnimp = new TransactionImpl();
    	Scanner sc = new Scanner(System.in);  
		String condition = " where customer_id="+customerID;
		String final_query="";
		String updated_date="";
		int company_id = 0,units;
		double selling_price,cost_price;
		String type,company_name;
		ResultSet rs;
		int count=0,flag=0;
		String input;
		
		System.out.println("Are you looking for a particular company? (y/n)");
		String choice=sc.next();
		if (choice.equals("y"))
		{
			System.out.println("Please provide the company id for which you would like to see the trend");
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
				   	     System.out.println("Enter the company ID");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isNumber(input, "company ID"))
				   	     {	 
				   	     company_id = Integer.parseInt(input); 
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
		   	    	UserOperations();
		   	     }		 
		     }
			
			condition = condition + " and a.company_id="+company_id;
		}
		else 
		{
			if (!choice.equals("n"))
			{
				System.out.println("Please make proper choice");
				generateShareReport();
			}
		}
				
		System.out.println("Are you looking for a particular date range? (y/n)");
		choice=sc.next();
		
		String startDate="",endDate="";
		count=0;
		if (choice.equals("y"))
		{		
			while(flag==0)
			{
				if (count<3)
				{
					
					System.out.println("Please provide the start date in 'yyyy-MM-dd' format");
					startDate=sc.next();
					
					if (Validators.isDate(startDate, "Date"))
					{
						count=0;
						flag=0;
						break;
					}
					else
					{
						count++;
						//generateShareReport();
					}
				}
				else
				{		
					System.out.println("Exceed input limit, aborting operation");
					UserOperations();					
				}
			}

		     
			while(flag==0)
			{
				if (count<3)
				{
					System.out.println("Please provide the end date in 'yyyy-MM-dd' format");
					endDate=sc.next();				
					
					if (Validators.isDate(endDate, "Date"))
					{
						count=0;
						flag=0;
						break;
					}
					else
					{
						count++;
						//generateShareReport();
					}
				}
				else
				{	
					System.out.println("Exceed input limit, aborting operation");
					UserOperations();				
				}
			}
			
			condition = condition + " and a.date between '"+startDate+"' and '"+endDate+"'";
		}
		else 
		{
			if (!choice.equals("n"))
			{
				System.out.println("Please make proper choice");
				generateShareReport();
			}
		}
		condition = condition + " and a.company_id=b.company_id";
						
      	final_query = "SELECT a.date,a.company_id,b.company_name,a.type,a.units,a.selling_price,a.cost_price " +
      				"FROM txn_stock as a, mst_company as b "+condition+" order by a.date";
      	
      	
      	
    	System.out.println("date  		        company_id   company_name  		type  		units  		selling_price  		cost_price");
        
    	try 
    	{    	
	    	
	    	rs = txnimp.read(final_query);
	        while(rs.next())
	        {
	        	updated_date = rs.getString(1);		        
		        company_id = rs.getInt(2);		        
		        company_name = rs.getString(3);
		        type = rs.getString(4);
		        units = rs.getInt(5);
		        selling_price = rs.getDouble(6);
		        cost_price = rs.getDouble(7); 
		        System.out.printf("%-20s  |%-18d |%-25s |%-10s |%-15d |%-15f |%-15f",updated_date,company_id , company_name,type , units , selling_price , cost_price  );
		        System.out.println();	        
		        
	        }
	        
	        return true;
	      
        }
    	catch (Exception e)
    	{
             System.err.println("Exception Occured! ");
             System.err.println(e.getMessage());
             UserOperations();
        }
    	
    	return false;

    }
    
    public boolean generateAccountReport()
    {
    	// generate report of the transactions he made for a particular date range
    	Transaction txnimp = new TransactionImpl();
    	
		Scanner sc = new Scanner(System.in);  
		String condition = " where customer_id="+customerID;
		String final_query="";
		String updated_date="";
		double deposit,withdrawl,balance;
		String type;
		ResultSet rs;
		int count=0;
		String startDate="",endDate="";
				
		System.out.println("Are you looking for a particular date range? (y/n)");
		String choice=sc.next();
		if (choice.equals("y"))
		{
			while(true)
			{			
				if (count<3)
				{
					System.out.println("Please provide the start date in 'yyyy-MM-dd' format");
					startDate=sc.next();
					
					if (Validators.isDate(startDate, "Date"))
					{
						count=0;
						break;
					}
					else
					{
						count++;
						//generateAccountReport();
					}
				}
				else
				{
		   	    	 System.out.println("Exceeded the limit, aborting operations");
		   	    	 UserOperations();
				}
			}
			
			
			while(true)
			{			
				if (count<3)
				{
					System.out.println("Please provide the end date in 'yyyy-MM-dd' format");
					endDate=sc.next();
					
					if (Validators.isDate(endDate, "Date"))
					{
						count=0;
						break;
					}
					else
					{
						count++;
					}
				}
				else
				{
		   	    	 System.out.println("Exceeded the limit, aborting operations");
		   	    	 UserOperations();
				}
			}
			
			condition = condition + " and date between '"+startDate+"' and '"+endDate+"'";
		}
		else if (choice.equals("n"))
		{
			
		}
		else
		{
			System.out.println("Please make proper choice");
			generateShareReport();
		}
								
      	final_query = "SELECT date,type, round(deposit,2)	,round(withdrawl,2),round(balance,2) from txn_account "+condition+" order by date";   	
      	
    	System.out.println("date 			type 	deposit 		withdrawl  		balance	");
        
    	try 
    	{    	
	    	
	    	rs = txnimp.read(final_query);
	        while(rs.next())
	        {
	        	updated_date = rs.getString(1);	        
		        type = rs.getString(2);
		        deposit = rs.getDouble(3);
		        withdrawl = rs.getDouble(4);
		        balance = rs.getDouble(5);

		        System.out.printf("%-20s  |%-10s |%-15f |%-15f |%-15f ",updated_date,type , deposit,withdrawl , balance);
		        System.out.println();	 
		        
	        }
	        
	        System.out.println("");
	        return true;
        }
    	catch (Exception e)
    	{
             System.err.println("Got an exception! ");
             System.err.println(e.getMessage());
             UserOperations();
        }
    	
    	 return false;

    }

}
