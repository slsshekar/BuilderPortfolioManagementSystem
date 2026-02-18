package com.zeta.service.TaskService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.Task;
import com.zeta.service.FileService.FileService;

import java.util.Map;

public class CreateTask {

    private static final String FILE_NAME = "database/tasks.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public boolean createTask(Task task) throws InvalidTaskException, TaskAlreadyExistsException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (task.getName() == null || task.getName().isBlank()) {
            throw new InvalidTaskException("Task name cannot be blank");
        }

        Map<String, Task> taskMap = FileService.loadFromFile(FILE_NAME, mapper, Task.class);
        String key = String.valueOf(task.getName());

        if (taskMap.containsKey(key)) {
            throw new TaskAlreadyExistsException("Task with Name " + task.getName() + " already exists");
        }

        taskMap.put(key, task);
        FileService.saveToFile(taskMap, FILE_NAME, mapper);

        System.out.println("Task created successfully: " + task.getName());
        return true;
    }
}