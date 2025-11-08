package com.udla.markenx.core.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * Used for:
 * - Student not found by ID or email
 * - Task not found by ID
 * - Course not found
 * - Academic period not found
 */
public class ResourceNotFoundException extends DomainException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceType, Object identifier) {
    super(String.format("%s no encontrado con identificador: %s", resourceType, identifier));
  }
}
