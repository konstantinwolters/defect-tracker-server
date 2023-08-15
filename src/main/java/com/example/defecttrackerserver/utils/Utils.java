package com.example.defecttrackerserver.utils;

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

        if (image.getSize() > 5 * 1024 * 1024) {  // 5 MB in bytes
            throw new IllegalArgumentException("Image size should be less than 5 MB.");
        }
    }

    public String saveImageToFileSystem(MultipartFile image, String folderPath, Integer defectId, Integer imageId) {
        // Construct filename using date and defect ID
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


}