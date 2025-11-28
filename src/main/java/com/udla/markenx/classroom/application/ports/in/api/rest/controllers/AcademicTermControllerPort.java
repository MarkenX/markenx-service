package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.requests.AcademicPeriod.CreateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.AcademicPeriod.UpdateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AcademicPeriodResponseDTO;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public interface AcademicTermControllerPort {

  ResponseEntity<AcademicPeriodResponseDTO> createAcademicTerm(@Valid CreateAcademicTermRequestDTO request);

  ResponseEntity<AcademicPeriodResponseDTO> updateAcademicTerm(UUID id, @Valid UpdateAcademicTermRequestDTO request);

  ResponseEntity<AcademicPeriodResponseDTO> getAcademicTermById(UUID id);

  ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicTerms(DomainBaseModelStatus status, Pageable pageable);

  ResponseEntity<Void> disableAcademicTerm(UUID id);

  ResponseEntity<Void> enableAcademicTerm(UUID id);
}
