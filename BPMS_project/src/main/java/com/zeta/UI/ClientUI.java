package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.UserDAO;
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

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    private static final ProjectDAO projectDAO =
            new ProjectDAO(mapper);

    private static final UserDAO userDAO =
            new UserDAO(mapper);

    private static final ProjectService projectService =
            new ProjectService(projectDAO, userDAO);

    public static void show(Scanner scanner, String clientName) {

        while (true) {
            printMenu();

            int choice = Utility.getValidChoice(scanner);

            switch (choice) {
                case 1 -> createProject(scanner, clientName);
                case 2 -> getProjectStatus(clientName);
                case 3 -> {
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Please enter a valid number (1-3)");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Client Menu ===");
        System.out.println("1. Create Project");
        System.out.println("2. Get Project Status");
        System.out.println("3. Logout");
        System.out.print("Enter your choice: ");
    }


    private static void createProject(Scanner scanner, String clientName) {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        System.out.print("Enter project description: ");
        String projectDescription = scanner.nextLine().trim();

        try {
            Project project = new Project(
                    projectName,
                    projectDescription,
                    null,
                    null
            );

            projectService.create(project, clientName);

            System.out.println("Project created successfully!");

        } catch (ProjectAlreadyExistsException |
                 ClientDoesNotExistException |
                 IllegalArgumentException e) {

            System.out.println("Project creation failed: " + e.getMessage());
        }
    }

    private static void getProjectStatus(String clientName) {

        try {
            Map<String, STATUS> projectStatusMap =
                    projectService.getProjectStatusByClient(clientName);

            if (projectStatusMap.isEmpty()) {
                System.out.println("No projects found.");
                return;
            }

            System.out.println("\nYour Projects:");

            projectStatusMap.forEach(
                    (name, status) ->
                            System.out.println(name + " : " + status)
            );

        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
