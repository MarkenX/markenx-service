package com.udla.markenx.shared.domain.exceptions;

public class UtilityClassInstantiationException extends UnsupportedOperationException {

  public UtilityClassInstantiationException(Class<?> clazz) {
    super("No se puede instanciar la clase utilitaria: " + clazz.getSimpleName());
  }
}