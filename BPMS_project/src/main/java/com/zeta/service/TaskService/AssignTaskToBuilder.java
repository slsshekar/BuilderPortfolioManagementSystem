package com.zeta.service.TaskService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.Builder;
import com.zeta.model.Task;
import com.zeta.service.FileService.FileService;

import java.util.Map;

public class AssignTaskToBuilder {

    private static final String FILE_NAME = "database/tasks.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public boolean assignTaskToBuilder(String taskName, String builderName)
            throws InvalidTaskException, TaskNotFoundException {

        // if (taskName == null) {
        // throw new InvalidTaskException("Task cannot be null");
        // }
        //
        // if (builder == null) {
        // throw new InvalidTaskException("Builder cannot be null");
        // }

        Map<String, Task> taskMap = FileService.loadFromFile(FILE_NAME, mapper, Task.class);
        if (!taskMap.containsKey(taskName)) {
            throw new TaskNotFoundException("Task with Name " + taskName + " not found");
        }
        Task task = taskMap.get(taskName);
        task.getBuilderList().add(builderName);
        FileService.saveToFile(taskMap, FILE_NAME, mapper);

        System.out.println("Builder assigned to task: " + task.getName());
        return true;
    }
}
