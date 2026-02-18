package MainTest;

import com.zeta.Main;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private String runMain(String input) {

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        try {
            Main.main(new String[]{});
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        return out.toString();
    }

    // ================= STARTUP TEST =================

    @Test
    void main_shouldShowWelcomeMessage() {

        // choose exit immediately
        String console = runMain("3\n");

        assertTrue(console.contains("Welcome to Builder Portfolio Management System"));
        assertTrue(console.contains("Main Menu"));
        assertTrue(console.contains("Exiting the application"));
    }

    // ================= REGISTER FLOW =================

    @Test
    void main_registerFlow_shouldOpenRegisterMenu() {

        // 1 -> Register
        // 4 -> Back
        // 3 -> Exit
        String console = runMain("""
                1
                4
                3
                """);

        assertTrue(console.contains("Register Menu"));
    }

    // ================= LOGIN FLOW =================

    @Test
    void main_loginFlow_shouldOpenLogin() {

        // 2 -> Login
        // give fake login
        // 3 -> exit
        String console = runMain("""
                2
                user
                pass
                3
                """);

        assertTrue(console.contains("Login"));
    }
}
