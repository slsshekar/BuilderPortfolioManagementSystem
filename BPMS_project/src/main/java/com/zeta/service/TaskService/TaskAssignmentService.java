package com.zeta.service.TaskService;

import com.zeta.model.Builder;
import com.zeta.model.Task;
import com.zeta.model.User;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;

import java.util.Map;
import java.util.Set;

public class TaskAssignmentService {

    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    public TaskAssignmentService(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }


    public void assignBuilderToTask(String taskName, String builderName) {

        Map<String, Task> taskMap = taskDAO.load();
        Map<String, User> userMap = userDAO.load();

        Task task = taskMap.get(taskName);
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskName);
        }

        User user = userMap.get(builderName);
        if (user == null) {
            throw new RuntimeException("User not found: " + builderName);
        }

        if (!(user instanceof Builder builder)) {
            throw new RuntimeException(builderName + " is not a builder");
        }

        if (task.getBuilderList().contains(builderName)) {
            throw new RuntimeException("Builder already assigned to task");
        }

        task.getBuilderList().add(builderName);
        builder.getTaskList().add(taskName);

        taskDAO.save(taskMap);
        userDAO.save(userMap);
    }


    public void removeBuilderFromTask(String taskName, String builderName) {

        Map<String, Task> taskMap = taskDAO.load();
        Map<String, User> userMap = userDAO.load();

        Task task = taskMap.get(taskName);
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskName);
        }

        User user = userMap.get(builderName);
        if (!(user instanceof Builder builder)) {
            throw new RuntimeException("Invalid builder: " + builderName);
        }

        task.getBuilderList().remove(builderName);
        builder.getTaskList().remove(taskName);

        taskDAO.save(taskMap);
        userDAO.save(userMap);
    }

    public Set<String> getBuildersOfTask(String taskName) {

        Map<String, Task> taskMap = taskDAO.load();

        Task task = taskMap.get(taskName);
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskName);
        }

        return task.getBuilderList();
    }

    public Set<String> getTasksOfBuilder(String builderName) {

        Map<String, User> userMap = userDAO.load();

        User user = userMap.get(builderName);
        if (!(user instanceof Builder builder)) {
            throw new RuntimeException("Builder not found: " + builderName);
        }

        return builder.getTaskList();
    }
}
