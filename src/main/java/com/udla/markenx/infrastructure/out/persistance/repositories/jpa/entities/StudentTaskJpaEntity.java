package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student-tasks")
public class StudentTaskJpaEntity extends StudentAssignmentJpaEntity {
  @Column(name = "active_attempt")
  private int activeAttempt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "public_id")
  private StudentJpaEntity student;

  @OneToMany(mappedBy = "studentAssignment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AttemptJpaEntity> attempts = new ArrayList<>();
}
