package com.zeta.service.TaskService;

import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.utility.LoadFromTaskFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetTasksBasedOnProjectStatus {

    private static final String FILE_NAME = "database/tasks.json";

    public List<Task> getTasksBasedOnProjectStatus(int projectId, STATUS status) {

        Map<String, Task> taskMap = LoadFromTaskFile.load(FILE_NAME);
        List<Task> result = new ArrayList<>();

        for (Task task : taskMap.values()) {
            if (task.getProjectId() == projectId && task.getStatus() == status) {
                result.add(task);
            }
        }

        return result;
    }
}