package com.zeta.model;

import javax.print.DocFlavor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Manager.class, name = "MANAGER"),
        @JsonSubTypes.Type(value = Builder.class, name = "BUILDER"),
        @JsonSubTypes.Type(value = Client.class, name = "CLIENT")
})
public abstract class User {
    private static int counter=1;
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
    public User() {
        this.id = counter++;
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
