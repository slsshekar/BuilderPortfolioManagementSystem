package ProjectServiceTest;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.Client;
import com.zeta.model.Project;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Register;
import com.zeta.model.User;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestGetProjectListByClientName {
    ProjectService projectService;
    @BeforeAll
    static void init() throws ProjectDoestNotExistException, UserNotFoundException, RoleMismatchException, ProjectAlreadyExistsException, ClientDoesNotExistException {
        Project project=new Project("testProject-1","six floors", LocalDate.of(2026,9,9),LocalDate.of(2029,9,9));
        ProjectService projectServiceInit=new ProjectService();
        Register register=new Register();
        register.register("client-1","12345", ROLE.CLIENT);
        projectServiceInit.create(project,"client-1");
        register.register("manager-1","12345", ROLE.MANAGER);

    }
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testGetProjectListWithValidClientName() throws UserNotFoundException {
        Set<String> projectSet=Set.of("testProject-1");
        assertEquals(projectSet,projectService.getProjectsByClientName("client-1"));
    }
    @Test
    void testGetProjectListWithInvalidUser(){
        assertThrowsExactly(UserNotFoundException.class,()->projectService.getProjectsByClientName("invalid"));
    }
    @Test
    void testGetProjectListWithInvalidInput(){
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.getProjectsByClientName(" "));
    }
    @Test
    void testGetProjectListWithNonClient(){
        assertThrowsExactly(ClassCastException.class,()->projectService.getProjectsByClientName("manager-1"));
    }
}
