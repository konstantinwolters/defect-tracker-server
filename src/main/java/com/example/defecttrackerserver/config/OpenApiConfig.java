package com.example.defecttrackerserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Swagger configuration.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Defect Tracker API",
                version = "1.0",
                description = "Defect Tracker API"),//TODO: Give better description
        servers = {@Server(
                        url = "http://localhost:8080",
                        description = "Local server"
                ),
                @Server(
                        url="https://defect-tracker-server.herokuapp.com",
                        description="Heroku server"
                )
        },
        security = @SecurityRequirement(
                name = "bearerAuth"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
