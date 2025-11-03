package com.udla.markenx.core.exceptions;

public class InvalidEntityException extends DomainException {

  private final String entityName;
  private final String field;

  public InvalidEntityException(String entityName, String field, String reason) {
    super(String.format("%s inválido. Campo '%s': %s",
        entityName, field, reason));
    this.entityName = entityName;
    this.field = field;
  }

  public InvalidEntityException(String message) {
    super(message);
    this.entityName = null;
    this.field = null;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getField() {
    return field;
  }
}