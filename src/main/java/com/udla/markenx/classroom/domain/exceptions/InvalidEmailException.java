package com.udla.markenx.classroom.domain.exceptions;

public class InvalidEmailException extends DomainException {

  public InvalidEmailException(String message) {
    super(message);
  }

  public InvalidEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
