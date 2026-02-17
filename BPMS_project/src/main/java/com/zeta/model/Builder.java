package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Builder extends User {
    public Builder() {
        super();
        setRole(ROLE.BUILDER);
    }

    public Builder(String name, String password) {
        super(name, password, ROLE.BUILDER);
    }
    private List<Task> taskList=new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
