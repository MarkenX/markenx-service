package com.udla.markenx.classroom.domain.exceptions;

import java.util.UUID;

public class PeriodHasCoursesException extends DomainException {
  public PeriodHasCoursesException(Long periodId, int coursesCount) {
    super(String.format("No se puede eliminar el período académico con ID %d porque tiene %d curso(s) asociado(s)",
        periodId, coursesCount));
  }

  public PeriodHasCoursesException(UUID periodId, int coursesCount) {
    super(
        String.format("No se puede deshabilitar el período académico con ID %s porque tiene %d curso(s) habilitado(s)",
            periodId, coursesCount));
  }
}
