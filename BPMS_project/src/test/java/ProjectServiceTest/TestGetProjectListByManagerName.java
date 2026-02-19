package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.Manager;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestGetProjectListByManagerName {

    private ProjectDAO projectDAO;
    private UserDAO userDAO;
    private ProjectService projectService;

    @BeforeEach
    void setup() {
        projectDAO = mock(ProjectDAO.class);
        userDAO = mock(UserDAO.class);
        projectService = new ProjectService(projectDAO, userDAO);
    }

    @Test
    void getProjects_validManager_shouldReturnProjects() throws Exception {

        Manager manager = mock(Manager.class);
        when(manager.getProjectList()).thenReturn(Set.of("ProjectA"));
        when(userDAO.load()).thenReturn(Map.of("manager1", manager));

        Set<String> result = projectService.getProjectsByManagerName("manager1");

        assertTrue(result.contains("ProjectA"));
    }

    @Test
    void getProjects_invalidManager_shouldThrow() {

        when(userDAO.load()).thenReturn(Map.of());

        assertThrows(UserNotFoundException.class,
                () -> projectService.getProjectsByManagerName("manager1"));
    }
}
