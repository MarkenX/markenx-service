package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.Student;

import java.util.Optional;

public interface StudentRepositoryPort {

  Optional<Student> findById(Long id);

  Optional<Student> findByIdIncludingDisabled(Long id);

  Optional<Student> findByEmail(String email);

  Page<Student> findAll(Pageable pageable);

  Page<Student> findAllIncludingDisabled(Pageable pageable);

  boolean existsByEmail(String email);
}
