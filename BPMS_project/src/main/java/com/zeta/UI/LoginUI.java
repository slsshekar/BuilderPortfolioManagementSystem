package com.zeta.UI;

import com.zeta.Exceptions.LoginException.InvalidPasswordException;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.ROLE;
import com.zeta.service.AuthService.Login;

import java.util.Scanner;

public class LoginUI {

    public static void show(Scanner scanner) {
        Login loginService = new Login();

        System.out.println("\n Login : ");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            ROLE role = loginService.login(username, password);

            switch (role) {
                case CLIENT:
                    System.out.println("Welcome Client!");
                    ClientUI.show(scanner, username);
                    break;
                case BUILDER:
                    System.out.println("Welcome Builder!");
                    break;
                case MANAGER:
                    System.out.println("Welcome Manager!");
                    break;
                default:
                    System.out.println("Welcome " + role + "!");
                    break;
            }
        } catch (UserNotFoundException | InvalidPasswordException e) {
            System.out.println("Login failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
}
