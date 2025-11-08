package com.udla.markenx.core.exceptions;

/**
 * Exception thrown when attempting to delete an academic period that has
 * related courses.
 */
public class PeriodHasCoursesException extends DomainException {
  public PeriodHasCoursesException(Long periodId, int coursesCount) {
    super(String.format("No se puede eliminar el período académico con ID %d porque tiene %d curso(s) asociado(s)",
        periodId, coursesCount));
  }
}
