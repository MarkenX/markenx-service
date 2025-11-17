package com.udla.markenx.classroom.application.dtos.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for updating an existing Academic Period.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAcademicPeriodRequestDTO {

  private LocalDate startDate;

  private LocalDate endDate;

  private Integer year;
}
