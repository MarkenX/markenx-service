package com.udla.markenx.infrastructure.in.api.rest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.application.mappers.AttemptMapper;
import com.udla.markenx.application.services.TaskService;
import com.udla.markenx.core.models.Attempt;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/markenx")
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/tasks/{taskId}/attempts")
	public ResponseEntity<Page<AttemptResponseDTO>> getTaskAttempts(
			@PathVariable Long taskId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Attempt> attempts = taskService.getTaskAttempts(taskId, page, size);

		Page<AttemptResponseDTO> response = attempts.map(AttemptMapper::toDto);
		return ResponseEntity.ok(response);
	}
}
