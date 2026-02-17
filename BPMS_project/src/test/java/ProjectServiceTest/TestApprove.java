package ProjectServiceTest;

import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
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

public class TestApprove {
    ProjectService projectService;
    @BeforeAll
    static void init() throws RoleMismatchException, ProjectAlreadyExistsException, ClientDoesNotExistException {
        Register register=new Register();
        register.register("approveClient","1234", ROLE.CLIENT);
        Project project=new Project("testApprove-1","test aprrove description",LocalDate.of(2026,9,12),LocalDate.of(2028,9,10));
        ProjectService projectService=new ProjectService();
        projectService.create(project,"approveClient");
    }
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testValidProjectApproval(){
        assertTrue(projectService.approve("testApprove-1"));
    }
    @Test
    void testInvalidProjectApproval(){
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.approve("test case 1"));
    }

}
