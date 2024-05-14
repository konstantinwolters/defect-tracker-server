package com.example.defecttrackerserver.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIoConfig {

    @Value("${MINIO.ENDPOINT}")
    private String endpoint;

    @Value("${MINIO.ACCESS-KEY}")
    private String accessKey;

    @Value("${MINIO.SECRET-KEY}")
    private String secretKey;

    @Value("${MINIO.BUCKET-NAME}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
    }
}
