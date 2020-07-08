package com.dmat.services.impl;

import com.dmat.Main;
import com.dmat.Validators;
import com.dmat.datasource.impl.DBConnectionImpl;
import com.dmat.datasource.impl.TransactionImpl;
import com.dmat.services.api.AdminService;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("resource")
public class AdminServiceImpl implements AdminService {
	
	TransactionImpl txnimp = new TransactionImpl();
	
	public void adminOperations()
    {
		//menu for admin operations
	Scanner sc = new Scanner(System.in);

	 while(true)
	 {
		System.out.println("");
    	System.out.println("Enter your choice");
    	System.out.println("1.Add Company \r\n2.Restrict Company \r\n3.Activate Company \r\n4.Update Shares by price or units  \r\n5.Generate Stock Report \r\n6.Generate earning Report \r\n7.Display current stock details \r\n8.Update Commision details \r\n9.Go To Main Menu \r\n10.Logout \r\n11.Quit");
                
        try 
        {
        	
	        String choice = sc.next();
	        switch(choice)
	        {
	        	case "1": System.out.println("                   ********Add Company********");
	        	        addCompany();
	           	        break;
	        	        
	        	case "2": System.out.println("                 ********Restrict Company********");
	        	        restrictCompany();
	        	        break;
	        	        
	        	case "3": System.out.println("                 ********Activate Company********");
		                activateCompany();
		                break;
		                
	        	case "4": System.out.println("                  ********Update Shares********");
	        	        updateStockInfo();
		                break;
		                
	        	case "5": System.out.println("                  ********Report on Stock trend********");
	        			generateStockTrend();
		                break;
		                
	        	case "6": System.out.println("                  ********Report on Earnings********");
	        			generateEarningReport();
		                break;
		                
	        	case "7": System.out.println("                  ********Shares Details********");
		        	        displayStock("");
			                break;
		                
	        	case "8": System.out.println("                  ********Update Commision details********");
			    	        updateCommision();
			                break;
		                
	        	case "9": Main.initial();
	        	
	        	case "10": Main.adminLoginMenu();
	        	
	        	case "11": DBConnectionImpl.killCon();
	        			   System.exit(1);
	        			   
	        	default:
	        			adminOperations();
	        }
	        
        }
        
        catch(Exception e)
        {
        	System.out.println("Select a valid option");
        }
	  }
 
    }
     
