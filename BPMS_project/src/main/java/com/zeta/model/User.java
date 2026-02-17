package com.zeta.model;

public abstract class User {
    private int id;
    private String name;
    private String password;

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    private ROLE role;
    public User(){

    }
    public User(String name, String password, ROLE role){
        this.name=name;
        this.password=password;
        this.role=role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
