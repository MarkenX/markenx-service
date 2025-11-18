package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.CourseJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.CourseMapper;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryAdapter implements CourseRepositoryPort {

  private final CourseJpaRepository jpaRepository;
  private final CourseMapper mapper;

  @Override
  public Course save(Course course) {
    CourseJpaEntity entity = mapper.toEntity(course);
    CourseJpaEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Course update(Course course) {
    Objects.requireNonNull(course, "Course cannot be null");
    Long courseId = Objects.requireNonNull(course.getSequence(),
        "Course sequence (internal ID) cannot be null for update");

    // Find existing entity
    CourseJpaEntity existingEntity = jpaRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("Course not found with id: " + course.getSequence()));

    // Update fields
    existingEntity.setName(course.getName());
    existingEntity.setStatus(course.getStatus());
    existingEntity.setUpdatedBy(course.getUpdatedBy());
    existingEntity.setUpdatedAt(course.getUpdatedAtDateTime());

    CourseJpaEntity saved = jpaRepository.save(existingEntity);
    return mapper.toDomainWithoutRelations(saved);
  }

  @Override
  public Optional<Course> findById(Long id) {
    Objects.requireNonNull(id, "Course ID cannot be null");
    return jpaRepository.findById(id)
        .filter(entity -> entity.getStatus() == DomainBaseModelStatus.ENABLED)
        .map(mapper::toDomain);
  }

  @Override
  public Optional<Course> findByIdIncludingDisabled(Long id) {
    Objects.requireNonNull(id, "Course ID cannot be null");
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Page<Course> findAll(Pageable pageable) {
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findByStatus(DomainBaseModelStatus.ENABLED, pageable)
        .map(mapper::toDomainWithoutRelations);
  }

  @Override
  public Page<Course> findAllIncludingDisabled(Pageable pageable) {
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable).map(mapper::toDomainWithoutRelations);
  }

  @Override
  public void deleteById(Long id) {
    Objects.requireNonNull(id, "Course ID cannot be null");
    jpaRepository.findById(id)
        .ifPresent(entity -> {
          // Soft delete: change status to DISABLED
          entity.setStatus(DomainBaseModelStatus.DISABLED);
          jpaRepository.save(entity);
        });
  }
}
