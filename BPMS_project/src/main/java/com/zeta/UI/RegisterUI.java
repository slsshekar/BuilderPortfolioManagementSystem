package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Register;
import com.zeta.service.utility.Utility;

import java.util.Scanner;

public class RegisterUI {

    // create mapper once (best practice)
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // DAO layer
    private static final UserDAO userDAO = new UserDAO(mapper);

    // service layer (dependency injection)
    private static final Register registerService = new Register(userDAO);

    public static void show(Scanner scanner) {

        while (true) {

            printMenu();

            int choice = Utility.getValidChoice(scanner);

            if (choice == 4) return;

            ROLE role = getRoleFromChoice(choice);
            if (role == null) continue;

            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            try {
                registerService.register(username, password, role);
                System.out.println(role + " registered successfully!");

            } catch (RoleMismatchException | IllegalArgumentException e) {
                System.out.println("Registration failed: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Register Menu ===");
        System.out.println("1. Register as Client");
        System.out.println("2. Register as Builder");
        System.out.println("3. Register as Manager");
        System.out.println("4. Back");
        System.out.print("Enter your choice: ");
    }

    private static ROLE getRoleFromChoice(int choice) {
        return switch (choice) {
            case 1 -> ROLE.CLIENT;
            case 2 -> ROLE.BUILDER;
            case 3 -> ROLE.MANAGER;
            default -> {
                System.out.println("Please enter a valid number (1-4)");
                yield null;
            }
        };
    }
}
