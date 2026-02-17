package ProjectServiceTest;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssignManager {
    ProjectService projectService;
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testAssignManagerWithValidInputs() throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {
        assertTrue(projectService.assignManager("tower-1","abc"));
    }
    @Test
    void testAssignManagerWithInvalidProject(){
        assertThrowsExactly(ProjectDoestNotExistException.class,()->projectService.assignManager("tower","abc"));
    }
    @Test
    void testAssignInvalidManager(){
        assertThrowsExactly(UserNotFoundException.class,()->projectService.assignManager("tower-1","xyz"));
    }
    @Test
    void testAssignNonManager(){
        assertThrowsExactly(ClassCastException.class,()->projectService.assignManager("tower-1","raman1"));
    }
    @Test
    void testAssignMultipleManagers() throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException {
        assertTrue(projectService.assignManager("tower-1","raman"));
    }
}
