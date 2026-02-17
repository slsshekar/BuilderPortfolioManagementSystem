package com.zeta.service.ProjectService;

import com.zeta.model.Manager;
import com.zeta.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    public Project create(Project project){
        return project;
    }
    public boolean approve(Project project){
        return true;
    }
    public void assignManager(Project project, Manager manager){
        project.getManagerList().add(manager);
    }
    public List<Project> getProjectsByClientId(int clientId){
        return new ArrayList<>();
    }
    public List<Project> getProjectsByManagerId(int managerId){
        return new ArrayList<>();
    }
//    public Project getProjectById(int id){
//        return new Project();
//    }
}
