package com.example.defecttrackerserver.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contains different utilities for data conversion and file operations.
 */
@Component
@RequiredArgsConstructor
public class Utils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DefaultFileSystemOperations fileSystemOperations;

    @Value("${IMAGE.UPLOAD.MAX-FILE-SIZE}")
    Integer MAX_FILE_SIZE;

    public LocalDateTime convertToDateTime(String date) {
        LocalDate dateObj = LocalDate.parse(date, FORMATTER);
        return dateObj.atStartOfDay();
    }

    public List<Integer> convertStringToListOfInteger(String string) {
        return (string != null) ? Arrays.stream(string.split(","))
                .map(Integer::valueOf)
                .toList() : null;
    }

    public void validateImage(MultipartFile image) {
        if (!"image/jpeg".equals(image.getContentType())) {
            throw new IllegalArgumentException("Only JPG or JPEG images are allowed.");
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Image size has to be less than 3 MB per image.");
        }
    }

    public <T, R> Set<R> mapToSet(Collection<T> collection, Function<T, R> mapper) {
        return collection.stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.toSet());
    }

    public String saveImageToFileSystem(MultipartFile image, String folderPath) {

        String filename = UUID.randomUUID().toString() + ".jpg";
        String filePath = folderPath + File.separator + filename;

        try {
            byte[] bytes = image.getBytes();
            Path path = Paths.get(filePath);
            fileSystemOperations.write(path,bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image " + filename, e);
        }
        return filePath;
    }

    public void removeFileFromFileSystem(String path) {
        Path filePath = Paths.get(path);
        if (!fileSystemOperations.exists(filePath)) {
            throw new RuntimeException("File not found at path: " + path);
        }

        boolean deleted = fileSystemOperations.delete(filePath);
        if (!deleted) {
            throw new RuntimeException("Failed to delete file at path: " + path);
        }
    }

    public void createDirectory(String folderPath) {
        Path directoryPath = Paths.get(folderPath);
        if (!fileSystemOperations.exists(directoryPath)) {
                boolean success = fileSystemOperations.createDirectories(directoryPath);
                if (!success) {
                    throw new RuntimeException("Failed to create image directory: " + directoryPath.toAbsolutePath());
                }
        }
    }
}