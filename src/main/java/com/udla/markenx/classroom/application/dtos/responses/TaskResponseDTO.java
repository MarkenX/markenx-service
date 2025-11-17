package com.udla.markenx.classroom.application.dtos.responses;

import java.time.LocalDate;

public record TaskResponseDTO(
		String code,
		String title,
		String summary,
		LocalDate dueDate,
		int maxAttempts,
		double minScoreToPass) {
}
