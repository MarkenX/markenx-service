package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.core.models.Course;

public class CourseDtoMapper {

  public static CourseResponseDTO toResponseDto(Course course) {
    if (course == null)
      return null;

    CourseResponseDTO dto = new CourseResponseDTO();
    dto.setId(course.getId());
    dto.setAssignmentsCount(course.getAssignments().size());
    dto.setStudentsCount(course.getStudents().size());
    dto.setAcademicPeriodId(course.getAcademicPeriodId());
    dto.setLabel(course.getLabel());

    if (course.getTimestamps() != null) {
      dto.setCreatedBy(course.getTimestamps().getCreatedBy());
      dto.setCreatedAt(course.getTimestamps().getCreatedDateTime());
      dto.setLastModifiedBy(course.getTimestamps().getLastModifiedBy());
      dto.setLastModifiedAt(course.getTimestamps().getUpdatedDateTime());
    }

    return dto;
  }
}
