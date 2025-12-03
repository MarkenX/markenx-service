package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public interface CourseManagementControllerPort {

  ResponseEntity<CourseResponseDTO> createCourse(@Valid CreateCourseRequestDTO request);

  ResponseEntity<CourseResponseDTO> updateCourse(UUID id, @Valid UpdateCourseRequestDTO request);

  ResponseEntity<CourseResponseDTO> getCourseById(UUID id);

  ResponseEntity<Page<CourseResponseDTO>> getAllCourses(
      EntityStatus status,
      Pageable pageable);

  ResponseEntity<Void> disableCourse(UUID id);

  ResponseEntity<Void> enableCourse(UUID id);
}
