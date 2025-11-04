package com.udla.markenx.application.dtos.responses;

import java.time.Duration;
import java.time.LocalDate;

public record AttemptResponseDTO(Long id, double score, LocalDate date, Duration duration) {
}
