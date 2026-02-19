package com.zeta.DAO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.User;

import java.nio.file.Paths;
import java.util.Map;

public class UserDAO extends BaseJsonDAO<User> {

    private static final String FILE =
            System.getProperty("user.dir") + "/database/users.json";

    public UserDAO(ObjectMapper mapper) {
        super(
                mapper,
                Paths.get(FILE),
                new TypeReference<Map<String, User>>() {
                }
        );
    }
}
