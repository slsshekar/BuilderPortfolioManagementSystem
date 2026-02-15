package com.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    private List<Task> taskList=new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
