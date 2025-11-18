package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper;

import org.springframework.lang.NonNull;

public interface BaseMapper<D, E> {

  @NonNull
  E toEntity(D domain);

  @NonNull
  D toDomain(E entity);
}