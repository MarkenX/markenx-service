package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.core.models.Attempt;

public class AttemptMapper {
  public static AttemptResponseDTO toDto(Attempt domain) {
    if (domain == null)
      return null;

    AttemptResponseDTO dto = new AttemptResponseDTO(
        domain.getCode(),
        domain.getScore(),
        domain.getCreatedAtDate(),
        domain.getTimeSpent(),
        domain.getAttemptStatus(),
        domain.getResult());

    return dto;
  }
}
