package com.zeta.service.AuthService;

import com.zeta.model.Client;
import com.zeta.model.ROLE;
import com.zeta.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register {

    private static Map<String, User> userList = new HashMap<>();

    private static final String FILE_NAME = "database/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();


    private static void loadFromFile() {
        try {
            File file = new File(FILE_NAME);

            if (file.exists() && file.length() > 0) {
                userList = mapper.readValue(
                        file,
                        new TypeReference<Map<String, User>>() {}
                );
            } else {
                userList = new HashMap<>();
            }

        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            userList = new HashMap<>();
        }
    }

    private static void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), userList);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void validateInput(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    public boolean register(String username, String password, ROLE role) {

        loadFromFile();

        validateInput(username, "Username");
        validateInput(password, "Password");

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        if (userList.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Client client = new Client(username, password);
        userList.put(username, client);

        saveToFile();

        System.out.println("User registered successfully: " + username);
        return true;
    }
}
