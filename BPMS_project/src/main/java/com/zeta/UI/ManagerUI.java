package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
import com.zeta.logging.Logger;
import com.zeta.model.STATUS;
import com.zeta.service.ManagerService.ManagerService;
import com.zeta.service.ProjectService.ProjectService;
import com.zeta.service.TaskService.TaskService;
import com.zeta.service.TaskService.TaskAssignmentService;
import com.zeta.service.utility.Utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ManagerUI {
    static Logger logger = Logger.getInstance();

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ProjectDAO projectDAO = new ProjectDAO(mapper);
    private static final TaskDAO taskDAO = new TaskDAO(mapper);
    private static final UserDAO userDAO = new UserDAO(mapper);

    private static final ProjectService projectService = new ProjectService(projectDAO, userDAO);

    private static final TaskService taskService = new TaskService(taskDAO, userDAO, projectDAO);

    private static final TaskAssignmentService assignmentService = new TaskAssignmentService(taskDAO, userDAO);

    private static final ManagerService managerService = new ManagerService(projectDAO, taskService);

    public static void show(Scanner scanner, String username) {

        while (true) {
            printMenu();
            int choice = Utility.getValidChoice(scanner);

            try {
                switch (choice) {
                    case 1 -> viewProjects(username);
                    case 2 -> createTask(scanner, username);
                    case 3 -> viewProject(scanner, username);
                    case 4 -> assignBuilder(scanner);
                    case 5 -> updateTaskStatus(scanner);
                    case 6 -> {
                        logger.info("Logging out...");
                        return;
                    }
                    default -> logger.info("Invalid choice");
                }

            } catch (Exception e) {
                logger.warn("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        logger.info("\n Manager Menu : ");
        logger.info("1. View Projects");
        logger.info("2. Create Task");
        logger.info("3. View Project Details");
        logger.info("4. Assign Builder");
        logger.info("5. Update Task Status");
        logger.info("6. Logout");
        logger.info("Enter your choice (1-6): ");
    }


    private static void viewProjects(String username) throws UserNotFoundException {

        Set<String> projectNames = projectService.getProjectsByManagerName(username);

        if (projectNames == null || projectNames.isEmpty()) {
            logger.info("No projects assigned to you.");
            return;
        }

        var groupedProjects = managerService.getProjectsByStatus(projectNames);

        logger.info("\n PROJECT BOARD: ");

        for (STATUS status : STATUS.values()) {

            if (status == STATUS.NOT_APPROVED)
                continue;

            logger.info("\n" + getEmoji(status) + " " + status);

            var projects = groupedProjects.getOrDefault(status, new ArrayList<>());

            if (projects.isEmpty()) {
                logger.info("No projects");
            } else {
                projects.forEach(p -> logger.info("- " + p.getName()));
            }
        }
    }

    private static String getEmoji(STATUS status) {
        return switch (status) {
            case UPCOMING -> "📌";
            case IN_PROGRESS -> "🚧";
            case COMPLETED -> "✅";
            case NOT_APPROVED -> "";
        };
    }

    private static void createTask(Scanner scanner, String username)
            throws UserNotFoundException {

        logger.info("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        if (!projectService.getProjectsByManagerName(username).contains(projectName)) {
            logger.info("Project not assigned to you");
            return;
        }

        logger.info("Enter task name: ");
        String taskName = scanner.nextLine().trim();

        logger.info("Enter task description: ");
        String desc = scanner.nextLine().trim();

        LocalDate start = Utility.readDate(scanner, "Enter start date (dd-MM-yyyy): ");

        LocalDate end = Utility.readDate(scanner, "Enter end date (dd-MM-yyyy): ");

        managerService.createTaskForProject(
                projectName, taskName, desc, username, start, end);

        logger.info("Task created successfully");
    }

    private static void viewProject(Scanner scanner, String username) throws UserNotFoundException {

        logger.info("Enter project name: ");
        String projectName = scanner.nextLine();
        Set<String> projectNames = projectService.getProjectsByManagerName(username);
        if (!projectNames.contains(projectName)) {
            logger.info(projectName + " does not exist for you");
        }
        var project = managerService.getProject(projectName);
        logger.info(String.valueOf(project));
        var tasks = managerService.getTasksOfProject(projectName);

        logger.info("\n------ TASKS ------");

        if (tasks.isEmpty()) {
            logger.info("No tasks created for this project");
            return;
        }

        for (var task : tasks) {
            logger.info("\nTask Name: " + task.getName());
            logger.info("Description: " + task.getDescription());
            logger.info("Status: " + task.getStatus());
            logger.info("Start: " + task.getStartDate());
            logger.info("End: " + task.getEndDate());
            logger.info("Builders: " + task.getBuilderList());
        }
    }

    private static void assignBuilder(Scanner scanner) {
        logger.info("Enter project name:");
        String projectName = scanner.nextLine();
        logger.info("Enter task name: ");
        String taskName = scanner.nextLine();
        logger.info("Enter builder name: ");
        String builderName = scanner.nextLine();
        assignmentService.assignBuilderToTask(taskName, builderName);
        logger.info("Builder assigned successfully");
    }

    private static void updateTaskStatus(Scanner scanner) {

        logger.info("Enter task name: ");
        String taskName = scanner.nextLine();
        logger.info("Enter status: ");
        STATUS status = STATUS.valueOf(scanner.nextLine().toUpperCase());
        taskService.updateTaskStatus(taskName, status);
        logger.info("Status updated successfully");
    }
}
