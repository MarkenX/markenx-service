package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDateTime;

import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "entities")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  // @Column(name = "status", nullable = false)
  @Column(name = "status")
  private DomainBaseModelStatus status;

  // @Column(name = "created_by", nullable = false, updatable = false)
  @Column(name = "created_by")
  private String createdBy;

  // @Column(name = "updated_by", nullable = false)
  @Column(name = "updated_by")
  private String updatedBy;

  // @Column(name = "created_at", nullable = false, updatable = false)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // @Column(name = "updated_at", nullable = false)
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "external_reference_id")
  private ExternalReferenceJpaEntity externalReference;
}
