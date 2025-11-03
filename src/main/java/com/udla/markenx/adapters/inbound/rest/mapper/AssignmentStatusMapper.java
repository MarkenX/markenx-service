package com.udla.markenx.adapters.inbound.rest.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.udla.markenx.adapters.inbound.rest.dto.AssignmentStatusResponseDTO;
import com.udla.markenx.core.valueobjects.AssignmentStatus;

public class AssignmentStatusMapper {
    public static AssignmentStatusResponseDTO toDto(AssignmentStatus status) {
        return new AssignmentStatusResponseDTO(status.name(), status.getLabel());
    }

    public static List<AssignmentStatusResponseDTO> toDtoList() {
        return Arrays.stream(AssignmentStatus.values())
                .map(AssignmentStatusMapper::toDto)
                .collect(Collectors.toList());
    }
}
