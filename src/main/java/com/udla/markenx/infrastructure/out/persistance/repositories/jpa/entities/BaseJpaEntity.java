package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

  @Id
  @Column(name = "public_id", nullable = false, unique = true, updatable = false)
  private UUID publicId;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private DomainBaseModelStatus status;

  @Column(name = "created_by", nullable = false, updatable = false)
  private String createdBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    if (publicId == null) {
      publicId = UUID.randomUUID();
    }
  }
}
