package com.udla.markenx.shared.domain.exceptions;

import com.udla.markenx.classroom.domain.exceptions.DomainException;
import lombok.Getter;

@Getter
public class InvalidEntityException extends DomainException {

  private final String entityName;
  private final String field;

  public InvalidEntityException(Class<?> clazz, String field, String reason) {
    super(String.format("%s inválido. Campo '%s': %s",
        clazz.getSimpleName(), field, reason));
    this.entityName = clazz.getSimpleName();
    this.field = field;
  }

  public InvalidEntityException(Class<?> clazz, String reason) {
    super(String.format("%s inválido: %s",
        clazz.getSimpleName(), reason));
    this.entityName = clazz.getSimpleName();
    this.field = null;
  }

  public InvalidEntityException(String message) {
    super(message);
    this.entityName = null;
    this.field = null;
  }

}