package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Builder extends User {
    public Builder(){

    }
    public Builder(String username, String password, ROLE role){
        this.setName(username);
        this.setPassword(password);
        this.setRole(role);
    }
    private List<Task> taskList=new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
