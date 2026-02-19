package com.zeta.DAO;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseConfig {

    private static final Path BASE =
            Paths.get(System.getProperty("user.home"), "bpms_database");

    static {
        BASE.toFile().mkdirs();
    }

    public static Path users() {
        return BASE.resolve("users.json");
    }

    public static Path tasks() {
        return BASE.resolve("tasks.json");
    }

    public static Path projects() {
        return BASE.resolve("projects.json");
    }
}
