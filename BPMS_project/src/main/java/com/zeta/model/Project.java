package com.zeta.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Project {
    private static int counter=1;
    private String name;
    private String description;
    private Set<String> taskList = new HashSet<>();
    private Set<String> managerList = new HashSet<>();
    private String clientName;
    private STATUS status;
    private LocalDate startDate;
    private LocalDate endDate;
    private int id;
    public Project(){

    }
    public Project(String name, String description, LocalDate startDate, LocalDate endDate){
        this.name=name;
        this.description=description;
        this.startDate=startDate;
        this.endDate=endDate;
        this.id=counter++;
        this.status=STATUS.NOT_APPROVED;
    }
    public int getId() {
        return id;
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

    public Set<String> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<String> taskList) {
        this.taskList = taskList;
    }

    public Set<String > getManagerList() {
        return managerList;
    }

    public void setManagerList(Set<String> managerList) {
        this.managerList = managerList;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", managerList=" + managerList +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", taskList=" + taskList +
                '}';
    }



}
