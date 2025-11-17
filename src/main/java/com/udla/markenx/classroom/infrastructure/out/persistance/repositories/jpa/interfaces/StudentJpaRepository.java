package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {

  Optional<StudentJpaEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  org.springframework.data.domain.Page<StudentJpaEntity> findByCourseId(Long courseId,
      org.springframework.data.domain.Pageable pageable);
}
