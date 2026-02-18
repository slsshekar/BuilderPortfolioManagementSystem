package com.zeta.service.TaskService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.FileService.FileService;

import java.util.Map;

public class UpdateTaskStatus {

    private static final String FILE_NAME = "database/tasks.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public boolean updateTaskStatus(Task task, STATUS newStatus) throws InvalidTaskException, TaskNotFoundException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (newStatus == null) {
            throw new InvalidTaskException("Status cannot be null");
        }

        Map<String, Task> taskMap = FileService.loadFromFile(FILE_NAME, mapper, Task.class);
        String key = String.valueOf(task.getName());

        if (!taskMap.containsKey(key)) {
            throw new TaskNotFoundException("Task with Name " + task.getName() + " not found");
        }

        task.setStatus(newStatus);
        taskMap.put(key, task);
        FileService.saveToFile(taskMap, FILE_NAME, mapper);

        System.out.println("Task status updated to " + newStatus + " for task: " + task.getName());
        return true;
    }
}