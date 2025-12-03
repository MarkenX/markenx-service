package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, Long> {

  @Query("SELECT c FROM CourseJpaEntity c WHERE c.status = :status")
  Page<CourseJpaEntity> findByStatus(EntityStatus status, Pageable pageable);
}
