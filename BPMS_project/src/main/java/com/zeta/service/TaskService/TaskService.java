package com.zeta.service.TaskService;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class TaskService {

    private final TaskDAO taskDAO;
    private final UserDAO userDAO;
    private final ProjectDAO projectDAO;

    public void createTask(String taskName,
                           String description,
                           String projectName,
                           String managerName,
                           LocalDate start,
                           LocalDate end) {

        Map<String, Task> tasks = taskDAO.load();

        if (taskName == null || taskName.isBlank()) {
            throw new RuntimeException("Task name cannot be empty");
        }

        if (tasks.containsKey(taskName)) {
            throw new RuntimeException("Task already exists: " + taskName);
        }

        Task task = new Task(taskName, description, projectName, managerName, start, end);

        tasks.put(taskName, task);
        taskDAO.save(tasks);

    }

    public TaskService(TaskDAO taskDAO, UserDAO userDAO, ProjectDAO projectDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
    }

    public boolean updateTaskStatus(String taskName, STATUS status) {

        Map<String, Task> tasks = taskDAO.load();

        Task task = tasks.get(taskName);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        task.setStatus(status);

        taskDAO.save(tasks);
        checkAndUpdateProjectStatus(task.getProjectName(), tasks);
        return true;
    }

    private void checkAndUpdateProjectStatus(String projectName, Map<String, Task> tasks) {
        if (projectName == null || projectName.isBlank()) return;

        Map<String, Project> projectMap = projectDAO.load();
        Project project = projectMap.get(projectName);
        if (project == null) return;

        Set<String> taskNames = project.getTaskList();
        if (taskNames == null || taskNames.isEmpty()) return;

        for (String tn : taskNames) {
            Task t = tasks.get(tn);
            if (t == null || t.getStatus() != STATUS.COMPLETED) {
                return;
            }
        }
        project.setStatus(STATUS.COMPLETED);
        projectDAO.save(projectMap);
    }

    public Task getTask(String taskName) {
        return taskDAO.load().get(taskName);
    }

    public Map<String, Task> getAllTasks() {
        return taskDAO.load();
    }

}
