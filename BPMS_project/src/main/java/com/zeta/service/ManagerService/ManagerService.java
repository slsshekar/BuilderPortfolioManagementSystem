package com.zeta.service.ManagerService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.*;
import com.zeta.service.FileService.FileService;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Map;

public class ManagerService {

    private final ObjectMapper mapper;

    public ManagerService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void createTask(String projectName, String taskName, String description, String username, LocalDate start, LocalDate end) {

        Map<String, Project> projectMap = FileService.loadFromFile("database/projects.json", mapper, Project.class);

        Map<String, Task> taskMap = FileService.loadFromFile("database/tasks.json", mapper, Task.class);

        Project project = projectMap.get(projectName);

        if (project == null) {
            throw new RuntimeException("Project not found");
        }

        if (taskMap.containsKey(taskName)) {
            throw new RuntimeException("Task already exists");
        }

        Task task = new Task(taskName, description, projectName, username, start, end);

        taskMap.put(taskName, task);
        project.getTaskList().add(taskName);

        if (project.getStatus() == STATUS.UPCOMING) {
            project.setStatus(STATUS.IN_PROGRESS);
        }

        FileService.saveToFile(projectMap, "database/projects.json", mapper);
        FileService.saveToFile(taskMap, "database/tasks.json", mapper);
    }

    public Project getProject(String projectName) {

        Map<String, Project> projects = FileService.loadFromFile("database/projects.json", mapper, Project.class);
        Project project = projects.get(projectName);
        if (project == null) {
            throw new RuntimeException("Project not found");
        }

        return project;
    }

    public void assignBuilder(String projectName, String taskName, String builderName) {

        Map<String, Project> projects = FileService.loadFromFile("database/projects.json", mapper, Project.class);

        Map<String, Task> tasks = FileService.loadFromFile("database/tasks.json", mapper, Task.class);

        Map<String, User> users = FileService.loadFromFile("database/users.json", mapper, User.class);

        Project project = projects.get(projectName);
        if (project == null) throw new RuntimeException("Project not found");

        Task task = tasks.get(taskName);
        if (task == null) throw new RuntimeException("Task not found");

        User user = users.get(builderName);
        if (!(user instanceof Builder builder))
            throw new RuntimeException("Invalid builder");

        task.getBuilderList().add(builderName);
        builder.getTaskList().add(taskName);

        FileService.saveToFile(tasks, "database/tasks.json", mapper);
        FileService.saveToFile(users, "database/users.json", mapper);
    }

    public void updateTaskStatus(String taskName, STATUS status) {

        Map<String, Task> tasks = FileService.loadFromFile("database/tasks.json", mapper, Task.class);

        Task task = tasks.get(taskName);

        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        task.setStatus(status);

        FileService.saveToFile(tasks, "database/tasks.json", mapper);
    }

    public Map<STATUS, List<Project>> getProjectsByStatusForManager(Set<String> projectNames) {

        Map<String, Project> allProjects =
                FileService.loadFromFile("database/projects.json", mapper, Project.class);

        List<Project> managerProjects = projectNames.stream()
                .map(allProjects::get)
                .filter(Objects::nonNull)
                .toList();

        return managerProjects.stream()
                .collect(Collectors.groupingBy(Project::getStatus));
    }


}
