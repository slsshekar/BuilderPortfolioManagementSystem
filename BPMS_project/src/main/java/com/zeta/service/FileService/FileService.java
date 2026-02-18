package com.zeta.service.FileService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileService {

    // Resolves file path relative to the project root (where pom.xml lives).
    private static File getProjectFile(String relativePath) {

        try {
            File codeLocation = new File(
                    FileService.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );

            // Go up until we find pom.xml
            File dir = codeLocation;

            while (dir != null) {
                File pomFile = new File(dir, "pom.xml");
                if (pomFile.exists()) {
                    return new File(dir, relativePath);
                }
                dir = dir.getParentFile();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve project root", e);
        }

        throw new RuntimeException("pom.xml not found in classpath hierarchy.");
    }

    // LOAD MAP<String, T>
    public static <T> Map<String, T> loadFromFile(
            String fileName,
            ObjectMapper mapper,
            Class<T> clazz) {

        try {
            File file = getProjectFile(fileName);

            // if file not present or empty → return empty map
            if (!file.exists() || file.length() == 0) {
                return new HashMap<>();
            }

            return mapper.readValue(
                    file,
                    mapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, clazz));

        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // SAVE MAP<String, T>
    public static <T> void saveToFile(
            Map<String, T> objectsList,
            String fileName,
            ObjectMapper mapper) {

        try {
            File file = getProjectFile(fileName);
            // Ensure parent directory exists
            file.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, objectsList);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
