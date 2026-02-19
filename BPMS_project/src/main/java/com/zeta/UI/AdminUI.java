package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.Exceptions.ProjectServiceException.RoleMismatchException;

import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;

import com.zeta.logging.Logger;
import com.zeta.model.STATUS;
import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.utility.Utility;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static com.zeta.UI.ClientUI.getEmoji;

public class AdminUI {
    static Logger logger = Logger.getInstance();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ProjectDAO projectDAO = new ProjectDAO(mapper);
    private static final UserDAO userDAO = new UserDAO(mapper);

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
                case 5 -> viewAllProjectsBoard();
                case 6 -> {
                    logger.info("Logged out successfully.");
                    return;
                }
                default -> logger.info("Please enter a valid number (1-5)");
            }
        }
    }

    private static void printMenu() {
        logger.info("\n Admin Menu : ");
        logger.info("1. Show all unapproved projects");
        logger.info("2. Show all approved projects");
        logger.info("3. Approve project");
        logger.info("4. Assign manager");
        logger.info("5. view project board");
        logger.info("6. Logout");

        System.out.print("Enter your choice: ");
    }


    private static void showUnapprovedProjects() {

        List<String> unapprovedProjects = projectService.getUnapprovedProjects();

        if (unapprovedProjects.isEmpty()) {
            logger.info("No unapproved projects found");
            return;
        }

        logger.info("\nUnapproved Projects:");
        for (int i = 0; i < unapprovedProjects.size(); i++) {
            logger.info((i + 1) + ". " + unapprovedProjects.get(i));
        }
    }


    private static void showApprovedProjects() {

        List<String> approvedProjects = projectService.getApprovedProjects();

        if (approvedProjects.isEmpty()) {
            logger.info("No approved projects found");
            return;
        }

        logger.info("\nApproved Projects:");
        for (int i = 0; i < approvedProjects.size(); i++) {
            logger.info((i + 1) + ". " + approvedProjects.get(i));
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
            logger.info("End date cannot be before start date.");
            logger.info("Project not approved");
            return;
        }

        try {
            projectService.approve(projectName, startDate, endDate);
            logger.info("Project approved successfully!");

        } catch (ProjectDoestNotExistException | IllegalArgumentException e) {
            logger.warn("Approval failed: " + e.getMessage());
        }
    }

    private static void assignManager(Scanner scanner) {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        System.out.print("Enter manager name: ");
        String managerName = scanner.nextLine().trim();

        try {
            projectService.assignManager(projectName, managerName);
            logger.info("Manager assigned successfully!");

        } catch (ProjectDoestNotExistException | UserNotFoundException | RoleMismatchException
                 | IllegalArgumentException e) {

            logger.warn("Assignment failed: " + e.getMessage());
        }
    }

    private static void viewAllProjectsBoard() {

        var groupedProjects = projectService.getAllProjectsGroupedByStatus();

        logger.info("\n PROJECT BOARD: ");

        for (STATUS status : STATUS.values()) {

            if (status == STATUS.NOT_APPROVED)
                continue;

            logger.info("\n" + getEmoji(status) + " " + status);

            var projects = groupedProjects.getOrDefault(status, new java.util.ArrayList<>());

            if (projects.isEmpty()) {
                logger.info("No projects");
            } else {
                projects.forEach(p -> logger.info("- " + p.getName()));
            }
        }
    }

}
