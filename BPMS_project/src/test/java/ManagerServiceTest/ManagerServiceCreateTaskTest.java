package ManagerServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.model.*;
import com.zeta.service.ManagerService.ManagerService;
import com.zeta.service.TaskService.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceCreateTaskTest {

    private ProjectDAO projectDAO;
    private TaskService taskService;
    private ManagerService service;

    @BeforeEach
    void setup() {
        projectDAO = mock(ProjectDAO.class);
        taskService = mock(TaskService.class);
        service = new ManagerService(projectDAO, taskService);
    }

    // ✅ SUCCESS CASE
    @Test
    void createTask_valid_shouldCreateTaskAndUpdateProject() {

        Project project = new Project();
        project.setManagerList(Set.of("manager1"));
        project.setTaskList(new HashSet<>());
        project.setStatus(STATUS.UPCOMING);

        Map<String, Project> projectMap = new HashMap<>();
        projectMap.put("P1", project);

        when(projectDAO.load()).thenReturn(projectMap);

        service.createTaskForProject("P1","task1","desc",
                "manager1", LocalDate.now(), LocalDate.now());

        verify(taskService).createTask(any(), any(), any(), any(), any(), any());
        verify(projectDAO).save(projectMap);

        assertTrue(project.getTaskList().contains("task1"));
        assertEquals(STATUS.IN_PROGRESS, project.getStatus());
    }

    // ❌ PROJECT NOT FOUND
    @Test
    void createTask_projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        assertThrows(RuntimeException.class,
                () -> service.createTaskForProject("P","task","d",
                        "manager", LocalDate.now(), LocalDate.now()));
    }

    // ❌ MANAGER NOT ASSIGNED
    @Test
    void createTask_managerNotAssigned_shouldThrow() {

        Project project = new Project();
        project.setManagerList(Set.of("otherManager"));

        when(projectDAO.load()).thenReturn(Map.of("P1", project));

        assertThrows(RuntimeException.class,
                () -> service.createTaskForProject("P1","task","d",
                        "manager1", LocalDate.now(), LocalDate.now()));
    }
}
