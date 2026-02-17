package com.zeta.service.TaskService;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateTask {

    private static final String FILE_NAME = "database/tasks.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static Map<String, Task> loadFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists() && file.length() > 0) {
                return mapper.readValue(
                        file,
                        new TypeReference<Map<String, Task>>() {
                        });
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return new HashMap<>();
    }

    private static void saveToFile(Map<String, Task> taskMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), taskMap);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public boolean createTask(Task task) throws InvalidTaskException, TaskAlreadyExistsException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (task.getName() == null || task.getName().isBlank()) {
            throw new InvalidTaskException("Task name cannot be blank");
        }

        Map<String, Task> taskMap = loadFromFile();
        String key = String.valueOf(task.getName());

        if (taskMap.containsKey(key)) {
            throw new TaskAlreadyExistsException("Task with Name " + task.getName() + " already exists");
        }

        taskMap.put(key, task);
        saveToFile(taskMap);

        System.out.println("Task created successfully: " + task.getName());
        return true;
    }
}