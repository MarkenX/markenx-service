package com.udla.markenx.classroom.application.dtos.mappers;

import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.domain.models.Course;

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
