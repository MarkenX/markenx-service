package com.udla.markenx.classroom.academicterms.application.mappers;

import com.udla.markenx.classroom.academicterms.application.dtos.ResponseDTO;
import com.udla.markenx.classroom.academicterms.domain.model.AcademicTerm;
import com.udla.markenx.classroom.domain.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.shared.domain.util.SecurityUtils;

public final class AcademicPeriodMapper {

  private AcademicPeriodMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static ResponseDTO toResponseDto(AcademicTerm domain) {
    if (domain == null) {
      return null;
    }

    return new ResponseDTO(
        domain.getId(),
        domain.getTermStartDate(),
        domain.getTermEndDate(),
        domain.getAcademicYear(),
        SecurityUtils.isAdmin() ? domain.getTermStatus() : null);
  }
}
