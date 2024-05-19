package com.example.defecttrackerserver.utils;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    private final MinioClient minioClient;

    @Value("${IMAGE.UPLOAD.MAX-FILE-SIZE}")
    Integer MAX_FILE_SIZE;

    @Value("${MINIO.BUCKET-NAME}")
    private String bucketName;

    @PostConstruct
    public void ensureBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("Bucket '" + bucketName + "' created successfully.");
            } else {
                System.out.println("Bucket '" + bucketName + "' already exists.");
            }
        } catch (MinioException e) {
            System.err.println("Error occurred: " + e);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

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

    public String uploadImage(MultipartFile image) {
        String uuid = UUID.randomUUID().toString();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uuid)
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .contentType("image/jpeg")
                            .build()
            );
            return uuid;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void removeImage(String imageUuid) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageUuid)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove image", e);
        }
    }

    public String getPresignedImageUrl(String imageUuid) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(imageUuid)
                            .expiry(60 * 60)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get presigned image url", e);
        }
    }
}