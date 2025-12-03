package com.udla.markenx.classroom.application.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public record TaskResponseDTO(
		UUID id,
		String code,
		String title,
		String summary,
		LocalDate dueDate,
		int maxAttempts,
		double minScoreToPass,
		@JsonInclude(JsonInclude.Include.NON_NULL) EntityStatus status) {
}
