package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.CreateTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CreateTaskTest {

    private final CreateTask createTask = new CreateTask();

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
    void testCreateValidTask() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(1, "Design UI", 100, STATUS.UPCOMING);
        boolean result = createTask.createTask(task);
        assertTrue(result);
    }

    @Test
    void testCreateTaskWithNullObject() {
        assertThrows(InvalidTaskException.class, () -> {
            createTask.createTask(null);
        });
    }

    @Test
    void testCreateTaskWithBlankName() {
        Task task = createSampleTask(2, "   ", 100, STATUS.UPCOMING);
        assertThrows(InvalidTaskException.class, () -> {
            createTask.createTask(task);
        });
    }

    @Test
    void testCreateDuplicateTask() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(1, "Design UI", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        Task duplicateTask = createSampleTask(2, "Design UI", 200, STATUS.IN_PROGRESS);
        assertThrows(TaskAlreadyExistsException.class, () -> {
            createTask.createTask(duplicateTask);
        });
    }

    @Test
    void testCreateMultipleUniqueTasks() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task1 = createSampleTask(1, "Task A", 100, STATUS.UPCOMING);
        Task task2 = createSampleTask(2, "Task B", 100, STATUS.IN_PROGRESS);
        assertTrue(createTask.createTask(task1));
        assertTrue(createTask.createTask(task2));
    }
}
