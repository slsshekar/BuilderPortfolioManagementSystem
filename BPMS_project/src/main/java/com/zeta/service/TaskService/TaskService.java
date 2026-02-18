package com.zeta.service.TaskService;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.*;

import java.time.LocalDate;
import java.util.Map;

public class TaskService {

    private final TaskDAO taskDAO;
    private final UserDAO userDAO;
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

    public TaskService(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    public void updateTaskStatus(String taskName, STATUS status) {

        Map<String, Task> tasks = taskDAO.load();

        Task task = tasks.get(taskName);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        task.setStatus(status);

        taskDAO.save(tasks);
    }

    public Task getTask(String taskName) {
        return taskDAO.load().get(taskName);
    }
    public Map<String, Task> getAllTasks() {
        return taskDAO.load();
    }
    
}
