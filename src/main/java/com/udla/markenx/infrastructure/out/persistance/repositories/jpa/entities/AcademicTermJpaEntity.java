package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class AcademicTermJpaEntity extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "term_id")
  private Long id;

  @Column(name = "public_id", nullable = false, unique = true, updatable = false)
  private UUID publicId;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private DomainBaseModelStatus status;

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
