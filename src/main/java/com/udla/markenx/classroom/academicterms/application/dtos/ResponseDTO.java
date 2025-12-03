package com.udla.markenx.classroom.academicterms.application.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.udla.markenx.classroom.academicterms.domain.valueobjects.AcademicTermStatus;

public record ResponseDTO(
    UUID id,
    LocalDate startDate,
    LocalDate endDate,
    int academicYear,
    AcademicTermStatus status) {
}
