package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.AssignTaskToBuilder;
import com.zeta.service.TaskService.CreateTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.service.FileService.FileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AssignTaskToBuilderTest {

    private final CreateTask createTask = new CreateTask();
    private final AssignTaskToBuilder assignTaskToBuilder = new AssignTaskToBuilder();

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
    void testAssignBuilderToExistingTask()
            throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(1, "Backend API", "Project Alpha", STATUS.UPCOMING);
        createTask.createTask(task);

        boolean result = assignTaskToBuilder.assignTaskToBuilder(task.getName(), "John");
        assertTrue(result);
    }

    @Test
    void testAssignBuilderToNonExistentTask() {
        assertThrows(TaskNotFoundException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder("NonExistentTask", "John");
        });
    }

    @Test
    void testAssignNullBuilderName() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(2, "Testing", "Project Alpha", STATUS.UPCOMING);
        createTask.createTask(task);

        assertDoesNotThrow(() -> {
            assignTaskToBuilder.assignTaskToBuilder(task.getName(), null);
        });
    }

    @Test
    void testAssignNullTaskName() {
        assertThrows(TaskNotFoundException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder(null, "John");
        });
    }

    @Test
    void testAssignMultipleBuildersToSameTask()
            throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(3, "Design DB", "Project Alpha", STATUS.IN_PROGRESS);
        createTask.createTask(task);

        assertTrue(assignTaskToBuilder.assignTaskToBuilder(task.getName(), "Person1"));
        assertTrue(assignTaskToBuilder.assignTaskToBuilder(task.getName(), "Person2"));


        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Map<String, Task> taskMap = FileService.loadFromFile("database/tasks.json", mapper, Task.class);
        Task reloaded = taskMap.get("Design DB");
        assertEquals(2, reloaded.getBuilderList().size());
    }
}
