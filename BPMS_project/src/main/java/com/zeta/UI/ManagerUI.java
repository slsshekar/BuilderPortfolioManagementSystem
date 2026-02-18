package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.model.STATUS;
import com.zeta.service.ManagerService.ManagerService;
import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.utility.Utility;

import java.time.LocalDate;
import java.util.Scanner;

public class ManagerUI {

    private static final ProjectService projectService = new ProjectService();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final ManagerService managerService = new ManagerService(mapper);

    public static void show(Scanner scanner, String username) {
        while (true) {
            printMenu();
            int choice = Utility.getValidChoice(scanner);
            try {
                switch (choice) {
                    case 1 -> viewProjects(username);
                    case 2 -> createTask(scanner, username);
                    case 3 -> viewProject(scanner);
                    case 4 -> assignBuilder(scanner);
                    case 5 -> updateTaskStatus(scanner);
                    case 6 -> {
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

    private static void printMenu() {
        System.out.println("\n Manager Menu : ");
        System.out.println("1. View Projects");
        System.out.println("2. Create Task");
        System.out.println("3. View Project Details");
        System.out.println("4. Assign Builder");
        System.out.println("5. Update Task Status");
        System.out.println("6. Logout");
        System.out.print("Enter your choice (1-6): ");
    }

    private static void viewProjects(String username) throws UserNotFoundException {

        // you already have this
        var projectNames = projectService.getProjectsByManagerName(username);

        var groupedProjects = managerService.getProjectsByStatusForManager(projectNames);

        System.out.println("\n PROJECT BOARD: ");

        for (STATUS status : STATUS.values()) {
            if (status == STATUS.NOT_APPROVED)
                continue;
            System.out.println("\n" + getEmoji(status) + " " + status);

            var projects = groupedProjects.get(status);

            if (projects == null || projects.isEmpty()) {
                System.out.println("No projects");
                continue;
            }

            for (var project : projects) {
                System.out.println("- " + project.getName());
            }
        }
    }

    private static String getEmoji(STATUS status) {
        return switch (status) {
            case UPCOMING -> "📌";
            case IN_PROGRESS -> "🚧";
            case COMPLETED -> "✅";
            case NOT_APPROVED -> "wont use";
        };
    }

    private static void createTask(Scanner scanner, String username) throws UserNotFoundException {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        if (!projectService.getProjectsByManagerName(username).contains(projectName)) {
            System.out.println("Project not assigned to you");
            return;
        }

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine().trim();

        System.out.print("Enter task description: ");
        String desc = scanner.nextLine().trim();

        LocalDate start = Utility.readDate(scanner, "Enter start date (dd-MM-yyyy): ");
        LocalDate end = Utility.readDate(scanner, "Enter end date (dd-MM-yyyy): ");

        managerService.createTask(projectName, taskName, desc, username, start, end);

        System.out.println("Task created successfully");
    }

    private static void viewProject(Scanner scanner) {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();

        System.out.println(managerService.getProject(projectName));
    }

    private static void assignBuilder(Scanner scanner) {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();

        System.out.print("Enter builder name: ");
        String builderName = scanner.nextLine();

        managerService.assignBuilder(projectName, taskName, builderName);

        System.out.println("Builder assigned successfully");
    }

    private static void updateTaskStatus(Scanner scanner) {
        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter status: ");
        STATUS status = STATUS.valueOf(scanner.nextLine().toUpperCase());
        managerService.updateTaskStatus(taskName, status);
        System.out.println("Status updated successfully");
    }
}
