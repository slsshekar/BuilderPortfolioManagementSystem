package com.zeta.service.AuthService;

import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.*;
import com.zeta.DAO.UserDAO;
import com.zeta.service.utility.Utility;

import java.util.Map;

public class Register {

    private final UserDAO userDAO;

    // dependency injection
    public Register(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(String username, String password, ROLE role)
            throws RoleMismatchException {
        Map<String, User> userList = userDAO.load();

        Utility.validateInput(username, "Username");
        Utility.validateInput(password, "Password");

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        if (userList.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = switch (role) {
            case MANAGER -> new Manager(username, password, role);
            case BUILDER -> new Builder(username, password, role);
            case CLIENT -> new Client(username, password, role);
            default -> throw new RoleMismatchException(role + " does not match any roles");
        };

        userList.put(username, user);
        userDAO.save(userList);

        System.out.println("User registered successfully: " + username);

        return true;
    }
}
