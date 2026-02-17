package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.Builder;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.AssignTaskToBuilder;
import com.zeta.service.TaskService.CreateTask;
import com.zeta.service.TaskService.GetTasksByProjectId;
import com.zeta.service.TaskService.GetTasksBasedOnProjectStatus;
import com.zeta.service.TaskService.UpdateTaskStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTests {

    private final CreateTask createTask = new CreateTask();
    private final AssignTaskToBuilder assignTaskToBuilder = new AssignTaskToBuilder();
    private final UpdateTaskStatus updateTaskStatus = new UpdateTaskStatus();
    private final GetTasksByProjectId getTasksByProjectId = new GetTasksByProjectId();
    private final GetTasksBasedOnProjectStatus getTasksBasedOnProjectStatus = new GetTasksBasedOnProjectStatus();

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
    void testCreateDuplicateTask() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(1, "Design UI", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        Task duplicateTask = createSampleTask(2, "Design UI", 200, STATUS.IN_PROGRESS);
        assertThrows(TaskAlreadyExistsException.class, () -> {
            createTask.createTask(duplicateTask);
        });
    }

    @Test
    void testAssignBuilderToExistingTask() throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(2, "Backend API", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        Builder builder = new Builder();
        builder.setId(10);
        builder.setName("John");

        boolean result = assignTaskToBuilder.assignTaskToBuilder(task, builder);
        assertTrue(result);
    }

    @Test
    void testAssignBuilderToNonExistentTask() {
        Task task = createSampleTask(999, "Ghost Task", 100, STATUS.UPCOMING);
        Builder builder = new Builder();
        builder.setId(10);
        builder.setName("John");

        assertThrows(TaskNotFoundException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder(task, builder);
        });
    }

    @Test
    void testAssignNullBuilder() throws InvalidTaskException, TaskAlreadyExistsException {
        Task task = createSampleTask(3, "Testing", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        assertThrows(InvalidTaskException.class, () -> {
            assignTaskToBuilder.assignTaskToBuilder(task, null);
        });
    }

    @Test
    void testUpdateStatusOfExistingTask() throws InvalidTaskException, TaskAlreadyExistsException, TaskNotFoundException {
        Task task = createSampleTask(4, "Database Setup", 100, STATUS.UPCOMING);
        createTask.createTask(task);

        boolean result = updateTaskStatus.updateTaskStatus(task, STATUS.IN_PROGRESS);
        assertTrue(result);
    }

    @Test
    void testUpdateStatusOfNonExistentTask() {
        Task task = createSampleTask(999, "Ghost Task", 100, STATUS.UPCOMING);

        assertThrows(TaskNotFoundException.class, () -> {
            updateTaskStatus.updateTaskStatus(task, STATUS.COMPLETED);
        });
    }



    @Test
    void testGetTasksByProjectIdAndStatusWithMatch() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(8, "Task X", 400, STATUS.UPCOMING));
        createTask.createTask(createSampleTask(9, "Task Y", 400, STATUS.IN_PROGRESS));
        createTask.createTask(createSampleTask(10, "Task Z", 400, STATUS.UPCOMING));

        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(400, STATUS.UPCOMING);
        assertEquals(2, tasks.size());
    }

    @Test
    void testGetTasksByProjectIdAndStatusNoMatch() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(11, "Task M", 500, STATUS.UPCOMING));

        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(500, STATUS.COMPLETED);
        assertTrue(tasks.isEmpty());
    }
}