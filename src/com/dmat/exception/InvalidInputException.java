package com.dmat.exception;

public class InvalidInputException extends Throwable {
	
	private String reason;
	
	public InvalidInputException(String reason)
    {
        super("InvalidInputException - " + reason);
        this.reason = reason;
    }
    
    /**
     * Prevent use of no parameter constructor.
     */
    private InvalidInputException()
    {        
    }

    /**
     * Gets the reason for the exception.
     * @return String
     */
    public String getReason()
    {
        return "InvalidInputException::getReason() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

    /**
     * Override default method.
     * @return String
     */
    @Override
    public String toString()
    {
        return "InvalidInputException::toString() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

}
