package com.udla.markenx.shared.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.udla.markenx.shared.domain.valueobjects.AuditParams;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;
import com.udla.markenx.shared.domain.utils.EntityValidator;
import lombok.Getter;

public abstract class DomainBaseModel {

    private final UUID id;
    @Getter
    private EntityStatus entityStatus;
    private final AuditInfo auditInfo;

    // Para cuando se crea nueva entidad desde el servicio
    protected DomainBaseModel() {
        this.id = UUID.randomUUID();
        enable();
        this.auditInfo = validateAuditInfo(new AuditInfo());
    }

    // Para cuando se crea una nueva entidad con datos fuera del servicio
    protected DomainBaseModel(String createdBy) {
        this.id = UUID.randomUUID();
        enable();
        this.auditInfo = validateAuditInfo(new AuditInfo(createdBy));
    }

    // Para cuando se carga una entidad desde la base de datos
    protected DomainBaseModel(UUID id, EntityStatus entityStatus, AuditParams params) {
        this.id = validateId(id);
        this.entityStatus = validateStatus(entityStatus);
        this.auditInfo = validateAuditInfo(new AuditInfo(params));
    }

    // #region Getters

    public UUID getId() {
        return Objects.requireNonNull(id);
    }

    public String getCreatedBy() {
        return auditInfo.getCreatedBy();
    }

    public String getUpdatedBy() {
        return auditInfo.getUpdatedBy();
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

    // #endregion

    // #region Validations

    private UUID validateId(UUID id) {
        return EntityValidator.ensureNotNull(getClass(), id, "id");
    }

    private EntityStatus validateStatus(EntityStatus status) {
        return EntityValidator.ensureNotNull(getClass(), status, "status");
    }

    private AuditInfo validateAuditInfo(AuditInfo auditInfo) {
        return EntityValidator.ensureNotNull(getClass(), auditInfo, "auditInfo");
    }

    // #endregion

    public void markUpdated() {
        auditInfo.markUpdated();
    }

    public void enable() {
        this.entityStatus = EntityStatus.ENABLED;
    }

    public void disable() {
        this.entityStatus = EntityStatus.DISABLED;
    }
}
