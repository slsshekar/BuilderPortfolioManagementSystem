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
                TaskTests.AssignTaskToBuilderTest.class,
                TaskTests.CreateTaskTest.class,
                TaskTests.GetTasksByProjectNameTest.class,
                TaskTests.UpdateTaskStatusTest.class
})
public class AllTests {
}