package com.udla.markenx.shared.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  private String jwkSetUri;

  @Value("${keycloak.realm}")
  private String realm;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/api/markenx/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/markenx/students/**").hasAnyRole("STUDENT", "ADMIN")
            .requestMatchers("/api/markenx/assignments/status").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                .jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())
                .decoder(jwtDecoder())))
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  /**
   * Custom JWT decoder that accepts tokens from both localhost and
   * host.docker.internal
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

    // Create custom validator that accepts multiple issuers
    OAuth2TokenValidator<Jwt> multiIssuerValidator = token -> {
      String issuer = token.getClaimAsString("iss");
      List<String> validIssuers = Arrays.asList(
          "http://localhost:7080/realms/" + realm,
          "http://host.docker.internal:7080/realms/" + realm);

      if (validIssuers.contains(issuer)) {
        return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success();
      }

      return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure(
          new org.springframework.security.oauth2.core.OAuth2Error("invalid_token", "Invalid issuer: " + issuer, null));
    };

    OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();

    OAuth2TokenValidator<Jwt> combinedValidator = new DelegatingOAuth2TokenValidator<>(
        multiIssuerValidator,
        timestampValidator);

    jwtDecoder.setJwtValidator(combinedValidator);

    return jwtDecoder;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
