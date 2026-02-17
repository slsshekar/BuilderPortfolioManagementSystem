package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.CreateTask;
import com.zeta.service.TaskService.GetTasksByProjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetTasksByProjectIdTest {

    private final CreateTask createTask = new CreateTask();
    private final GetTasksByProjectId getTasksByProjectId = new GetTasksByProjectId();

    @BeforeEach
    void setUp() throws IOException {
        File file = new File("database/tasks.json");
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write("{}");
        writer.close();
    }

    private Task createSampleTask(int id, String name, int projectId, STATUS status) {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription("Sample description for " + name);
        task.setProjectId(projectId);
        task.setStatus(status);
        return task;
    }

    @Test
    void testGetTasksForExistingProjectId() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task A", 300, STATUS.UPCOMING));
        createTask.createTask(createSampleTask(2, "Task B", 300, STATUS.IN_PROGRESS));

        List<Task> tasks = getTasksByProjectId.getTasksByProjectId(300);
        assertEquals(2, tasks.size());
    }

    @Test
    void testGetTasksForNonExistentProjectId() {
        List<Task> tasks = getTasksByProjectId.getTasksByProjectId(9999);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testGetTasksReturnsSingleMatch() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task A", 100, STATUS.UPCOMING));
        createTask.createTask(createSampleTask(2, "Task B", 200, STATUS.UPCOMING));

        List<Task> tasks = getTasksByProjectId.getTasksByProjectId(100);
        assertEquals(1, tasks.size());
    }

    @Test
    void testGetTasksFromEmptyFile() {
        List<Task> tasks = getTasksByProjectId.getTasksByProjectId(100);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testGetTasksDoesNotReturnOtherProjectTasks() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task A", 500, STATUS.UPCOMING));
        createTask.createTask(createSampleTask(2, "Task B", 600, STATUS.IN_PROGRESS));
        createTask.createTask(createSampleTask(3, "Task C", 500, STATUS.COMPLETED));

        List<Task> tasks = getTasksByProjectId.getTasksByProjectId(500);
        assertEquals(2, tasks.size());
        for (Task task : tasks) {
            assertEquals(500, task.getProjectId());
        }
    }
}
