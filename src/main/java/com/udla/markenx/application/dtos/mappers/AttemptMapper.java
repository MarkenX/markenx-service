package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.core.models.Attempt;

public class AttemptMapper {
  public static AttemptResponseDTO toDto(Attempt attempt) {
    if (attempt == null)
      return null;

    AttemptResponseDTO dto = new AttemptResponseDTO(
        attempt.getId(),
        attempt.getScore(),
        attempt.getSubmittedAt(),
        attempt.getTimeSpent());

    return dto;
  }
}
