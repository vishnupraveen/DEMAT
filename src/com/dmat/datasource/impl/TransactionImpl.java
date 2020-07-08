package com.dmat.datasource.impl;

import com.dmat.datasource.api.DbConnection;
import com.dmat.datasource.api.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionImpl implements DbConnection,Transaction {

    public ResultSet read(String query)
    {
    	// method to read the out put of the query
    	ResultSet rs;
    	
		try 
		{	
			// calling the connections and preparing the statement		
			Connection con = DBConnectionImpl.getCon();	
			PreparedStatement stmt = con.prepareStatement(query);		
			
			// storing the results of the execution and returning the same
			rs = stmt.executeQuery(); 			
					
			return rs;
		}
		
		catch (SQLException e) {
			e.toString();
			
			// return null on failure of execution
	    	return null;
		} 

    }

    public int update(String query)
    {
    	// method execute the query where we can update or insert a record
    	int result=0;
    	
    	// calling the connections
		Connection con = DBConnectionImpl.getCon();	
    	
		try 
		{			
			//preparing the statement and executing	
			PreparedStatement stmt = con.prepareStatement(query);			
			result = stmt.executeUpdate(); 		
			
			//commit the changes on success
			con.commit();
			return result;
		}
		
		catch (SQLException e) 
		{
			//rollback the updates on failure
			try 
			{
				if(con != null)
					con.rollback();
			} catch (SQLException e1) {

			}
			e.toString();	
			
			//return zero indicating the update was not sucessful
			return result;
		}  	
		
    }
    
    public int update(ArrayList<String> query )
    {
    	// method execute the query where we can update or insert a record
    	int result=0;
    	
    	// calling the connections
    	Connection con = DBConnectionImpl.getCon();	
    	
		try 
		{				
			Iterator<String> itr = query.iterator(); 
			
			while(itr.hasNext())	
			{
				//preparing the statement and executing	them in loop
				//this method is used with buying or selling shares, all the relevant information has to be update
				//hence running all the queries in a loop, even if one execution fail, we are rolling back the changes
				PreparedStatement stmt = con.prepareStatement(itr.next().toString());	
				result = stmt.executeUpdate(); 	
				
				if (result==0)
				{
					con.rollback();
					return 0;					
				}
			}
		
			//commit the changes on success
			con.commit();
			return result;
		}
		
		catch (SQLException e) 
		{
			e.toString();
			e.printStackTrace();
			//rollback the updates on failure
			try 
			{
				if(con != null)
					con.rollback();
			} catch (SQLException e1) {

			}
			
			//return zero indicating the update was not sucessful
			return result;
		}
    }

}
