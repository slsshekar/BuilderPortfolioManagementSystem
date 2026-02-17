package com.zeta.model;

import java.util.HashSet;
import java.util.Set;

public class Manager extends User{
    Manager(){

    }
    public Manager(String name,String password,ROLE role){
        this.setName(name);
        this.setPassword(password);
        this.setRole(role);
    }
    private Set<Project> projectList=new HashSet<>();

    public Set<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(Set<Project> projectList) {
        this.projectList = projectList;
    }
}
