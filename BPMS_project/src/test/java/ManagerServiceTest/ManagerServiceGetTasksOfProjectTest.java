package ManagerServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.model.Project;
import com.zeta.model.Task;
import com.zeta.service.ManagerService.ManagerService;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceGetTasksOfProjectTest {

    private ProjectDAO projectDAO;
    private TaskService taskService;
    private ManagerService service;

    @BeforeEach
    void setup() {
        projectDAO = mock(ProjectDAO.class);
        taskService = mock(TaskService.class);
        service = new ManagerService(projectDAO, taskService);
    }

    @Test
    void shouldReturnTasksOfProject() {

        Project project = new Project();
        project.setTaskList(Set.of("task1"));

        Task task = new Task();

        when(projectDAO.load()).thenReturn(Map.of("P1", project));
        when(taskService.getAllTasks()).thenReturn(Map.of("task1", task));

        List<Task> result = service.getTasksOfProject("P1");

        assertEquals(1, result.size());
    }

    @Test
    void projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.getTasksOfProject("P1"));
    }

    @Test
    void shouldIgnoreMissingTasks() {

        Project project = new Project();
        project.setTaskList(Set.of("missing"));

        when(projectDAO.load()).thenReturn(Map.of("P1", project));
        when(taskService.getAllTasks()).thenReturn(new HashMap<>());

        List<Task> result = service.getTasksOfProject("P1");

        assertTrue(result.isEmpty());
    }
}
