package com.udla.markenx.classroom.terms.application.ports.out.persistance.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.udla.markenx.shared.domain.valueobjects.DateInterval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public interface TermRepositoryPort {

    int countByYearBeforeDate(int year, LocalDate date);

    List<Term> findAll();

    Page<Term> findAllPaged(Pageable pageable);

    Page<Term> findAllIncludingDisabled(Pageable pageable);

    Page<Term> findByStatus(EntityStatus entityStatus, Pageable pageable);

    List<Term> findByAcademicYear(int academicYear);

    Optional<Term> findById(UUID academicTermId);

    Optional<Term> findByIdIncludingDisabled(UUID academicTermId);

    List<Term> findOverlapping(DateInterval interval);

    Term save(Term term);

    Term update(Term term);

    boolean existsByAcademicYearAndSemesterNumber(int academicYear, int termNumber);

    long countByAcademicYear(int academicYear);

    int countCoursesByTermId(UUID academicTermId);

    List<Course> findCoursesByTermId(UUID academicTermId);
}
