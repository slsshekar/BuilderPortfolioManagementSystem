package com.zeta.model;

import java.util.HashSet;
import java.util.Set;

public class Builder extends User {
    public Builder() {

    }

    public Builder(String username, String password, ROLE role) {
        this.setName(username);
        this.setPassword(password);
        this.setRole(role);
    }

    private Set<String> taskList = new HashSet<>();

    public Set<String> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<String> taskList) {
        this.taskList = taskList;
    }
}
