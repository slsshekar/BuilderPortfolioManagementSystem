package com.zeta.service.utility;

public class Utility {
    public static void validateInput(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }
}
