package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.Exceptions.ProjectServiceException.ClientDoesNotExistException;
import com.zeta.Exceptions.ProjectServiceException.ProjectAlreadyExistsException;
import com.zeta.logging.Logger;
import com.zeta.model.Project;
import com.zeta.model.STATUS;
import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.utility.Utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ClientUI {
    static Logger logger = Logger.getInstance();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ProjectDAO projectDAO = new ProjectDAO(mapper);

    private static final UserDAO userDAO = new UserDAO(mapper);

    private static final ProjectService projectService = new ProjectService(projectDAO, userDAO);

    public static void show(Scanner scanner, String clientName) {

        while (true) {
            printMenu();

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1 -> createProject(scanner, clientName);
                case 2 -> getProjectStatus(clientName);
                case 3 -> {
                    logger.info("Logged out successfully.");
                    return;
                }
                default -> logger.info("Please enter a valid number (1-3)");
            }
        }
    }

    private static void printMenu() {
        logger.info("\n Client Menu : ");
        logger.info("1. Create Project");
        logger.info("2. Get Project Status");
        logger.info("3. Logout");
        logger.info("Enter your choice: ");
    }

    private static void createProject(Scanner scanner, String clientName) {

        logger.info("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        logger.info("Enter project description: ");
        String projectDescription = scanner.nextLine().trim();

        try {
            Project project = new Project(
                    projectName,
                    projectDescription,
                    null,
                    null);

            projectService.create(project, clientName);

            logger.info("Project created successfully!");

        } catch (ProjectAlreadyExistsException | ClientDoesNotExistException | IllegalArgumentException e) {

            logger.warn("Project creation failed: " + e.getMessage());
        }
    }

    private static void getProjectStatus(String clientName) {

        try {
            Map<String, STATUS> projectStatusMap = projectService.getProjectStatusByClient(clientName);

            if (projectStatusMap.isEmpty()) {
                logger.info("No projects found.");
                return;
            }

            Map<STATUS, List<String>> grouped = new LinkedHashMap<>();
            for (STATUS s : STATUS.values()) {
                grouped.put(s, new ArrayList<>());
            }
            for (Map.Entry<String, STATUS> entry : projectStatusMap.entrySet()) {
                grouped.get(entry.getValue()).add(entry.getKey());
            }

            logger.info("\n PROJECT BOARD: ");

            for (STATUS status : STATUS.values()) {
                logger.info("\n" + getEmoji(status) + " " + status);
                List<String> projects = grouped.get(status);
                if (projects == null || projects.isEmpty()) {
                    logger.info("  No projects");
                } else {
                    for (String name : projects) {
                        logger.info("  - " + name);
                    }
                }
            }
        } catch (UserNotFoundException e) {
            logger.warn("Error: " + e.getMessage());
        }
    }

    static String getEmoji(STATUS status) {
        return switch (status) {
            case NOT_APPROVED -> "📋";
            case UPCOMING -> "📌";
            case IN_PROGRESS -> "🚧";
            case COMPLETED -> "✅";
        };
    }
}
