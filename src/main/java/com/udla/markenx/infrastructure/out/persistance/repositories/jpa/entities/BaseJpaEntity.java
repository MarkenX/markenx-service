package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseJpaEntity {

  @Column(name = "public_id", nullable = false, unique = true, updatable = false)
  private UUID publicId;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    if (publicId == null) {
      publicId = UUID.randomUUID();
    }
  }
}
