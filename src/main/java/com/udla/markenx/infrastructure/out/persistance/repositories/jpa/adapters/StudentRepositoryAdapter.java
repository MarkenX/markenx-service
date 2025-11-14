package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.StudentMapper;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepositoryPort {

  private final StudentJpaRepository jpaRepository;
  private final StudentMapper mapper;

  @Override
  public Student save(Student student, String externalAuthId) {
    StudentJpaEntity entity = mapper.toEntity(student);
    StudentJpaEntity savedEntity = jpaRepository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Student update(Student student) {
    // Find existing entity to preserve externalAuthId
    StudentJpaEntity existingEntity = jpaRepository.findById(student.getId())
        .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + student.getId()));

    // Update with existing externalAuthId
    // StudentJpaEntity entity = mapper.toEntity(student,
    // existingEntity.getKeycloakUserId());
    StudentJpaEntity entity = mapper.toEntity(student);
    StudentJpaEntity savedEntity = jpaRepository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Student> findById(Long id) {
    // return jpaRepository.findById(id).map(StudentMapper::toDomain);
    return null;
  }

  @Override
  public Optional<Student> findByEmail(String email) {
    // return jpaRepository.findByEmail(email).map(StudentMapper::toDomain);
    return null;
  }

  @Override
  public Page<Student> findAll(Pageable pageable) {
    // return jpaRepository.findAll(pageable).map(StudentMapper::toDomain);
    return null;
  }

  @Override
  public Page<Student> findByCourseId(Long courseId, Pageable pageable) {
    // return jpaRepository.findByCourseId(courseId,
    // pageable).map(StudentMapper::toDomain);
    return null;
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  @Override
  public Optional<String> findExternalAuthIdById(Long id) {
    // return jpaRepository.findById(id).map(StudentJpaEntity::getKeycloakUserId);
    return null;
  }

  @Override
  public void deleteById(Long id) {
    // jpaRepository.deleteById(id);
  }
}
