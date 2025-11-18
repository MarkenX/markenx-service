package com.udla.markenx.classroom.application.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.domain.models.Course;

@Service
public class CourseService {

  private final CourseRepositoryPort courseRepository;

  public CourseService(CourseRepositoryPort courseRepository) {
    this.courseRepository = courseRepository;
  }

  public Page<Course> getAllCourseIds(int page, int size) {
    return courseRepository.findAll(PageRequest.of(page, size));
  }

  public Course getCourseById(Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
  }
}
