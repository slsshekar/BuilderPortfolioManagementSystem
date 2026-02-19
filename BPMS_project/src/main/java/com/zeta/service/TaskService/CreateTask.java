package com.zeta.service.TaskService;

import com.zeta.DAO.TaskDAO;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.logging.Logger;
import com.zeta.model.Task;

import java.util.Map;

public class CreateTask {
    static Logger logger = Logger.getInstance();
    private final TaskDAO taskDAO;

    public CreateTask(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public boolean createTask(Task task) throws InvalidTaskException, TaskAlreadyExistsException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (task.getName() == null || task.getName().isBlank()) {
            throw new InvalidTaskException("Task name cannot be blank");
        }

        Map<String, Task> taskMap = taskDAO.load();
        String key = String.valueOf(task.getName());

        if (taskMap.containsKey(key)) {
            throw new TaskAlreadyExistsException("Task with Name " + task.getName() + " already exists");
        }

        taskMap.put(key, task);
        taskDAO.save(taskMap);

        logger.info("Task created successfully: " + task.getName());
        return true;
    }
}