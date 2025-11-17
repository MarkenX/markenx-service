package com.udla.markenx.classroom.application.dtos.requests;

import java.time.Duration;
import java.time.LocalDate;

public record AttemptRequestDTO(double score, LocalDate date, Duration duration) {
}
