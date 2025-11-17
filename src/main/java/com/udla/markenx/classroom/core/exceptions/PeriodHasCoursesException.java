package com.udla.markenx.classroom.core.exceptions;

public class PeriodHasCoursesException extends DomainException {
  public PeriodHasCoursesException(Long periodId, int coursesCount) {
    super(String.format("No se puede eliminar el período académico con ID %d porque tiene %d curso(s) asociado(s)",
        periodId, coursesCount));
  }
}
