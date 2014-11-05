package com.timeperformance.apiclient;

/**
 * Exception raised when the request fails
 */
public class APIException extends Exception {
    
    private int responseCode;
    
    private String errorMessage;
    
    public APIException(int responseCode, String errorMessage) {
        super();
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String getMessage() {
        return "Request failed with code " + responseCode + " message: " + errorMessage;
    }
    
    public int getResponseCode() {
        return responseCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
}
