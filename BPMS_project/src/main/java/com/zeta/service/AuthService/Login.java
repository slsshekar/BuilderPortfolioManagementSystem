package com.zeta.service.AuthService;

import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.ROLE;
import com.zeta.model.User;
import com.zeta.service.utility.Utility;

import java.util.Map;

public class Login {

    private final UserDAO userDAO;
    public Login(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public ROLE login(String username, String password)
            throws UserNotFoundException, InvalidPasswordException {

        Utility.validateInput(username, "Username");
        Utility.validateInput(password, "Password");

        Map<String, User> userList = userDAO.load();

        User user = userList.get(username);

        if (user == null) {
            throw new UserNotFoundException("User not found: " + username);
        }

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException(
                    "Invalid password for user: " + username
            );
        }

        return user.getRole();
    }
}
