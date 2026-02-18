package UITest;

import com.zeta.UI.MainMenuUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuUITest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // ================= SHOW WELCOME =================

    @Test
    void show_shouldDisplayWelcomeMessage() {

        // choose exit immediately
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenuUI.show();

        assertTrue(output.toString()
                .contains("Welcome to Builder Portfolio Management System"));
    }

    // ================= DISPLAY MENU =================

    @Test
    void show_shouldDisplayMainMenuOptions() {

        String input = "3\n"; // exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenuUI.show();

        String console = output.toString();

        assertTrue(console.contains("Main Menu"));
        assertTrue(console.contains("Register"));
        assertTrue(console.contains("Login"));
        assertTrue(console.contains("Exit"));
    }

    // ================= EXIT OPTION =================

    @Test
    void choosingExit_shouldTerminateApplication() {

        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenuUI.show();

        assertTrue(output.toString().contains("Exiting the application"));
    }

    // ================= INVALID OPTION =================

    @Test
    void invalidChoice_shouldShowErrorMessage() {

        String input = "9\n3\n"; // invalid then exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenuUI.show();

        assertTrue(output.toString().contains("valid number"));
    }

    // ================= REGISTER ROUTING =================
    // only checks navigation happens (not full RegisterUI)

    @Test
    void choosingRegister_shouldNavigateToRegisterMenu() {

        String input =
                "1\n" +   // register
                        "4\n" +   // back from register menu
                        "3\n";    // exit

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenuUI.show();

        assertTrue(output.toString().contains("Register Menu"));
    }

    // ================= LOGIN ROUTING =================

    @Test
    void choosingLogin_shouldNavigateToLoginScreen() {

        String input =
                "2\n" +
                        "wrong\n123\n" + // login fails
                        "3\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenuUI.show();

        assertTrue(output.toString().contains("Login"));
    }
}
