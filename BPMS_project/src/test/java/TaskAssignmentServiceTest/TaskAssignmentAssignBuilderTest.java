package TaskAssignmentServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Builder;
import com.zeta.model.Task;
import com.zeta.model.User;
import com.zeta.service.TaskService.TaskAssignmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskAssignmentAssignBuilderTest {

    private TaskDAO taskDAO;
    private UserDAO userDAO;
    private TaskAssignmentService service;

    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        userDAO = mock(UserDAO.class);
        service = new TaskAssignmentService(taskDAO, userDAO);
    }

    // ✅ SUCCESS
    @Test
    void assignBuilder_valid_shouldAssign() {

        Task task = new Task();
        task.setBuilderList(new HashSet<>());

        Builder builder = new Builder();
        builder.setTaskList(new HashSet<>());

        when(taskDAO.load()).thenReturn(Map.of("task1", task));
        when(userDAO.load()).thenReturn(Map.of("builder1", builder));

        service.assignBuilderToTask("task1", "builder1");

        assertTrue(task.getBuilderList().contains("builder1"));
        assertTrue(builder.getTaskList().contains("task1"));

        verify(taskDAO).save(any());
        verify(userDAO).save(any());
    }

    // ❌ TASK NOT FOUND
    @Test
    void assignBuilder_taskNotFound_shouldThrow() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.assignBuilderToTask("task1", "builder"));
    }

    // ❌ USER NOT FOUND
    @Test
    void assignBuilder_userNotFound_shouldThrow() {

        Task task = new Task();
        task.setBuilderList(new HashSet<>());

        when(taskDAO.load()).thenReturn(Map.of("task1", task));
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.assignBuilderToTask("task1", "builder"));
    }

    // ❌ NOT A BUILDER
    @Test
    void assignBuilder_invalidRole_shouldThrow() {

        Task task = new Task();
        task.setBuilderList(new HashSet<>());

        User user = mock(User.class);

        when(taskDAO.load()).thenReturn(Map.of("task1", task));
        when(userDAO.load()).thenReturn(Map.of("x", user));

        assertThrows(RuntimeException.class,
                () -> service.assignBuilderToTask("task1", "x"));
    }

    // ❌ DUPLICATE BUILDER
    @Test
    void assignBuilder_alreadyAssigned_shouldThrow() {

        Task task = new Task();
        task.setBuilderList(new HashSet<>(Set.of("builder1")));

        Builder builder = new Builder();

        when(taskDAO.load()).thenReturn(Map.of("task1", task));
        when(userDAO.load()).thenReturn(Map.of("builder1", builder));

        assertThrows(RuntimeException.class,
                () -> service.assignBuilderToTask("task1", "builder1"));
    }
}
