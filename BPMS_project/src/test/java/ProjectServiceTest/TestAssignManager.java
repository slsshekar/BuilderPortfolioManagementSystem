package ProjectServiceTest;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.Project;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Register;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssignManager {
    ProjectService projectService;

    @BeforeAll
    static void init() throws RoleMismatchException, ProjectAlreadyExistsException, ClientDoesNotExistException {
        Register register = new Register();
        register.register("assignManager", "1234", ROLE.MANAGER);
        register.register("assignManager2", "1234", ROLE.MANAGER);
        register.register("assignClient", "1234", ROLE.CLIENT);
        Project project = new Project("testAssign-1", "test assign description", LocalDate.of(2026, 9, 12),
                LocalDate.of(2028, 9, 10));
        ProjectService projectService = new ProjectService();
        projectService.create(project, "assignClient");
    }

    @BeforeEach
    void setup() {
        projectService = new ProjectService();
    }

    @Test
    void testAssignManagerWithValidInputs()
            throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {
        assertTrue(projectService.assignManager("testAssign-1", "assignManager"));
    }

    @Test
    void testAssignManagerWithInvalidProject() {
        assertThrowsExactly(ProjectDoestNotExistException.class, () -> projectService.assignManager("tower", "abc"));
    }

    @Test
    void testAssignInvalidManager() {
        assertThrowsExactly(UserNotFoundException.class, () -> projectService.assignManager("testAssign-1", "xyz"));
    }

    @Test
    void testAssignNonManager() {
        assertThrowsExactly(RoleMismatchException.class,
                () -> projectService.assignManager("testAssign-1", "assignClient"));
    }

    @Test
    void testAssignMultipleManagers()
            throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {
        assertTrue(projectService.assignManager("testAssign-1", "assignManager2"));
    }
}
