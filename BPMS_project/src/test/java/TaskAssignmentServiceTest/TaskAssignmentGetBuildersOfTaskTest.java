package TaskAssignmentServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskAssignmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskAssignmentGetBuildersOfTaskTest {

    private TaskDAO taskDAO;
    private UserDAO userDAO;
    private TaskAssignmentService service;

    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        userDAO = mock(UserDAO.class);
        service = new TaskAssignmentService(taskDAO, userDAO);
    }

    @Test
    void shouldReturnBuilders() {

        Task task = new Task();
        task.setBuilderList(Set.of("b1","b2"));

        when(taskDAO.load()).thenReturn(Map.of("task1", task));

        Set<String> result = service.getBuildersOfTask("task1");

        assertEquals(2, result.size());
    }

    @Test
    void taskNotFound_shouldThrow() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.getBuildersOfTask("task1"));
    }
}
