package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
import com.zeta.model.Client;
import com.zeta.model.Project;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestCreate {

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
    void create_validProject_shouldSave() throws Exception {

        Project project = mock(Project.class);
        Client client = mock(Client.class);

        when(project.getName()).thenReturn("ProjectA");
        when(project.getDescription()).thenReturn("desc");

        when(projectDAO.load()).thenReturn(new HashMap<>());
        when(userDAO.load()).thenReturn(Map.of("client1", client));
        when(client.getProjectList()).thenReturn(new HashSet<>());

        boolean result = projectService.create(project, "client1");

        assertTrue(result);
        verify(projectDAO).save(any());
        verify(userDAO).save(any());
    }

    @Test
    void create_projectExists_shouldThrow() {

        Project project = mock(Project.class);
        Client client = mock(Client.class);

        when(project.getName()).thenReturn("ProjectA");
        when(project.getDescription()).thenReturn("desc");

        when(projectDAO.load()).thenReturn(Map.of("ProjectA", project));
        when(userDAO.load()).thenReturn(Map.of("client1", client));

        assertThrows(ProjectAlreadyExistsException.class,
                () -> projectService.create(project, "client1"));
    }

    @Test
    void create_clientNotExist_shouldThrow() {

        Project project = mock(Project.class);

        when(project.getName()).thenReturn("ProjectA");
        when(project.getDescription()).thenReturn("desc");

        when(projectDAO.load()).thenReturn(new HashMap<>());
        when(userDAO.load()).thenReturn(new HashMap<>());

        assertThrows(ClientDoesNotExistException.class,
                () -> projectService.create(project, "client1"));
    }
}
