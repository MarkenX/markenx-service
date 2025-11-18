package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.exception;

public class DomainMappingException extends RuntimeException {

  public DomainMappingException() {
    super("Failed to map JPA entity to domain object");
  }

  public DomainMappingException(String message) {
    super(message);
  }

  public DomainMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  public DomainMappingException(Throwable cause) {
    super("Failed to map JPA entity to domain object", cause);
  }

  public static DomainMappingException forEntity(Class<?> entityClass) {
    return new DomainMappingException(
        String.format("Failed to map %s entity to domain object", entityClass.getSimpleName()));
  }

  public static DomainMappingException nullEntity(Class<?> entityClass) {
    return new DomainMappingException(
        String.format("JPA entity %s cannot be null", entityClass.getSimpleName()));
  }

  public static DomainMappingException missingField(Class<?> entityClass, String fieldName) {
    return new DomainMappingException(
        String.format("Required field '%s' is missing in %s entity", fieldName, entityClass.getSimpleName()));
  }

  public static DomainMappingException invalidState(Class<?> entityClass, String reason) {
    return new DomainMappingException(
        String.format("Invalid state in %s entity: %s", entityClass.getSimpleName(), reason));
  }
}