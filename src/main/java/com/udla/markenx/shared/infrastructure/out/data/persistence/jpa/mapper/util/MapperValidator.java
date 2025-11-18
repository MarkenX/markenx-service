package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util;

import org.springframework.stereotype.Component;

import com.udla.markenx.shared.infrastructure.out.data.persistence.exception.DomainMappingException;
import com.udla.markenx.shared.infrastructure.out.data.persistence.exception.EntityMappingException;

@Component
public class MapperValidator {

  public void validateDomainNotNull(Object domain, Class<?> domainClass) {
    if (domain == null) {
      throw EntityMappingException.nullDomain(domainClass);
    }
  }

  public void validateEntityNotNull(Object entity, Class<?> entityClass) {
    if (entity == null) {
      throw DomainMappingException.nullEntity(entityClass);
    }
  }

  public void validateDomainField(Object fieldValue, Class<?> domainClass, String fieldName) {
    if (fieldValue == null) {
      throw EntityMappingException.missingField(domainClass, fieldName);
    }
  }

  public void validateDomainStringField(String fieldValue, Class<?> domainClass, String fieldName) {
    if (fieldValue == null || fieldValue.isBlank()) {
      throw EntityMappingException.missingField(domainClass, fieldName);
    }
  }

  public void validateEntityField(Object fieldValue, Class<?> entityClass, String fieldName) {
    if (fieldValue == null) {
      throw DomainMappingException.missingField(entityClass, fieldName);
    }
  }
}