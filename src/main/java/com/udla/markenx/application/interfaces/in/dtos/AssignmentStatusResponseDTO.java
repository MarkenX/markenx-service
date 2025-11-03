package com.udla.markenx.application.interfaces.in.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentStatusResponseDTO {
    @NotNull
    public String id;
    @NotNull
    public String label;
}
