package TaskServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceCreateTaskTest {

    private TaskDAO taskDAO;
    private UserDAO userDAO;
    private TaskService service;

    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        userDAO = mock(UserDAO.class);
        service = new TaskService(taskDAO, userDAO);
    }

    // ✅ SUCCESS
    @Test
    void createTask_valid_shouldCreate() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        service.createTask(
                "task1",
                "desc",
                "project1",
                "manager1",
                LocalDate.now(),
                LocalDate.now()
        );

        verify(taskDAO).save(any());
    }

    // ❌ NULL TASK NAME
    @Test
    void createTask_nullName_shouldThrow() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class, () ->
                service.createTask(
                        null,
                        "desc",
                        "project",
                        "manager",
                        LocalDate.now(),
                        LocalDate.now()));
    }

    // ❌ BLANK TASK NAME
    @Test
    void createTask_blankName_shouldThrow() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class, () ->
                service.createTask(
                        " ",
                        "desc",
                        "project",
                        "manager",
                        LocalDate.now(),
                        LocalDate.now()));
    }

    // ❌ DUPLICATE TASK
    @Test
    void createTask_duplicate_shouldThrow() {

        Map<String, Task> tasks = new HashMap<>();
        tasks.put("task1", new Task());

        when(taskDAO.load()).thenReturn(tasks);

        assertThrows(RuntimeException.class, () ->
                service.createTask(
                        "task1",
                        "desc",
                        "project",
                        "manager",
                        LocalDate.now(),
                        LocalDate.now()));
    }
}
