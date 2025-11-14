package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

public record AcademicPeriodResponseDTO(
    String code,
    LocalDate startOfTerm,
    LocalDate endOfTerm,
    int academicYear,
    String label) {
}
