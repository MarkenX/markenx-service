package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.Course;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepositoryPort {

  Course save(Course course);

  Course update(Course course);

  Optional<Course> findById(Long id);

  Optional<Course> findByIdIncludingDisabled(Long id);

  Optional<Course> findById(UUID id);

  Optional<Course> findByIdIncludingDisabled(UUID id);

  Page<Course> findAll(Pageable pageable);

  Page<Course> findAllIncludingDisabled(Pageable pageable);

  void deleteById(Long id);

  void deleteById(UUID id);
}
