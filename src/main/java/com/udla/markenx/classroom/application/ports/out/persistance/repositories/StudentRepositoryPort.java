package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.Student;

public interface StudentRepositoryPort {

  Optional<Student> findById(Long id);

  Optional<Student> findByIdIncludingDisabled(Long id);

  Optional<Student> findById(UUID id);

  Optional<Student> findByIdIncludingDisabled(UUID id);

  Optional<Student> findByEmail(String email);

  Page<Student> findAll(Pageable pageable);

  Page<Student> findAllIncludingDisabled(Pageable pageable);

  Page<Student> findByStatus(com.udla.markenx.shared.domain.valueobjects.EntityStatus status,
      Pageable pageable);

  boolean existsByEmail(String email);

  List<Student> findByCourseId(UUID courseId);

  Student save(Student student);

  Student update(Student student);

  void deleteById(UUID id);
}
