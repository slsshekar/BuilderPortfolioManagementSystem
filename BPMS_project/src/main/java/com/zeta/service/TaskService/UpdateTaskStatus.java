package com.zeta.service.TaskService;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.utility.LoadFromTaskFile;
import com.zeta.service.utility.SaveToTaskFile;

import java.util.Map;

public class UpdateTaskStatus {

    private static final String FILE_NAME = "database/tasks.json";

    public boolean updateTaskStatus(Task task, STATUS newStatus) throws InvalidTaskException, TaskNotFoundException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (newStatus == null) {
            throw new InvalidTaskException("Status cannot be null");
        }

        Map<String, Task> taskMap = LoadFromTaskFile.load(FILE_NAME);
        String key = String.valueOf(task.getName());

        if (!taskMap.containsKey(key)) {
            throw new TaskNotFoundException("Task with Name " + task.getName() + " not found");
        }

        task.setStatus(newStatus);
        taskMap.put(key, task);
        SaveToTaskFile.save(taskMap, FILE_NAME);

        System.out.println("Task status updated to " + newStatus + " for task: " + task.getName());
        return true;
    }
}