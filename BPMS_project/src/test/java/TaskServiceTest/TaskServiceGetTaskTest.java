package TaskServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceGetTaskTest {

    private TaskDAO taskDAO;
    private UserDAO userDAO;
    private TaskService service;

    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        userDAO = mock(UserDAO.class);
        service = new TaskService(taskDAO, userDAO);
    }

    @Test
    void getTask_shouldReturnTask() {

        Task task = new Task();
        when(taskDAO.load()).thenReturn(Map.of("task1", task));

        Task result = service.getTask("task1");

        assertNotNull(result);
    }

    @Test
    void getTask_notFound_shouldReturnNull() {

        when(taskDAO.load()).thenReturn(Map.of());

        Task result = service.getTask("task1");

        assertNull(result);
    }
}
