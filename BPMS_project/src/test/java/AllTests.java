import MainTest.MainTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

// Auth tests
import AuthTest.LoginTests;
import AuthTest.RegisterTest;
import UITest.*;
// Manager Service tests
import ManagerServiceTest.ManagerServiceCreateTaskTest;
import ManagerServiceTest.ManagerServiceGetProjectsByStatusTest;
import ManagerServiceTest.ManagerServiceGetProjectTest;
import ManagerServiceTest.ManagerServiceGetTasksOfProjectTest;

// Project Service tests
import ProjectServiceTest.TestApprove;
import ProjectServiceTest.TestAssignManager;
import ProjectServiceTest.TestCreate;
import ProjectServiceTest.TestGetProjectListByClientName;
import ProjectServiceTest.TestGetProjectListByManagerName;

// Task Assignment tests
import TaskAssignmentServiceTest.TaskAssignmentAssignBuilderTest;
import TaskAssignmentServiceTest.TaskAssignmentGetBuildersOfTaskTest;
import TaskAssignmentServiceTest.TaskAssignmentGetTasksOfBuilderTest;
import TaskAssignmentServiceTest.TaskAssignmentRemoveBuilderTest;

// Task Service tests
import TaskServiceTest.TaskServiceCreateTaskTest;
import TaskServiceTest.TaskServiceGetAllTasksTest;
import TaskServiceTest.TaskServiceGetTaskTest;
import TaskServiceTest.TaskServiceUpdateTaskStatusTest;

// Task Tests (legacy)
import TaskTests.AssignTaskToBuilderTest;
import TaskTests.CreateTaskTest;
import TaskTests.GetTasksByProjectNameTest;
import TaskTests.UpdateTaskStatusTest;

@Suite
@SelectClasses({

                // Auth
                LoginTests.class,
                RegisterTest.class,

                // Manager Service
                ManagerServiceCreateTaskTest.class,
                ManagerServiceGetProjectsByStatusTest.class,
                ManagerServiceGetProjectTest.class,
                ManagerServiceGetTasksOfProjectTest.class,

                // Project Service
                TestApprove.class,
                TestAssignManager.class,
                TestCreate.class,
                TestGetProjectListByClientName.class,
                TestGetProjectListByManagerName.class,

                // Task Assignment Service
                TaskAssignmentAssignBuilderTest.class,
                TaskAssignmentGetBuildersOfTaskTest.class,
                TaskAssignmentGetTasksOfBuilderTest.class,
                TaskAssignmentRemoveBuilderTest.class,

                // Task Service
                TaskServiceCreateTaskTest.class,
                TaskServiceGetAllTasksTest.class,
                TaskServiceGetTaskTest.class,
                TaskServiceUpdateTaskStatusTest.class,

                // Task Tests (legacy)
                AssignTaskToBuilderTest.class,
                CreateTaskTest.class,
                GetTasksByProjectNameTest.class,
                UpdateTaskStatusTest.class,

                // UI Tests
                AdminUITest.class,
                BuilderUITest.class,
                ClientUITest.class,
                LoginUITest.class,
                MainMenuUITest.class,
                ManagerUITest.class,
                RegisterUITest.class,
                MainTest.class

})
public class AllTests {
}
