package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.CourseJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.CourseMapper;

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
    // jpaRepository.findById(course.getId())
    // .orElseThrow(() -> new IllegalArgumentException("Course not found with id: "
    // + course.getId()));
    // CourseJpaEntity entity = mapper.toEntity(course);
    // CourseJpaEntity saved = jpaRepository.save(entity);
    // return mapper.toDomain(saved);
    return null;
  }

  @Override
  public Optional<Course> findById(Long id) {
    // return jpaRepository.findById(id).map(mapper::toDomain);
    return null;
  }

  @Override
  public Page<Course> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    // jpaRepository.deleteById(id);
  }
}
