package com.zeta.service.FileService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileService {
    private static File getProjectFile(String relativePath) {
        File file = new File(relativePath);
        if (file.exists()) {
            return file;
        }

        File dir = new File(System.getProperty("user.dir"));
        while (dir != null) {
            File candidate = new File(dir, relativePath);
            File pom = new File(dir, "pom.xml");
            if (pom.exists() && (candidate.exists() || candidate.getParentFile().exists())) {
                return candidate;
            }

            File[] children = dir.listFiles(File::isDirectory);
            if (children != null) {
                for (File child : children) {
                    File childCandidate = new File(child, relativePath);
                    File childPom = new File(child, "pom.xml");
                    if (childPom.exists() && (childCandidate.exists() || childCandidate.getParentFile().exists())) {
                        return childCandidate;
                    }
                }
            }

            dir = dir.getParentFile();
        }
        return file;
    }

    public static <T> Map<String, T> loadFromFile(
            String fileName,
            ObjectMapper mapper,
            Class<T> clazz) {

        try {
            File file = getProjectFile(fileName);
            if (!file.exists() || file.length() == 0) {
                return new HashMap<>();
            }

            return mapper.readValue(file, mapper.getTypeFactory().constructMapType(HashMap.class, String.class, clazz));

        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static <T> void saveToFile(
            Map<String, T> objectsList,
            String fileName,
            ObjectMapper mapper) {

        try {
            File file = getProjectFile(fileName);
            file.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, objectsList);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
