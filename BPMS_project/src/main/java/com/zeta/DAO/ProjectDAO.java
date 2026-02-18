package com.zeta.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.Project;
import com.zeta.service.FileService.FileService;

import java.util.Map;

public class ProjectDAO {

    private final ObjectMapper mapper;
    private static final String FILE = "database/projects.json";

    public ProjectDAO(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, Project> load() {
        return FileService.loadFromFile(FILE, mapper, Project.class);
    }

    public void save(Map<String, Project> projects) {
        FileService.saveToFile(projects, FILE, mapper);
    }
}
