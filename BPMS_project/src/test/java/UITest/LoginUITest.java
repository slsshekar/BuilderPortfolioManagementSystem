package UITest;

import com.zeta.UI.LoginUI;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUITest {

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
        LoginUI.show(new Scanner(System.in));
        return output.toString();
    }


    @Test
    void shouldDisplayLoginPrompt() {
        String console = runUI("user\npass\n");

        assertTrue(console.contains("=== Login ==="));
        assertTrue(console.contains("Enter username"));
        assertTrue(console.contains("Enter password"));
    }


    @Test
    void adminLogin_shouldWelcomeAdmin() {

        String console = runUI("""
            admin
            admin
            5
            """);

        assertTrue(console.contains("Welcome Admin!"));
    }


    @Test
    void invalidUser_shouldShowLoginFailed() {
        String console = runUI("invalidUser\n1234\n");

        assertTrue(console.contains("Login failed"));
    }

    @Test
    void wrongPassword_shouldShowLoginFailed() {
        String console = runUI("someUser\nwrongPassword\n");

        assertTrue(console.contains("Login failed"));
    }


    @Test
    void blankUsername_shouldShowError() {
        String console = runUI(" \npass\n");

        assertTrue(console.contains("Login failed"));
    }

    @Test
    void blankPassword_shouldShowError() {
        String console = runUI("user\n \n");

        assertTrue(console.contains("Login failed"));
    }

    @Test
    void validLogin_shouldRouteBasedOnRole() {
        String console = runUI("admin\nadmin\n5\n");
        assertTrue(console.contains("Welcome Admin!"));
    }

}
