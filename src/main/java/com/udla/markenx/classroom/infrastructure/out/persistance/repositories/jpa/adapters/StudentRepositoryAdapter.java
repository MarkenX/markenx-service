package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.CourseJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.StudentMapper;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepositoryPort {

  private final StudentJpaRepository jpaRepository;
  private final CourseJpaRepository courseJpaRepository;
  private final StudentMapper mapper;

  @Override
  public Optional<Student> findById(Long id) {
    Objects.requireNonNull(id, "Student ID cannot be null");
    return jpaRepository.findById(id)
        .filter(
            entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
        .map(mapper::toDomain);
  }

  @Override
  public Optional<Student> findByIdIncludingDisabled(Long id) {
    Objects.requireNonNull(id, "Student ID cannot be null");
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Student> findByEmail(String email) {
    return jpaRepository.findByEmail(email)
        .filter(
            entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
        .map(mapper::toDomain);
  }

  @Override
  public Page<Student> findAll(Pageable pageable) {
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable)
        .map(entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED
            ? mapper.toDomainWithoutTasks(entity)
            : null)
        .map(domain -> domain);
  }

  @Override
  public Page<Student> findAllIncludingDisabled(Pageable pageable) {
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable).map(mapper::toDomainWithoutTasks);
  }

  @Override
  public Page<Student> findByStatus(com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus status,
      Pageable pageable) {
    Objects.requireNonNull(status, "Status cannot be null");
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findByStatus(status, pageable)
        .map(mapper::toDomainWithoutTasks);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  @Override
  public java.util.List<Student> findByCourseId(UUID courseId) {
    Objects.requireNonNull(courseId, "Course UUID cannot be null");
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getCourse() != null &&
            entity.getCourse().getExternalReference() != null &&
            entity.getCourse().getExternalReference().getPublicId().equals(courseId) &&
            entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
        .map(mapper::toDomainWithoutTasks)
        .toList();
  }

  @Override
  public Optional<Student> findById(UUID id) {
    Objects.requireNonNull(id, "Student UUID cannot be null");
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id) &&
            entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
        .findFirst()
        .map(mapper::toDomain);
  }

  @Override
  public Optional<Student> findByIdIncludingDisabled(UUID id) {
    Objects.requireNonNull(id, "Student UUID cannot be null");
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .map(mapper::toDomain);
  }

  @Override
  public Student save(Student student) {
    Objects.requireNonNull(student, "Student cannot be null");

    // Find course entity if student has a courseId
    CourseJpaEntity courseEntity = null;
    if (student.getEnrolledCourseId() != null) {
      courseEntity = courseJpaRepository.findAll().stream()
          .filter(c -> c.getExternalReference() != null &&
              c.getExternalReference().getPublicId().equals(student.getEnrolledCourseId()))
          .findFirst()
          .orElseThrow(
              () -> new IllegalArgumentException("Curso no encontrado con ID: " + student.getEnrolledCourseId()));
    }

    var entity = mapper.toEntity(student, courseEntity);
    var savedEntity = jpaRepository.save(entity);

    // Generate code after persistence if it's a new entity
    if (student.getCode() == null && savedEntity.getId() != null) {
      String generatedCode = com.udla.markenx.classroom.domain.models.Student.generateCodeFromId(savedEntity.getId());
      savedEntity.getExternalReference().setCode(generatedCode);
      savedEntity = jpaRepository.save(savedEntity);
    }

    return mapper.toDomain(savedEntity);
  }

  @Override
  public Student update(Student student) {
    Objects.requireNonNull(student, "Student cannot be null");
    Objects.requireNonNull(student.getId(), "Student ID cannot be null for update");

    // Find existing entity by UUID
    var existingEntity = jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(student.getId()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + student.getId()));

    // Update modifiable fields
    existingEntity.setFirstName(student.getFirstName());
    existingEntity.setLastName(student.getLastName());
    existingEntity.setEmail(student.getAcademicEmail());
    existingEntity.setStatus(student.getStatus());
    existingEntity.setUpdatedBy(student.getUpdatedBy());
    existingEntity.setUpdatedAt(student.getUpdatedAtDateTime());

    var savedEntity = jpaRepository.save(existingEntity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public void deleteById(UUID id) {
    Objects.requireNonNull(id, "Student UUID cannot be null");

    var entity = jpaRepository.findAll().stream()
        .filter(e -> e.getExternalReference() != null &&
            e.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));

    // Soft delete: set status to DISABLED
    entity.setStatus(com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.DISABLED);
    jpaRepository.save(entity);
  }
}
