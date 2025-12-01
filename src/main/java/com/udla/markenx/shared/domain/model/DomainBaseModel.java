package com.udla.markenx.shared.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.udla.markenx.classroom.domain.valueobjects.AuditInfo;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public abstract class DomainBaseModel {

  private final UUID id;
  private DomainBaseModelStatus status;
  private final AuditInfo auditInfo;
  private String code;

  public DomainBaseModel(
      UUID id,
      String code,
      DomainBaseModelStatus status,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = requireId(id);
    this.code = code;
    this.status = requireStatus(status);
    this.auditInfo = requireAuditInfo(new AuditInfo(createdBy, createdAt, updatedAt));
  }

  public DomainBaseModel(String createdBy) {
    this.id = UUID.randomUUID();
    this.status = DomainBaseModelStatus.ENABLED;
    this.auditInfo = new AuditInfo(createdBy);
    this.code = generateCode();
  }

  public DomainBaseModel() {
    this.id = UUID.randomUUID();
    this.status = DomainBaseModelStatus.ENABLED;
    this.auditInfo = new AuditInfo();
    this.code = generateCode();
  }

  @NonNull
  public UUID getId() {
    return Objects.requireNonNull(id);
  }

  public DomainBaseModelStatus getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  protected void setCode(String code) {
    this.code = code;
  }

  public String getCreatedBy() {
    return auditInfo.getCreatedBy();
  }

  public String getUpdatedBy() {
    return auditInfo.getLastModifiedBy();
  }

  public LocalDate getCreatedAtDate() {
    return auditInfo.getCreatedDate();
  }

  public LocalDateTime getCreatedAtDateTime() {
    return auditInfo.getCreatedDateTime();
  }

  public LocalDate getUpdatedAtDate() {
    return auditInfo.getUpdatedDate();
  }

  public LocalDateTime getUpdatedAtDateTime() {
    return auditInfo.getUpdatedDateTime();
  }

  private UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(getClass(), id, "id");
  }

  private DomainBaseModelStatus requireStatus(DomainBaseModelStatus status) {
    return EntityValidator.ensureNotNull(getClass(), status, "status");
  }

  private AuditInfo requireAuditInfo(AuditInfo auditInfo) {
    return EntityValidator.ensureNotNull(getClass(), auditInfo, "auditInfo");
  }

  protected abstract String generateCode();

  public void regenerateCode() {
    this.code = generateCode();
  }

  public void markUpdated() {
    auditInfo.markUpdated();
  }

  public void enable() {
    this.status = DomainBaseModelStatus.ENABLED;
  }

  public void disable() {
    this.status = DomainBaseModelStatus.DISABLED;
  }
}
