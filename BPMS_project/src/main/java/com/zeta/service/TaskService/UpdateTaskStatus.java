package com.zeta.service.TaskService;

import com.zeta.DAO.TaskDAO;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.logging.Logger;
import com.zeta.model.STATUS;
import com.zeta.model.Task;

import java.util.Map;

public class UpdateTaskStatus {
    static Logger logger = Logger.getInstance();

    private final TaskDAO taskDAO;

    public UpdateTaskStatus(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public boolean updateTaskStatus(Task task, STATUS newStatus) throws InvalidTaskException, TaskNotFoundException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (newStatus == null) {
            throw new InvalidTaskException("Status cannot be null");
        }

        Map<String, Task> taskMap = taskDAO.load();
        String key = String.valueOf(task.getName());

        if (!taskMap.containsKey(key)) {
            throw new TaskNotFoundException("Task with Name " + task.getName() + " not found");
        }

        task.setStatus(newStatus);
        taskMap.put(key, task);
        taskDAO.save(taskMap);

        logger.info("Task status updated to " + newStatus + " for task: " + task.getName());
        return true;
    }
}