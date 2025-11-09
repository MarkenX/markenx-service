package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity for Academic Period persistence.
 * 
 * Extends AuditableEntity to automatically track admin CRUD operations:
 * - createdBy: Email of admin who created the period
 * - createdAt: Timestamp when period was created
 * - lastModifiedBy: Email of admin who last modified the period
 * - lastModifiedAt: Timestamp when period was last modified
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "academic_periods", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "year", "semester_number" })
})
public class AcademicPeriodJpaEntity extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "period_id")
  private Long id;

  @Column(name = "period_start_date", nullable = false)
  @Temporal(TemporalType.DATE)
  private LocalDate startDate;

  @Column(name = "period_end_date", nullable = false)
  @Temporal(TemporalType.DATE)
  private LocalDate endDate;

  @Column(name = "year", nullable = false)
  private int year;

  @Column(name = "semester_number", nullable = false)
  private int semesterNumber;

  @OneToMany(mappedBy = "academicPeriod", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseJpaEntity> courses = new ArrayList<>();

  public AcademicPeriodJpaEntity(LocalDate startDate, LocalDate endDate, int year, int semesterNumber) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.year = year;
    this.semesterNumber = semesterNumber;
  }
}
