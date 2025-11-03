package com.udla.markenx.api.inbound.rest.mapper;

import com.udla.markenx.api.inbound.rest.dto.AttemptResponseDTO;
import com.udla.markenx.core.model.Attempt;

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
