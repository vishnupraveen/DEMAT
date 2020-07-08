package com.dmat.services.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import com.dmat.Validators;
import com.dmat.datasource.impl.TransactionImpl;
import com.dmat.services.api.IUserTransactionServices;
import com.dmat.views.impl.UserLoginImpl;

@SuppressWarnings("resource")
public class UserTransactionServices implements IUserTransactionServices{

	double charges=0;
	double t_stt=0;
	double min_charges=0;
	int commission_flag=0;
	int firmID=122345;

    public boolean buyShare() 
    {
    	// codes to buys share
    	
    	Scanner sc = new Scanner(System.in);  
    	TransactionImpl txnimp = new TransactionImpl();
    	UserServiceImpl userClass = new UserServiceImpl();
    	
    	UserLoginImpl al = UserLoginImpl.getInstance();	
    	int customerID = al.customerID;
        
    	// Declarations
    	AdminServiceImpl as = new AdminServiceImpl();
    	as.displayStock(" Where status='Active'");
    	ResultSet rs;

        double currentPrice=0;
        String status="",companyName="",input="";
    	
        //Initializations
    	int companyId=0,flag=0,count=0,units=0;
    
    	//validating the inputs
	     while(flag==0)
	     {
	   	     if (count<3)
	   	     {
	   	    	 	System.out.println("Please provide the company id you would like to buys stock from the above list");
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
	   	    	 userClass.UserOperations();
	   	     }		 
	     }	
    	
	     while(flag==0)
	     {
	   	     if (count<3)
	   	     {
	   	    	System.out.println("Please provide the number of units you would like to buy");
			   	     input  = sc.next();
			   	     
			   	     if (Validators.isNumber(input, "company ID"))
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
	   	    	 userClass.UserOperations();
	   	     }		 
	     }	
    	
    	// Query for the available stocks
    	
        String availableShares= "SELECT company_id, units, price, status, company_name FROM current_stock WHERE company_id="+companyId+" and status='Active'";
        
        // Query for the available balance
        String getBalance= "SELECT balance FROM txn_account WHERE customer_id="+customerID+" order by date desc limit 1";
        
        try
        {   
        	 // import the results of the available balance
	         rs= txnimp.read(getBalance);	
	         double balance=0;
	         
	         while(rs.next())
	         {
	        	 balance = rs.getDouble("balance");
	         }
         
	        // import the results of the available stock 
            rs= txnimp.read(availableShares);
            
            while(rs.next())
            {
                if(units<=rs.getInt("units"))
                {
					int avbUnits=rs.getInt("units");
                    currentPrice=rs.getDouble("price");
                    status=rs.getString("status");
                    companyName=rs.getString("company_name");
                }
                else
                {
                	System.out.println("We have only "+rs.getInt("units")+" Units availalbe, aborting operation");
                	userClass.UserOperations();
                }
            }
                  
            // import the commission/charges details
            if (commission_flag==0)
            {
            	commission();
            }
            
            // calculate the commission
            double txnCharge=(units*charges*currentPrice)/100;
            double costPrice=units*currentPrice;//variables must be set here
            double stt=(units*t_stt*currentPrice)/100;//variables must be set here                    
                          
            if(txnCharge<min_charges)
            {
                  	txnCharge=min_charges;
            }
                    
            double totalCharges = txnCharge+stt;
            double totalAmt = txnCharge + (units*currentPrice) + stt;
            
            //check if we have sufficient funds to make this transactions.
            //1000 rs is the minimum balance required on the account
            
            if(totalAmt > (balance-1000))
            {
                 System.out.println("Insufficient Funds, please add some money into you account");
                 userClass.UserOperations();
                 return false;
            }
                    	
            ArrayList<String> queries = new ArrayList<String>();  
            
            //Update the current stock information

            String update_current_stock="UPDATE `test`.`current_stock` SET `units` = units-"+units+" WHERE `company_ID` = "+companyId+" and status='Active'";
            
            // add new record in the transaction stock table, for the customer
            String updateUserStock="INSERT INTO `test`.`txn_stock` (`customer_id`,`company_id`,`type`,`units`,`cost_price`,`date`) VALUES ("+customerID+","+companyId+",'Buy',"+units+","+costPrice+",now())";
             
            // add new record in the account table with the widthdrawl information, in favour of customer
            String updateUserBalance = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
              					 "("+customerID+",now(),'Withdraw',"+totalAmt+",(select a.bal from (select balance-"+totalAmt+" as bal from txn_account where customer_id='"+customerID+"' order by date desc limit 1) as a))";
            
            // add new record in the account table, in favour of firm
            String updateFirmBalance_money = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
              					 "("+firmID+",now(),'Deposit',"+costPrice+",(select a.bal from (select balance+"+costPrice+" as bal from txn_account where customer_id='"+firmID+"' order by date desc limit 1) as a ))";
            
            // add new record into account table, to add the charges applied on the transaction, in favour of firm
            String updateFirmBalance_commission = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
              					 "("+firmID+",now(),'Deposit',"+totalCharges+",(select a.bal from (select balance+"+totalCharges+" as bal from txn_account where customer_id='"+firmID+"' order by date desc limit 1) as a ))";
            
            // add new record of charges in the table, this is a record of earning firm has made with each transaction
            String add_newCharges_record="Insert INTO txn_charges (`tranx_id`,`stock_price`, `txn_charge`, `stt`, `total_charge`, date ) values "+
              					 "((SELECT tranx_id FROM txn_stock WHERE customer_id ="+customerID+" ORDER BY date DESC LIMIT 1),"+costPrice+","+txnCharge+","+stt+","+totalCharges+",now())";

            // add them all in an arraylist
            queries.add(update_current_stock);
            queries.add(updateUserStock);
            queries.add(updateUserBalance);
            queries.add(updateFirmBalance_money);	
            queries.add(updateFirmBalance_commission);
            queries.add(add_newCharges_record);                     
                    
            //run the query and fetch the result
            int result = txnimp.update(queries);
                        
            if (result==1)
            {	
            	// print success message
            	 System.out.println("Transaction sucessfull");
            	 userClass.UserOperations(); 	
            }
            else
            {
                 System.out.println("Please note the transaction was not complete due to some reason, kindly contact the admin");
                 userClass.UserOperations();
            }
            
        }

        catch(Exception e)
        {
              System.out.println("Transaction Unsuccessful, exception caught. Aborting operation");
              e.printStackTrace();
              userClass.UserOperations();
        }
        
        return false;

    }

