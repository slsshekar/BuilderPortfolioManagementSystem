package AuthTest;

import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.ROLE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zeta.service.AuthService.Register;

import java.io.File;

public class RegisterTest {

    Register register;

    @BeforeEach
    void setregister() throws Exception {
        new File("database/users.json").delete(); // reset data
        register = new Register();
        register.register("abc","1234", ROLE.MANAGER);
    }

    @Test
    void registerValidUser() throws RoleMismatchException {
        boolean result = register.register("raman1", "1234", ROLE.BUILDER);
        assertTrue(result);
    }

    @Test
    void registerExistingUser() {
        assertThrows(IllegalArgumentException.class,
                () -> register.register("abc", "1234", ROLE.MANAGER));
    }

    @Test
    void registerEmptyUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> register.register("", "1234", ROLE.MANAGER));
    }

    @Test
    void registerNullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> register.register(null, "1234", ROLE.MANAGER));
    }

    @Test
    void registerNullRole() {
        assertThrows(IllegalArgumentException.class,
                () -> register.register("ram", "123", null));
    }
}
