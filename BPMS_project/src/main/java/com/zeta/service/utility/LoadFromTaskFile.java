package com.zeta.service.utility;

import com.zeta.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadFromTaskFile {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static Map<String, Task> load(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists() && file.length() > 0) {
                return mapper.readValue(
                        file,
                        mapper.getTypeFactory()
                                .constructMapType(HashMap.class, String.class, Task.class));
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return new HashMap<>();
    }
}
