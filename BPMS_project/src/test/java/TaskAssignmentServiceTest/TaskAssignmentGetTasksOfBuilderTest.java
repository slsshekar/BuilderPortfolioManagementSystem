package TaskAssignmentServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Builder;
import com.zeta.service.TaskService.TaskAssignmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskAssignmentGetTasksOfBuilderTest {

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
    void shouldReturnTasksOfBuilder() {

        Builder builder = new Builder();
        builder.setTaskList(Set.of("t1", "t2"));

        when(userDAO.load()).thenReturn(Map.of("builder1", builder));

        Set<String> result = service.getTasksOfBuilder("builder1");

        assertEquals(2, result.size());
    }

    @Test
    void builderNotFound_shouldThrow() {

        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.getTasksOfBuilder("builder1"));
    }
}
