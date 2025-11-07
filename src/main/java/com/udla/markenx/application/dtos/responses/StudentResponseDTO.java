package com.udla.markenx.application.dtos.responses;

public record StudentResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email) {
}
