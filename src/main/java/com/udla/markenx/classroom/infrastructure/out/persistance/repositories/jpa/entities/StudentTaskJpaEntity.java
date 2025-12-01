package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student_tasks")
@PrimaryKeyJoinColumn(name = "id")
public class StudentTaskJpaEntity extends StudentAssignmentJpaEntity {
  @OneToMany(mappedBy = "studentTask", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AttemptJpaEntity> attempts = new ArrayList<>();
}
