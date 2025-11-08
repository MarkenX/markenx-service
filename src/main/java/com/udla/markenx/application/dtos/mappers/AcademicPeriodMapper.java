package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.AcademicPeriodResponseDTO;
import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.AcademicPeriod;

/**
 * Mapper for converting AcademicPeriod domain models to DTOs.
 * 
 * Utility class with static methods only - cannot be instantiated.
 */
public final class AcademicPeriodMapper {

  private AcademicPeriodMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  /**
   * Converts domain model to response DTO.
   */
  public static AcademicPeriodResponseDTO toResponseDto(AcademicPeriod domain) {
    if (domain == null) {
      return null;
    }

    return new AcademicPeriodResponseDTO(
        domain.getStartDate(),
        domain.getEndDate(),
        domain.getYear(),
        domain.getLabel());
  }
}
