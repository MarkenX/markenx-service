package com.udla.markenx.application.dtos.responses;

import java.time.Duration;
import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.core.valueobjects.enums.AttemptStatus;

public record AttemptResponseDTO(
    String code,
    double score,
    LocalDate submittedAt,
    Duration timeSpent,
    AttemptStatus status,
    AttemptResult result) {
}
