package com.udla.markenx.classroom.terms.application.mappers;

import com.udla.markenx.classroom.terms.application.dtos.ResponseDTO;
import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.shared.domain.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.shared.domain.utils.SecurityUtils;

public final class TermMapper {

  private TermMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static ResponseDTO toResponseDto(Term domain) {
    if (domain == null) {
      return null;
    }

    return new ResponseDTO(
        domain.getId(),
        domain.getStartDate(),
        domain.getEndDate(),
        domain.getYear(),
        SecurityUtils.isAdmin() ? domain.getStatus() : null);
  }
}
