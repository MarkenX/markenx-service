package com.udla.markenx.application.mappers;

import com.udla.markenx.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.core.models.Attempt;

public class AttemptMapper {
  public static AttemptResponseDTO toDto(Attempt attempt) {
    if (attempt == null)
      return null;

    AttemptResponseDTO dto = new AttemptResponseDTO(
        attempt.getId(),
        attempt.getScore(),
        attempt.getDate(),
        attempt.getDuration());

    return dto;
  }
}
