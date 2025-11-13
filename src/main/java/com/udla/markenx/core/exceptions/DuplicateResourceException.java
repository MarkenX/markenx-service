package com.udla.markenx.core.exceptions;

public class DuplicateResourceException extends DomainException {

  public DuplicateResourceException(String message) {
    super(message);
  }

  public DuplicateResourceException(String resourceType, String field, Object value) {
    super(String.format("%s ya existe con %s: %s", resourceType, field, value));
  }
}
