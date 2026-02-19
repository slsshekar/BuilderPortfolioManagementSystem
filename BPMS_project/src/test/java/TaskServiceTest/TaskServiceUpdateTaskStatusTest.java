package TaskServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.TaskException.InvalidTaskException;
import com.zeta.Exceptions.TaskException.TaskNotFoundException;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceUpdateTaskStatusTest {

    private TaskDAO taskDAO;
    private TaskService taskService;
    private UserDAO userDAO;
    private ProjectDAO projectDAO;
    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        taskService = new TaskService(taskDAO,userDAO,projectDAO);
    }

    @Test
    void updateTaskStatus_valid_shouldUpdateAndSave() throws Exception {

        Task task = mock(Task.class);

        when(task.getName()).thenReturn("Task1");

        Map<String, Task> taskMap = new HashMap<>();
        taskMap.put("Task1", task);

        when(taskDAO.load()).thenReturn(taskMap);

        boolean result = taskService.updateTaskStatus(task.getName(), STATUS.IN_PROGRESS);

        assertTrue(result);
        verify(task).setStatus(STATUS.IN_PROGRESS);
        verify(taskDAO).save(taskMap);
    }

    @Test
    void updateTaskStatus_nullTask_shouldThrow() {

        assertThrows(InvalidTaskException.class,
                () -> taskService.updateTaskStatus(null, STATUS.IN_PROGRESS));
    }

    @Test
    void updateTaskStatus_nullStatus_shouldThrow() {

        Task task = mock(Task.class);

        assertThrows(InvalidTaskException.class,
                () -> taskService.updateTaskStatus(task.getName(), null));
    }

    @Test
    void updateTaskStatus_taskNotFound_shouldThrow() {

        Task task = mock(Task.class);

        when(task.getName()).thenReturn("Task1");
        when(taskDAO.load()).thenReturn(new HashMap<>());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.updateTaskStatus(task.getName(), STATUS.COMPLETED));
    }

    @Test
    void updateTaskStatus_shouldCallLoadAndSave() throws Exception {

        Task task = mock(Task.class);

        when(task.getName()).thenReturn("Task1");

        Map<String, Task> taskMap = new HashMap<>();
        taskMap.put("Task1", task);

        when(taskDAO.load()).thenReturn(taskMap);

        taskService.updateTaskStatus(task.getName(), STATUS.COMPLETED);

        verify(taskDAO).load();
        verify(taskDAO).save(taskMap);
    }

    @Test
    void updateTaskStatus_shouldUpdateCorrectTaskOnly() throws Exception {

        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);

        when(task1.getName()).thenReturn("Task1");
        when(task2.getName()).thenReturn("Task2");

        Map<String, Task> taskMap = new HashMap<>();
        taskMap.put("Task1", task1);
        taskMap.put("Task2", task2);

        when(taskDAO.load()).thenReturn(taskMap);

        taskService.updateTaskStatus(task1.getName(), STATUS.COMPLETED);

        verify(task1).setStatus(STATUS.COMPLETED);
        verify(task2, never()).setStatus(any());
    }
}
