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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestGetProjectListByManagerName {
    ProjectService projectService;
    @BeforeAll
    static void init() throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException, ProjectAlreadyExistsException, ClientDoesNotExistException {
        Project project=new Project("testProject-2","six floors", LocalDate.of(2026,9,9),LocalDate.of(2029,9,9));
        ProjectService projectServiceInit=new ProjectService();
        Register register=new Register();
        register.register("client-2","12345", ROLE.CLIENT);
        projectServiceInit.create(project,"client-2");
        register.register("manager-2","12345", ROLE.MANAGER);
        projectServiceInit.assignManager("testProject-2","manager-2");

    }
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testGetProjectListWithValidManagerName() throws UserNotFoundException {
        Set<String> projectSet=Set.of("testProject-2");
        assertEquals(projectSet,projectService.getProjectsByManagerName("manager-2"));
    }
    @Test
    void testGetProjectListWithInvalidUser(){
        assertThrowsExactly(UserNotFoundException.class,()->projectService.getProjectsByManagerName("invalid"));
    }
    @Test
    void testGetProjectListWithInvalidInput(){
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.getProjectsByManagerName(" "));
    }
    @Test
    void testGetProjectListWithNonManager(){
        assertThrowsExactly(ClassCastException.class,()->projectService.getProjectsByManagerName("client-2"));
    }
}
