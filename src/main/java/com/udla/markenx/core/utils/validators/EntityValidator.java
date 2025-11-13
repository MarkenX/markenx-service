package com.udla.markenx.core.utils.validators;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.exceptions.NullFieldException;
import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;

public final class EntityValidator {

  private EntityValidator() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static long ensureValidId(Class<?> clazz, Long id) {
    if (id == null) {
      throw new NullFieldException(clazz, "id");
    }
    if (id <= 0) {
      throw new InvalidEntityException(clazz, "id", "debe ser mayor a 0");
    }
    return id;
  }

  public static <T> T ensureNotNull(
      Class<?> clazz,
      T value,
      String fieldName) {
    if (value == null) {
      throw new NullFieldException(clazz, fieldName);
    }
    return value;
  }

  public static String ensureNotNullOrEmpty(Class<?> clazz, String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new NullFieldException(clazz, fieldName);
    }
    String normalized = value.trim();
    if (normalized.isEmpty()) {
      throw new InvalidEntityException(clazz, fieldName, "no puede estar vacío");
    }
    return normalized;
  }

}
