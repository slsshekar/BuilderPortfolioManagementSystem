package com.zeta.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Task {
    private static int counter=1;
    private int id;
    private String name;
    private String description;
    private String projectName;
    private Set<String> builderList = new HashSet<>();
    private String managerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private STATUS status;
    public Task(){

    }
    public Task(String name,String description,String projectName,String managerName,LocalDate startDate,LocalDate endDate){
        this.id=counter++;
        this.name=name;
        this.description=description;
        this.projectName=projectName;
        this.managerName=managerName;
        this.startDate=startDate;
        this.endDate=endDate;
        this.status=STATUS.UPCOMING;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getBuilderList() {
        return builderList;
    }

    public void setBuilderList(Set<String> builderList) {
        this.builderList = builderList;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
