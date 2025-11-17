package com.udla.markenx.classroom.core.exceptions;

public class InvalidValueException extends DomainException {

  private final String field;

  public InvalidValueException(String field, String reason) {
    super(String.format("%s inválido. %s",
        field, reason));
    this.field = field;
  }

  public String getField() {
    return field;
  }
}
