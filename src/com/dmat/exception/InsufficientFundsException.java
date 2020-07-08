package com.dmat.exception;

public class InsufficientFundsException extends Throwable{
	private String reason;
	public InsufficientFundsException(String reason)
    {
        super("MyException - " + reason);
        this.reason = reason;
    }
    
    /**
     * Prevent use of no parameter constructor.
     */
    private InsufficientFundsException()
    {        
    }

    /**
     * Gets the reason for the exception.
     * @return String
     */
    public String getReason()
    {
        return "InsufficientFundsException::getReason() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }

    /**
     * Override default method.
     * @return String
     */
    @Override
    public String toString()
    {
        return "InsufficientFundsException::toString() - " + reason; // Prefixed with the method name to understand how printStackTrace() works.
    }
}