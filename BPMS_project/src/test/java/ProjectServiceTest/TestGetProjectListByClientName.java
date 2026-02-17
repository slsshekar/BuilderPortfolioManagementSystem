package ProjectServiceTest;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestGetProjectListByClientName {
    ProjectService projectService;
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testGetProjectListWithValidClientName() throws UserNotFoundException {
        Set<String> projectSet=Set.of("tower-1");
        assertEquals(projectSet,projectService.getProjectsByManagerName("raman"));
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
    void testGetProjectListWithNonClient(){
        assertThrowsExactly(ClassCastException.class,()->projectService.getProjectsByManagerName("raman1"));
    }
}
