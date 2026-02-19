package com.zeta.DAO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.Project;

import java.nio.file.Paths;
import java.util.Map;

public class ProjectDAO extends BaseJsonDAO<Project> {

    private static final String FILE = "database/projects.json";

    public ProjectDAO(ObjectMapper mapper) {
        super(
                mapper,
                Paths.get(FILE),
                new TypeReference<Map<String, Project>>() {
                }
        );
    }
}
