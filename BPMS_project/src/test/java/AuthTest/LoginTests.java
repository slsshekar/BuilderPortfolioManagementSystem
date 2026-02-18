package AuthTest;

import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.ROLE;
import com.zeta.model.User;
import com.zeta.service.AuthService.Login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginTests {

    private UserDAO userDAO;
    private Login login;

    @BeforeEach
    void setup() {
        userDAO = mock(UserDAO.class);
        login = new Login(userDAO);
    }

    // ================= VALID LOGIN =================

    @Test
    void login_validCredentials_shouldReturnRole() throws Exception {

        // mock abstract class
        User user = mock(User.class);

        when(user.getPassword()).thenReturn("1234");
        when(user.getRole()).thenReturn(ROLE.MANAGER);

        when(userDAO.load()).thenReturn(Map.of("john", user));

        ROLE result = login.login("john", "1234");

        assertEquals(ROLE.MANAGER, result);
        verify(userDAO).load();
    }

    // ================= USER NOT FOUND =================

    @Test
    void login_userNotFound_shouldThrowException() {

        when(userDAO.load()).thenReturn(Map.of());

        assertThrows(UserNotFoundException.class,
                () -> login.login("unknown", "1234"));
    }

    // ================= WRONG PASSWORD =================

    @Test
    void login_wrongPassword_shouldThrowException() {

        User user = mock(User.class);
        when(user.getPassword()).thenReturn("correct");

        when(userDAO.load()).thenReturn(Map.of("john", user));

        assertThrows(InvalidPasswordException.class,
                () -> login.login("john", "wrong"));
    }

    // ================= INVALID USERNAME =================

    @Test
    void login_blankUsername_shouldThrowIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> login.login(" ", "1234"));
    }

    // ================= INVALID PASSWORD =================

    @Test
    void login_blankPassword_shouldThrowIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> login.login("john", " "));
    }
}
