package com.zeta.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private int id;
    private String name;
    private String description;
    private List<Builder> builderList = new ArrayList<>();
    private List<Manager> managerList = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private STATUS status;

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

    public List<Builder> getBuilderList() {
        return builderList;
    }

    public void setBuilderList(List<Builder> builderList) {
        this.builderList = builderList;
    }

    public List<Manager> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<Manager> managerList) {
        this.managerList = managerList;
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
