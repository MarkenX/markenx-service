package com.udla.markenx.application.dtos.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for updating a course.
 * Currently empty - update semantics can be extended later (e.g. move to
 * another period).
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateCourseRequestDTO {
  @jakarta.validation.constraints.Positive(message = "El ID del período debe ser positivo")
  private Long academicPeriodId;
  private String label;
}
