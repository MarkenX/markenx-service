package com.udla.markenx.classroom.application.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public record AcademicTermResponseDTO(
    UUID id,
    String code,
    LocalDate startOfTerm,
    LocalDate endOfTerm,
    int academicYear,
    String label,
    @JsonInclude(JsonInclude.Include.NON_NULL) DomainBaseModelStatus status) {
}
