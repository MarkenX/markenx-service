package com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.entities.TermJpaEntity;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public interface TermJpaRepository extends JpaRepository<TermJpaEntity, Long> {

  Page<TermJpaEntity> findByStatus(EntityStatus status, Pageable pageable);

  List<TermJpaEntity> findByAcademicYear(int academicYear);
}