    public boolean sellShare() 
    
    {
    	//sell the share user has
    	Scanner sc = new Scanner(System.in);  
    	TransactionImpl txnimp = new TransactionImpl();
    	UserServiceImpl userClass = new UserServiceImpl();
    	
    	UserLoginImpl al = UserLoginImpl.getInstance();	
    	int customerID = al.customerID;
    	
        ResultSet rs;
    	int units = 0,Company_ID=0,count = 0,avbUnits = 0,flag=0;
    	double currentPrice=0;
    	String input;
    	
       try
        {          
    	   
    	   //display all the shares he currently has with the fresh prices
            
    	    String availalbe_shares= "SELECT a.company_id as cid,b.company_name as cname,sum(if(a.type='buy',a.units,0)) - sum(if(a.type='sell',a.units,0)) as availalbe_units,c.price as current_price"+ 
						" FROM txn_stock as a, mst_company as b, current_stock as c where a.customer_id='"+customerID+"' and a.company_id=b.company_id and a.company_id=c.company_id group by a.company_id,b.company_name having availalbe_units>0";

            rs= txnimp.read(availalbe_shares);
            System.out.println();
		    System.out.println("CompanyId   	  company Name	  	Units      current_price");
	
				while(rs.next())
			    {
					int companyID = rs.getInt(1);
					String companyName = rs.getString(2);
					units = rs.getInt(3);
					double price = rs.getDouble(4);
					
					System.out.printf("%-15d  |%-20s |%-10d |%-10f ",companyID,companyName,units,price);
			        System.out.println();
					count++;
			    }
            
            if (count==0)
            {
            	System.out.println("No stocks availalbe in your transaction account, aborting operation");
            	userClass.UserOperations();            	
            }
            else
            {
            	// validating inputs
            	 count=0;
	       	     while(flag==0)
	    	     {
	    	   	     if (count<3)
	    	   	     {
	    	   	    	 	System.out.println("Please provide the company id you would like to sell stock from the above list");
	    			   	     input  = sc.next();
	    			   	     
	    			   	     if (Validators.isNumber(input, "company ID"))
	    			   	     {	 
	    			   	     Company_ID = Integer.parseInt(input); 
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
	    	   	    	 userClass.UserOperations();
	    	   	     }		 
	    	     }	
	        	
	    	     while(flag==0)
	    	     {
	    	   	     if (count<3)
	    	   	     {
	    	   	    	System.out.println("Please provide the number of units you would like to sell");
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
	    	   	    	 userClass.UserOperations();
	    	   	     }		 
	    	     }
	    	     
	            count = 0;
            	
	            // check if the company is available
	    	    availalbe_shares= "SELECT a.company_id as cid,b.company_name as cname,sum(if(a.type='buy',a.units,0)) - sum(if(a.type='sell',a.units,0)) as availalbe_units,c.price as current_price"+ 
							" FROM txn_stock as a, mst_company as b, current_stock as c where a.customer_id='"+customerID+"' and a.company_id='"+Company_ID+"' and a.company_id=b.company_id and a.company_id=c.company_id group by a.company_id,b.company_name having availalbe_units>0";

                rs= txnimp.read(availalbe_shares);

                if(rs.next())
                {
	                avbUnits=rs.getInt("availalbe_units");
	                count++;
	                System.out.println(avbUnits);
                }
                
                if (count==0)
                {
                	System.out.println("There are no shares availalbe with company id:"+Company_ID+", aborting operation");
                	userClass.UserOperations();     
                }
                else
                {
                	// check if we have sufficent shares with that company
                	if (avbUnits<units)
                	{
                    	System.out.println("We have only "+avbUnits+" shares with company id:"+Company_ID+", aborting operation");
                    	userClass.UserOperations();   
                	}
                }                
                
            }	
            
            String current_price = "SELECT * FROM test.current_stock where company_ID='"+Company_ID+"'";          
            rs = txnimp.read(current_price);
            
            while(rs.next())
            {
            	currentPrice = rs.getDouble("price");
            }
            		
            double sellPrice=units*currentPrice;
            double txnCharge=(units*charges*currentPrice)/100;
            double stt=(units*t_stt*currentPrice)/100;                    
            
            if(txnCharge<min_charges)
            {
            	txnCharge=min_charges;
            }
            
            double totalCharges = txnCharge+stt;
            double totalAmt = txnCharge + (units*currentPrice) + stt;
                        
        	ArrayList<String> queries = new ArrayList<String>();                    	                    	
        	// update current stock table with new share values
            String update_current_stock="UPDATE `test`.`current_stock` SET `units` = units+"+units+" WHERE `company_ID` = "+Company_ID;
           
            // insert new record in transaction stock table with the transaction values
            String updateUserStock="INSERT INTO `test`.`txn_stock` (`customer_id`,`company_id`,`type`,`units`,`selling_price`,`date`) VALUES ("+customerID+","+Company_ID+",'Sell',"+units+","+sellPrice+",now())";
            
            // insert new record in account table for Firm's id indicating withdrawl
            String updateFirmBalance_money = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
   					 "("+firmID+",now(),'Withdraw',"+sellPrice+",(select a.bal from (select balance-"+sellPrice+" as bal from txn_account where customer_id='"+firmID+"' order by date desc limit 1) as a ))";
            
            //inserting new record in account table for user's id indicating deposit
            String updateUserBalance = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
  					 "("+customerID+",now(),'Deposit',"+sellPrice+",(select a.bal from (select balance+"+sellPrice+" as bal from txn_account where customer_id='"+customerID+"' order by date desc limit 1) as a))";
            
            // inserting new record in account table for user's id indicating deduction of commission
            String updateUserBalance_commission = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`withdrawl`,`balance`) VALUES "+
  					 "("+customerID+",now(),'Withdraw',"+totalCharges+",(select a.bal from (select balance+"+totalCharges+" as bal from txn_account where customer_id='"+customerID+"' order by date desc limit 1) as a))";

            // insert new record in account table for Firm's id indicating deposit of commission
            String updateFirmBalance_commission = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
  					 "("+firmID+",now(),'Deposit',"+totalCharges+",(select a.bal from (select balance+"+totalCharges+" as bal from txn_account where customer_id='"+firmID+"' order by date desc limit 1) as a ))";
            
            // insert new record in transaction charges table with all the charges information
            String add_newCharges_record="Insert INTO txn_charges (`tranx_id`,`stock_price`, `txn_charge`, `stt`, `total_charge`, date ) values "+
  					 "((SELECT tranx_id FROM txn_stock WHERE customer_id ="+customerID+" ORDER BY date DESC LIMIT 1),"+sellPrice+","+txnCharge+","+stt+","+totalCharges+",now())";

            
            //adding all of them into an arrayList and sending it to execution
            queries.add(update_current_stock);
            queries.add(updateUserStock);            
            queries.add(updateFirmBalance_money);
            queries.add(updateUserBalance);
            queries.add(updateUserBalance_commission);
            queries.add(updateFirmBalance_commission);
            queries.add(add_newCharges_record);                     
            
            int result = txnimp.update(queries);
            
            if (result!=0)
            {
            	System.out.println("Transaction sucessfull");
            	userClass.UserOperations();
            }
            else
            {
            	System.out.println("please note the transaction was not complete due to some reason, kindly contact the admin");
            	userClass.UserOperations();
            }

            return true;            

        }
        catch (Exception e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return false;
    }
        
    public void commission() {
    	
    	// fetching the commission details for the DB
    	Scanner sc = new Scanner(System.in);  
    	TransactionImpl txnimp = new TransactionImpl();
    	UserServiceImpl userClass = new UserServiceImpl();
    	
    	UserLoginImpl al = UserLoginImpl.getInstance();	
    	int customerID = al.customerID;
    	
        String commission = "SELECT * FROM charges";
        ResultSet rs = txnimp.read(commission);
        
        try 
        { 
	        while(rs.next())
	         {
	        		charges = rs.getDouble(1);
	        		t_stt = rs.getDouble(2);
	        		min_charges = rs.getDouble(3);
	        		commission_flag = 1;
	         }
        }
        
        catch(Exception e)
        {
			 e.printStackTrace();
        }
    }

}
