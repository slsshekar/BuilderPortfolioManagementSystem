//import TaskTests.TaskTests;
import com.zeta.service.ProjectService.ProjectService;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthTest.LoginTests.class,
        AuthTest.RegisterTest.class,
        ProjectServiceTest.TestApprove.class,
        ProjectServiceTest.TestCreate.class,
        ProjectServiceTest.TestGetProjectListByClientName.class,
        ProjectServiceTest.TestGetProjectListByManagerName.class,
//        TaskTests.class
})
public class AllTests {
}