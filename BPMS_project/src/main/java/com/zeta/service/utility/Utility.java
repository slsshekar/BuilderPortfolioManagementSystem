package com.zeta.service.utility;

import com.zeta.logging.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

public class Utility {
    static Logger logger = Logger.getInstance();
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void validateInput(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        if (!"Password".equals(fieldName) && !Character.isLetter(input.charAt(0))) {
            throw new IllegalArgumentException(fieldName + " must start with a letter");
        }

        if (input.length() < 3) {
            throw new IllegalArgumentException("Fieldname should have atleast 3 characters");
        }
    }

    public static int getValidChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static LocalDate readDate(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String dateStr = scanner.nextLine().trim();
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
            if (date.isBefore(LocalDate.now())) {
                logger.warn("Date cannot be in the past. Please enter today or a future date.");
                return null;
            }
            return LocalDate.parse(dateStr, DATE_FORMAT);

        } catch (DateTimeParseException e) {
            logger.warn("Invalid date format. Please use dd-MM-yyyy (e.g. 15-03-2026)");
            return null;
        }
    }
}
