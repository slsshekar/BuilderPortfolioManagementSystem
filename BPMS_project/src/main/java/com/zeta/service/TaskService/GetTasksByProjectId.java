package com.zeta.service.TaskService;

import com.zeta.model.Task;
import com.zeta.service.utility.LoadFromTaskFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetTasksByProjectId {

    private static final String FILE_NAME = "database/tasks.json";

    public List<Task> getTasksByProjectId(int projectId) {

        Map<String, Task> taskMap = LoadFromTaskFile.load(FILE_NAME);
        List<Task> result = new ArrayList<>();

        for (Task task : taskMap.values()) {
            if (task.getProjectId() == projectId) {
                result.add(task);
            }
        }

        return result;
    }
}