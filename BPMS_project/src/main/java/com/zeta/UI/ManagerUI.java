package com.zeta.UI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.DAO.ProjectDAO;
import com.zeta.DAO.TaskDAO;
import com.zeta.DAO.UserDAO;
import com.zeta.Exceptions.LoginException.UserNotFoundException;
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
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ProjectDAO projectDAO = new ProjectDAO(mapper);
    private static final TaskDAO taskDAO = new TaskDAO(mapper);
    private static final UserDAO userDAO = new UserDAO(mapper);

    private static final ProjectService projectService =
            new ProjectService(projectDAO, userDAO);

    private static final TaskService taskService =
            new TaskService(taskDAO, userDAO);

    private static final TaskAssignmentService assignmentService =
            new TaskAssignmentService(taskDAO, userDAO);

    private static final ManagerService managerService =
            new ManagerService(projectDAO, taskService);

    public static void show(Scanner scanner, String username) {

        while (true) {
            printMenu();
            int choice = Utility.getValidChoice(scanner);

            try {
                switch (choice) {
                    case 1 -> viewProjects(username);
                    case 2 -> createTask(scanner, username);
                    case 3 -> viewProject(scanner,username);
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
        System.out.println("\n=== Manager Menu ===");
        System.out.println("1. View Projects");
        System.out.println("2. Create Task");
        System.out.println("3. View Project Details");
        System.out.println("4. Assign Builder");
        System.out.println("5. Update Task Status");
        System.out.println("6. Logout");
    }


    private static void viewProjects(String username) throws UserNotFoundException {

        Set<String> projectNames =
                projectService.getProjectsByManagerName(username);

        if (projectNames == null || projectNames.isEmpty()) {
            System.out.println("No projects assigned to you.");
            return;
        }

        var groupedProjects =
                managerService.getProjectsByStatus(projectNames);

        System.out.println("\n====== PROJECT BOARD ======");

        for (STATUS status : STATUS.values()) {

            if (status == STATUS.NOT_APPROVED) continue;

            System.out.println("\n" + getEmoji(status) + " " + status);

            var projects = groupedProjects.getOrDefault(status, new ArrayList<>());

            if (projects.isEmpty()) {
                System.out.println("No projects");
            } else {
                projects.forEach(p -> System.out.println("- " + p.getName()));
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

        LocalDate start =
                Utility.readDate(scanner, "Enter start date (dd-MM-yyyy): ");

        LocalDate end =
                Utility.readDate(scanner, "Enter end date (dd-MM-yyyy): ");

        managerService.createTaskForProject(
                projectName, taskName, desc, username, start, end
        );

        System.out.println("Task created successfully");
    }

    private static void viewProject(Scanner scanner, String username) throws UserNotFoundException {

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();
        Set<String> projectNames = projectService.getProjectsByManagerName(username);
        if(!projectNames.contains(projectName)){
            System.out.println(projectName+" does not exist for you");
        }
        var project = managerService.getProject(projectName);
        System.out.println(project);
        var tasks = managerService.getTasksOfProject(projectName);

        System.out.println("\n------ TASKS ------");

        if (tasks.isEmpty()) {
            System.out.println("No tasks created for this project");
            return;
        }

        for (var task : tasks) {
            System.out.println("\nTask Name: " + task.getName());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Status: " + task.getStatus());
            System.out.println("Start: " + task.getStartDate());
            System.out.println("End: " + task.getEndDate());
            System.out.println("Builders: " + task.getBuilderList());
        }
    }

    private static void assignBuilder(Scanner scanner) {
        System.out.println("Enter project name:");
        String projectName= scanner.nextLine();
        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter builder name: ");
        String builderName = scanner.nextLine();
        assignmentService.assignBuilderToTask(taskName, builderName);
        System.out.println("Builder assigned successfully");
    }

    private static void updateTaskStatus(Scanner scanner) {

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter status: ");
        STATUS status = STATUS.valueOf(scanner.nextLine().toUpperCase());
        taskService.updateTaskStatus(taskName, status);
        System.out.println("Status updated successfully");
    }
}
