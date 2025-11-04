package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public record TaskResponseDTO(
		Long id,
		String title,
		String summary,
		LocalDate dueDate,
		AssignmentStatus currentStatus,
		int activeAttempt,
		int maxAttempts) {
}