    private void updateCommision() {
    	//every transaction made by user is an earning to the firm
    	//we are currently chaging them 0.5% for transaction
    	//0.1 % for Securities Transfer Tax
    	//100 rs is the minimum transaction charges
    	// admin will be using this function to update these charges if required any time
    	Scanner sc = new Scanner(System.in);

    	int flag=0,count=0;
    	double charges=0, stt=0, minCharges=0;  	
    	String input;
    	
    	//validating and taking value for variables
		try 
		{
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
				   	     System.out.println("Enter the Transaction Charges in Number format. Ex, 5% => 0.05");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isDouble(input, "Transaction Charges"))
				   	     {	 
				   	     charges = Double.parseDouble(input); 
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
		   	    	adminOperations();
		   	     }		 
		     }
		     
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
				   	     System.out.println("Enter the Securities Transfer Tax in Number format. Ex, 5% => 0.05");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isDouble(input, "Securities Transfer Tax"))
				   	     {	 
				   	     stt = Double.parseDouble(input); 
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
		   	    	adminOperations();
		   	     }		 
		     }
		     
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
				   	     System.out.println("Enter the minimum transaction charges in Number format. Ex, 5% => 0.05");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isDouble(input, "Minimum Transaction Charges"))
				   	     {	 
				   	     minCharges = Double.parseDouble(input); 
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
		   	    	adminOperations();
		   	     }		 
		     }
		     
		     // query to update the changes in the database, prints messages according to the result
		     String Update_charges= "UPDATE .`charges` SET `tranx_charges` = '"+charges+"',`stt` = '"+stt+"',`minimun_charges` = '"+minCharges+"'" ;    
		     int result = txnimp.update(Update_charges);
		     
		     if(result==1)
		     { 
	   	    	 System.out.println("Sucessfully update the charges information");
		     }
		     else
		     {
	   	    	 System.out.println("Update not sucessful, please reach out to admin");
		     }
		     
		}
		catch(Exception e)
		{
			System.out.println("Caught an exception");
			e.printStackTrace();
			adminOperations();
		}
	}

    @Override  
    public void addCompany(){
    	
    	//to add any new company
    
    	 Scanner sc = new Scanner(System.in);

	     int result=0,flag=0,count=0;
	     double unit=0,price=0;
	     String input="",companyName="";
	     int companyId=0;
	     
	     try 
	     {
	    	 //validating and taking the inputs
		     System.out.println("Enter the name of the company");
		     companyName = sc.next();
		     
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
				   	     System.out.println("Enter the company ID");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isNumber(input, "company ID"))
				   	     {	 
				   	     companyId = Integer.parseInt(input); 
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
		   	    	adminOperations();
		   	     }		 
		     }	
		     
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
		   	    	System.out.println("Enter number of Units");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isDouble(input, "Units"))
				   	     {	 
				   	     unit = Double.parseDouble(input); 
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
		   	    	adminOperations();
		   	     }		 
		     }
		     
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
		   	    	System.out.println("Enter price of each share");
				   	     input  = sc.next();
				   	     
				   	     if (Validators.isDouble(input, "Price"))
				   	     {	 
				   	     price = Double.parseDouble(input); 
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
		   	    	 adminOperations();
		   	     }		 
		     }
		    		    
		     TransactionImpl txnImp = new TransactionImpl();
		     
		     //adding the company in master company table
		     String add_comp= "INSERT ignore INTO `mst_company` (`company_id`,`company_name`,`status`,`OnBoarding_date`)" + 
						      "VALUES ('"+companyId+"','"+companyName+"','Active',now())" ;    
		     
		     result = txnImp.update(add_comp);
		     	     
		      if(result==1)
		      {
		    	  	// on sucess, adding it into stock trend and curretn stock tables
		    	     String add_stcktrned = "INSERT INTO `txn_stocktrend` (`company_id`,`company_name`,`price_per_unit`,`updated_date`)" + 
								             "VALUES ('"+companyId+"','"+companyName+"','"+price+"',now())";
					 txnImp.update(add_stcktrned);
						
					 String add_curstock = "INSERT INTO `current_stock` (`company_id`,`company_name`,`units`,`price`,`status`)" + 
						                     "VALUES ('"+companyId+"','"+companyName+"','"+unit+"','"+price+"','Active')";
					 txnImp.update(add_curstock);
		    	  
					 System.out.println("Successfully added Company "+companyName+" to the firm and is available for the user transaction");
					 adminOperations();
		      }
		      else 
		      { 	 
		    	  
		    	     System.out.println("Company ID : "+companyId+" already exits, please try again with the correct company ID");
		    	     adminOperations();
		      }
	     }
	     catch(Exception e)
	     {
	     }
	     finally
	     {
	    	 sc.close();
	     }
	     
    } 
 
    @Override
    public void restrictCompany() 
    {
    	
    	// the admin can also restrict company, this is the method to complete that activity
    	// if a company is restricted, no one will be able to buy shares of that company
    	
    	Scanner sc = new Scanner(System.in);
    	boolean info;
    	info = displayStock(" Where status='Active'");

    	
    	if (info = true)
    	{
	    		
	    	String CurStatus = null,input="" ;
	    	int result=0,flag=0,count=0,CompID=0;
	    	ResultSet rs;
	    	
	 	   try 
	 	   {   
	 		   //validating inputs
			     while(flag==0)
			     {
			   	     if (count<3)
			   	     {
					   	     System.out.println("\r\nPlease provide the CompanyID you wish to Restrict from the above report ");

					   	     input  = sc.next();
					   	     
					   	     if (Validators.isNumber(input, "company ID"))
					   	     {	 
					   	     CompID = Integer.parseInt(input); 
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
			   	    	adminOperations();
			   	     }		 
			    }	
			    
		    	//updating the current stock table 
		    	String upd_CurStock = "UPDATE current_stock SET `status` = 'Restricted' WHERE `company_id` = "+CompID+" and `status`='Active' ";
		    	
		        result = txnimp.update(upd_CurStock);
		        
		        if(result == 1)
		        {
		         // on sucess, update the master table
		        	String upd_comp = "UPDATE mst_company SET `status` = 'Restricted' WHERE company_id = "+CompID;   	
		            txnimp.update(upd_comp);
		        	
		        	System.out.println("Restricted the CompanyID = "+CompID+", clients can not be able to buy stock from this company until it is activated  \r\n Below is the list of Companies restricted");
		            
		        	displayStock(" Where status='Restricted'");
		        	adminOperations();
		        }
		        else  
		        {
		        	// on failure, check if the company id is availalbe, route we have
		        	   String checkCid = "SELECT company_id,status FROM current_stock WHERE `company_id`= "+CompID;
		        	   
		        	   rs = txnimp.read(checkCid);       	   
		
		        	   if(rs.next())
		        	   {
		        		   CurStatus = rs.getString(2);
		        		   if(CurStatus.equals("Restricted"))
		        		   {
							   	System.out.println("The company is already restricted, No Actions Taken"); 
							   	adminOperations();
		        		   }        		   
		           	   }
		        	   else
		        	   {
		        		   System.out.println("Company ID is not available, please try again with the correct company ID");   
		        		   adminOperations();
		        	   }
		        }
	    	
			} 
	 	   catch (SQLException e) 
	 	   {
	 		    System.out.println("We have encountered an error, please contact admin");
				adminOperations();
	 	   }
		     finally
		     {
		    	 sc.close();
		     }
    	}
    	else
    	{
	    	System.out.println("We do not have any active company, operation aborted");	    	
	    	adminOperations();
    	}

    }

    public void activateCompany()    
    {
    	// to activate any restricted company
    	Scanner sc = new Scanner(System.in);
 
    	boolean info;
    	info = displayStock(" Where status<>'Active'");
    	
    	String CurStatus = null,input="" ;
    	int result=0,flag=0,count=0,CompID=0;
    	ResultSet rs;
    	
    	//checking for valid data 
    	if (info==true)
    	{		    	
		     while(flag==0)
		     {
		   	     if (count<3)
		   	     {
		 	    	System.out.println("\r\nProvide the CompanyID you wish to Activate from the above report ");

				   	     input  = sc.next();
				   	     
				   	     if (Validators.isNumber(input, "company ID"))
				   	     {	 
				   	     CompID = Integer.parseInt(input); 
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
		   	    	adminOperations();
		   	     }		 
		    }	
	    	
		    try 
		    {
		       // updating the master company table  	
		    	String upd_curStock = "UPDATE mst_company SET `status` = 'Active' WHERE company_id = "+CompID+" and `status`<>'Active'";
		    	
		        result = txnimp.update(upd_curStock);          
		        
		        if(result==1)
		         	{
		        	// on success updating the other tables
				    	upd_curStock = "UPDATE current_stock SET `status` = 'Active' WHERE company_id = "+CompID;
				    	
				        result = txnimp.update(upd_curStock);
				        
		            	System.out.println("Activated the company with the ID = "+CompID+". Below is the list of active companies in firm");
		            	displayStock(" Where status='Active'");
		                adminOperations();
		            }
		        else  
		        	{
		        	// on failure checking for availability of the company
		     	        String checkcid = "SELECT company_id,status FROM current_stock WHERE `company_id`= "+CompID;     	   
		     	         
		     	        rs = txnimp.read(checkcid);    		
		
		     	        if(rs.next())
		     	        {
						    CurStatus = rs.getString(2);
						    if(CurStatus.equals("Active"))
						    {
						    	System.out.println("The company is already Activated, No Actions Taken");
						    	displayStock(" Where status='Active'");
						    	adminOperations();
						    }  
		        	    }
		     	        else
		     	        {						    
		     		        System.out.println("Company ID is not available, please try again with the correct company ID.");
		     		        System.out.println("Below is the list of inactive companies");
		     		        displayStock(" Where status<>'Active'");
		     		        adminOperations();
		     	        }
		            }
		        
			} 
		    catch (SQLException e) 
		    {			
	 		    System.out.println("We have encountered an error, please contact admin");
				adminOperations();
			}
		     finally
		     {
		    	 sc.close();
		     }
    	}
    	else
    	{
	    	System.out.println("We do not have any deactivated company, operation aborted");	    	
	    	adminOperations();
    	}
    }  
    
    public void updateStockInfo() 
    {   
    	// any change in the price or the units has to be updated in the DB by admin
    	Scanner sc = new Scanner(System.in);

    	System.out.println("Do you want to update stock price or stock units ? \r\n1.Units\r\n2.Price");

        String choice=sc.next();             
    	String input="" ;
    	int result=0,flag=0,count=0,CompID=0,units=0;
    	ResultSet rs;
    	double price=0;
    	        
        TransactionImpl txnimp = new TransactionImpl();
        
        switch(choice)
        {
         case "1":        	 
           	 try
           	 {
           		 // update units information
           		displayStock("");
           			//validating the inputs before taking them
          		     while(flag==0)
        		     {
        		   	     if (count<3)
        		   	     {
        		   	    	 	System.out.println("Enter the Company ID of the company to change the units ");
        		   	    	
        				   	     input  = sc.next();
        				   	     
        				   	     if (Validators.isNumber(input, "company ID"))
        				   	     {	 
        				   	     CompID = Integer.parseInt(input); 
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
        		   	    	  adminOperations();
        		   	      }		 
        		       }
                   
        		     while(flag==0)
        		     {
        		   	     if (count<3)
        		   	     {
        		   	    	System.out.println("Enter number of Units");
        				   	     input  = sc.next();
        				   	     
        				   	     if (Validators.isNumber(input, "Units"))
        				   	     {	 
        				   	     units = Integer.parseInt(input); 
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
        		   	    	 adminOperations();
        		   	     }		 
        		     }
                   // updating the units information
                   String upd_unit_stck = "UPDATE current_stock SET `units` = "+units+" WHERE company_id = "+CompID+"";
                   
                   result = txnimp.update(upd_unit_stck);  
                   
                   if(result==1)
                   {
                	   //on success, notify the customer 
                      System.out.println("Updated the Units of shares of the company : "+CompID);
                      displayStock("");
                      adminOperations();
                   }
                   else
                   {   
                	   //on failure, check for the existence of the company Id
            	        String checkcid = "SELECT company_id,status FROM current_stock WHERE `company_id`= "+CompID;     	   
            	         
            	        rs = txnimp.read(checkcid);    			

            	        if(rs.next())
            	        {	     	        	
        				    System.out.println("Looks like there is an issue, the update was not successful. Please reach out to admin");
        				    displayStock("");
        				    adminOperations();
        				    
            	        }
            	        else
            	        {
            		        System.out.println("Company ID is not available, please try again with the correct company ID");
            		        displayStock("");
            		        adminOperations();
            	        }
                   	
                   }

        	 	}
        	 	catch (Exception e)
           	 	{
        		    System.out.println("We have encountered an error, please contact admin");
        	 		adminOperations();

        	 	 }
		         break;
		         
         case "2":
     	 	try
    	 	{
     	 		// to update the pricing information of 
        	 	displayStock("");
        	 	
        	 	//validating inputs before taking it
       		     while(flag==0)
    		     {
    		   	     if (count<3)
    		   	     {
    		   	    	 	System.out.println("Enter the Company ID of the company to change the share price ");
    		   	    	
    				   	     input  = sc.next();
    				   	     
    				   	     if (Validators.isNumber(input, "company ID"))
    				   	     {	 
    				   	     CompID = Integer.parseInt(input); 
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
    		   	    	 adminOperations();
    		   	      }		 
    		       }	
    		    
    		     while(flag==0)
    		     {
    		   	     if (count<3)
    		   	     {
    		   	    	System.out.println("Enter price of each share");
    				   	     input  = sc.next();
    				   	     
    				   	     if (Validators.isDouble(input, "Price"))
    				   	     {	 
    				   	     price = Double.parseDouble(input); 
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
    		   	    	 adminOperations();
    		   	     }		 
    		     }			    				    
    		    // update the price in the current stock table			    
    		    String upd_price_stck = "UPDATE `current_stock` SET `price` = "+price+" WHERE `company_id` = "+CompID;
    		    result = txnimp.update(upd_price_stck);  
    		    
                if(result==1)
                {
                	//on success, print message
                   System.out.println("Updated the Price per share of the company :"+CompID);
                   displayStock("");
                   adminOperations();
                }
                else
                {                   	
                	// on failure, check for the availability of company id
                	String checkcid = "SELECT company_id,status FROM current_stock WHERE `company_id`= "+CompID; 
         	        rs = txnimp.read(checkcid);    			

         	        if(rs.next())
         	        {	     	        	
    				    System.out.println("Looks like there is an issue, the update was not sucessful. Please reach out to admin");
    				    displayStock("");
    				    adminOperations();
    				    
            	    }
         	        else
         	        {   
         	        	System.out.println("Company ID is not available, please try again with the correct company ID");
         		        displayStock("");
         		        adminOperations();
         	        }
                }
            
    	 	}
    	 	catch (Exception e){
     		    System.out.println("We have encountered an error, please contact admin");
     		    adminOperations();
    	 	}
		 break;
         
         default : System.out.println("Incorrect Option Chosen");
        	       adminOperations();
        	       break;
        }

    }
    
    public boolean displayStock(String condition)
    {
    	// show all the current stock information, used by both admin and user to see the real time stock availability and values
    	Scanner sc = new Scanner(System.in);

      	String Stock_display = "SELECT Company_ID,Company_Name,Units,price,Status FROM `current_stock` "+condition;
      	
    	TransactionImpl txnImp = new TransactionImpl();
    	int count=0;
    	ResultSet rs;
    	
    	try 
    	{    		
	    	    rs = txnImp.read(Stock_display);
	    	    
	    		System.out.println("");
	    		System.out.println("Company_ID   Company_Name    Units       Price_Per_Unit    Status");
	    		
		        while(rs.next())
		        {
			         int company_id = rs.getInt(1);
			         String company_name = rs.getString(2);		         	         
			         int units = rs.getInt(3);		     
			         double share_price = rs.getDouble(4);		        			         
			         String status = rs.getString(5);		
			         
			         System.out.printf("%-10d  |%-15s |%-10d |%-15f |%-10s",company_id,company_name,units,share_price,status);
			         System.out.println();
			        
			         count++;
		        }
		        
		        if (count>0) 
		        {
		        	return true;
		        }
		        else
		        {
		        	System.out.println("         *********** No data found************");
		        	System.out.println("");
		        	return false;
		        }
		        
        }
    	catch (Exception e)
    	{
             System.err.println("Got an exception! ");
             System.err.println(e.getMessage());
             adminOperations();
        }
    	 return false;
     }

	@Override
	public void generateStockTrend() 
	{
		// to see the trend of a company's performance in market
		Scanner sc = new Scanner(System.in);
		String date_query = "",final_query="";
		String updated_date="";
		double price_per_unit=0.00;
		ResultSet rs;
		int compID;
		int flag=0,count=0;
		String input="";
			
		displayStock("");
				
	     while(flag==0)
	     {
	   	     if (count<3)
	   	     {
	   			System.out.println("Please provide the company ID for which you would like to see the trend");
			   	     input  = sc.next();
			   	     
			   	     if (Validators.isNumber(input, "company ID"))
			   	     {	 
			   	     compID = Integer.parseInt(input); 
			   	     date_query = " Where company_id="+compID;
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
	   	    	adminOperations();
	   	     }		 
	     }	
		//Please add company ID check in the txn_stocktrend table 
		
		System.out.println("Are you looking for a particular date range? (y/n)");
		String choice=sc.next();
		
		String startDate="",endDate="";
		
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
						//generateStockTrend();
					}
				}
				else
				{		
					System.out.println("Exceed input limit, aborting operation");
					adminOperations();					
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
						//generateStockTrend();
					}
				}
				else
				{	
					System.out.println("Exceed input limit, aborting operation");
					adminOperations();				
				}
			}
			
			date_query = date_query + " and updated_date between '"+startDate+"' and '"+endDate+"'";
		}
		else if (choice.equals("n"))
		{

		}
		else
		{
			System.out.println("Please make proper choice");
			generateStockTrend();
		}
		
      	final_query = "SELECT updated_date,price_per_unit FROM `txn_stocktrend`"+date_query+" order by updated_date";
    	               System.out.println("updated_date            price_per_unit	       Trend");
        
    	try 
    	{    	
	    	
	    	rs = txnimp.read(final_query);
	        while(rs.next())
	        {
	        	updated_date = rs.getString(1);
		        System.out.printf("%-20s  ",updated_date);
		        
		        if (price_per_unit<rs.getDouble(2))
		        {
			        price_per_unit = rs.getDouble(2);		         
			        System.out.printf("| %-10.4f |",price_per_unit);
			        System.out.printf(" %15s","Raise");
			        System.out.println();
		        }
		        else if (price_per_unit==rs.getDouble(2))
		        {
			        price_per_unit = rs.getDouble(2);		         
			        System.out.printf("| %-10.4f |",price_per_unit);
			        System.out.printf(" %15s","Stable");
		            System.out.println();
		        }  
		        else if (price_per_unit>rs.getDouble(2))
		        {
			        price_per_unit = rs.getDouble(2);		         
			        System.out.printf("| %-10.4f |",price_per_unit);
		        	System.out.printf(" %15s","Drop");
		        	System.out.println();
		        }  
		        
	        }
        }
    	catch (Exception e)
    	{
             System.err.println("Got an exception! ");
             System.err.println(e.getMessage());
             adminOperations();
        }
		
	}

	@Override
	public void generateEarningReport() {
		// to see how much is the firm benefited by the transactions
		Scanner sc = new Scanner(System.in);

		String date_query = "";
		String Transaction_date="";
		double amount=0.00,charges=0.00,stt=0.00;
		String final_query="";
		ResultSet rs;
		String startDate="",endDate="";
		int count=0,flag=0;
		
		System.out.println("Are you looking for a particular date range? (y/n)");
		
		String choice=sc.next();
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
						//generateStockTrend();
					}
				}
				else
				{		
					System.out.println("Exceed input limit, aborting operation");
					adminOperations();					
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
						//generateStockTrend();
					}
				}
				else
				{	
					System.out.println("Exceed input limit, aborting operation");
					adminOperations();				
				}
			}
			
			date_query = " where date between '"+startDate+"' and '"+endDate+"'";
		}
		else if (choice.equals("n"))
		{
			date_query = "";
		}
		else
		{
			System.out.println("Please make proper choice");
			generateEarningReport();
		}
		
    	try 
    	{   
		
      	final_query = "SELECT date,round(stock_price,2),round(txn_charge,2),round(stt,2),round(total_charge,2) FROM `txn_charges` "+date_query+" order by date";

    	System.out.println("Transaction_date         Transaction_amount	      Firm_Charges	        Securities_Transfer_Tax 	");
        	
	    	rs = txnimp.read(final_query);
	        while(rs.next())
	        {
	        	Transaction_date = rs.getString(1);      
		        
		        amount = rs.getDouble(2);		         
		       		        
		        charges = rs.getDouble(3);		         
		        		        
		        stt = rs.getDouble(4);		         
		       	
		        System.out.printf("%15s | %20.2f | %20.2f | %20.2f ",Transaction_date,amount,charges,stt);
		        System.out.println();
	        }
	        
	        rs.close();	        
	        
	        final_query = "SELECT round(sum(stock_price),2),round(sum(txn_charge),2),round(sum(stt),2),round(sum(total_charge),2) FROM `txn_charges` "+date_query+" order by date";
	        	
		    	rs = txnimp.read(final_query);
		        while(rs.next())
		        {

			        System.out.printf("%18s  |","Total");		       
			        
			        amount = rs.getDouble(2);		         
			        System.out.printf("%20.2f  |",amount);	
			        
			        charges = rs.getDouble(3);		         
			        System.out.printf("%20.2f  |",charges);	
			        
			        stt = rs.getDouble(4);		         
			        System.out.printf("%24.2f",stt);	
			        
		        }
        }
    	catch (Exception e)
    	{
             System.err.println("Got an exception! ");
             e.printStackTrace();
             adminOperations();
        }
	}
    
}