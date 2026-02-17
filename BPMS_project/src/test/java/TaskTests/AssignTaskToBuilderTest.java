package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.Builder;
import com.zeta.model.ROLE;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.AssignTaskToBuilder;
import com.zeta.service.TaskService.CreateTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    void testAssignBuilderToExistingTask()
            throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(1, "Backend API", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        Builder builder = new Builder("John", "pass123", ROLE.BUILDER);
        boolean result = assignTaskToBuilder.assignTaskToBuilder(task, builder);
        assertTrue(result);
    }

    @Test
    void testAssignBuilderToNonExistentTask() {
        Task task = createSampleTask(999, "Task123", 100, STATUS.UPCOMING);
        Builder builder = new Builder("John", "pass123", ROLE.BUILDER);

        assertThrows(TaskNotFoundException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder(task, builder);
        });
    }

    @Test
    void testAssignNullBuilder() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(2, "Testing", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        assertThrows(InvalidTaskException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder(task, null);
        });
    }

    @Test
    void testAssignNullTask() {
        Builder builder = new Builder("John", "pass123", ROLE.BUILDER);

        assertThrows(InvalidTaskException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder(null, builder);
        });
    }

    @Test
    void testAssignMultipleBuildersToSameTask()
            throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(3, "Design DB", 100, STATUS.IN_PROGRESS);
        createTask.createTask(task);

        Builder builder1 = new Builder("Person1", "pass1", ROLE.BUILDER);
        Builder builder2 = new Builder("Person2", "pass2", ROLE.BUILDER);

        assertTrue(assignTaskToBuilder.assignTaskToBuilder(task, builder1));
        assertTrue(assignTaskToBuilder.assignTaskToBuilder(task, builder2));
        assertEquals(2, task.getBuilderList().size());
    }
}
