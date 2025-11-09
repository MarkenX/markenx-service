package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.CourseJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.CourseMapper;

/**
 * JPA adapter implementing course repository operations.
 */
@Repository
public class CourseRepositoryAdapter implements CourseRepositoryPort {

  private final CourseJpaRepository jpaRepository;

  public CourseRepositoryAdapter(CourseJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Course save(Course course) {
    CourseJpaEntity entity = CourseMapper.toEntity(course);
    CourseJpaEntity saved = jpaRepository.save(entity);
    return CourseMapper.toDomain(saved);
  }

  @Override
  public Course update(Course course) {
    // Ensure exists
    jpaRepository.findById(course.getId())
        .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + course.getId()));
    CourseJpaEntity entity = CourseMapper.toEntity(course);
    CourseJpaEntity saved = jpaRepository.save(entity);
    return CourseMapper.toDomain(saved);
  }

  @Override
  public Optional<Course> findById(Long id) {
    return jpaRepository.findById(id).map(CourseMapper::toDomain);
  }

  @Override
  public Page<Course> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(CourseMapper::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }
}
