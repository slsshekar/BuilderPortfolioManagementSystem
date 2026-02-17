package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Client extends User{
    public Client(){

    }
    public Client(String username, String password, ROLE role){
        this.setName(username);
        this.setPassword(password);
        this.setRole(role);
    }
    private List<Project> projectList=new ArrayList<>();

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
