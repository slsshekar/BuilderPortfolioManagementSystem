package com.zeta.service.TaskService;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.Task;
import com.zeta.service.utility.LoadFromTaskFile;
import com.zeta.service.utility.SaveToTaskFile;

import java.util.Map;

public class CreateTask {

    private static final String FILE_NAME = "database/tasks.json";

    public boolean createTask(Task task) throws InvalidTaskException, TaskAlreadyExistsException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (task.getName() == null || task.getName().isBlank()) {
            throw new InvalidTaskException("Task name cannot be blank");
        }

        Map<String, Task> taskMap = LoadFromTaskFile.load(FILE_NAME);
        String key = String.valueOf(task.getName());

        if (taskMap.containsKey(key)) {
            throw new TaskAlreadyExistsException("Task with Name " + task.getName() + " already exists");
        }

        taskMap.put(key, task);
        SaveToTaskFile.save(taskMap, FILE_NAME);

        System.out.println("Task created successfully: " + task.getName());
        return true;
    }
}