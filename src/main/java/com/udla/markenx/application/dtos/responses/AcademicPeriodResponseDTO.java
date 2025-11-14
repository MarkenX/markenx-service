package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcademicPeriodResponseDTO {
  private String code;
  private LocalDate startOfTerm;
  private LocalDate endOfTerm;
  private int academicYear;
  private String label;
}
