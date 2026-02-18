package com.zeta.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.Task;
import com.zeta.service.FileService.FileService;

import java.util.Map;

public class TaskDAO {

    private final ObjectMapper mapper;
    private static final String FILE = "database/tasks.json";

    public TaskDAO(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, Task> load() {
        return FileService.loadFromFile(FILE, mapper, Task.class);
    }

    public void save(Map<String, Task> tasks) {
        FileService.saveToFile(tasks, FILE, mapper);
    }
}
