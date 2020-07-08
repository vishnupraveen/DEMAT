package com.dmat.exception;

public class NoAvailableSharesException extends Throwable {
	
	private String reason;
	
	public NoAvailableSharesException(String reason)
    {
        super("NoAvailableShares - " + reason);
        this.reason = reason;
    }
    
    /**
     * Prevent use of no parameter constructor.
     */
    private NoAvailableSharesException()
    {        
    }

    /**
     * Gets the reason for the exception.
     * @return String
     */
    public String getReason()
    {
        return "NoAvailableSharesException::getReason() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

    /**
     * Override default method.
     * @return String
     */
    @Override
    public String toString()
    {
        return "NoAvailableSharesException::toString() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

}
