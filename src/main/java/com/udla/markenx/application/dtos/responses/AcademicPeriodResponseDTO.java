package com.udla.markenx.application.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcademicPeriodResponseDTO {
  private final String code;
  private final LocalDate startOfTerm;
  private final LocalDate endOfTerm;
  private final int academicYear;
  private final String label;
}
