package com.udla.markenx.classroom.application.dtos.mappers;

import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.shared.domain.util.SecurityUtils;

public class TaskMapper {
	public static TaskResponseDTO toResponseDto(Task domain) {
		if (domain == null)
			return null;

		TaskResponseDTO dto = new TaskResponseDTO(
				domain.getId(),
				domain.getCode(),
				domain.getTitle(),
				domain.getSummary(),
				domain.getDueDate(),
				domain.getMaxAttempts(),
				domain.getMinScoreToPass(),
				SecurityUtils.isAdmin() ? domain.getEntityStatus() : null);

		return dto;
	}
}
