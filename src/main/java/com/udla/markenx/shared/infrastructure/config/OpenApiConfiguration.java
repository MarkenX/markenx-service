package com.udla.markenx.shared.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

  @Value("${server.port:8080}")
  private String serverPort;

  @Bean
  public OpenAPI markenxOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
        .info(new Info()
            .title("MarkenX API")
            .description("""
                REST API for MarkenX Service - A gamified learning platform that manages custom game sessions
                based on administrator-defined configurations. These configurations are exported to a Unity-based
                video game and deployed to student clients.

                ## Authentication
                This API uses OAuth2 with JWT tokens (Keycloak). Most endpoints require authentication.

                To authenticate:
                1. Obtain a JWT token from Keycloak (POST to /realms/{realm}/protocol/openid-connect/token)
                2. Click the 'Authorize' button below
                3. Enter your token in the format: Bearer {your-token}

                ## Roles
                - **ADMIN**: Full access to all endpoints including student and course management
                - **STUDENT**: Limited access to own profile and assigned tasks
                """)
            .version("0.0.1-SNAPSHOT")
            .contact(new Contact()
                .name("UDLA MarkenX Team")
                .email("support@udla.edu.ec")
                .url("https://github.com/ChrisJMora/udla-markenx-service"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
        .servers(List.of(
            new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server"),
            new Server()
                .url("http://host.docker.internal:" + serverPort)
                .description("Docker Development Server")))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(new io.swagger.v3.oas.models.Components()
            .addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token from Keycloak. Format: Bearer {token}")));
  }
}
