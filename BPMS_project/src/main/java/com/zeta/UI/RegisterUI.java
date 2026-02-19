package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.logging.Logger;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Register;
import com.zeta.service.utility.Utility;

import java.util.Scanner;

public class RegisterUI {
    static Logger logger = Logger.getInstance();

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final UserDAO userDAO = new UserDAO(mapper);

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
                logger.info(role + " registered successfully!");

            } catch (RoleMismatchException | IllegalArgumentException e) {
                logger.info("Registration failed: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        logger.info("\n=== Register Menu ===");
        logger.info("1. Register as Client");
        logger.info("2. Register as Builder");
        logger.info("3. Register as Manager");
        logger.info("4. Back");
        System.out.print("Enter your choice: ");
    }

    private static ROLE getRoleFromChoice(int choice) {
        return switch (choice) {
            case 1 -> ROLE.CLIENT;
            case 2 -> ROLE.BUILDER;
            case 3 -> ROLE.MANAGER;
            default -> {
                logger.info("Please enter a valid number (1-4)");
                yield null;
            }
        };
    }
}
