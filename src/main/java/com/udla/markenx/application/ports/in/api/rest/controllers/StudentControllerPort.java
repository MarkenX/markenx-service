package com.udla.markenx.application.ports.in.api.rest.controllers;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.udla.markenx.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

/**
 * Port for student operations.
 * 
 * Provides endpoints for students to access their data and assigned tasks.
 */
public interface StudentControllerPort {

  /**
   * Retrieves the profile of the currently authenticated student.
   * 
   * @param authentication the authentication object from JWT token
   * @return ResponseEntity with student profile DTO
   */
  ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication);

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
