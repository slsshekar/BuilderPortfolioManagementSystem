package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User{
    public Manager() {
        super();
        setRole(ROLE.MANAGER);
    }

    public Manager(String name, String password) {
        super(name, password, ROLE.MANAGER);
    }
    private List<Project>projectList=new ArrayList<>();

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
