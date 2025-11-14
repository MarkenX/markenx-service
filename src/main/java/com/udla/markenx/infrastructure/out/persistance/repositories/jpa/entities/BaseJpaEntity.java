package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseJpaEntity {

  @Column(name = "public_id", nullable = false, unique = true, updatable = false)
  private UUID publicId;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @PrePersist
  protected void onCreate() {
    if (publicId == null) {
      publicId = UUID.randomUUID();
    }
  }
}
