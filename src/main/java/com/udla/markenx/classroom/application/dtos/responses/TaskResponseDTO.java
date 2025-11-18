package com.udla.markenx.classroom.application.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

public record TaskResponseDTO(
		UUID id,
		String code,
		String title,
		String summary,
		LocalDate dueDate,
		int maxAttempts,
		double minScoreToPass) {
}
