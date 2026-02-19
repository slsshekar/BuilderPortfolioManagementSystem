package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.logging.Logger;
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
    static Logger logger = Logger.getInstance();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final TaskDAO taskDAO = new TaskDAO(mapper);
    private static final UserDAO userDAO = new UserDAO(mapper);

    private static final TaskService taskService = new TaskService(taskDAO, userDAO);
    private static final TaskAssignmentService assignmentService = new TaskAssignmentService(taskDAO, userDAO);

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
        logger.info("\n=== Builder Menu ===");
        logger.info("1. View All Assigned Tasks");
        logger.info("2. View Tasks By Status");
        logger.info("3. View Task Details");
        logger.info("4. Logout");
        logger.info("Enter choice: ");
    }

    private static void viewAssignedTasks(String builderName) {

        Set<String> taskNames = assignmentService.getTasksOfBuilder(builderName);

        if (taskNames.isEmpty()) {
            logger.info("No tasks assigned");
            return;
        }

        logger.info("\nYour Tasks:");
        taskNames.forEach(t -> logger.info("• " + t));
    }


    private static void viewTasksByStatus(Scanner scanner, String builderName) {

        Set<String> assignedTasks = assignmentService.getTasksOfBuilder(builderName);

        if (assignedTasks.isEmpty()) {
            logger.info("No tasks assigned");
            return;
        }

        logger.info("\nAvailable Status:");
        for (STATUS s : STATUS.values()) {
            logger.info("- " + s);
        }

        logger.info("Enter status: ");
        STATUS status = STATUS.valueOf(scanner.nextLine().toUpperCase());

        List<Task> filteredTasks = assignedTasks.stream()
                .map(taskService::getTask)
                .filter(t -> t != null && t.getStatus() == status)
                .collect(Collectors.toList());

        if (filteredTasks.isEmpty()) {
            logger.info("No tasks with status " + status);
            return;
        }

        logger.info("\nTasks with status " + status + ":");
        filteredTasks.forEach(t -> logger.info("• " + t.getName()));
    }


    private static void viewTaskDetails(Scanner scanner, String builderName) {

        logger.info("Enter task name: ");
        String taskName = scanner.nextLine();

        if (!assignmentService.getTasksOfBuilder(builderName).contains(taskName)) {
            logger.info("Task not assigned to you");
            return;
        }

        Task task = taskService.getTask(taskName);

        if (task == null) {
            logger.info("Task not found");
            return;
        }

        printTask(task);
    }


    private static void printTask(Task task) {

        logger.info("\n===== Task Details =====");
        logger.info("Name: " + task.getName());
        logger.info("Description: " + task.getDescription());
        logger.info("Project: " + task.getProjectName());
        logger.info("Manager: " + task.getManagerName());
        logger.info("Start Date: " + task.getStartDate());
        logger.info("End Date: " + task.getEndDate());
        logger.info("Status: " + task.getStatus());
        logger.info("Builders: " + task.getBuilderList());
    }
}
