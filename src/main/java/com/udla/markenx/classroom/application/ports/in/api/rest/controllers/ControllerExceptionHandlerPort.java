package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import com.udla.markenx.classroom.application.dtos.responses.ErrorResponseDTO;
import com.udla.markenx.classroom.domain.exceptions.BulkImportException;
import com.udla.markenx.classroom.domain.exceptions.DuplicateResourceException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEmailException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;

/**
 * Port for global exception handling across all controllers.
 * 
 * Provides consistent error responses with appropriate HTTP status codes
 * and user-friendly messages for different exception types.
 */
public interface ControllerExceptionHandlerPort {

  /**
   * Handles domain entity validation exceptions.
   * 
   * @param ex the invalid entity exception
   * @return ResponseEntity with error details and HTTP 400 status
   */
  ResponseEntity<ErrorResponseDTO> handleInvalidEntity(InvalidEntityException ex);

  /**
   * Handles JPA entity not found exceptions.
   * 
   * @param ex the entity not found exception
   * @return ResponseEntity with error details and HTTP 404 status
   */
  ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex);

  /**
   * Handles resource not found exceptions.
   * 
   * @param ex the resource not found exception
   * @return ResponseEntity with error details and HTTP 404 status
   */
  ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex);

  /**
   * Handles email validation exceptions.
   * 
   * @param ex the invalid email exception
   * @return ResponseEntity with error details and HTTP 400 status
   */
  ResponseEntity<ErrorResponseDTO> handleInvalidEmail(InvalidEmailException ex);

  /**
   * Handles duplicate resource exceptions.
   * 
   * @param ex the duplicate resource exception
   * @return ResponseEntity with error details and HTTP 409 status
   */
  ResponseEntity<ErrorResponseDTO> handleDuplicateResource(DuplicateResourceException ex);

  /**
   * Handles bulk import validation failures.
   * 
   * @param ex the bulk import exception with all validation errors
   * @return ResponseEntity with detailed error map and HTTP 400 status
   */
  ResponseEntity<Map<String, Object>> handleBulkImport(BulkImportException ex);

  /**
   * Handles bean validation constraint violations.
   * 
   * @param ex the constraint violation exception
   * @return ResponseEntity with field-level errors and HTTP 400 status
   */
  ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex);

  /**
   * Handles request body validation errors.
   * 
   * @param ex the method argument validation exception
   * @return ResponseEntity with field-level errors and HTTP 400 status
   */
  ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex);

  /**
   * Handles path variable or request parameter type mismatch.
   * 
   * @param ex the type mismatch exception
   * @return ResponseEntity with error details and HTTP 400 status
   */
  ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex);

  /**
   * Handles unexpected generic exceptions.
   * 
   * @param ex the generic exception
   * @return ResponseEntity with error details and HTTP 500 status
   */
  ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex);
}
