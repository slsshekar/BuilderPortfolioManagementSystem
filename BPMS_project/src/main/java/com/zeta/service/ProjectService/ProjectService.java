package com.zeta.service.ProjectService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.*;
import com.zeta.service.FileService.FileService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zeta.service.utility.Utility;

import java.util.*;

public class ProjectService {
    private static final String FILE_NAME = "database/projects.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Map<String, Project> projectHashMap=new HashMap<>();
    Map<String, User> userMap=new HashMap<>();

    public boolean create(Project project,String clientName) throws ProjectAlreadyExistsException, ClientDoesNotExistException {
        projectHashMap=FileService.loadFromFile(FILE_NAME,mapper, Project.class);
        userMap=FileService.loadFromFile("database/users.json",mapper, User.class);
        Utility.validateInput(project.getName(),"project name");
        Utility.validateInput(project.getDescription(),"project description" );
        Utility.validateInput(clientName,"client name");
        if(!userMap.containsKey(clientName)){
            throw new ClientDoesNotExistException(clientName+" doesnt exist");
        }
        if(projectHashMap.containsKey(project.getName())){
            throw new ProjectAlreadyExistsException(project.getName()+" already exists!");
        }
        project.setClientName(clientName);
        projectHashMap.put(project.getName(),project);
        FileService.saveToFile(projectHashMap,FILE_NAME,mapper);
        return projectHashMap.containsKey(project.getName());

    }

    public boolean approve(String projectName){
        projectHashMap=FileService.loadFromFile(FILE_NAME,mapper, Project.class);
        userMap=FileService.loadFromFile("database/users.json",mapper, User.class);
        if(!projectHashMap.containsKey(projectName)){
            throw new IllegalArgumentException("project does not exist");
        }
        Project project=projectHashMap.get(projectName);
        project.setStatus(STATUS.UPCOMING);
        FileService.saveToFile(projectHashMap,FILE_NAME,mapper);
        return true;
    }
    public void assignManager(String projectName, String managerName) throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        userMap = FileService.loadFromFile("database/users.json", mapper, User.class);
        Utility.validateInput(projectName, "project name");
        Utility.validateInput(managerName, "manager name");
        if (!projectHashMap.containsKey(projectName)) {
            throw new ProjectDoestNotExistException(projectName + " doesnt exist");
        }
        Project project=projectHashMap.get(projectName);
        User manager;
        if (!userMap.containsKey(managerName)) {
            throw new UserNotFoundException(managerName + " manager does not exist");
        } else {
            manager = userMap.get(managerName);
            if (manager.getRole() != ROLE.MANAGER) {
                throw new RoleMismatchException(managerName + " is not a manager");
            }
        }
        Set<String> list= project.getManagerList();
        list.add(managerName);
        project.setManagerList(list);
//        manager.getManagerList
        FileService.saveToFile(projectHashMap,FILE_NAME,mapper);
        FileService.saveToFile(userMap,"database/users.json",mapper);


    }
    public List<Project> getProjectsByClientId(int clientId){
        return new ArrayList<>();
    }
    public List<Project> getProjectsByManagerId(int managerId){
        return new ArrayList<>();
    }
//    public Project getProjectById(int id){
//        return new Project();
//    }
}
