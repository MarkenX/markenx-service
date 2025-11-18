package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "external_references")
public class ExternalReferenceJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  // @Column(name = "code", nullable = false, unique = true)
  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "public_id", nullable = false, unique = true, updatable = false)
  private UUID publicId;

  // @Column(name = "entity_type", nullable = false, length = 50)
  @Column(name = "entity_type", nullable = false)
  private String entityType;

  @OneToMany(mappedBy = "externalReference", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BaseJpaEntity> entities;
}
