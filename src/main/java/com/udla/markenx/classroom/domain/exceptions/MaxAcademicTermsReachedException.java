package com.udla.markenx.classroom.domain.exceptions;

public class MaxAcademicTermsReachedException extends DomainException {
  public MaxAcademicTermsReachedException(int academicYear) {
    super(String.format("No se puede determinar el número de semestre: ya existen dos períodos para el año %d",
        academicYear));
  }
}
