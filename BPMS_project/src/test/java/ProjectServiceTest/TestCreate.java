package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.*;
import com.zeta.model.*;
import com.zeta.service.ProjectService.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestCreate {

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
    void create_validProject_shouldSucceed() throws Exception {

        Project project = new Project("P1", "desc", null, null);
        Client client = new Client();
        client.setProjectList(new HashSet<>());

        when(projectDAO.load()).thenReturn(new HashMap<>());
        when(userDAO.load()).thenReturn(Map.of("client1", client));

        boolean result = service.create(project, "client1");

        assertTrue(result);
        verify(projectDAO).save(any());
        verify(userDAO).save(any());
    }

    @Test
    void create_duplicateProject_shouldThrow() {

        Project project = new Project();
        project.setName("P1");
        project.setDescription("desc");

        Client client = new Client("client1", "123", ROLE.CLIENT);
        client.setProjectList(new HashSet<>());

        // project already exists
        when(projectDAO.load()).thenReturn(Map.of("P1", project));

        // client must exist (important)
        when(userDAO.load()).thenReturn(Map.of("client1", client));

        assertThrows(ProjectAlreadyExistsException.class,
                () -> service.create(project, "client1"));
    }


    @Test
    void create_clientNotFound_shouldThrow() {

        Project project = new Project("P1", "desc", null, null);

        when(projectDAO.load()).thenReturn(new HashMap<>());
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(ClientDoesNotExistException.class,
                () -> service.create(project, "client1"));
    }
}
