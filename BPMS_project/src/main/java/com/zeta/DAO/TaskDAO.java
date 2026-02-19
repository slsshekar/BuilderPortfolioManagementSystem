package com.zeta.DAO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.Task;

import java.nio.file.Paths;
import java.util.Map;

public class TaskDAO extends BaseJsonDAO<Task> {

    private static final String FILE = "database/tasks.json";

    public TaskDAO(ObjectMapper mapper) {
        super(
                mapper,
                Paths.get(FILE),
                new TypeReference<Map<String, Task>>() {
                }
        );
    }
}
