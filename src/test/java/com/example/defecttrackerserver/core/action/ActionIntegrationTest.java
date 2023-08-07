package com.example.defecttrackerserver.core.action;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActionIntegrationTest {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void start() {postgres.start();}

    @AfterAll
    static void stop() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    ActionRepository actionRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        actionRepository.deleteAll();
    }

    @Test
    void shouldSaveAction() {
        System.out.println("Hello world");
    }
}
