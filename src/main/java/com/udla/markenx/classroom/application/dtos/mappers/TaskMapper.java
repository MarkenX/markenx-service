package com.udla.markenx.classroom.application.dtos.mappers;

import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.classroom.domain.models.Task;

public class TaskMapper {
	public static TaskResponseDTO toResponseDto(Task domain) {
		if (domain == null)
			return null;

		TaskResponseDTO dto = new TaskResponseDTO(
				domain.getCode(),
				domain.getTitle(),
				domain.getSummary(),
				domain.getDueDate(),
				domain.getMaxAttempts(),
				domain.getMinScoreToPass());

		return dto;
	}
}
