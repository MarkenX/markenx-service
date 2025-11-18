package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities;

import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "courses")
@PrimaryKeyJoinColumn(name = "id")
public class CourseJpaEntity extends BaseJpaEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AssignmentJpaEntity> assignments = new ArrayList<>();

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StudentJpaEntity> students = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "term_id")
  private AcademicTermJpaEntity academicTerm;
}
