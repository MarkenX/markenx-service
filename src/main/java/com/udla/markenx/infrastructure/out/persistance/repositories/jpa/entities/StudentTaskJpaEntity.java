package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "student-tasks")
public class StudentTaskJpaEntity extends BaseJpaEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "public_id")
  private StudentJpaEntity student;
}
