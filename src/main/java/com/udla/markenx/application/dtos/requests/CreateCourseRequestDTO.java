package com.udla.markenx.application.dtos.requests;

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
  @jakarta.validation.constraints.Positive(message = "El ID del período debe ser positivo")
  private Long academicPeriodId;
  @jakarta.validation.constraints.NotBlank(message = "El label del curso es requerido")
  private String label;
}
