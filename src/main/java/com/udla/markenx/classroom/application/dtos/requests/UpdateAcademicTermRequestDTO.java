package com.udla.markenx.classroom.application.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAcademicTermRequestDTO {

  @NotNull(message = "La fecha de inicio es obligatoria")
  private LocalDate startDate;

  @NotNull(message = "La fecha de fin es obligatoria")
  private LocalDate endDate;

  @NotNull(message = "El año es obligatorio")
  private Integer year;
}
