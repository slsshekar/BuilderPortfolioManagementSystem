package TaskTests;

import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskAlreadyExistsException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.CreateTask;
import com.zeta.service.TaskService.GetTasksBasedOnProjectStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetTasksBasedOnProjectStatusTest {

    private final CreateTask createTask = new CreateTask();
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
    void testGetTasksWithMatchingProjectAndStatus() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task X", 400, STATUS.UPCOMING));
        createTask.createTask(createSampleTask(2, "Task Y", 400, STATUS.IN_PROGRESS));
        createTask.createTask(createSampleTask(3, "Task Z", 400, STATUS.UPCOMING));

        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(400, STATUS.UPCOMING);
        assertEquals(2, tasks.size());
    }

    @Test
    void testGetTasksWithNoStatusMatch() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task M", 500, STATUS.UPCOMING));

        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(500, STATUS.COMPLETED);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testGetTasksWithNoProjectMatch() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task N", 600, STATUS.UPCOMING));

        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(9999, STATUS.UPCOMING);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testGetTasksFromEmptyFile() {
        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(100, STATUS.UPCOMING);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testGetTasksOnlyMatchesBothCriteria() throws InvalidTaskException, TaskAlreadyExistsException {
        createTask.createTask(createSampleTask(1, "Task A", 700, STATUS.UPCOMING));
        createTask.createTask(createSampleTask(2, "Task B", 700, STATUS.IN_PROGRESS));
        createTask.createTask(createSampleTask(3, "Task C", 800, STATUS.UPCOMING));

        List<Task> tasks = getTasksBasedOnProjectStatus.getTasksBasedOnProjectStatus(700, STATUS.UPCOMING);
        assertEquals(1, tasks.size());
        assertEquals(700, tasks.get(0).getProjectId());
        assertEquals(STATUS.UPCOMING, tasks.get(0).getStatus());
    }
}
