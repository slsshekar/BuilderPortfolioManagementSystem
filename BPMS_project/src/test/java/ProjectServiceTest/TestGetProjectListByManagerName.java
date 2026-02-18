package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.Client;
import com.zeta.model.Manager;
import com.zeta.model.ROLE;
import com.zeta.service.ProjectService.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
    void testGetProjectListWithValidManagerName() throws Exception {

        Manager manager = new Manager("manager-2", "12345", ROLE.MANAGER);
        manager.setProjectList(Set.of("testProject-2"));

        when(userDAO.load())
                .thenReturn(Map.of("manager-2", manager));

        Set<String> result =
                projectService.getProjectsByManagerName("manager-2");

        assertEquals(Set.of("testProject-2"), result);
    }

    @Test
    void testGetProjectListWithInvalidUser() {

        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(UserNotFoundException.class,
                () -> projectService.getProjectsByManagerName("invalid"));
    }

    @Test
    void testGetProjectListWithInvalidInput() {

        assertThrows(IllegalArgumentException.class,
                () -> projectService.getProjectsByManagerName(" "));
    }

    @Test
    void testGetProjectListWithNonManager() {

        Client client = new Client();

        when(userDAO.load())
                .thenReturn(Map.of("client-2", client));

        assertThrows(UserNotFoundException.class,
                () -> projectService.getProjectsByManagerName("client-2"));
    }
}
