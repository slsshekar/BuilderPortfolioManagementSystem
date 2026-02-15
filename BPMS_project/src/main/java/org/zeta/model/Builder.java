package org.zeta.model;

import java.util.ArrayList;
import java.util.List;

public class Builder extends User{
    private List<Task> taskList=new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
