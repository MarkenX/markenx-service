package com.udla.markenx.classroom.academicterms.application.ports.out.persistance.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.academicterms.domain.model.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public interface AcademicTermRepositoryPort {

  List<AcademicTerm> findAll();

  Page<AcademicTerm> findAllPaged(Pageable pageable);

  Page<AcademicTerm> findAllIncludingDisabled(Pageable pageable);

  Page<AcademicTerm> findByStatus(EntityStatus entityStatus, Pageable pageable);

  List<AcademicTerm> findByAcademicYear(int academicYear);

  Optional<AcademicTerm> findById(UUID academicTermId);

  Optional<AcademicTerm> findByIdIncludingDisabled(UUID academicTermId);

  AcademicTerm save(AcademicTerm academicTerm);

  AcademicTerm update(AcademicTerm academicTerm);

  boolean existsByAcademicYearAndSemesterNumber(int academicYear, int termNumber);

  long countByAcademicYear(int academicYear);

  int countCoursesByTermId(UUID academicTermId);

  List<Course> findCoursesByTermId(UUID academicTermId);
}
