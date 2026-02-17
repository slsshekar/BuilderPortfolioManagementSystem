package com.zeta.service.TaskService;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.Builder;
import com.zeta.model.Task;
import com.zeta.service.utility.LoadFromTaskFile;
import com.zeta.service.utility.SaveToTaskFile;

import java.util.Map;

public class AssignTaskToBuilder {

    private static final String FILE_NAME = "database/tasks.json";

    public boolean assignTaskToBuilder(Task task, Builder builder) throws InvalidTaskException, TaskNotFoundException {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (builder == null) {
            throw new InvalidTaskException("Builder cannot be null");
        }

        Map<String, Task> taskMap = LoadFromTaskFile.load(FILE_NAME);
        String key = String.valueOf(task.getName());

        if (!taskMap.containsKey(key)) {
            throw new TaskNotFoundException("Task with Name " + task.getName() + " not found");
        }

        task.getBuilderList().add(builder);
        taskMap.put(key, task);
        SaveToTaskFile.save(taskMap, FILE_NAME);

        System.out.println("Builder assigned to task: " + task.getName());
        return true;
    }
}
