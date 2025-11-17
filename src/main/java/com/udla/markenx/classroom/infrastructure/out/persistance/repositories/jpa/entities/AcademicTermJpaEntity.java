package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "academic_terms")
@PrimaryKeyJoinColumn(name = "id")
public class AcademicTermJpaEntity extends BaseJpaEntity {
  @Column(name = "start_of_term", nullable = false)
  private LocalDate startOfTerm;

  @Column(name = "end_of_term", nullable = false)
  private LocalDate endOfTerm;

  @Column(name = "academic_year", nullable = false)
  private int academicYear;

  @Column(name = "number", nullable = false)
  private int termNumber;

  @OneToMany(mappedBy = "academicTerm", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseJpaEntity> courses = new ArrayList<>();
}
