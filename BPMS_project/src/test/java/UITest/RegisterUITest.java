package UITest;

import com.zeta.UI.RegisterUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterUITest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }


    @Test
    void show_shouldDisplayRegisterMenu() {

        String input = "4\n"; // immediately exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        RegisterUI.show(new java.util.Scanner(System.in));

        String console = output.toString();

        assertTrue(console.contains("Register Menu"));
        assertTrue(console.contains("Register as Client"));
        assertTrue(console.contains("Register as Builder"));
        assertTrue(console.contains("Register as Manager"));
    }

    @Test
    void invalidChoice_shouldShowError() {

        String input = "9\n4\n"; // invalid then exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        RegisterUI.show(new java.util.Scanner(System.in));

        assertTrue(output.toString().contains("Please enter a valid number"));
    }

    @Test
    void registerClient_flow_shouldAskUsernameAndPassword() {

        String input =
                "1\n" +         // client
                        "testUser\n" +
                        "1234\n" +
                        "4\n";          // exit

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        RegisterUI.show(new java.util.Scanner(System.in));

        String console = output.toString();

        assertTrue(console.contains("Enter username"));
        assertTrue(console.contains("Enter password"));
    }

    @Test
    void registerBuilder_flow_shouldAskInputs() {

        String input =
                "2\n" +
                        "builder1\n" +
                        "1234\n" +
                        "4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        RegisterUI.show(new java.util.Scanner(System.in));

        assertTrue(output.toString().contains("Enter username"));
    }

    @Test
    void registerManager_flow_shouldAskInputs() {

        String input =
                "3\n" +
                        "manager1\n" +
                        "1234\n" +
                        "4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        RegisterUI.show(new java.util.Scanner(System.in));

        assertTrue(output.toString().contains("Enter password"));
    }
}
