package com.zeta.Exceptions.ProjectServiceException;

public class ProjectDoestNotExistException extends Exception {
    public ProjectDoestNotExistException(String message) {
        super(message);
    }
}
