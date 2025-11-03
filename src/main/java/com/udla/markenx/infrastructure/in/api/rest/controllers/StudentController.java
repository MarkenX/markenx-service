package com.udla.markenx.infrastructure.in.api.rest.controllers;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.application.services.StudentService;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

@RestController
@RequestMapping("/api/markenx")
@Validated
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping("/students/{studentId}/tasks")
  public ResponseEntity<Page<TaskResponseDTO>> getTasksByFilters(
      @PathVariable @Positive(message = "El ID del estudiante debe ser positivo") Long studentId,
      @RequestParam(required = false) AssignmentStatus status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {

    Page<TaskResponseDTO> response = studentService.getStudentTasks(
        studentId, startDate, endDate, status, page, size);
    return ResponseEntity.ok(response);
  }
}