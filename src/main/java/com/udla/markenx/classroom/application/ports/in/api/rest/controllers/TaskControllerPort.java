package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import com.udla.markenx.classroom.application.dtos.requests.AttemptRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.udla.markenx.classroom.application.dtos.responses.AttemptResponseDTO;

/**
 * Port for task operations.
 * 
 * Provides endpoints for retrieving task-related information
 * such as attempts made on specific tasks.
 */
public interface TaskControllerPort {

  /**
   * Retrieves all attempts made on a specific task with pagination.
   * 
   * @param taskId the task ID
   * @param page   page number (default 0)
   * @param size   page size (default 10)
   * @return ResponseEntity with page of attempt DTOs
   */
  ResponseEntity<Page<AttemptResponseDTO>> getTaskAttempts(Long taskId, int page, int size);

  ResponseEntity<Page<AttemptResponseDTO>> getTaskAttemptsByStudent(Long taskId, Long studentId, int page,
      int size);

  ResponseEntity<AttemptResponseDTO> createAttempt(Long taskId, Long studentId,
      AttemptRequestDTO request);
}
