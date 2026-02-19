package ProjectServiceTest;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.model.Project;
import com.zeta.model.STATUS;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestApprove {

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
    void approve_validProject_shouldSetDatesAndStatus() throws Exception {

        Project project = mock(Project.class);

        Map<String, Project> projectMap = new HashMap<>();
        projectMap.put("ProjectA", project);

        when(projectDAO.load()).thenReturn(projectMap);

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(10);

        projectService.approve("ProjectA", start, end);

        verify(project).setStartDate(start);
        verify(project).setEndDate(end);
        verify(project).setStatus(STATUS.UPCOMING);
        verify(projectDAO).save(projectMap);
    }

    @Test
    void approve_projectNotFound_shouldThrow() {

        when(projectDAO.load()).thenReturn(new HashMap<>());

        assertThrows(ProjectDoestNotExistException.class,
                () -> projectService.approve(
                        "ProjectA",
                        LocalDate.now(),
                        LocalDate.now().plusDays(5)
                ));
    }


    @Test
    void approve_shouldCallLoadAndSave() throws Exception {

        Project project = mock(Project.class);

        Map<String, Project> projectMap = new HashMap<>();
        projectMap.put("ProjectA", project);

        when(projectDAO.load()).thenReturn(projectMap);

        projectService.approve(
                "ProjectA",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        verify(projectDAO).load();
        verify(projectDAO).save(projectMap);
    }

    @Test
    void approve_shouldUpdateCorrectProjectOnly() throws Exception {

        Project project1 = mock(Project.class);
        Project project2 = mock(Project.class);

        Map<String, Project> projectMap = new HashMap<>();
        projectMap.put("ProjectA", project1);
        projectMap.put("ProjectB", project2);

        when(projectDAO.load()).thenReturn(projectMap);

        projectService.approve(
                "ProjectA",
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        verify(project1).setStatus(STATUS.UPCOMING);
        verify(project2, never()).setStatus(any());
    }
}
