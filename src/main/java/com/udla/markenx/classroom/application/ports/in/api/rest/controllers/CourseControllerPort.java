package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public interface CourseControllerPort {

    // ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody
    // CreateCourseRequestDTO request);

    ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id,
            @Valid @RequestBody UpdateCourseRequestDTO request);

    ResponseEntity<Void> deleteCourse(
            @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id);

    ResponseEntity<Page<CourseResponseDTO>> getAllCourses(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size);

    ResponseEntity<CourseResponseDTO> getCourseById(
            @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id);

    ResponseEntity<Page<StudentResponseDTO>> getStudentsByCourseId(
            @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size);
}
