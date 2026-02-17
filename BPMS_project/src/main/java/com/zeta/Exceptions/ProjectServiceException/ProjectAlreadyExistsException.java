package com.zeta.Exceptions.ProjectServiceException;

public class ProjectAlreadyExistsException extends Exception{
    public ProjectAlreadyExistsException(String message){
        super(message);
    }
}
