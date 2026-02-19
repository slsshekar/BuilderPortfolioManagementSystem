package com.zeta.model;

import java.util.HashSet;
import java.util.Set;

public class Manager extends User {
    public Manager() {

    }

    private Set<String> projectList;

    public Manager(String name, String password, ROLE role) {
        this.setName(name);
        this.setPassword(password);
        this.setRole(role);
        projectList = new HashSet<>();
    }

    public Set<String> getProjectList() {
        return projectList;
    }

    public void setProjectList(Set<String> projectList) {
        this.projectList = projectList;
    }
}
