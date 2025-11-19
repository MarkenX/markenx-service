package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentTaskWithDetailsResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentWithCourseResponseDTO;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public interface StudentControllerPort {

  ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
      DomainBaseModelStatus status,
      Pageable pageable);

  ResponseEntity<StudentResponseDTO> getStudentById(UUID id);

  ResponseEntity<StudentWithCourseResponseDTO> getCurrentStudentProfile();

  ResponseEntity<List<StudentTaskWithDetailsResponseDTO>> getCurrentStudentTasks();

  ResponseEntity<List<AttemptResponseDTO>> getCurrentStudentTaskAttempts(UUID taskId);

  ResponseEntity<StudentResponseDTO> createStudent(@Valid CreateStudentRequestDTO request);

  ResponseEntity<Void> disableStudent(UUID id);

  ResponseEntity<Void> enableStudent(UUID id);

  ResponseEntity<BulkImportResponseDTO> bulkImportStudents(UUID courseId, MultipartFile file);

  ResponseEntity<byte[]> downloadBulkImportTemplate();

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
