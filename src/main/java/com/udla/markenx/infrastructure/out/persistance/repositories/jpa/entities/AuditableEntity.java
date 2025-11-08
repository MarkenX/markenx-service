package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base auditable entity for tracking creation and modification metadata.
 * 
 * Provides automatic auditing for admin CRUD operations:
 * - createdBy: Email of admin who created the entity
 * - createdAt: Timestamp when entity was created
 * - lastModifiedBy: Email of admin who last modified the entity
 * - lastModifiedAt: Timestamp when entity was last modified
 * 
 * Students can only view and upload data, so audit fields track admin actions
 * only.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedBy
  @Column(name = "last_modified_by")
  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_at")
  private LocalDateTime lastModifiedAt;
}
