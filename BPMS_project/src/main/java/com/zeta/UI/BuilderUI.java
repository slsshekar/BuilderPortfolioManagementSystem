package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.FileService.FileService;
import com.zeta.service.utility.Utility;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BuilderUI {
    public static void show(Scanner scanner, String builderName) {
        while (true) {
            System.out.println("\n Builder Menu : ");
            System.out.println("1. View assigned tasks");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1:
                    viewAssignedTasks(builderName);
                    break;
                case 2:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Please enter a valid number (1-2)");
                    break;
            }
        }
    }

    private static void viewAssignedTasks(String builderName) {

        final String TASK_FILE = "database/tasks.json";
        final String USER_FILE = "database/users.json";

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Load all tasks
        Map<String, Task> taskMap = FileService.loadFromFile(TASK_FILE, mapper, Task.class);

        boolean found = false;

        System.out.println("\nYour IN_PROGRESS Tasks:");

        for (Map.Entry<String, Task> entry : taskMap.entrySet()) {

            Task task = entry.getValue();

            if (task.getBuilderList() != null
                    && task.getBuilderList().contains(builderName)
                    && task.getStatus() == STATUS.IN_PROGRESS) {

                System.out.println("- " + task.getName());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks currently in progress.");
        }
    }

}
