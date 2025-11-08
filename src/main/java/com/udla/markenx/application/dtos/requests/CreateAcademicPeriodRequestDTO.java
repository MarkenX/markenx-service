package com.udla.markenx.application.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for creating a new Academic Period.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAcademicPeriodRequestDTO {

  @NotNull(message = "La fecha de inicio es obligatoria")
  private LocalDate startDate;

  @NotNull(message = "La fecha de fin es obligatoria")
  private LocalDate endDate;

  @NotNull(message = "El año es obligatorio")
  private Integer year;
}
