package com.udla.markenx.application.interfaces.in.mappers;

import com.udla.markenx.application.interfaces.in.dtos.AttemptResponseDTO;
import com.udla.markenx.core.models.Attempt;

public class AttemptMapper {
  public static AttemptResponseDTO toDto(Attempt attempt) {
    if (attempt == null)
      return null;

    AttemptResponseDTO dto = new AttemptResponseDTO();
    dto.setId(attempt.getId());
    dto.setScore(attempt.getScore());
    dto.setDate(attempt.getDate());
    dto.setDuration(attempt.getDuration());

    return dto;
  }
}
