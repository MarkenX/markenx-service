package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.CourseJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.CourseMapper;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

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

    // Generate code after persistence if it's a new entity
    if (course.getCode() == null && saved.getId() != null && saved.getAcademicTerm() != null) {
      String generatedCode = com.udla.markenx.classroom.domain.models.Course.generateCodeFromId(
          saved.getId(),
          saved.getAcademicTerm().getAcademicYear());
      saved.getExternalReference().setCode(generatedCode);
      saved = jpaRepository.save(saved);
    }

    return mapper.toDomain(saved);
  }

  @Override
  public Course update(Course course) {
    Objects.requireNonNull(course, "Course cannot be null");
    Objects.requireNonNull(course.getId(), "Course UUID cannot be null for update");

    // Find existing entity by UUID
    CourseJpaEntity existingEntity = jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(course.getId()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Course not found with UUID: " + course.getId()));

    // Update only modifiable fields
    existingEntity.setName(course.getName());
    existingEntity.setStatus(course.getEntityStatus());
    existingEntity.setUpdatedBy(course.getUpdatedBy());
    existingEntity.setUpdatedAt(course.getUpdatedAtDateTime());

    CourseJpaEntity saved = jpaRepository.save(existingEntity);
    return mapper.toDomainWithoutRelations(saved);
  }

  @Override
  public Optional<Course> findById(Long id) {
    Objects.requireNonNull(id, "Course ID cannot be null");
    return jpaRepository.findById(id)
        .filter(entity -> entity.getStatus() == EntityStatus.ENABLED)
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
    return jpaRepository.findByStatus(EntityStatus.ENABLED, pageable)
        .map(mapper::toDomainWithoutRelations);
  }

  @Override
  public Page<Course> findAllIncludingDisabled(Pageable pageable) {
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable).map(mapper::toDomainWithoutRelations);
  }

  @Override
  public Page<Course> findByStatus(EntityStatus status, Pageable pageable) {
    Objects.requireNonNull(status, "Status cannot be null");
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findByStatus(status, pageable)
        .map(mapper::toDomainWithoutRelations);
  }

  @Override
  public void deleteById(Long id) {
    Objects.requireNonNull(id, "Course ID cannot be null");
    jpaRepository.findById(id)
        .ifPresent(entity -> {
          // Soft delete: change status to DISABLED
          entity.setStatus(EntityStatus.DISABLED);
          jpaRepository.save(entity);
        });
  }

  @Override
  public Optional<Course> findById(UUID id) {
    Objects.requireNonNull(id, "Course UUID cannot be null");
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id) &&
            entity.getStatus() == EntityStatus.ENABLED)
        .findFirst()
        .map(mapper::toDomain);
  }

  @Override
  public Optional<Course> findByIdIncludingDisabled(UUID id) {
    Objects.requireNonNull(id, "Course UUID cannot be null");
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .map(mapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    Objects.requireNonNull(id, "Course UUID cannot be null");
    jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .ifPresent(entity -> {
          entity.setStatus(EntityStatus.DISABLED);
          jpaRepository.save(entity);
        });
  }
}
