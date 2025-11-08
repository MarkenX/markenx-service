package com.udla.markenx.core.exceptions;

/**
 * Exception thrown when attempting to create more than 2 academic periods for
 * the same year.
 */
public class MaxPeriodsPerYearExceededException extends DomainException {
  public MaxPeriodsPerYearExceededException(int year) {
    super(String.format("El año %d ya tiene dos semestres registrados. No se pueden agregar más períodos académicos.",
        year));
  }
}
