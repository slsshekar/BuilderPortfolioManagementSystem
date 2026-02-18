package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.model.Project;
import com.zeta.service.ProjectService.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestApprove {

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
    void approve_validProject_shouldSucceed() throws Exception {

        Project project = new Project();
        Map<String, Project> map = new HashMap<>();
        map.put("P1", project);

        when(projectDAO.load()).thenReturn(map);

        boolean result = service.approve("P1",
                LocalDate.now(),
                LocalDate.now().plusDays(1));

        assertTrue(result);
        verify(projectDAO).save(map);
    }

    @Test
    void approve_projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        assertThrows(ProjectDoestNotExistException.class,
                () -> service.approve("P1",
                        LocalDate.now(),
                        LocalDate.now()));
    }
}
