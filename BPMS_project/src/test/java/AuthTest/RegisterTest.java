package AuthTest;

import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.*;
import com.zeta.service.AuthService.Register;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterTest {

    private UserDAO userDAO;
    private Register register;

    @BeforeEach
    void setup() {
        userDAO = mock(UserDAO.class);
        register = new Register(userDAO);
    }

    // ================= SUCCESS CASES =================

    @Test
    void register_validManager_shouldCreateUser() throws Exception {

        Map<String, User> users = new HashMap<>();
        when(userDAO.load()).thenReturn(users);

        boolean result = register.register("manager1", "1234", ROLE.MANAGER);

        assertTrue(result);
        assertTrue(users.containsKey("manager1"));
        assertTrue(users.get("manager1") instanceof Manager);

        verify(userDAO).save(users);
    }

    @Test
    void register_validClient_shouldCreateUser() throws Exception {

        Map<String, User> users = new HashMap<>();
        when(userDAO.load()).thenReturn(users);

        boolean result = register.register("client1", "1234", ROLE.CLIENT);

        assertTrue(result);
        assertTrue(users.get("client1") instanceof Client);
    }

    @Test
    void register_validBuilder_shouldCreateUser() throws Exception {

        Map<String, User> users = new HashMap<>();
        when(userDAO.load()).thenReturn(users);

        boolean result = register.register("builder1", "1234", ROLE.BUILDER);

        assertTrue(result);
        assertTrue(users.get("builder1") instanceof Builder);
    }


    // ================= VALIDATION TESTS =================

    @Test
    void register_nullUsername_shouldThrow() {

        assertThrows(IllegalArgumentException.class,
                () -> register.register(null, "1234", ROLE.CLIENT));
    }

    @Test
    void register_blankUsername_shouldThrow() {

        assertThrows(IllegalArgumentException.class,
                () -> register.register("   ", "1234", ROLE.CLIENT));
    }

    @Test
    void register_nullPassword_shouldThrow() {

        assertThrows(IllegalArgumentException.class,
                () -> register.register("user", null, ROLE.CLIENT));
    }

    @Test
    void register_blankPassword_shouldThrow() {

        assertThrows(IllegalArgumentException.class,
                () -> register.register("user", "   ", ROLE.CLIENT));
    }
    @Test
    void register_nullRole_shouldThrowIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class,
                () -> register.register("user", "1234", null));
    }

    // ================= BUSINESS RULE TESTS =================

    @Test
    void register_duplicateUsername_shouldThrow() {

        Map<String, User> users = new HashMap<>();
        users.put("existing", new Client("existing", "1234", ROLE.CLIENT));

        when(userDAO.load()).thenReturn(users);

        assertThrows(IllegalArgumentException.class,
                () -> register.register("existing", "1234", ROLE.CLIENT));

        verify(userDAO, never()).save(any());
    }


}
