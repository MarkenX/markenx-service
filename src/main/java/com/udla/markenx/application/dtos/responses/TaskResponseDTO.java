package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

public record TaskResponseDTO(
		String code,
		String title,
		String summary,
		LocalDate dueDate,
		int maxAttempts,
		double minScoreToPass) {
}
