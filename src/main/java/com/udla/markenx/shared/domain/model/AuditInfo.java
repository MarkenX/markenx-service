package com.udla.markenx.shared.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.udla.markenx.shared.domain.valueobjects.AuditParams;

public class AuditInfo {
  private static final String DEFAULT_USER = "system";

  private final String createdBy;
  private final Instant createdAt;
  private String updatedBy;
  private Instant updatedAt;

  // Para cuando se crea nueva entidad desde el servicio
  public AuditInfo() {
    this.createdBy = DEFAULT_USER;
    this.createdAt = Instant.now();
    this.updatedBy = this.createdBy;
    this.updatedAt = this.createdAt;
  }

  // Para cuando se crea nueva entidad desde fuera del servicio
  public AuditInfo(String createdBy) {
    this.createdBy = createdBy;
    this.createdAt = Instant.now();
    this.updatedBy = this.createdBy;
    this.updatedAt = this.createdAt;
  }

  // Para cuando se obtiene información desde la base de datos
  public AuditInfo(AuditParams params) {
    ZoneId zone = ZoneId.systemDefault();
    this.createdBy = params.createdBy();
    this.createdAt = params.createdAt().atZone(zone).toInstant();
    this.updatedBy = params.updatedBy();
    this.updatedAt = params.updatedAt().atZone(zone).toInstant();
  }

  public void markUpdated() {
    this.updatedAt = Instant.now();
  }

  // #region Getters

  public String getCreatedBy() {
    return this.createdBy;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  public LocalDate getCreatedDate() {
    return createdAt.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public LocalDate getUpdatedDate() {
    return updatedAt.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public LocalDateTime getCreatedDateTime() {
    return createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public LocalDateTime getUpdatedDateTime() {
    return updatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  // #endregion Getters
}
