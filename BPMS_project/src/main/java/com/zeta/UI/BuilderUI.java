package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.model.STATUS;
import com.zeta.model.Task;
import com.zeta.service.TaskService.TaskAssignmentService;
import com.zeta.service.TaskService.TaskService;
import com.zeta.service.utility.Utility;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class BuilderUI {

    // ===== shared mapper =====
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // ===== DAO layer =====
    private static final TaskDAO taskDAO = new TaskDAO(mapper);
    private static final UserDAO userDAO = new UserDAO(mapper);

    // ===== service layer =====
    private static final TaskService taskService = new TaskService(taskDAO, userDAO);
    private static final TaskAssignmentService assignmentService = new TaskAssignmentService(taskDAO, userDAO);

    // ================= MAIN UI =================

    public static void show(Scanner scanner, String builderName) {

        while (true) {
            printMenu();
            int choice = Utility.getValidChoice(scanner);

            try {
                switch (choice) {
                    case 1 -> viewAssignedTasks(builderName);
                    case 2 -> viewTasksByStatus(scanner, builderName);
                    case 3 -> viewTaskDetails(scanner, builderName);
                    case 4 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ================= MENU =================

    private static void printMenu() {
        System.out.println("\n=== Builder Menu ===");
        System.out.println("1. View All Assigned Tasks");
        System.out.println("2. View Tasks By Status");
        System.out.println("3. View Task Details");
        System.out.println("4. Logout");
        System.out.print("Enter choice: ");
    }

    // ================= FEATURES =================

    /**
     * Show all assigned tasks
     */
    private static void viewAssignedTasks(String builderName) {

        Set<String> taskNames = assignmentService.getTasksOfBuilder(builderName);

        if (taskNames.isEmpty()) {
            System.out.println("No tasks assigned");
            return;
        }

        System.out.println("\nYour Tasks:");
        taskNames.forEach(t -> System.out.println("• " + t));
    }

    /**
     * Filter builder tasks by status
     */
    private static void viewTasksByStatus(Scanner scanner, String builderName) {

        Set<String> assignedTasks = assignmentService.getTasksOfBuilder(builderName);

        if (assignedTasks.isEmpty()) {
            System.out.println("No tasks assigned");
            return;
        }

        System.out.println("\nAvailable Status:");
        for (STATUS s : STATUS.values()) {
            System.out.println("- " + s);
        }

        System.out.print("Enter status: ");
        STATUS status = STATUS.valueOf(scanner.nextLine().toUpperCase());

        List<Task> filteredTasks = assignedTasks.stream()
                .map(taskService::getTask)
                .filter(t -> t != null && t.getStatus() == status)
                .collect(Collectors.toList());

        if (filteredTasks.isEmpty()) {
            System.out.println("No tasks with status " + status);
            return;
        }

        System.out.println("\nTasks with status " + status + ":");
        filteredTasks.forEach(t -> System.out.println("• " + t.getName()));
    }

    /**
     * Show full task info
     */
    private static void viewTaskDetails(Scanner scanner, String builderName) {

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();

        // ensure builder owns task
        if (!assignmentService.getTasksOfBuilder(builderName).contains(taskName)) {
            System.out.println("Task not assigned to you");
            return;
        }

        Task task = taskService.getTask(taskName);

        if (task == null) {
            System.out.println("Task not found");
            return;
        }

        printTask(task);
    }

    // ================= HELPER =================

    private static void printTask(Task task) {

        System.out.println("\n===== Task Details =====");
        System.out.println("Name: " + task.getName());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Project: " + task.getProjectName());
        System.out.println("Manager: " + task.getManagerName());
        System.out.println("Start Date: " + task.getStartDate());
        System.out.println("End Date: " + task.getEndDate());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Builders: " + task.getBuilderList());
    }
}
