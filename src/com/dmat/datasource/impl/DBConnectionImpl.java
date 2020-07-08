package com.dmat.datasource.impl;
import java.sql.*;


import com.dmat.datasource.api.DbConnection;

public class DBConnectionImpl implements DbConnection{
	
	//---> single ton pattern---- Eager Instantiation
    private static Connection con;
    
    
    //private constructor to enable single instance creation 
    private DBConnectionImpl() {
        
    }

    //importing config details
    
    
    // instance to create the connection
	public static Connection getCon()
	{		    
	    
		try
		{
			if(con==null) 
			{
				//defining the driver and connection string
				Class.forName("com.mysql.cj.jdbc.Driver");  
				con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","atlas","atlas"); 
				
				//setting autocommit to false
				con.setAutoCommit(false);
			}
			
		}
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		
		return con;				
	}

	// instance to kill the connection
	public static void killCon()
	{
		// if con is still available close it
		if (con!=null)
		{
			try 
			{
				con.close();
				System.out.println("Closing connections, terminating application");
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				// TODO Auto-generated catch block
			}
		}
	}
}
