package com.udla.markenx.infrastructure.in.api.rest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.application.dtos.mappers.AttemptMapper;
import com.udla.markenx.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.application.ports.in.api.rest.controllers.TaskControllerPort;
import com.udla.markenx.application.services.TaskService;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.application.dtos.requests.AttemptRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/markenx")
public class TaskController implements TaskControllerPort {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@Override
	@GetMapping("/tasks/{taskId}/attempts")
	public ResponseEntity<Page<AttemptResponseDTO>> getTaskAttempts(
			@PathVariable Long taskId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Attempt> attempts = taskService.getTaskAttempts(taskId, page, size);
		Page<AttemptResponseDTO> response = attempts.map(AttemptMapper::toDto);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/tasks/{taskId}/students/{studentId}/attempts")
	public ResponseEntity<Page<AttemptResponseDTO>> getTaskAttemptsByStudent(
			@PathVariable Long taskId,
			@PathVariable Long studentId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Attempt> attempts = taskService.getTaskAttemptsByStudent(taskId, studentId, page, size);
		Page<AttemptResponseDTO> response = attempts.map(AttemptMapper::toDto);
		return ResponseEntity.ok(response);
	}

	@Override
	@PostMapping("/tasks/{taskId}/students/{studentId}/attempts")
	public ResponseEntity<AttemptResponseDTO> createAttempt(
			@PathVariable Long taskId,
			@PathVariable Long studentId,
			@org.springframework.web.bind.annotation.RequestBody AttemptRequestDTO request) {
		Attempt created = taskService.createAttempt(taskId, studentId, request.score(), request.date(), request.duration());
		AttemptResponseDTO dto = AttemptMapper.toDto(created);
		return ResponseEntity.ok(dto);
	}
}
