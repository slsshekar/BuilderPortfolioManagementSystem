package com.zeta.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Client extends User{
    public Client(){

    }
    public Client(String username, String password, ROLE role){
        this.setName(username);
        this.setPassword(password);
        this.setRole(role);
    }
    private Set<String> projectList=new HashSet<>();

    public Set<String> getProjectList() {
        return projectList;
    }

    public void setProjectList(Set<String> projectList) {
        this.projectList = projectList;
    }
}
