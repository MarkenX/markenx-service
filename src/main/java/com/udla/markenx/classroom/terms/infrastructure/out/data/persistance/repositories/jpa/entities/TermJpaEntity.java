package com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "academic_terms")
@PrimaryKeyJoinColumn(name = "id")
public class TermJpaEntity extends BaseJpaEntity {
  @Column(name = "start_of_term", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_of_term", nullable = false)
  private LocalDate endDate;

  @Column(name = "academic_year", nullable = false)
  private int year;

  @Column(name = "number", nullable = false)
  private int sequence;

//  @OneToMany(mappedBy = "academicTerm", cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<CourseJpaEntity> courses = new ArrayList<>();
}
