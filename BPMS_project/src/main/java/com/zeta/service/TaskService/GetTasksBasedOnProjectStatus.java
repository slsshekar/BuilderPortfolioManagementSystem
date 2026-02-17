package com.zeta.service.TaskService;

import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTasksBasedOnProjectStatus {

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

    public List<Task> getTasksBasedOnProjectStatus(int projectId, STATUS status) {

        Map<String, Task> taskMap = loadFromFile();
        List<Task> result = new ArrayList<>();

        for (Task task : taskMap.values()) {
            if (task.getProjectId() == projectId && task.getStatus() == status) {
                result.add(task);
            }
        }

        return result;
    }
}