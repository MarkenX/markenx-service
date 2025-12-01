package com.udla.markenx.classroom.application.dtos.responses;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import com.udla.markenx.classroom.domain.valueobjects.enums.AttemptResult;
import com.udla.markenx.classroom.domain.valueobjects.enums.AttemptStatus;

public record AttemptResponseDTO(
        UUID id,
        String code,
        double score,
        LocalDate submittedAt,
        Duration timeSpent,
        AttemptStatus status,
        AttemptResult result) {
}
