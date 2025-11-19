package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.requests.CreateAcademicPeriodRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AcademicPeriodResponseDTO;

public interface AcademicTermControllerPort {

  ResponseEntity<AcademicPeriodResponseDTO> createAcademicTerm(@Valid CreateAcademicPeriodRequestDTO request);

  ResponseEntity<AcademicPeriodResponseDTO> updateAcademicTerm(UUID id, @Valid UpdateAcademicTermRequestDTO request);

  ResponseEntity<AcademicPeriodResponseDTO> getAcademicTermById(UUID id);

  ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicTerms(
      com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus status,
      Pageable pageable);

  ResponseEntity<Void> disableAcademicTerm(UUID id);

  ResponseEntity<Void> enableAcademicTerm(UUID id);
}
