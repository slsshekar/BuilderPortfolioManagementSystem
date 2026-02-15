package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Client{
    private List<Project> projectList=new ArrayList<>();

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
