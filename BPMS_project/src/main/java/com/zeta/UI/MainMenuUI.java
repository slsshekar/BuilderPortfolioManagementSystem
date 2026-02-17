package com.zeta.UI;

import com.zeta.service.utility.Utility;

import java.util.Scanner;

public class MainMenuUI {

    public static void show() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Builder Portfolio Management System");

        while (true) {
            System.out.println("\n Main Menu : ");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1:
                    RegisterUI.show(scanner);
                    break;
                case 2:
                    LoginUI.show(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the application");
                    scanner.close();
                    return;
                default:
                    System.out.println("Please enter a valid number (1-3)");
                    break;
            }
        }
    }
}
