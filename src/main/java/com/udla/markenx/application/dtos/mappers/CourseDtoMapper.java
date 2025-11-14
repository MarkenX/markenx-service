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
    dto.setAcademicPeriodId(course.getAcademicTermId());
    dto.setLabel(course.getName());

    dto.setCreatedBy(course.getCreatedBy());
    dto.setCreatedAt(course.getCreatedAtDateTime());
    dto.setLastModifiedBy(course.getLastModifiedBy());
    dto.setLastModifiedAt(course.getUpdatedAtDateTime());

    return dto;
  }
}
