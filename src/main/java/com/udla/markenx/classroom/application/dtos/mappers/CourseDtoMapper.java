package com.udla.markenx.classroom.application.dtos.mappers;

import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.shared.domain.util.SecurityUtils;

public class CourseDtoMapper {

  public static CourseResponseDTO toResponseDto(Course domain) {
    if (domain == null)
      return null;

    CourseResponseDTO dto = new CourseResponseDTO(
        domain.getId(),
        domain.getCode(),
        domain.getName(),
        SecurityUtils.isAdmin() ? domain.getStatus() : null);
    return dto;
  }
}
