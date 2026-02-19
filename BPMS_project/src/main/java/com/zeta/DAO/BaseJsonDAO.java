package com.zeta.DAO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseJsonDAO<T> {

    protected final ObjectMapper mapper;
    protected final Path path;
    protected final TypeReference<Map<String, T>> typeReference;

    protected BaseJsonDAO(ObjectMapper mapper, Path path,
                          TypeReference<Map<String, T>> typeReference) {
        this.mapper = mapper;
        this.path = path;
        this.typeReference = typeReference;

    }

    public Map<String, T> load() {

        path.toFile().getParentFile().mkdirs();

        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock(0L, Long.MAX_VALUE, true)) {

            if (file.length() == 0) {
                return new HashMap<>();
            }

            return mapper.readValue(path.toFile(), typeReference);

        } catch (Exception e) {
            e.printStackTrace(); // remove silent failure
            return new HashMap<>();
        }
    }


    public void save(Map<String, T> data) {

        path.toFile().getParentFile().mkdirs();

        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            mapper.writeValue(path.toFile(), data);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

}
