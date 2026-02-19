package com.zeta.UI;

import com.zeta.logging.Logger;
import com.zeta.service.utility.Utility;

import java.util.Scanner;

public class MainMenuUI {
    static Logger logger = Logger.getInstance();

    public static void show() {
        Scanner scanner = new Scanner(System.in);

        logger.info("Welcome to Builder Portfolio Management System");

        while (true) {
            logger.info("\n Main Menu : ");
            logger.info("1. Register");
            logger.info("2. Login");
            logger.info("3. Exit");
            logger.info("Enter your choice: ");

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1:
                    RegisterUI.show(scanner);
                    break;
                case 2:
                    LoginUI.show(scanner);
                    break;
                case 3:
                    logger.info("Exiting the application");
                    scanner.close();
                    return;
                default:
                    logger.info("Please enter a valid number (1-3)");
                    break;
            }
        }
    }
}
