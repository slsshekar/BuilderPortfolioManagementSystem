package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.CreateTask;
import com.zeta.service.TaskService.UpdateTaskStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateTaskStatusTest {

    private final CreateTask createTask = new CreateTask();
    private final UpdateTaskStatus updateTaskStatus = new UpdateTaskStatus();

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
    void testUpdateStatusOfExistingTask()
            throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(1, "Database Setup", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        boolean result = updateTaskStatus.updateTaskStatus(task, STATUS.IN_PROGRESS);
        assertTrue(result);
    }

    @Test
    void testUpdateStatusOfNonExistentTask() {
        Task task = createSampleTask(999, "Sample Task", 100, STATUS.UPCOMING);

        assertThrows(TaskNotFoundException.class, () -> {
            updateTaskStatus.updateTaskStatus(task, STATUS.COMPLETED);
        });
    }

    @Test
    void testUpdateStatusWithNullTask() {
        assertThrows(InvalidTaskException.class, () -> {
            updateTaskStatus.updateTaskStatus(null, STATUS.COMPLETED);
        });
    }

    @Test
    void testUpdateStatusWithNullStatus() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(2, "API Design", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        assertThrows(InvalidTaskException.class, () -> {
            updateTaskStatus.updateTaskStatus(task, null);
        });
    }

    @Test
    void testUpdateStatusToCompleted() throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(3, "Write Tests", 200, STATUS.IN_PROGRESS);
        createTask.createTask(task);

        boolean result = updateTaskStatus.updateTaskStatus(task, STATUS.COMPLETED);
        assertTrue(result);
        assertEquals(STATUS.COMPLETED, task.getStatus());
    }
}
