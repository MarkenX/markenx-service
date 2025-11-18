package com.udla.markenx.classroom.application.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for creating a course. No payload required now (course is an
 * empty container).
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateCourseRequestDTO {
  @NotNull(message = "El ID del período es requerido")
  private UUID academicPeriodId;
  @NotBlank(message = "El label del curso es requerido")
  private String label;
}
