package com.dmat.pojo;

public class Admin {
	
    private final static int AdminID = 1234;
    private final static char[] password = {'a','d','m','i','n'};
    
    public static int getAdminID()
	{
		return AdminID;
	}
	public static char[] getPassword()
	{
		return password;
	}

}
