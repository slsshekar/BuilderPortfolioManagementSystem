package UITest;

import com.zeta.UI.AdminUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class AdminUITest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // ================= MENU PRINT TEST =================

    @Test
    void show_shouldDisplayMenu() {

        String input = "5\n"; // logout immediately
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        AdminUI.show(new java.util.Scanner(System.in));

        String console = output.toString();

        assertTrue(console.contains("Admin Menu"));
        assertTrue(console.contains("Show all unapproved projects"));
        assertTrue(console.contains("Logout"));
    }

    // ================= INVALID CHOICE =================

    @Test
    void show_invalidChoice_shouldPrintError() {

        String input = "9\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        AdminUI.show(new java.util.Scanner(System.in));

        assertTrue(output.toString().contains("Please enter a valid number"));
    }

    // ================= LOGOUT =================

    @Test
    void show_logout_shouldExit() {

        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        AdminUI.show(new java.util.Scanner(System.in));

        assertTrue(output.toString().contains("Logged out successfully"));
    }

    // ================= APPROVE PROJECT INVALID DATE =================

    @Test
    void approveProject_endDateBeforeStart_shouldShowError() {

        String input =
                "3\n" +                  // approve project
                        "project1\n" +
                        "10-10-2026\n" +        // start date
                        "01-01-2026\n" +        // end date (invalid)
                        "5\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        AdminUI.show(new java.util.Scanner(System.in));

        assertTrue(output.toString().contains("End date cannot be before start date"));
    }

    // ================= ASSIGN MANAGER FLOW =================

    @Test
    void assignManager_shouldAskInputs() {

        String input =
                "4\n" +      // assign manager
                        "project1\n" +
                        "manager1\n" +
                        "5\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        AdminUI.show(new java.util.Scanner(System.in));

        String console = output.toString();

        assertTrue(console.contains("Enter project name"));
        assertTrue(console.contains("Enter manager name"));
    }

}
