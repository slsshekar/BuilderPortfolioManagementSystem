package com.zeta.service.TaskService;

import com.zeta.DAO.TaskDAO;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.logging.Logger;
import com.zeta.model.Task;

import java.util.Map;

public class AssignTaskToBuilder {
    static Logger logger = Logger.getInstance();
    private final TaskDAO taskDAO;

    public AssignTaskToBuilder(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public boolean assignTaskToBuilder(String taskName, String builderName)
            throws InvalidTaskException, TaskNotFoundException {

        Map<String, Task> taskMap = taskDAO.load();
        if (!taskMap.containsKey(taskName)) {
            throw new TaskNotFoundException("Task with Name " + taskName + " not found");
        }
        Task task = taskMap.get(taskName);
        task.getBuilderList().add(builderName);
        taskDAO.save(taskMap);

        logger.info("Builder assigned to task: " + task.getName());
        return true;
    }
}
