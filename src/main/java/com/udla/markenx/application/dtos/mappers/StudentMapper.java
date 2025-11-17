package com.udla.markenx.application.dtos.mappers;

import com.udla.markenx.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.Student;

public final class StudentMapper {

  private StudentMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static StudentResponseDTO toDto(Student domain) {
    if (domain == null) {
      return null;
    }

    return new StudentResponseDTO(
        domain.getCode(),
        domain.getFirstName(),
        domain.getLastName(),
        domain.getAcademicEmail());
  }
}
