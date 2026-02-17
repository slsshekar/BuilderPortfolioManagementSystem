package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Client extends User{
    public Client() {
        super();
        setRole(ROLE.CLIENT);
    }

    public Client(String name, String password) {
        super(name, password, ROLE.CLIENT);
    }
    private List<Project> projectList=new ArrayList<>();

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
