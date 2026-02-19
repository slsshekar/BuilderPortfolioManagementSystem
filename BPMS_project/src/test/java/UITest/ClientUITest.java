package UITest;

import com.zeta.UI.ClientUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ClientUITest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }


    @Test
    void show_shouldDisplayMenu() {

        String input = "3\n"; // logout immediately
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ClientUI.show(new Scanner(System.in), "client1");

        String console = output.toString();

        assertTrue(console.contains("Client Menu"));
        assertTrue(console.contains("Create Project"));
        assertTrue(console.contains("Get Project Status"));
    }

    @Test
    void show_invalidChoice_shouldPrintError() {

        String input = "9\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ClientUI.show(new Scanner(System.in), "client1");

        assertTrue(output.toString().contains("Please enter a valid number"));
    }


    @Test
    void show_logout_shouldExit() {

        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ClientUI.show(new Scanner(System.in), "client1");

        assertTrue(output.toString().contains("Logged out successfully"));
    }


    @Test
    void createProject_shouldAskInputs() {

        String input =
                "1\n" +        // create project
                        "ProjectA\n" +
                        "Test Description\n" +
                        "3\n";         // logout

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ClientUI.show(new Scanner(System.in), "client1");

        String console = output.toString();

        assertTrue(console.contains("Enter project name"));
        assertTrue(console.contains("Enter project description"));
    }


    @Test
    void getProjectStatus_shouldExecuteFlow() {

        String input =
                "2\n" +   // get project status
                        "3\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ClientUI.show(new Scanner(System.in), "client1");

        String console = output.toString();

        assertTrue(
                console.contains("Your Projects") ||
                        console.contains("No projects found") ||
                        console.contains("Error")
        );
    }

}
