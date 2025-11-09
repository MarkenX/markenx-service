package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {

  Optional<StudentJpaEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  org.springframework.data.domain.Page<StudentJpaEntity> findByCourseId(Long courseId,
      org.springframework.data.domain.Pageable pageable);
}
