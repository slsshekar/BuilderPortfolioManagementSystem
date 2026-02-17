package com.zeta.service.FileService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileService {

    // LOAD MAP<String, T>
    public static <T> Map<String, T> loadFromFile(
            String fileName,
            ObjectMapper mapper,
            Class<T> clazz) {

        try {
            File file = new File(fileName);

            // if file not present or empty → return empty map
            if (!file.exists() || file.length() == 0) {
                return new HashMap<>();
            }

            return mapper.readValue(
                    file,
                    mapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, clazz)
            );

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
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), objectsList);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
