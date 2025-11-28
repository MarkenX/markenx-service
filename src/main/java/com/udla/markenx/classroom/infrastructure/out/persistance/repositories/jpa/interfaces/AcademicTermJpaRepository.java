package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public interface AcademicTermJpaRepository extends JpaRepository<AcademicTermJpaEntity, Long> {

  Page<AcademicTermJpaEntity> findByStatus(DomainBaseModelStatus status, Pageable pageable);

  List<AcademicTermJpaEntity> findByAcademicYear(int academicYear);
}
