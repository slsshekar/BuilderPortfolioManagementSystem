package UITest;

import com.zeta.UI.ManagerUI;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerUITest {

    private ByteArrayOutputStream output;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setup() {
        originalOut = System.out;
        originalIn = System.in;

        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String runUI(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ManagerUI.show(new Scanner(System.in), "manager1");
        return output.toString();
    }

    // ================= MENU =================

    @Test
    void shouldDisplayManagerMenu() {
        String console = runUI("6\n");

        assertTrue(console.contains("=== Manager Menu ==="));
        assertTrue(console.contains("View Projects"));
        assertTrue(console.contains("Create Task"));
    }

    // ================= LOGOUT =================

    @Test
    void logout_shouldExit() {
        String console = runUI("6\n");

        assertTrue(console.contains("Logging out"));
    }

    // ================= INVALID CHOICE =================

    @Test
    void invalidChoice_shouldShowError() {
        String console = runUI("99\n6\n");

        assertTrue(console.contains("Invalid choice"));
    }

    // ================= VIEW PROJECTS FLOW =================

    @Test
    void viewProjects_flow_shouldExecute() {
        String console = runUI(
                "1\n" +   // view projects
                        "6\n"
        );

        assertTrue(console.contains("PROJECT BOARD"));
    }

    // ================= CREATE TASK FLOW =================

    @Test
    void createTask_shouldAskForProjectName() {

        String console = runUI(
                "2\n" +           // create task
                        "ProjectA\n" +    // project name
                        "6\n"
        );

        assertTrue(console.contains("Enter project name"));
    }

    @Test
    void createTask_projectNotAssigned_shouldStopFlow() {

        String console = runUI(
                "2\n" +
                        "UnknownProject\n" +
                        "6\n"
        );

        assertTrue(console.contains("Project not assigned to you")
                || console.contains("Error"));
    }

    // ================= VIEW PROJECT DETAILS =================

    @Test
    void viewProject_shouldAskForProjectName() {

        String console = runUI(
                "3\n" +
                        "ProjectA\n" +
                        "6\n"
        );

        assertTrue(console.contains("Enter project name"));
    }

    // ================= ASSIGN BUILDER =================

    @Test
    void assignBuilder_shouldAskForInputs() {

        String console = runUI(
                "4\n" +
                        "ProjectA\n" +
                        "Task1\n" +
                        "builder1\n" +
                        "6\n"
        );

        assertTrue(console.contains("Enter project name"));
        assertTrue(console.contains("Enter task name"));
        assertTrue(console.contains("Enter builder name"));
    }

    // ================= UPDATE TASK STATUS =================

    @Test
    void updateTaskStatus_shouldAskForInputs() {

        String console = runUI(
                "5\n" +
                        "Task1\n" +
                        "IN_PROGRESS\n" +
                        "6\n"
        );

        assertTrue(console.contains("Enter task name"));
        assertTrue(console.contains("Enter status"));
    }

    // ================= ERROR HANDLING PATH =================

    @Test
    void exception_shouldBeHandledGracefully() {

        String console = runUI(
                "5\n" +
                        "Task1\n" +
                        "INVALID_STATUS\n" +  // triggers exception
                        "6\n"
        );

        assertTrue(console.contains("Error"));
    }
}
