package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.exception.DomainMappingException;

@Component
public class ExternalReferenceMapperHelper {

  public ExternalReferenceJpaEntity createExternalReference(UUID publicId, String code, String entityType) {
    ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
    ref.setPublicId(publicId);
    ref.setCode(code);
    ref.setEntityType(entityType);
    return ref;
  }

  public UUID extractPublicId(ExternalReferenceJpaEntity externalReference, Class<?> entityClass) {
    if (externalReference == null) {
      throw DomainMappingException.missingField(entityClass, "externalReference");
    }
    UUID publicId = externalReference.getPublicId();
    if (publicId == null) {
      throw DomainMappingException.invalidState(
          entityClass,
          "External reference publicId cannot be null");
    }
    return publicId;
  }

  public String extractCode(ExternalReferenceJpaEntity externalReference) {
    return externalReference != null ? externalReference.getCode() : "";
  }

  public UUID extractPublicIdOrDefault(ExternalReferenceJpaEntity externalReference, UUID defaultValue) {
    return externalReference != null && externalReference.getPublicId() != null
        ? externalReference.getPublicId()
        : defaultValue;
  }
}