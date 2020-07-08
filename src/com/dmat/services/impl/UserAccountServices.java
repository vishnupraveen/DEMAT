package com.dmat.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dmat.Validators;
import com.dmat.datasource.api.Transaction;
import com.dmat.datasource.impl.TransactionImpl;
import com.dmat.services.api.IUserAccountServices;
import com.dmat.services.api.UserService;
import com.dmat.views.impl.UserLoginImpl;

@SuppressWarnings("resource")
public class UserAccountServices  implements IUserAccountServices
{
	
    public boolean depositAmount() {
    	UserService userClass = new UserServiceImpl();
    	Scanner sc = new Scanner(System.in);  
    	
    	UserLoginImpl al = UserLoginImpl.getInstance();	
    	int customerID = al.customerID;
    	int count=0,flag=0;
        double amount=0;
        ResultSet rs;
                
	     while(flag==0)
	     {
	   	     if (count<3)
	   	     {
	   	        System.out.println("Amount to be deposit: ");
	   	        	 String input = sc.next();
			   	     
			   	     if (Validators.isDouble(input, "Price"))
			   	     {	 
				   	     amount = Double.parseDouble(input); 
				   	     
				         while(amount <= 0)
				         {
				         	if (count<3)
				         	{
				 	        	count++;
				 	            System.out.println("Deposit value cannot " + amount +", please enter the correct amount");
				 	            amount = sc.nextDouble();  
				         	}
				         	else
				         	{
				         		System.out.println("Exceeded input limit, aborting operation");
				         		userClass.UserOperations();
				         		return false;
				         	}
				             
				         }
				   	     
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
	   	    	userClass.UserOperations();;
	   	     }		 
	     }
          
        	Transaction txnimp = new TransactionImpl();

             String add_deposit = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`deposit`,`balance`) VALUES "+
               					 "("+customerID+",now(),'Deposit',"+amount+",(select a.bal from (select balance+"+amount+" as bal from txn_account where customer_id='"+customerID+"' order by date desc limit 1) as a ))";
                
             int result = txnimp.update(add_deposit);

			try 
			{
	             if (result==1)
	             {           	             	             	 
	            	 String getBalance = "SELECT * FROM txn_account WHERE customer_id= "+customerID+" order by date desc limit 1";

	                 rs= txnimp.read(getBalance);	
	                 double balance=0;
	                 
	                 while(rs.next())
	                 {
	                 balance = rs.getDouble("balance");
	                 }
	                 System.out.println("Deposit sucessful");
	                 System.out.println("Current Balance is  "+balance);
	                 
	                 userClass.UserOperations();
	             }
	             else
	             {
	            	 System.out.println("Deposit not sucessful, please try again (or) check with the admin");
	            	 userClass.UserOperations();
	             }
             
			} 
			catch (SQLException e) 
			{
				System.out.println("Deposit not sucessful");
				e.printStackTrace();
				userClass.UserOperations();
			}
			finally{
				userClass.UserOperations();
			}
            
        return false;
     }

    public boolean withdrawAmount() {
    	UserService userClass = new UserServiceImpl();
    	Scanner sc = new Scanner(System.in);  
    	
    	UserLoginImpl al = UserLoginImpl.getInstance();	
    	int customerID = al.customerID;
    	int flag = 0,count = 0;
    	
        double amount=0,balance=0;
            
	     while(flag==0)
	     {
	   	     if (count<3)
	   	     {
	   	    	System.out.println("Amount to be withdrawn: ");
			   	     String input  = sc.next();
			   	     
			   	     if (Validators.isDouble(input, "Price"))
			   	     {	 
			   	    	amount = Double.parseDouble(input); 
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
        
        if(amount<500)
        {
            System.out.println("Minimum amount to Withdraw is 500 , please try again!");
            return false;
        }

        String getBalance= "SELECT balance FROM txn_account WHERE customer_id= "+customerID+" order by date desc limit 1";
        TransactionImpl txnimp = new TransactionImpl();
        try
        {
            ResultSet rs;
            rs = txnimp.read(getBalance);
            
            while(rs.next())
            {
            balance =rs.getDouble("balance");
            }
            
            if(amount > (balance-1000))// if necessary we can also add an additional condition for minimum withdrawal amount
            {
                System.out.println("Insufficient Funds");
                userClass.UserOperations();
                return false;
            }
            else
            {
                String subtract_deposit = "INSERT INTO `txn_account` (`customer_id`,`date`,`type`,`withdrawl`,`balance`) VALUES "+
   					 "("+customerID+",now(),'Withdraw',"+amount+",(select a.bal from (select balance-"+amount+" as bal from txn_account where customer_id='"+customerID+"' order by date desc limit 1) as a))";

                int result = txnimp.update(subtract_deposit);

                if(result==1)
                {                                 
                    System.out.println(" Withdrawl Successful");
                    System.out.println(" Current Balance is  "+(balance-amount));
                    userClass.UserOperations();
                }
                else 
                {
                    System.out.println("Withdrawl not sucessfull, please try again (or) check with the admin");
                    userClass.UserOperations();
                }
            }
        }
        catch(Exception e)
        {
             System.out.println("Withdrawl not sucessfull");
			 e.printStackTrace();
			 userClass.UserOperations();
        }
            
        return true;
       
     }

}
