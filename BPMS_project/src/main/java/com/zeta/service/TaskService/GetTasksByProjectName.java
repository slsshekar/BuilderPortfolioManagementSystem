package com.zeta.service.TaskService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zeta.Exceptions.ProjectServiceException.ProjectDoestNotExistException;
import com.zeta.model.Project;
import com.zeta.model.Task;
import com.zeta.service.FileService.FileService;
import com.zeta.service.utility.LoadFromTaskFile;

import java.util.*;

public class GetTasksByProjectName {

    private static final String FILE_NAME = "database/projects.json";

    public Set<String> getTasksByProjectName(String projectName) throws ProjectDoestNotExistException {
        Map<String , Project>projectHashMap=new HashMap<>();
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        projectHashMap= FileService.loadFromFile(FILE_NAME,mapper, Project.class);
        if(!projectHashMap.containsKey(projectName)){
            System.out.println("project doesnt exist");
            throw new ProjectDoestNotExistException(projectName+" does not exist");
        }
        return projectHashMap.get(projectName).getTaskList();

    }
}