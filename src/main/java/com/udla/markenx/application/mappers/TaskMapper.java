package com.udla.markenx.application.mappers;

import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.core.models.Task;

public class TaskMapper {
	public static TaskResponseDTO toDto(Task task) {
		if (task == null)
			return null;

		TaskResponseDTO dto = new TaskResponseDTO();
		dto.setId(task.getId());
		dto.setTitle(task.getTitle());
		dto.setSummary(task.getSummary());
		dto.setDueDate(task.getDueDate());
		dto.setCurrentStatus(task.getCurrentStatus());
		dto.setActiveAttempt(task.getActiveAttempt());
		dto.setMaxAttempts(task.getMaxAttempts());

		return dto;
	}
}
