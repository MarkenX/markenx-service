package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

public record TaskResponseDTO(
		Long id,
		String title,
		String summary,
		LocalDate dueDate,
		int maxAttempts) {
}
