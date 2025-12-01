package com.udla.markenx.classroom.application.dtos.mappers;

import com.udla.markenx.classroom.application.dtos.responses.AcademicTermResponseDTO;
import com.udla.markenx.classroom.domain.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.shared.domain.util.SecurityUtils;

public final class AcademicPeriodMapper {

  private AcademicPeriodMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static AcademicTermResponseDTO toResponseDto(AcademicTerm domain) {
    if (domain == null) {
      return null;
    }

    return new AcademicTermResponseDTO(
        domain.getId(),
        domain.getCode(),
        domain.getStartOfTerm(),
        domain.getEndOfTerm(),
        domain.getAcademicYear(),
        domain.getLabel(),
        SecurityUtils.isAdmin() ? domain.getStatus() : null);
  }
}
