package com.udla.markenx.application.ports.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import com.udla.markenx.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.application.dtos.responses.StudentResponseDTO;

import jakarta.validation.Valid;

/**
 * Port for administrative operations.
 * 
 * Handles admin-only functionality like managing students and game scenarios.
 * All endpoints require ADMIN role.
 */
public interface AdminControllerPort {

  /**
   * DEBUG: Test authentication and roles.
   * Temporary endpoint to verify token is working.
   * 
   * @param authentication the authentication object from security context
   * @return ResponseEntity with authentication debug information
   */
  ResponseEntity<String> debugAuth(Authentication authentication);

  /**
   * Retrieves all students with pagination.
   * 
   * @param pageable pagination parameters (page, size, sort)
   * @return ResponseEntity with page of student DTOs
   */
  ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable);

  /**
   * Retrieves a single student by ID.
   * 
   * @param id the student ID
   * @return ResponseEntity with student DTO
   */
  ResponseEntity<StudentResponseDTO> getStudentById(Long id);

  /**
   * Updates an existing student.
   * 
   * @param id      the student ID
   * @param request DTO containing updated firstName, lastName, email
   * @return ResponseEntity with updated student DTO
   */
  ResponseEntity<StudentResponseDTO> updateStudent(Long id, @Valid UpdateStudentRequestDTO request);

  /**
   * Deletes a student.
   * 
   * @param id the student ID
   * @return ResponseEntity with HTTP 204 No Content
   */
  ResponseEntity<Void> deleteStudent(Long id);

  /**
   * Imports multiple students from CSV file.
   * 
   * CSV format (with header):
   * firstName,lastName,email,enrollmentCode
   * 
   * Email validation: All emails must belong to @udla.edu.ec domain
   * 
   * ALL-OR-NOTHING: Either all students are imported or none.
   * If any validation fails, the entire import is rolled back.
   * 
   * @param file CSV file with student data
   * @return ResponseEntity with bulk import results (HTTP 201 Created)
   * @throws BulkImportException if any validation fails (HTTP 400)
   */
  ResponseEntity<BulkImportResponseDTO> bulkImportStudents(MultipartFile file);
}
