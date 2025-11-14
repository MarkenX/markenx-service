package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcademicPeriodResponseDTO {
  private String code;
  private LocalDate startDate;
  private LocalDate endDate;
  private int academicYear;
  private String label;
}
