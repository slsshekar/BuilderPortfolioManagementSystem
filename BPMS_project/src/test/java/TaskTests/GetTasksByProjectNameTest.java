package TaskTests;

import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.model.Project;
import com.zeta.model.STATUS;
import com.zeta.service.TaskService.GetTasksByProjectName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.service.FileService.FileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GetTasksByProjectNameTest {

    private final GetTasksByProjectName getTasksByProjectName = new GetTasksByProjectName();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() throws IOException {
        File projectsFile = new File("database/projects.json");
        projectsFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(projectsFile);
        writer.write("{}");
        writer.close();

        File tasksFile = new File("database/tasks.json");
        FileWriter writer2 = new FileWriter(tasksFile);
        writer2.write("{}");
        writer2.close();
    }

    private void seedProject(String projectName, Set<String> taskNames) {
        Map<String, Project> projectMap = new HashMap<>();
        Project project = new Project();
        project.setName(projectName);
        project.setDescription("Description for " + projectName);
        project.setStatus(STATUS.IN_PROGRESS);
        project.setTaskList(taskNames);
        projectMap.put(projectName, project);

        Map<String, Project> existing = FileService.loadFromFile("database/projects.json", mapper, Project.class);
        existing.putAll(projectMap);
        FileService.saveToFile(existing, "database/projects.json", mapper);
    }

    @Test
    void testGetTasksForExistingProject() throws ProjectDoestNotExistException {
        seedProject("Project Alpha", Set.of("Task A", "Task B"));

        Set<String> tasks = getTasksByProjectName.getTasksByProjectName("Project Alpha");
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains("Task A"));
        assertTrue(tasks.contains("Task B"));
    }

    @Test
    void testGetTasksForNonExistentProject() {
        assertThrows(ProjectDoestNotExistException.class, () -> {
            getTasksByProjectName.getTasksByProjectName("NonExistentProject");
        });
    }

    @Test
    void testGetTasksReturnsSingleMatch() throws ProjectDoestNotExistException {
        seedProject("Project Alpha", Set.of("Task A"));
        seedProject("Project Beta", Set.of("Task B"));

        Set<String> tasks = getTasksByProjectName.getTasksByProjectName("Project Alpha");
        assertEquals(1, tasks.size());
        assertTrue(tasks.contains("Task A"));
    }

    @Test
    void testGetTasksFromProjectWithEmptyTaskList() throws ProjectDoestNotExistException {
        seedProject("Empty Project", Set.of());

        Set<String> tasks = getTasksByProjectName.getTasksByProjectName("Empty Project");
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testGetTasksDoesNotReturnOtherProjectTasks() throws ProjectDoestNotExistException {
        seedProject("Project Alpha", Set.of("Task A", "Task C"));
        seedProject("Project Beta", Set.of("Task B"));

        Set<String> tasks = getTasksByProjectName.getTasksByProjectName("Project Alpha");
        assertEquals(2, tasks.size());
        assertFalse(tasks.contains("Task B"));
    }
}
