package com.zeta.service.AuthService;

import com.zeta.model.ROLE;
import com.zeta.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.service.FileService.FileService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register {

    private static Map<String, User> userList = new HashMap<>();

    private static final String FILE_NAME = "database/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();



    private void validateInput(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    public boolean register(String username, String password, ROLE role) {

        userList=FileService.loadFromFile((HashMap) userList,FILE_NAME,mapper);

        validateInput(username, "Username");
        validateInput(password, "Password");

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        if (userList.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(username, password, role);
        userList.put(username, user);

        FileService.saveToFile(userList,FILE_NAME,mapper);

        System.out.println("User registered successfully: " + username);
        return true;
    }
}
