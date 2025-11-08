package com.udla.markenx.core.exceptions;

/**
 * Exception thrown when a duplicate resource is detected.
 * 
 * Used for:
 * - Duplicate email in student creation
 * - Duplicate enrollment code
 * - Duplicate Keycloak user ID
 */
public class DuplicateResourceException extends DomainException {

  public DuplicateResourceException(String message) {
    super(message);
  }

  public DuplicateResourceException(String resourceType, String field, Object value) {
    super(String.format("%s ya existe con %s: %s", resourceType, field, value));
  }
}
