package com.udla.markenx.infrastructure.out.persistance.exceptions;

public class EntityMappingException extends MappingException {

  public EntityMappingException() {
    super("No se puede mapear un objeto de dominio nulo a una entidad JPA.");
  }
}