package UITest;

import com.zeta.UI.BuilderUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderUITest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // ================= MENU DISPLAY =================

    @Test
    void show_shouldDisplayMenu() {

        String input = "4\n"; // logout immediately
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BuilderUI.show(new Scanner(System.in), "builder1");

        String console = output.toString();

        assertTrue(console.contains("Builder Menu"));
        assertTrue(console.contains("View All Assigned Tasks"));
        assertTrue(console.contains("Logout"));
    }

    // ================= INVALID CHOICE =================

    @Test
    void show_invalidChoice_shouldPrintError() {

        String input = "9\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BuilderUI.show(new Scanner(System.in), "builder1");

        assertTrue(output.toString().contains("Invalid choice"));
    }

    // ================= LOGOUT =================

    @Test
    void show_logout_shouldExit() {

        String input = "4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BuilderUI.show(new Scanner(System.in), "builder1");

        assertTrue(output.toString().contains("Logging out"));
    }

    // ================= VIEW ASSIGNED TASKS FLOW =================

    @Test
    void viewAssignedTasks_shouldExecuteFlow() {

        String input =
                "1\n" + // view tasks
                        "4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BuilderUI.show(new Scanner(System.in), "builder1");

        String console = output.toString();

        // since DB may be empty → expect either
        assertTrue(
                console.contains("Your Tasks") ||
                        console.contains("No tasks assigned")
        );
    }

    // ================= VIEW TASKS BY STATUS FLOW =================

    @Test
    void viewTasksByStatus_shouldAskForStatus() {

        String input =
                "2\n" + // view by status
                        "IN_PROGRESS\n" +
                        "4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BuilderUI.show(new Scanner(System.in), "builder1");

        String console = output.toString();

        assertTrue(console.contains("Available Status") ||
                console.contains("No tasks assigned"));
    }

    // ================= VIEW TASK DETAILS FLOW =================

    @Test
    void viewTaskDetails_shouldAskTaskName() {

        String input =
                "3\n" +
                        "task1\n" +
                        "4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BuilderUI.show(new Scanner(System.in), "builder1");

        assertTrue(output.toString().contains("Enter task name"));
    }

}
