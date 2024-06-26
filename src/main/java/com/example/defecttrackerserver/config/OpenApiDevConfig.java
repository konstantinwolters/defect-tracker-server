package com.example.defecttrackerserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Profile;

/**
 * Swagger configuration.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Defect Tracker API",
                version = "0.8",
                description = """ 
                        This is a prototype of a REST API for tracking raw material defects and deriving corrective actions.
                        
                        Visit [GitHub repository](https://github.com/konstantinwolters/defect-tracker-server) for more details.
                        
                        Authentication has been bypassed for demonstration purposes. Feel free to try out the endpoints."""),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Development server"
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
@Profile("dev")
public class OpenApiDevConfig {
}
