package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Login;

import java.util.Scanner;

public class LoginUI {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final UserDAO userDAO = new UserDAO(mapper);

    private static final Login loginService = new Login(userDAO);

    public static void show(Scanner scanner) {

        System.out.println("\n=== Login ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (isAdmin(username, password)) {
            System.out.println("Welcome Admin!");
            AdminUI.show(scanner);
            return;
        }

        try {
            ROLE role = loginService.login(username, password);
            routeUser(role, scanner, username);

        } catch (UserNotFoundException | InvalidPasswordException | IllegalArgumentException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static boolean isAdmin(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    private static void routeUser(ROLE role, Scanner scanner, String username) {

        switch (role) {

            case CLIENT -> {
                System.out.println("Welcome Client!");
                ClientUI.show(scanner, username);
            }

            case BUILDER -> {
                System.out.println("Welcome Builder!");
                BuilderUI.show(scanner, username); // if exists
            }

            case MANAGER -> {
                System.out.println("Welcome Manager!");
                ManagerUI.show(scanner, username);
            }

            default -> System.out.println("Welcome " + role + "!");
        }
    }
}
