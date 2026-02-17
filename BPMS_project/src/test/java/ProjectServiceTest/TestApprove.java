package ProjectServiceTest;

import com.zeta.model.Project;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestApprove {
    ProjectService projectService;
    @BeforeEach
    void setup(){
        projectService=new ProjectService();
    }
    @Test
    void testValidProjectApproval(){
        assertTrue(projectService.approve("tower-1"));
    }
    @Test
    void testInvalidProjectApproval(){
        assertThrowsExactly(IllegalArgumentException.class,()->projectService.approve("test case 1"));
    }

}
