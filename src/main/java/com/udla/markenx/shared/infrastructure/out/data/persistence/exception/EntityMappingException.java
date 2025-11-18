package com.udla.markenx.shared.infrastructure.out.data.persistence.exception;

public class EntityMappingException extends RuntimeException {

  public EntityMappingException() {
    super("Failed to map domain object to JPA entity");
  }

  public EntityMappingException(String message) {
    super(message);
  }

  public EntityMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityMappingException(Throwable cause) {
    super("Failed to map domain object to JPA entity", cause);
  }

  public static EntityMappingException forEntity(Class<?> entityClass) {
    return new EntityMappingException(
        String.format("Failed to map domain object to %s entity", entityClass.getSimpleName()));
  }

  public static EntityMappingException nullDomain(Class<?> domainClass) {
    return new EntityMappingException(
        String.format("Domain %s cannot be null", domainClass.getSimpleName()));
  }

  public static EntityMappingException missingField(Class<?> domainClass, String fieldName) {
    return new EntityMappingException(
        String.format("Required field '%s' is missing in domain %s", fieldName, domainClass.getSimpleName()));
  }

  public static EntityMappingException invalidState(Class<?> domainClass, String reason) {
    return new EntityMappingException(
        String.format("Invalid state in domain %s: %s", domainClass.getSimpleName(), reason));
  }
}