package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "academic_terms", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "academic_year", "term_number" })
})
public class AcademicTermJpaEntity extends BaseJpaEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "term_id")
  private Long id;

  @Column(name = "start_date", nullable = false)
  private LocalDate startOfTerm;

  @Column(name = "end_date", nullable = false)
  private LocalDate endOfTerm;

  @Column(name = "academic_year", nullable = false)
  private int academicYear;

  @Column(name = "term_number", nullable = false)
  private int termNumber;

  @OneToMany(mappedBy = "academicTerm", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseJpaEntity> assignedCourses = new ArrayList<>();
}
