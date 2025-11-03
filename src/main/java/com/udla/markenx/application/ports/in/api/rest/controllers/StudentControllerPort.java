package com.udla.markenx.application.ports.in.api.rest.controllers;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public interface StudentControllerPort {
  ResponseEntity<Page<TaskResponseDTO>> getTasksByFilters(
      @PathVariable @Positive(message = "El ID del estudiante debe ser positivo") Long studentId,
      @RequestParam(required = false) AssignmentStatus status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size);
}
