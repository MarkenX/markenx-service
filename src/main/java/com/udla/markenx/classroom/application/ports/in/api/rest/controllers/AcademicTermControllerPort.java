package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.udla.markenx.classroom.application.dtos.requests.CreateAcademicPeriodRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AcademicPeriodResponseDTO;

import jakarta.validation.Valid;

public interface AcademicTermControllerPort {

  ResponseEntity<AcademicPeriodResponseDTO> createAcademicTerm(@Valid CreateAcademicPeriodRequestDTO request);

  ResponseEntity<AcademicPeriodResponseDTO> updateAcademicTerm(UUID id, @Valid UpdateAcademicTermRequestDTO request);

  ResponseEntity<AcademicPeriodResponseDTO> getAcademicTermById(UUID id);

  ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicTerms(Pageable pageable);

  ResponseEntity<Void> deleteAcademicTerm(UUID id);
}
