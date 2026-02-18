package com.zeta.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.User;
import com.zeta.service.FileService.FileService;

import java.util.Map;

public class UserDAO {

    private final ObjectMapper mapper;
    private static final String FILE = "database/users.json";

    public UserDAO(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, User> load() {
        return FileService.loadFromFile(FILE, mapper, User.class);
    }

    public void save(Map<String, User> users) {
        FileService.saveToFile(users, FILE, mapper);
    }
}
