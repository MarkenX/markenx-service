package com.udla.markenx.classroom.application.dtos.requests.AcademicPeriod;

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
public class CreateAcademicTermRequestDTO {

  @NotNull(message = "La fecha de inicio es obligatoria")
  private LocalDate startOfTerm;

  @NotNull(message = "La fecha de fin es obligatoria")
  private LocalDate endOfTerm;

  @NotNull(message = "El año es obligatorio")
  private Integer academicYear;
}
