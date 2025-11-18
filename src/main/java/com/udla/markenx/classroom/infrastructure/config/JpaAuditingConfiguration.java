package com.udla.markenx.classroom.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Configuration for JPA Auditing.
 * 
 * Enables automatic population of audit fields (createdBy, createdAt,
 * lastModifiedBy, lastModifiedAt)
 * on entities annotated with @EntityListeners(AuditingEntityListener.class).
 * 
 * Only applies to admin CRUD operations as students can only view/upload data.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
public class JpaAuditingConfiguration {

  /**
   * Provides the current auditor (admin user) from SecurityContext.
   * 
   * @return AuditorAware<String> with current admin's email
   */
  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !authentication.isAuthenticated()) {
        return Optional.of("system");
      }

      // Return authenticated user's email (from JWT)
      String auditor = authentication.getName();
      return Optional.ofNullable(auditor);
    };
  }

  /**
   * Provides current LocalDateTime for audit timestamps.
   * 
   * @return DateTimeProvider with current LocalDateTime
   */
  @Bean
  public DateTimeProvider dateTimeProvider() {
    return () -> Optional.of(LocalDateTime.now());
  }
}
