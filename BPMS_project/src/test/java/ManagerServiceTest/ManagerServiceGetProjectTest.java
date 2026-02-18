package ManagerServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.model.Project;
import com.zeta.service.ManagerService.ManagerService;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceGetProjectTest {

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
    void shouldReturnProject() {

        Project project = new Project();

        when(projectDAO.load()).thenReturn(Map.of("P1", project));

        assertEquals(project, service.getProject("P1"));
    }

    @Test
    void projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.getProject("P1"));
    }
}
