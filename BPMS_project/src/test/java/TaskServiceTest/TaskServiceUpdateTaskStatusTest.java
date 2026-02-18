package TaskServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceUpdateTaskStatusTest {

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
    void updateStatus_valid_shouldUpdate() {

        Task task = new Task();

        when(taskDAO.load()).thenReturn(Map.of("task1", task));

        service.updateTaskStatus("task1", STATUS.COMPLETED);

        assertEquals(STATUS.COMPLETED, task.getStatus());
        verify(taskDAO).save(any());
    }

    // ❌ TASK NOT FOUND
    @Test
    void updateStatus_taskNotFound_shouldThrow() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.updateTaskStatus("task1", STATUS.COMPLETED));
    }
}
