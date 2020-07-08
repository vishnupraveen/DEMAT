package com.dmat.exception;

public class InvalidCompanyIDException extends Throwable {
	
	private String reason;
	
	public InvalidCompanyIDException(String reason)
    {
        super("MyException - " + reason);
        this.reason = reason;
    }
    
    /**
     * Prevent use of no parameter constructor.
     */
    private InvalidCompanyIDException()
    {        
    }

    /**
     * Gets the reason for the exception.
     * @return String
     */
    public String getReason()
    {
        return "MyException::getReason() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

    /**
     * Override default method.
     * @return String
     */
    @Override
    public String toString()
    {
        return "MyException::toString() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

}