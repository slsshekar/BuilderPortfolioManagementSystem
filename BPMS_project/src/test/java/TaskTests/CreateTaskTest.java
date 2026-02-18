package TaskTests;

import com.zeta.DAO.TaskDAO;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.CreateTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CreateTaskTest {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final TaskDAO taskDAO = new TaskDAO(mapper);

    private final CreateTask createTask = new CreateTask(taskDAO);

    @BeforeEach
    void setUp() throws IOException {
        File file = new File("database/tasks.json");
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write("{}");
        writer.close();
    }

    private Task createSampleTask(int id, String name, String projectName, STATUS status) {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription("Sample description for " + name);
        task.setProjectName(projectName);
        task.setStatus(status);
        return task;
    }

    @Test
    void testCreateValidTask() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(1, "Design UI", "Project Alpha", STATUS.UPCOMING);
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
        Task task = createSampleTask(2, "   ", "Project Alpha", STATUS.UPCOMING);
        assertThrows(InvalidTaskException.class, () -> {
            createTask.createTask(task);
        });
    }

    @Test
    void testCreateDuplicateTask() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(1, "Design UI", "Project Alpha", STATUS.UPCOMING);
        createTask.createTask(task);

        Task duplicateTask = createSampleTask(2, "Design UI", "Project Beta", STATUS.IN_PROGRESS);
        assertThrows(TaskAlreadyExistsException.class, () -> {
            createTask.createTask(duplicateTask);
        });
    }

    @Test
    void testCreateMultipleUniqueTasks() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task1 = createSampleTask(1, "Task A", "Project Alpha", STATUS.UPCOMING);
        Task task2 = createSampleTask(2, "Task B", "Project Alpha", STATUS.IN_PROGRESS);
        assertTrue(createTask.createTask(task1));
        assertTrue(createTask.createTask(task2));
    }
}
