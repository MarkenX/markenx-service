package com.udla.markenx.shared.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

import org.springframework.lang.NonNull;

import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.classroom.domain.valueobjects.AuditInfo;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public abstract class DomainBaseModel {

  private final UUID id;
  private DomainBaseModelStatus status;
  private final AuditInfo auditInfo;

  public DomainBaseModel(UUID id, String code, DomainBaseModelStatus status, String createdBy, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = requireId(id);
    this.status = requireStatus(status);
    this.auditInfo = requireAuditInfo(new AuditInfo(createdBy, createdAt, updatedAt));
  }

  public DomainBaseModel(String createdBy) {
    this.id = UUID.randomUUID();
    this.status = DomainBaseModelStatus.ENABLED;
    this.auditInfo = new AuditInfo(createdBy);
  }

  public DomainBaseModel() {
    this.id = UUID.randomUUID();
    this.status = DomainBaseModelStatus.ENABLED;
    this.auditInfo = new AuditInfo();
  }

  public @NonNull UUID getId() {
    return Objects.requireNonNull(this.id);
  }

  public DomainBaseModelStatus getStatus() {
    return this.status;
  }

  public String getCreatedBy() {
    return this.auditInfo.getCreatedBy();
  }

  public String getUpdatedBy() {
    return this.auditInfo.getLastModifiedBy();
  }

  public LocalDate getCreatedAtDate() {
    return this.auditInfo.getCreatedDate();
  }

  public LocalDateTime getCreatedAtDateTime() {
    return this.auditInfo.getCreatedDateTime();
  }

  public LocalDate getUpdatedAtDate() {
    return this.auditInfo.getUpdatedDate();
  }

  public LocalDateTime getUpdatedAtDateTime() {
    return this.auditInfo.getUpdatedDateTime();
  }

  private UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(getClass(), id, "id");
  }

  protected String requireCode(String code) {
    // Allow null for codes that will be generated after persistence
    if (code == null) {
      return null;
    }
    return EntityValidator.ensureNotNullOrEmpty(getClass(), code, "code");
  }

  private AuditInfo requireAuditInfo(AuditInfo auditInfo) {
    return EntityValidator.ensureNotNull(getClass(), auditInfo, "auditInfo");
  }

  private DomainBaseModelStatus requireStatus(DomainBaseModelStatus status) {
    // return EntityValidator.ensureNotNull(getClass(), status, "status");
    return status;
  }

  protected abstract String generateCode();

  public void markUpdated() {
    this.auditInfo.markUpdated();
  }

  public void enable() {
    this.status = DomainBaseModelStatus.ENABLED;
  }

  public void disable() {
    this.status = DomainBaseModelStatus.DISABLED;
  }
}
