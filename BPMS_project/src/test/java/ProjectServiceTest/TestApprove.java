//package ProjectServiceTest;
//
//import com.zeta.model.Project;
//import com.zeta.service.ProjectService.ProjectService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class TestApprove {
//    ProjectService projectService;
//    @BeforeEach
//    void setup(){
//        projectService=new ProjectService();
//    }
//    @Test
//    void testValidProjectApproval(){
//        Project project=new Project("tower-1","five floors,near highway", LocalDate.of(2026,10,9),LocalDate.of(2027,10,8));
//        assertTrue(projectService.approve(project));
//    }
//    @Test
//    void testInvalidProjectApproval(){
//        Project project=new Project("test case 1","seven floors",LocalDate.of(2029,10,8),LocalDate.of(2030,9,12));
//        assertThrowsExactly(IllegalArgumentException.class,()->projectService.approve(project));
//    }
//
//}
