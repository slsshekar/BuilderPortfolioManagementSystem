package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;

import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.utility.Utility;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AdminUI {

    // ---------- ObjectMapper ----------
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // ---------- DAO Layer ----------
    private static final ProjectDAO projectDAO = new ProjectDAO(mapper);
    private static final UserDAO userDAO = new UserDAO(mapper);

    // ---------- Service Layer ----------
    private static final ProjectService projectService = new ProjectService(projectDAO, userDAO);


    public static void show(Scanner scanner) {

        while (true) {
            printMenu();

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1 -> showUnapprovedProjects();
                case 2 -> showApprovedProjects();
                case 3 -> approveProject(scanner);
                case 4 -> assignManager(scanner);
                case 5 -> {
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Please enter a valid number (1-5)");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Show all unapproved projects");
        System.out.println("2. Show all approved projects");
        System.out.println("3. Approve project");
        System.out.println("4. Assign manager");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }


    private static void showUnapprovedProjects() {

        List<String> unapprovedProjects = projectService.getUnapprovedProjects();

        if (unapprovedProjects.isEmpty()) {
            System.out.println("No unapproved projects found");
            return;
        }

        System.out.println("\nUnapproved Projects:");
        for (int i = 0; i < unapprovedProjects.size(); i++) {
            System.out.println((i + 1) + ". " + unapprovedProjects.get(i));
        }
    }


    private static void showApprovedProjects() {

        List<String> approvedProjects = projectService.getApprovedProjects();

        if (approvedProjects.isEmpty()) {
            System.out.println("No approved projects found");
            return;
        }

        System.out.println("\nApproved Projects:");
        for (int i = 0; i < approvedProjects.size(); i++) {
            System.out.println((i + 1) + ". " + approvedProjects.get(i));
        }
    }


    private static void approveProject(Scanner scanner) {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        LocalDate startDate = Utility.readDate(scanner, "Enter start date (dd-MM-yyyy): ");
        if (startDate == null)
            return;

        LocalDate endDate = Utility.readDate(scanner, "Enter end date (dd-MM-yyyy): ");
        if (endDate == null)
            return;

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

    private static void assignManager(Scanner scanner) {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        System.out.print("Enter manager name: ");
        String managerName = scanner.nextLine().trim();

        try {
            projectService.assignManager(projectName, managerName);
            System.out.println("Manager assigned successfully!");

        } catch (ProjectDoestNotExistException | UserNotFoundException | RoleMismatchException
                | IllegalArgumentException e) {

            System.out.println("Assignment failed: " + e.getMessage());
        }
    }
}
