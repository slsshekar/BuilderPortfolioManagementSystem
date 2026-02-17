package AuthTest;

import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Register;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    Register register;

    @BeforeEach
    void setregister() {
        register = new Register();
    }

    @Test
    void registerValidUser() {
        boolean result = register.register("raman", "123", ROLE.MANAGER);
        assertTrue(result);
    }

    @Test
    void registerExistingUser() {
        assertThrowsExactly(IllegalArgumentException.class, () -> register.register("abc", "1234", ROLE.MANAGER));

    }

    @Test
    void registerEmptyUsername() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                register.register("", "1234", ROLE.MANAGER)
        );
    }

    @Test
    void registerNullUsername() {
        assertThrows(IllegalArgumentException.class, () ->
                register.register(null, "1234", ROLE.MANAGER)
        );
    }

    @Test
    void registerNullRole() {
        assertThrows(IllegalArgumentException.class, () ->
                register.register("ram", "123", null)
        );
    }
}
