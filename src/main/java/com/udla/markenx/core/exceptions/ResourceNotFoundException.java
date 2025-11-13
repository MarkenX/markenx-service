package com.udla.markenx.core.exceptions;

public class ResourceNotFoundException extends DomainException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceType, Object identifier) {
    super(String.format("%s no encontrado con identificador: %s", resourceType, identifier));
  }
}
