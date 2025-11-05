package com.udla.markenx.infrastructure.out.persistance.exceptions;

public class DomainMappingException extends MappingException {

  public DomainMappingException() {
    super("No se puede mapear una entidad JPA nula a un objeto de dominio.");
  }
}