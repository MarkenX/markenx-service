package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.core.models.Task;

public class TaskMapper {
	public static TaskResponseDTO toResponseDto(Task task) {
		if (task == null)
			return null;

		TaskResponseDTO dto = new TaskResponseDTO(
				task.getId(),
				task.getTitle(),
				task.getSummary(),
				task.getDueDate(),
				task.getCurrentStatus(),
				task.getActiveAttempt(),
				task.getMaxAttempts());

		return dto;
	}
}
