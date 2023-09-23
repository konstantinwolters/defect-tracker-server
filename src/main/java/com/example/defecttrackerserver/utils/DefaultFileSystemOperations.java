package com.example.defecttrackerserver.utils;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DefaultFileSystemOperations implements FileSystemOperations {
    @Override
    public void write(Path path, byte[] bytes) throws IOException {
        Files.write(path, bytes);
    }

    @Override
    public boolean exists(Path path) {
        return Files.exists(path);
    }

    @Override
    public boolean delete(Path path) {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean createDirectories(Path path) {
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
