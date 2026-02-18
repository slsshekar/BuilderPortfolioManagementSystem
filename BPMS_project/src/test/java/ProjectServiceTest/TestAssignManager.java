package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.*;
import com.zeta.model.*;
import com.zeta.service.ProjectService.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestAssignManager {

    private ProjectDAO projectDAO;
    private UserDAO userDAO;
    private ProjectService service;

    @BeforeEach
    void setup() {
        projectDAO = mock(ProjectDAO.class);
        userDAO = mock(UserDAO.class);
        service = new ProjectService(projectDAO, userDAO);
    }

    @Test
    void assignManager_valid_shouldSucceed() throws Exception {

        Project project = new Project();
        project.setManagerList(new HashSet<>());

        // ✅ use constructor
        Manager manager = new Manager("manager1", "pass", ROLE.MANAGER);
        manager.setProjectList(new HashSet<>());

        when(projectDAO.load()).thenReturn(Map.of("P1", project));
        when(userDAO.load()).thenReturn(Map.of("manager1", manager));

        boolean result = service.assignManager("P1", "manager1");

        assertTrue(result);
        verify(projectDAO).save(any());
        verify(userDAO).save(any());
    }

    @Test
    void assignManager_projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        assertThrows(ProjectDoestNotExistException.class,
                () -> service.assignManager("P1", "m"));
    }

    @Test
    void assignManager_userNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(Map.of("P1", new Project()));
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(UserNotFoundException.class,
                () -> service.assignManager("P1", "m"));
    }

    @Test
    void assignManager_wrongRole_shouldThrow() {

        // mock user with wrong role
        User user = mock(User.class);
        when(user.getRole()).thenReturn(ROLE.CLIENT);

        when(projectDAO.load()).thenReturn(Map.of("P1", new Project()));
        when(userDAO.load()).thenReturn(Map.of("m", user));

        assertThrows(RoleMismatchException.class,
                () -> service.assignManager("P1", "m"));
    }
}
