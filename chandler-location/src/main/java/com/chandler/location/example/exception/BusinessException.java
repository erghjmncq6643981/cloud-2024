package com.chandler.location.example.exception;


/**
 * @author hj
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -9046217168571190648L;

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(Throwable cause){
        super(cause);
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
