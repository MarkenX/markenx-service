package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.StudentMapper;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepositoryPort {

  private final StudentJpaRepository jpaRepository;
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
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }
}
