package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import com.udla.markenx.classroom.application.dtos.responses.ErrorResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.ControllerExceptionHandlerPort;
import com.udla.markenx.classroom.domain.exceptions.BulkImportException;
import com.udla.markenx.classroom.domain.exceptions.DuplicateResourceException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEmailException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.exceptions.PeriodHasCoursesException;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler implements ControllerExceptionHandlerPort {

  @Override
  @ExceptionHandler(InvalidEntityException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidEntity(InvalidEntityException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @Override
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @Override
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @Override
  @ExceptionHandler(InvalidEmailException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidEmail(InvalidEmailException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @Override
  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponseDTO> handleDuplicateResource(DuplicateResourceException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @Override
  @ExceptionHandler(BulkImportException.class)
  public ResponseEntity<Map<String, Object>> handleBulkImport(BulkImportException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", ex.getMessage());
    response.put("totalRecords", ex.getTotalRecords());
    response.put("failureCount", ex.getFailureCount());
    response.put("failedImports", ex.getFailedImports());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolation(
      ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      String fieldName = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(fieldName, message);
    });
    return ResponseEntity.badRequest().body(errors);
  }

  @Override
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
  }

  @Override
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", String.format("Valor inválido para '%s': %s",
        ex.getName(), ex.getValue()));
    return ResponseEntity.badRequest().body(error);
  }

  // @ExceptionHandler(OverlappingPeriodsException.class)
  // public ResponseEntity<ErrorResponseDTO>
  // handleOverlappingPeriods(OverlappingPeriodsException ex) {
  // ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  // }

  // @ExceptionHandler(MaxPeriodsPerYearExceededException.class)
  // public ResponseEntity<ErrorResponseDTO> handleMaxPeriodsPerYearExceeded(
  // MaxPeriodsPerYearExceededException ex) {
  // ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  // }

  @ExceptionHandler(PeriodHasCoursesException.class)
  public ResponseEntity<ErrorResponseDTO> handlePeriodHasCourses(PeriodHasCoursesException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @Override
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
    ErrorResponseDTO error = new ErrorResponseDTO("Ocurrió un error inesperado: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}