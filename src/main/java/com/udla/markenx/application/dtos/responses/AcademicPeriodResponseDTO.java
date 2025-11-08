package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Academic Period responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicPeriodResponseDTO {
  private LocalDate startDate;
  private LocalDate endDate;
  private int year;
  private String label;
}
