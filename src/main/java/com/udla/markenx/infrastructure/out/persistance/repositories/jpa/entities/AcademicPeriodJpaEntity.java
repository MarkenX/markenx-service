package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "academic-period")
public class AcademicPeriodJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "period_id")
  private Long id;

  @Column(name = "period_startDate")
  @Temporal(TemporalType.DATE)
  private LocalDate startDate;

  @Column(name = "period_endDate")
  @Temporal(TemporalType.DATE)
  private LocalDate endDate;

  @Column(name = "period_label")
  private String label;

  @OneToMany(mappedBy = "academicPeriod", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseJpaEntity> courses = new ArrayList<>();

  public AcademicPeriodJpaEntity(
      LocalDate startDate,
      LocalDate endDate,
      String label) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.label = label;
  }
}
