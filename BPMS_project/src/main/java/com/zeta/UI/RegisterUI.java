package com.zeta.UI;

import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Register;
import com.zeta.service.utility.Utility;

import java.util.Scanner;

public class RegisterUI {

    public static void show(Scanner scanner) {
        Register registerService = new Register();

        while (true) {
            System.out.println("\n Register Menu : ");
            System.out.println("1. Register as Client");
            System.out.println("2. Register as Builder");
            System.out.println("3. Register as Manager");
            System.out.println("4. Back");
            System.out.print("Enter your choice: ");

            int choice = Utility.getValidChoice(scanner);

            if (choice == 4) {
                return;
            }

            ROLE role;
            String roleLabel;

            switch (choice) {
                case 1:
                    role = ROLE.CLIENT;
                    roleLabel = "Client";
                    break;
                case 2:
                    role = ROLE.BUILDER;
                    roleLabel = "Builder";
                    break;
                case 3:
                    role = ROLE.MANAGER;
                    roleLabel = "Manager";
                    break;
                default:
                    System.out.println("Please enter a valid number (1-4)");
                    continue;
            }

            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            try {
                registerService.register(username, password, role);
                System.out.println(roleLabel + " registered successfully!");
            } catch (RoleMismatchException | IllegalArgumentException e) {
                System.out.println("Registration failed: " + e.getMessage());
            }
        }
    }
}
