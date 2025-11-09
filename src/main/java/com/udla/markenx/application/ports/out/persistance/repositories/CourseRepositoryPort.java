package com.udla.markenx.application.ports.out.persistance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.core.models.Course;

import java.util.Optional;

/**
 * Repository port for Course persistence operations.
 */
public interface CourseRepositoryPort {

  Course save(Course course);

  Course update(Course course);

  Optional<Course> findById(Long id);

  Page<Course> findAll(Pageable pageable);

  void deleteById(Long id);
}
