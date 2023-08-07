package com.example.defecttrackerserver.core.action;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class SimpleTestcontainersTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void start() {
        postgres.start();
    }

    @AfterAll
    static void stop() {
        postgres.stop();
    }

    @Test
    void simpleContainerTest() {
        System.out.println("Container JDBC URL: " + postgres.getJdbcUrl());
        System.out.println("Container is running: " + postgres.isRunning());
    }
}