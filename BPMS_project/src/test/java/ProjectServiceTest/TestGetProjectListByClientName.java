package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.Client;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

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
    void getProjects_validClient_shouldReturnProjects() throws Exception {

        Client client = mock(Client.class);
        when(client.getProjectList()).thenReturn(Set.of("ProjectA"));
        when(userDAO.load()).thenReturn(Map.of("client1", client));

        Set<String> result = projectService.getProjectsByClientName("client1");

        assertTrue(result.contains("ProjectA"));
    }

    @Test
    void getProjects_invalidClient_shouldThrow() {

        when(userDAO.load()).thenReturn(Map.of());

        assertThrows(UserNotFoundException.class,
                () -> projectService.getProjectsByClientName("client1"));
    }
}
