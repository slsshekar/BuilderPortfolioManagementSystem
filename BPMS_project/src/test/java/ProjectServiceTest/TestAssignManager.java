package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.*;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestAssignManager {

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
    void assignManager_valid_shouldSave() throws Exception {

        Project project = mock(Project.class);
        Manager manager = mock(Manager.class);

<<<<<<< Updated upstream
        when(projectDAO.load()).thenReturn(Map.of("ProjectA", project));
=======
        Manager manager = new Manager("manager1", "pass", ROLE.MANAGER);
        manager.setProjectList(new HashSet<>());

        when(projectDAO.load()).thenReturn(Map.of("P1", project));
>>>>>>> Stashed changes
        when(userDAO.load()).thenReturn(Map.of("manager1", manager));

        when(manager.getRole()).thenReturn(ROLE.MANAGER);
        when(project.getManagerList()).thenReturn(new HashSet<>());
        when(manager.getProjectList()).thenReturn(new HashSet<>());

        boolean result = projectService.assignManager("ProjectA", "manager1");

        assertTrue(result);
        verify(projectDAO).save(any());
        verify(userDAO).save(any());
    }

    @Test
    void assignManager_projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(ProjectDoestNotExistException.class,
                () -> projectService.assignManager("ProjectA", "manager1"));
    }

    @Test
    void assignManager_userNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(Map.of("ProjectA", mock(Project.class)));
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(UserNotFoundException.class,
                () -> projectService.assignManager("ProjectA", "manager1"));
    }

    @Test
    void assignManager_roleMismatch_shouldThrow() {

<<<<<<< Updated upstream
        Project project = mock(Project.class);
=======
>>>>>>> Stashed changes
        User user = mock(User.class);

        when(projectDAO.load()).thenReturn(Map.of("ProjectA", project));
        when(userDAO.load()).thenReturn(Map.of("manager1", user));
        when(user.getRole()).thenReturn(ROLE.CLIENT);

        assertThrows(RoleMismatchException.class,
                () -> projectService.assignManager("ProjectA", "manager1"));
    }
}
