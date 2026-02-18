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

import java.time.LocalDate;
import java.util.*;

public class ProjectService {
    private static final String FILE_NAME = "database/projects.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Map<String, Project> projectHashMap = new HashMap<>();
    Map<String, User> userMap = new HashMap<>();

    public boolean create(Project project, String clientName)
            throws ProjectAlreadyExistsException, ClientDoesNotExistException {
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        userMap = FileService.loadFromFile("database/users.json", mapper, User.class);
        Utility.validateInput(project.getName(), "project name");
        Utility.validateInput(project.getDescription(), "project description");
        Utility.validateInput(clientName, "client name");
        if (!userMap.containsKey(clientName)) {
            throw new ClientDoesNotExistException(clientName + " client does not exist");
        }
        if (projectHashMap.containsKey(project.getName())) {
            throw new ProjectAlreadyExistsException(project.getName() + " project already exists!");
        }
        project.setClientName(clientName);
        projectHashMap.put(project.getName(), project);
        Client client = (Client) userMap.get(clientName);
        client.getProjectList().add(project.getName());
        FileService.saveToFile(projectHashMap, FILE_NAME, mapper);
        FileService.saveToFile(userMap, "database/users.json", mapper);
        return projectHashMap.containsKey(project.getName());
    }

    public boolean approve(String projectName, LocalDate startDate, LocalDate endDate)
            throws ProjectDoestNotExistException {
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        Utility.validateInput(projectName, "project name");
        if (!projectHashMap.containsKey(projectName)) {
            throw new ProjectDoestNotExistException(projectName + " project does not exist");
        }
        Project project = projectHashMap.get(projectName);
        project.setStatus(STATUS.UPCOMING);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        FileService.saveToFile(projectHashMap, FILE_NAME, mapper);
        return true;
    }

    public boolean assignManager(String projectName, String managerName)
            throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        userMap = FileService.loadFromFile("database/users.json", mapper, User.class);
        Utility.validateInput(projectName, "project name");
        Utility.validateInput(managerName, "manager name");
        if (!projectHashMap.containsKey(projectName)) {
            throw new ProjectDoestNotExistException(projectName + " project does not exist");
        }
        Project project = projectHashMap.get(projectName);
        if (!userMap.containsKey(managerName)) {
            throw new UserNotFoundException(managerName + " manager does not exist");
        }
        User user = userMap.get(managerName);
        if (user.getRole() != ROLE.MANAGER) {
            throw new RoleMismatchException(managerName + " is not a Manager");
        }
        Manager manager = (Manager) user;
        project.getManagerList().add(managerName);
        manager.getProjectList().add(projectName);
        FileService.saveToFile(projectHashMap, FILE_NAME, mapper);
        FileService.saveToFile(userMap, "database/users.json", mapper);
        return true;

    }

    public Set<String> getProjectsByClientName(String clientName) throws UserNotFoundException {
        userMap = FileService.loadFromFile("database/users.json", mapper, User.class);
        Client client;
        Utility.validateInput(clientName, "client name");
        if (!userMap.containsKey(clientName)) {
            throw new UserNotFoundException(clientName + " client does not exist");
        }
        client = (Client) userMap.get(clientName);
        return client.getProjectList();

    }

    public Set<String> getProjectsByManagerName(String managerName) throws UserNotFoundException {
        userMap = FileService.loadFromFile("database/users.json", mapper, User.class);
        Manager manager;
        Utility.validateInput(managerName, "manager name");
        if (!userMap.containsKey(managerName)) {
            throw new UserNotFoundException(managerName + " manager does not exist");
        }
        manager = (Manager) userMap.get(managerName);
        return manager.getProjectList();
    }

    public Map<String, STATUS> getProjectStatusByClient(String clientName) throws UserNotFoundException {
        userMap = FileService.loadFromFile("database/users.json", mapper, User.class);
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        Utility.validateInput(clientName, "client name");
        if (!userMap.containsKey(clientName)) {
            throw new UserNotFoundException(clientName + " client does not exist");
        }
        Client client = (Client) userMap.get(clientName);
        Map<String, STATUS> projectStatusMap = new HashMap<>();
        for (String projectName : client.getProjectList()) {
            if (projectHashMap.containsKey(projectName)) {
                projectStatusMap.put(projectName, projectHashMap.get(projectName).getStatus());
            }
        }
        return projectStatusMap;
    }

    public List<String> getUnapprovedProjects() {
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        List<String> unapprovedProjects = new ArrayList<>();
        for (Map.Entry<String, Project> entry : projectHashMap.entrySet()) {
            if (entry.getValue().getStatus() == STATUS.NOT_APPROVED) {
                unapprovedProjects.add(entry.getKey());
            }
        }
        return unapprovedProjects;
    }

    public List<String> getApprovedProjects() {
        projectHashMap = FileService.loadFromFile(FILE_NAME, mapper, Project.class);
        List<String> approvedProjects = new ArrayList<>();
        for (Map.Entry<String, Project> entry : projectHashMap.entrySet()) {
            if (entry.getValue().getStatus() != STATUS.NOT_APPROVED) {
                approvedProjects.add(entry.getKey());
            }
        }
        return approvedProjects;
    }
    

}
