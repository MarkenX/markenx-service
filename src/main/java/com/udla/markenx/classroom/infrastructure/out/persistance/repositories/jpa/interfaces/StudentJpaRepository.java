package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {

  Optional<StudentJpaEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  Page<StudentJpaEntity> findByCourseId(Long courseId, Pageable pageable);

  Page<StudentJpaEntity> findByStatus(EntityStatus status, Pageable pageable);
}
