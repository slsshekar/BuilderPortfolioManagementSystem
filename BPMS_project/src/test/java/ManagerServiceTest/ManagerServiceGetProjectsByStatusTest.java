package ManagerServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.model.Project;
import com.zeta.model.STATUS;
import com.zeta.service.ManagerService.ManagerService;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceGetProjectsByStatusTest {

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
    void shouldGroupProjectsByStatus() {

        Project p1 = new Project();
        p1.setStatus(STATUS.UPCOMING);

        Project p2 = new Project();
        p2.setStatus(STATUS.COMPLETED);

        when(projectDAO.load()).thenReturn(Map.of("p1", p1, "p2", p2));

        Map<STATUS, List<Project>> result =
                service.getProjectsByStatus(Set.of("p1","p2"));

        assertTrue(result.get(STATUS.UPCOMING).contains(p1));
        assertTrue(result.get(STATUS.COMPLETED).contains(p2));
    }

    @Test
    void shouldIgnoreInvalidProjectNames() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        Map<STATUS, List<Project>> result =
                service.getProjectsByStatus(Set.of("invalid"));

        assertTrue(result.isEmpty());
    }
}
