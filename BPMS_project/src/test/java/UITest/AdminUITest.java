package UITest;

import com.zeta.UI.AdminUI;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AdminUITest {

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
        AdminUI.show(new Scanner(System.in));
        return output.toString();
    }


    @Test
    void shouldDisplayAdminMenu() {
        String console = runUI("6\n");

        assertTrue(console.contains("Admin Menu"));
        assertTrue(console.contains("Show all unapproved projects"));
        assertTrue(console.contains("Approve project"));
    }

    @Test
    void invalidChoice_shouldShowError() {
        String console = runUI("99\n6\n");

        assertTrue(console.contains("Please enter a valid number"));
    }


    @Test
    void showUnapprovedProjects_shouldExecute() {
        String console = runUI("1\n6\n");

        assertTrue(
                console.contains("Unapproved Projects") ||
                        console.contains("No unapproved projects")
        );
    }


    @Test
    void showApprovedProjects_shouldExecute() {
        String console = runUI("2\n6\n");

        assertTrue(
                console.contains("Approved Projects") ||
                        console.contains("No approved projects")
        );
    }

    @Test
    void approveProject_invalidDates_shouldShowError() {

        String console = runUI(
                "3\n" +
                        "ProjectA\n" +
                        "10-10-2025\n" +
                        "01-10-2025\n" +
                        "6\n"
        );

        assertTrue(console.contains("End date cannot be"));
    }
    @Test
    void assignManager_shouldAskForInputs() {

        String console = runUI(
                "4\n" +
                        "ProjectA\n" +
                        "manager1\n" +
                        "6\n"
        );

        assertTrue(console.contains("Enter project name"));
        assertTrue(console.contains("Enter manager name"));
    }


    @Test
    void viewProjectBoard_shouldDisplayBoard() {
        String console = runUI("5\n6\n");

        assertTrue(console.contains("PROJECT BOARD"));
    }
}
