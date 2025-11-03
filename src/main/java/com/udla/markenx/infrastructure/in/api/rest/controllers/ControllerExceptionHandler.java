package com.udla.markenx.infrastructure.in.api.rest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import com.udla.markenx.application.dtos.responses.ErrorResponseDTO;
import com.udla.markenx.core.exceptions.InvalidEntityException;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(InvalidEntityException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidEntity(InvalidEntityException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidEntity(EntityNotFoundException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

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

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", String.format("Valor inválido para '%s': %s",
        ex.getName(), ex.getValue()));
    return ResponseEntity.badRequest().body(error);
  }
}