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
}
