package com.example.defecttrackerserver.utils;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemOperations {
    void write(Path path, byte[] bytes) throws Exception;
    boolean exists(Path path);
    boolean delete(Path path) throws IOException;
    boolean createDirectories(Path path) throws IOException;

}
