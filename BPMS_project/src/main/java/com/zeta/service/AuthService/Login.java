package com.zeta.service.AuthService;
import com.zeta.service.FileService.FileService;
import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.ROLE;
import com.zeta.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.service.FileService.FileService;
import com.zeta.service.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private static final String FILE_NAME = "database/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<String, User> userList = new HashMap<>();

    public ROLE login(String username, String password) throws UserNotFoundException, InvalidPasswordException {

        Utility.validateInput(username, "Username");
        Utility.validateInput(password, "Password");

        userList=FileService.loadFromFile(FILE_NAME,mapper, User.class);
        System.out.println(userList);
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
