package com.zeta.service.TaskService;

import com.zeta.DAO.ProjectDAO;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.model.Project;

import java.util.Map;
import java.util.Set;

public class GetTasksByProjectName {

    private final ProjectDAO projectDAO;

    public GetTasksByProjectName(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public Set<String> getTasksByProjectName(String projectName) throws ProjectDoestNotExistException {
        Map<String, Project> projectMap = projectDAO.load();
        if (!projectMap.containsKey(projectName)) {
            System.out.println("project doesnt exist");
            throw new ProjectDoestNotExistException(projectName + " does not exist");
        }
        return projectMap.get(projectName).getTaskList();
    }
}