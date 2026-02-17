package com.zeta.Exceptions.ProjectServiceException;

public class ClientDoesNotExistException extends Exception{
    public ClientDoesNotExistException(String message){
        super(message);
    }
}
