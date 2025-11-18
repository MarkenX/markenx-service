package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;

import jakarta.validation.Valid;

public interface CourseManagementControllerPort {

  ResponseEntity<CourseResponseDTO> createCourse(@Valid CreateCourseRequestDTO request);

  ResponseEntity<CourseResponseDTO> updateCourse(UUID id, @Valid UpdateCourseRequestDTO request);

  ResponseEntity<CourseResponseDTO> getCourseById(UUID id);

  ResponseEntity<Page<CourseResponseDTO>> getAllCourses(Pageable pageable);

  ResponseEntity<Void> deleteCourse(UUID id);
}
