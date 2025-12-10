package com.udla.markenx.classroom.terms.application.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.udla.markenx.classroom.terms.domain.valueobjects.TermStatus;

public record ResponseDTO(
    UUID id,
    LocalDate startDate,
    LocalDate endDate,
    int academicYear,
    TermStatus status) {
}
