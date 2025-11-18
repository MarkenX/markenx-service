package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;

import jakarta.validation.Valid;

public interface CourseManagementControllerPort {

  ResponseEntity<CourseResponseDTO> createCourse(@Valid CreateCourseRequestDTO request);

  ResponseEntity<CourseResponseDTO> updateCourse(Long id, @Valid UpdateCourseRequestDTO request);

  ResponseEntity<CourseResponseDTO> getCourseById(Long id);

  ResponseEntity<Page<CourseResponseDTO>> getAllCourses(Pageable pageable);

  ResponseEntity<Void> deleteCourse(Long id);
}
