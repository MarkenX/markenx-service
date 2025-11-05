package com.udla.markenx.core.exceptions;

public class NullFieldException extends InvalidEntityException {
  public NullFieldException(String entityName, String field) {
    super(entityName, field, "no puede ser nulo.");
  }
}
