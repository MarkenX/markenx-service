package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.udla.markenx.classroom.domain.valueobjects.enums.AuditEventType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit_events")
@EntityListeners(AuditingEntityListener.class)
public class AuditJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "audit_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_type", nullable = false)
  private AuditEventType eventType;

  @CreatedBy
  @Column(name = "performed_by", updatable = false, nullable = false)
  private String performedBy;

  @CreatedDate
  @Column(name = "performed_at", updatable = false, nullable = false)
  private LocalDateTime performedAt;

  @Column(name = "target_entity_id", nullable = false)
  private UUID targetEntityId;

  @Column(name = "target_entity_name", nullable = false)
  private String targetEntityName;
}
