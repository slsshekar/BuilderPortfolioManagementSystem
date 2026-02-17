package AuthTest;

import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Login;
import com.zeta.service.AuthService.Register;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {

    private final Login login = new Login();
    @BeforeAll
    static void init() throws RoleMismatchException, RoleMismatchException {
        Register register=new Register();
        register.register("LoginTest-1","1234",ROLE.MANAGER);
    }
    @Test
    void testLoginWithValidUser() throws UserNotFoundException, InvalidPasswordException {
        ROLE role = login.login("LoginTest-1", "1234");
        assertEquals(ROLE.MANAGER, role);
    }

    @Test
    void testLoginWithInvalidUser() {
        assertThrows(UserNotFoundException.class, () -> {
            login.login("ddv", "874r6876");
        });
    }

    @Test
    void testLoginWithWrongPassword() {
        assertThrows(InvalidPasswordException.class, () -> {
            login.login("LoginTest-1", "wrongPassword");
        });
    }

    @Test
    void testLoginWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            login.login(null, "1234");
        });
    }

    @Test
    void testLoginWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            login.login("abc", null);
        });
    }

    @Test
    void testLoginWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            login.login("   ", "1234");
        });
    }

    @Test
    void testLoginWithBlankPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            login.login("abc", "");
        });
    }
}
