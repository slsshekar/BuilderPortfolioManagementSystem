package TaskServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceGetAllTasksTest {

    private TaskDAO taskDAO;
    private UserDAO userDAO;
    private ProjectDAO projectDAO;
    private TaskService service;

    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        userDAO = mock(UserDAO.class);
        service = new TaskService(taskDAO, userDAO, projectDAO);
    }

    @Test
    void shouldReturnAllTasks() {

        Map<String, Task> tasks = Map.of(
                "t1", new Task(),
                "t2", new Task()
        );

        when(taskDAO.load()).thenReturn(tasks);

        Map<String, Task> result = service.getAllTasks();

        assertEquals(2, result.size());
    }
}
