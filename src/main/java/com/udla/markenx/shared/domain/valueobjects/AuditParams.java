package com.udla.markenx.shared.domain.valueobjects;

import java.time.LocalDateTime;

public record AuditParams(
    String createdBy,
    String updatedBy,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
