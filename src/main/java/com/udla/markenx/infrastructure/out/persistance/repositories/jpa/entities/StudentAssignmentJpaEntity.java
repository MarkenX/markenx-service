package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student_assignments")
@EqualsAndHashCode(callSuper = true)
public class StudentAssignmentJpaEntity extends BaseJpaEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "student_assignment_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private AssignmentJpaEntity assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  private StudentJpaEntity student;

  @Column(name = "current_status")
  private AssignmentStatus currentStatus;
}
