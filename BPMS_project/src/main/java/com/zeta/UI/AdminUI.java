package com.zeta.UI;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;
import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.utility.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class AdminUI {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void show(Scanner scanner) {
        ProjectService projectService = new ProjectService();

        while (true) {
            System.out.println("\n Admin Menu : ");
            System.out.println("1. Show all unapproved projects");
            System.out.println("2. Show all approved projects");
            System.out.println("3. Approve project");
            System.out.println("4. Assign manager");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1:
                    showUnapprovedProjects(projectService);
                    break;
                case 2:
                    showApprovedProjects(projectService);
                    break;
                case 3:
                    approveProject(scanner, projectService);
                    break;
                case 4:
                    assignManager(scanner, projectService);
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Please enter a valid number (1-5)");
                    break;
            }
        }
    }

    private static void showUnapprovedProjects(ProjectService projectService) {
        List<String> unapprovedProjects = projectService.getUnapprovedProjects();
        if (unapprovedProjects.isEmpty()) {
            System.out.println("No unapproved projects found");
            return;
        }
        System.out.println("\n Unapproved Projects : ");
        for (int i = 0; i < unapprovedProjects.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + unapprovedProjects.get(i));
        }
    }

    private static void showApprovedProjects(ProjectService projectService) {
        List<String> approvedProjects = projectService.getApprovedProjects();
        if (approvedProjects.isEmpty()) {
            System.out.println("No approved projects found.");
            return;
        }
        System.out.println("\n Approved Projects : ");
        for (int i = 0; i < approvedProjects.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + approvedProjects.get(i));
        }
    }

    private static void approveProject(Scanner scanner, ProjectService projectService) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        LocalDate startDate = readDate(scanner, "Enter start date (dd-MM-yyyy): ");
        if (startDate == null) {
            System.out.println("Project not approved");
            return;
        }

        LocalDate endDate = readDate(scanner, "Enter end date (dd-MM-yyyy): ");
        if (endDate == null) {
            System.out.println("Project not approved");
            return;
        }

        if (endDate.isBefore(startDate)) {
            System.out.println("End date cannot be before start date.");
            System.out.println("Project not approved");
            return;
        }

        try {
            projectService.approve(projectName, startDate, endDate);
            System.out.println("Project approved successfully!");
        } catch (ProjectDoestNotExistException | IllegalArgumentException e) {
            System.out.println("Approval failed: " + e.getMessage());
        }
    }

    private static void assignManager(Scanner scanner, ProjectService projectService) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        System.out.print("Enter manager name: ");
        String managerName = scanner.nextLine().trim();

        try {
            projectService.assignManager(projectName, managerName);
            System.out.println("Manager assigned successfully!");
        } catch (ProjectDoestNotExistException | UserNotFoundException | RoleMismatchException e) {
            System.out.println("Assignment failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Assignment failed: " + e.getMessage());
        }
    }

    private static LocalDate readDate(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String dateStr = scanner.nextLine().trim();
        try {
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy (e.g. 15-03-2026)");
            return null;
        }
    }
}
