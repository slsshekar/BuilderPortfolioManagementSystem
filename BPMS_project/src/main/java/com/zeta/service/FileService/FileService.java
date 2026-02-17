package com.zeta.service.FileService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileService {

    public static Map<String, User> loadFromFile(
            Map<String, User> objectsList,
            String FILE_NAME,
            ObjectMapper mapper) {

        try {
            File file = new File(FILE_NAME);

            if (file.exists() && file.length() > 0) {

                objectsList = mapper.readValue(
                        file,
                        new TypeReference<Map<String, User>>() {}
                );

            } else {
                objectsList = new HashMap<>();
            }

        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            objectsList = new HashMap<>();
        }

        return objectsList;
    }

    public static void saveToFile(
            Map<String, User> objectsList,
            String FILE_NAME,
            ObjectMapper mapper) {

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), objectsList);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
