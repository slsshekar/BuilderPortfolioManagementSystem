package com.zeta.service.AuthService;

import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.ROLE;
import com.zeta.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private static final String FILE_NAME = "users.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Map<String, User> loadFromFile() {
        try {
            File file = new File(FILE_NAME);

            if (file.exists() && file.length() > 0) {
                return mapper.readValue(
                        file,
                        new TypeReference<Map<String, User>>() {
                        });
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return new HashMap<>();
    }

    private void validateInput(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    public ROLE login(String username, String password) throws UserNotFoundException, InvalidPasswordException {

        validateInput(username, "Username");
        validateInput(password, "Password");

        Map<String, User> userList = loadFromFile();

        if (!userList.containsKey(username)) {
            throw new UserNotFoundException("User not found: " + username);
        }

        User user = userList.get(username);

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("Invalid password for user: " + username);
        }

        return user.getRole();
    }
}
