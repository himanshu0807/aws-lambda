package com.aws.lambda.model;

public class ErrorDTO<T> {
    
    private String errorCode;
    private String errorMessage;
    private T response;
    
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public T getResponse() {
        return response;
    }
    public void setResponse(T response) {
        this.response = response;
    }
    @Override
    public String toString() {
        return "Error [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", response=" + response + "]";
    }
}
