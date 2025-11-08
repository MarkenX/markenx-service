package com.udla.markenx.core.exceptions;

/**
 * Exception thrown when email validation fails.
 * 
 * Used for:
 * - Invalid email format
 * - Email domain not allowed (e.g., non-@udla.edu.ec emails)
 * - Duplicate email addresses
 */
public class InvalidEmailException extends DomainException {

  public InvalidEmailException(String message) {
    super(message);
  }

  public InvalidEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
