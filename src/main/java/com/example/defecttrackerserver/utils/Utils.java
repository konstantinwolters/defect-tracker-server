package com.example.defecttrackerserver.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class Utils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public String saveImageToFileSystem(MultipartFile image, String folderPath, Integer defectId, Integer imageId) {

        String filename = LocalDate.now() + "_" + defectId + "_" + imageId + ".jpg";
        String filePath = folderPath + File.separator + filename;

        try {
            byte[] bytes = image.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image " + filename, e);
        }

        return filePath;
    }

    public void createDirectory(String folderPath) {
        File directory = new File(folderPath);
        if(!directory.exists()){
            boolean success = directory.mkdirs();
            if (!success) {
                throw new RuntimeException("Failed to create image directory: " + directory.getAbsolutePath());
            }
        }
    }
}