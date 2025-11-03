package com.udla.markenx.application.ports.in.api.rest.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.ConstraintViolationException;

import com.udla.markenx.application.dtos.responses.ErrorResponseDTO;
import com.udla.markenx.core.exceptions.InvalidEntityException;

public interface ControllerExceptionHandlerPort {
  ResponseEntity<ErrorResponseDTO> handleInvalidEntity(InvalidEntityException ex);

  ResponseEntity<ErrorResponseDTO> handleInvalidEntity(EntityNotFoundException ex);

  ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex);

  ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex);
}
