package TaskAssignmentServiceTest;

import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.Builder;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskAssignmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskAssignmentRemoveBuilderTest {

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
    void removeBuilder_valid_shouldRemove() {

        Task task = new Task();
        task.setBuilderList(new HashSet<>(Set.of("builder1")));

        Builder builder = new Builder();
        builder.setTaskList(new HashSet<>(Set.of("task1")));

        when(taskDAO.load()).thenReturn(Map.of("task1", task));
        when(userDAO.load()).thenReturn(Map.of("builder1", builder));

        service.removeBuilderFromTask("task1", "builder1");

        assertFalse(task.getBuilderList().contains("builder1"));
        assertFalse(builder.getTaskList().contains("task1"));

        verify(taskDAO).save(any());
        verify(userDAO).save(any());
    }

    @Test
    void removeBuilder_taskNotFound_shouldThrow() {

        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.removeBuilderFromTask("task1", "builder"));
    }

    @Test
    void removeBuilder_invalidBuilder_shouldThrow() {

        Task task = new Task();
        when(taskDAO.load()).thenReturn(Map.of("task1", task));
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.removeBuilderFromTask("task1", "builder"));
    }
}
