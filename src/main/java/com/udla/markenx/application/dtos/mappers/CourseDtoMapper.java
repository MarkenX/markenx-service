package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.core.models.Course;

public class CourseDtoMapper {

  public static CourseResponseDTO toResponseDto(Course domain) {
    if (domain == null)
      return null;

    CourseResponseDTO dto = new CourseResponseDTO(
        domain.getCode(),
        domain.getName());
    return dto;
  }
}
