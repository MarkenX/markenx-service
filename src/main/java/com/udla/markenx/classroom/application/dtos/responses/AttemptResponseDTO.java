package com.udla.markenx.classroom.application.dtos.responses;

import java.time.Duration;
import java.time.LocalDate;

import com.udla.markenx.classroom.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.classroom.core.valueobjects.enums.AttemptStatus;

public record AttemptResponseDTO(
    String code,
    double score,
    LocalDate submittedAt,
    Duration timeSpent,
    AttemptStatus status,
    AttemptResult result) {
}
