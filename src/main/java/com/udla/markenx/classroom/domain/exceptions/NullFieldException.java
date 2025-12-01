package com.udla.markenx.classroom.domain.exceptions;

public class NullFieldException extends InvalidEntityException {
  public NullFieldException(Class<?> clazz, String field) {
    super(clazz, field, "no puede ser nulo.");
  }
}
