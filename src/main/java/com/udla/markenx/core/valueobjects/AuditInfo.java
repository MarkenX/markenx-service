package com.udla.markenx.core.valueobjects;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.Getter;

@Getter
public class AuditInfo {
  private static final String DEFAULT_USER = "system";

  private final String createdBy;
  private final Instant createdAt;
  private String lastModifiedBy;
  private Instant lastModifiedAt;

  public AuditInfo() {
    this.createdBy = DEFAULT_USER;
    this.createdAt = Instant.now();
    this.lastModifiedAt = this.createdAt;
  }

  public AuditInfo(String createdBy) {
    this.createdBy = createdBy;
    this.createdAt = Instant.now();
    this.lastModifiedAt = this.createdAt;
  }

  public AuditInfo(LocalDateTime createdAt, LocalDateTime updatedAt) {
    ZoneId zone = ZoneId.systemDefault();
    this.createdBy = DEFAULT_USER;
    this.createdAt = createdAt.atZone(zone).toInstant();
    this.lastModifiedAt = updatedAt.atZone(zone).toInstant();
  }

  public AuditInfo(String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
    ZoneId zone = ZoneId.systemDefault();
    this.createdBy = createdBy;
    this.createdAt = createdAt.atZone(zone).toInstant();
    this.lastModifiedAt = updatedAt.atZone(zone).toInstant();
  }

  public void markUpdated() {
    this.lastModifiedAt = Instant.now();
  }

  public LocalDate getCreatedDate() {
    return createdAt.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public LocalDate getUpdatedDate() {
    return lastModifiedAt.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public LocalDateTime getCreatedDateTime() {
    return createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public LocalDateTime getUpdatedDateTime() {
    return lastModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
