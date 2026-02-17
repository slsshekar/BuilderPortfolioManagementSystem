package com.zeta.service.AuthService;

import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.service.FileService.FileService;
import com.zeta.service.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register {

    private static Map<String, User> userList = new HashMap<>();

    private static final String FILE_NAME = "database/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();


    public boolean register(String username, String password, ROLE role) throws RoleMismatchException {

        userList=FileService.loadFromFile(FILE_NAME,mapper, User.class);

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

        FileService.saveToFile(userList,FILE_NAME,mapper);

        System.out.println("User registered successfully: " + username);
        return true;
    }
}
