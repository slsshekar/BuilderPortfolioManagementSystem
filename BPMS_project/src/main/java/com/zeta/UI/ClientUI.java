package com.zeta.UI;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
import com.zeta.model.Project;
import com.zeta.model.STATUS;
import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.utility.Utility;

import java.util.Map;
import java.util.Scanner;

public class ClientUI {

    public static void show(Scanner scanner, String clientName) {
        ProjectService projectService = new ProjectService();

        while (true) {
            System.out.println("\n Client Menu : ");
            System.out.println("1. Create Project");
            System.out.println("2. Get Project Status");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1:
                    createProject(scanner, projectService, clientName);
                    break;
                case 2:
                    getProjectStatus(projectService, clientName);
                    break;
                case 3:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Please enter a valid number (1-3)");
                    break;
            }
        }
    }

    private static void createProject(Scanner scanner, ProjectService projectService, String clientName) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        System.out.print("Enter project description: ");
        String projectDescription = scanner.nextLine().trim();

        try {
            Project project = new Project(projectName, projectDescription, null, null);
            projectService.create(project, clientName);
            System.out.println("Project created successfully!");
        } catch (ProjectAlreadyExistsException | ClientDoesNotExistException | IllegalArgumentException e) {
            System.out.println("Project creation failed: " + e.getMessage());
        }
    }

    private static void getProjectStatus(ProjectService projectService, String clientName) {
        try {
            Map<String, STATUS> projectStatusMap = projectService.getProjectStatusByClient(clientName);

            if (projectStatusMap.isEmpty()) {
                System.out.println("No projects found.");
                return;
            }

            // Group projects by status
            Map<STATUS, java.util.List<String>> grouped = new java.util.LinkedHashMap<>();
            for (STATUS s : STATUS.values()) {
                grouped.put(s, new java.util.ArrayList<>());
            }
            for (Map.Entry<String, STATUS> entry : projectStatusMap.entrySet()) {
                grouped.get(entry.getValue()).add(entry.getKey());
            }

            System.out.println("\n PROJECT BOARD: ");

            for (STATUS status : STATUS.values()) {
                System.out.println("\n" + getEmoji(status) + " " + status);
                java.util.List<String> projects = grouped.get(status);
                if (projects == null || projects.isEmpty()) {
                    System.out.println("  No projects");
                } else {
                    for (String name : projects) {
                        System.out.println("  - " + name);
                    }
                }
            }
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getEmoji(STATUS status) {
        return switch (status) {
            case NOT_APPROVED -> "📋";
            case UPCOMING -> "📌";
            case IN_PROGRESS -> "🚧";
            case COMPLETED -> "✅";
        };
    }
}
