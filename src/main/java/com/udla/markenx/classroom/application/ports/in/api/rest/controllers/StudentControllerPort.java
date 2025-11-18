package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;

public interface StudentControllerPort {

  ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication);

  ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable);

  ResponseEntity<StudentResponseDTO> getStudentById(Long id);

  // /**
  // * Retrieves tasks assigned to a specific student with filters.
  // *
  // * Implementation note: Tasks are retrieved from the student's enrolled
  // course.
  // * This follows DDD design where Course is the aggregate root for
  // * assignments/tasks.
  // * The service layer resolves student -> course -> tasks.
  // *
  // * @param studentId the student ID
  // * @param status optional assignment status filter
  // * @param startDate optional start date filter
  // * @param endDate optional end date filter
  // * @param page page number (default 0)
  // * @param size page size (default 10)
  // * @return ResponseEntity with page of task DTOs
  // */
  // ResponseEntity<Page<TaskResponseDTO>> getTasksByFilters(
  // @PathVariable @Positive(message = "El ID del estudiante debe ser positivo")
  // Long studentId,
  // @RequestParam(required = false) AssignmentStatus status,
  // @RequestParam(required = false) @DateTimeFormat(iso =
  // DateTimeFormat.ISO.DATE) LocalDate startDate,
  // @RequestParam(required = false) @DateTimeFormat(iso =
  // DateTimeFormat.ISO.DATE) LocalDate endDate,
  // @RequestParam(defaultValue = "0") @Min(0) int page,
  // @RequestParam(defaultValue = "10") @Min(1) int size);
}
