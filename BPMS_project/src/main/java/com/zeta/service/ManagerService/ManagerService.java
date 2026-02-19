package com.zeta.service.ManagerService;

import com.zeta.DAO.ProjectDAO;
import com.zeta.model.Project;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ManagerService {

    private final ProjectDAO projectDAO;
    private final TaskService taskService;

    public ManagerService(ProjectDAO projectDAO, TaskService taskService) {
        this.projectDAO = projectDAO;
        this.taskService = taskService;
    }

    public void createTaskForProject(String projectName,
                                     String taskName,
                                     String description,
                                     String managerName,
                                     LocalDate start,
                                     LocalDate end) {

        Map<String, Project> projects = projectDAO.load();

        Project project = projects.get(projectName);

        if (project == null) {
            throw new RuntimeException("Project not found: " + projectName);
        }

        if (!project.getManagerList().contains(managerName)) {
            throw new RuntimeException("Manager not assigned to this project");
        }
        taskService.createTask(
                taskName,
                description,
                projectName,
                managerName,
                start,
                end);

        project.getTaskList().add(taskName);

        if (project.getStatus() == STATUS.UPCOMING) {
            project.setStatus(STATUS.IN_PROGRESS);
        }

        projectDAO.save(projects);
    }

    public Map<STATUS, List<Project>> getProjectsByStatus(Set<String> projectNames) {

        Map<String, Project> allProjects = projectDAO.load();

        return projectNames.stream()
                .map(allProjects::get)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Project::getStatus));
    }

    public Project getProject(String projectName) {

        Map<String, Project> projects = projectDAO.load();

        Project project = projects.get(projectName);

        if (project == null) {
            throw new RuntimeException("Project not found: " + projectName);
        }

        return project;
    }

    public List<Task> getTasksOfProject(String projectName) {

        Map<String, Project> projectMap = projectDAO.load();

        Project project = projectMap.get(projectName);

        if (project == null) {
            throw new RuntimeException("Project not found: " + projectName);
        }

        Map<String, Task> allTasks = taskService.getAllTasks();

        return project.getTaskList()
                .stream()
                .map(allTasks::get)
                .filter(Objects::nonNull)
                .toList();
    }

}
