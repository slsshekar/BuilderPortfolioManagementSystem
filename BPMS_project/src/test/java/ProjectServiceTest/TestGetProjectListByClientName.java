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

public class TestGetProjectListByClientName {

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
    void testGetProjectListWithValidClientName() throws Exception {

        Client client = new Client();
        client.setProjectList(Set.of("testProject-1"));

        when(userDAO.load())
                .thenReturn(Map.of("client-1", client));

        Set<String> result =
                projectService.getProjectsByClientName("client-1");

        assertEquals(Set.of("testProject-1"), result);
    }

    @Test
    void testGetProjectListWithInvalidUser() {

        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(UserNotFoundException.class,
                () -> projectService.getProjectsByClientName("invalid"));
    }

    @Test
    void testGetProjectListWithInvalidInput() {

        assertThrows(IllegalArgumentException.class,
                () -> projectService.getProjectsByClientName(" "));
    }

    @Test
    void testGetProjectListWithNonClient() {

        Manager manager = new Manager("manager-1", "123", ROLE.MANAGER);

        when(userDAO.load())
                .thenReturn(Map.of("manager-1", manager));

        assertThrows(UserNotFoundException.class,
                () -> projectService.getProjectsByClientName("manager-1"));
    }
}
