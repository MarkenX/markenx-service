package com.udla.markenx.classroom.application.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

public record AcademicPeriodResponseDTO(
        UUID id,
        String code,
        LocalDate startOfTerm,
        LocalDate endOfTerm,
        int academicYear,
        String label) {
}
