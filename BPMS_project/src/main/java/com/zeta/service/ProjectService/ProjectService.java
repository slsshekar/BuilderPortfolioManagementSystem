package com.zeta.service.ProjectService;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.*;
import com.zeta.model.*;
import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.service.utility.Utility;

import java.time.LocalDate;
import java.util.*;

public class ProjectService {

    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;

    public ProjectService(ProjectDAO projectDAO, UserDAO userDAO) {
        this.projectDAO = projectDAO;
        this.userDAO = userDAO;
    }

    public boolean create(Project project, String clientName)
            throws ProjectAlreadyExistsException, ClientDoesNotExistException {

        Map<String, Project> projectMap = projectDAO.load();
        Map<String, User> userMap = userDAO.load();

        Utility.validateInput(project.getName(), "project name");
        Utility.validateInput(project.getDescription(), "project description");
        Utility.validateInput(clientName, "client name");

        if (!userMap.containsKey(clientName)) {
            throw new ClientDoesNotExistException(clientName + " doesn't exist");
        }

        if (projectMap.containsKey(project.getName())) {
            throw new ProjectAlreadyExistsException(project.getName() + " already exists");
        }

        project.setClientName(clientName);
        projectMap.put(project.getName(), project);

        Client client = (Client) userMap.get(clientName);
        client.getProjectList().add(project.getName());

        projectDAO.save(projectMap);
        userDAO.save(userMap);

        return true;
    }

    public void approve(String projectName,LocalDate startDate,LocalDate endDate) throws ProjectDoestNotExistException {
        Map<String, Project> projects = projectDAO.load();

        if (!projects.containsKey(projectName)) {
            throw new ProjectDoestNotExistException("Project not found: " + projectName);
        }

        Utility.validateInput(projectName, "Project name");

        Project project = projects.get(projectName);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setStatus(STATUS.UPCOMING);

        projectDAO.save(projects);
    }


    public boolean assignManager(String projectName, String managerName)
            throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {

        Map<String, Project> projectMap = projectDAO.load();
        Map<String, User> userMap = userDAO.load();

        Utility.validateInput(projectName, "project name");
        Utility.validateInput(managerName, "manager name");

        Project project = projectMap.get(projectName);
        if (project == null) {
            throw new ProjectDoestNotExistException(projectName + " doesn't exist");
        }

        User user = userMap.get(managerName);
        if (user == null) {
            throw new UserNotFoundException(managerName + " does not exist");
        }

        if (user.getRole() != ROLE.MANAGER) {
            throw new RoleMismatchException(managerName + " is not a manager");
        }

        Manager manager = (Manager) user;

        project.getManagerList().add(managerName);
        manager.getProjectList().add(projectName);

        projectDAO.save(projectMap);
        userDAO.save(userMap);

        return true;
    }

    public Set<String> getProjectsByClientName(String clientName)
            throws UserNotFoundException {

        Map<String, User> userMap = userDAO.load();

        Utility.validateInput(clientName, "client name");

        User user = userMap.get(clientName);

        if (!(user instanceof Client client)) {
            throw new UserNotFoundException(clientName + " does not exist");
        }

        return client.getProjectList();
    }

    public Set<String> getProjectsByManagerName(String managerName)
            throws UserNotFoundException {

        Map<String, User> userMap = userDAO.load();

        Utility.validateInput(managerName, "manager name");

        User user = userMap.get(managerName);

        if (!(user instanceof Manager manager)) {
            throw new UserNotFoundException(managerName + " does not exist");
        }

        return manager.getProjectList();
    }

    public Map<String, STATUS> getProjectStatusByClient(String clientName)
            throws UserNotFoundException {

        Map<String, User> userMap = userDAO.load();
        Map<String, Project> projectMap = projectDAO.load();

        User user = userMap.get(clientName);

        if (!(user instanceof Client client)) {
            throw new UserNotFoundException(clientName + " does not exist");
        }

        Map<String, STATUS> result = new HashMap<>();

        for (String projectName : client.getProjectList()) {
            Project project = projectMap.get(projectName);
            if (project != null) {
                result.put(projectName, project.getStatus());
            }
        }

        return result;
    }

    public List<String> getUnapprovedProjects() {

        Map<String, Project> projectMap = projectDAO.load();

        return projectMap.entrySet().stream()
                .filter(e -> e.getValue().getStatus() == STATUS.NOT_APPROVED)
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<String> getApprovedProjects() {

        Map<String, Project> projectMap = projectDAO.load();

        return projectMap.entrySet().stream()
                .filter(e -> e.getValue().getStatus() != STATUS.NOT_APPROVED)
                .map(Map.Entry::getKey)
                .toList();
    }
    public Map<STATUS, List<Project>> getAllProjectsGroupedByStatus() {

        Map<String, Project> projectMap = projectDAO.load();

        Map<STATUS, List<Project>> grouped = new HashMap<>();

        for (STATUS status : STATUS.values()) {
            grouped.put(status, new ArrayList<>());
        }

        for (Project project : projectMap.values()) {
            grouped.get(project.getStatus()).add(project);
        }

        return grouped;
    }

}
