package com.udla.markenx.classroom.application.dtos.mappers;

import com.udla.markenx.classroom.application.dtos.responses.AcademicPeriodResponseDTO;
import com.udla.markenx.classroom.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.classroom.core.models.AcademicTerm;

public final class AcademicPeriodMapper {

  private AcademicPeriodMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static AcademicPeriodResponseDTO toResponseDto(AcademicTerm domain) {
    if (domain == null) {
      return null;
    }

    return new AcademicPeriodResponseDTO(
        domain.getCode(),
        domain.getStartOfTerm(),
        domain.getEndOfTerm(),
        domain.getAcademicYear(),
        domain.getLabel());
  }
}
