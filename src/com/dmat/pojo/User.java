package com.dmat.pojo;

import java.util.List;
import java.util.Map;

public class User {

    private String fname,lname,status;
    public int userID;
    private long accountID;
    private String email;
    private double amount;
    private long phoneNumber;
   

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User(int customerID) 
    {
    	this.userID = customerID;
    }
    
    public User() 
    {
    	
    }
    
	public long getPhone() {
        return phoneNumber;
    }

    public void setPhone(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public void setStatus(String status) {
		this.status = status;
		
	}
	
	public String getStatus() {
		return status;
		
	}



}
