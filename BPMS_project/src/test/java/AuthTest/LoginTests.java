package AuthTest;

import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Login;
import com.zeta.service.AuthService.Register;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {

    private final Login login = new Login();
    Register reg;
    @BeforeEach
    void setLogin(){
        reg = new Register();
    }

    @Test
    void testLoginWithValidUser() throws UserNotFoundException, InvalidPasswordException, RoleMismatchException {
        reg.register("abc", "1234", ROLE.MANAGER);
        ROLE role = login.login("abc", "1234");
        assertEquals(ROLE.MANAGER, role);
    }

    @Test
    void testLoginWithInvalidUser() {
        assertThrows(UserNotFoundException.class, () -> {
            login.login("ddv", "874r6876");
        });
    }

    @Test
    void testLoginWithWrongPassword() throws RoleMismatchException {
        reg.register("kkk", "345", ROLE.MANAGER);
        assertThrows(InvalidPasswordException.class, () -> {
            login.login("kkk", "445");
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
