package com.udla.markenx.core.exceptions;

/**
 * Exception thrown when academic periods overlap in time.
 */
public class OverlappingPeriodsException extends DomainException {
  public OverlappingPeriodsException(String message) {
    super(message);
  }
}
