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

import static org.junit.jupiter.api.Assertions.*;

public class TestCreate {
    ProjectService projectService;
    @BeforeAll
    static void init() throws RoleMismatchException {
        Register register=new Register();
        register.register("testCreateClient","1234", ROLE.CLIENT);
    }
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testCreate() throws ProjectAlreadyExistsException, ClientDoesNotExistException {
        Project project=new Project("testCreate-1","five floors,near highway", LocalDate.of(2026,10,9),LocalDate.of(2027,10,8));
        assertTrue(projectService.create(project, "testCreateClient"));
    }
    @Test
    void testCreateWithInvalidClient() throws ProjectAlreadyExistsException,ClientDoesNotExistException{
        Project project=new Project("tower-2","5 floors",LocalDate.of(2026,10,9),LocalDate.of(2028,10,8));
        assertThrowsExactly(ClientDoesNotExistException.class,()->projectService.create(project,"invalidClient"));
    }
    @Test
    void testCreateWithNullClientName(){
        Project project=new Project("tower-3","6 floors",LocalDate.of(2026,8,9),LocalDate.of(2028,10,8));
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.create(project,""));
    }
    @Test
    void testCreateWithNullDescription(){
        Project project=new Project("tower dummy","",LocalDate.of(2026,9,6),LocalDate.of(2027,9,12));
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.create(project,"abc"));
    }
    @Test
    void testCreateWithNullProjectName(){
        Project project=new Project(" ","dummy description",LocalDate.of(2026,9,6),LocalDate.of(2027,9,12));
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.create(project,"abc"));
    }
}
