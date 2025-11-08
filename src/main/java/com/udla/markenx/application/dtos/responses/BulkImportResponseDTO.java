package com.udla.markenx.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for successful bulk student import operation.
 * 
 * Only returned when ALL students are imported successfully.
 * If any validation fails, BulkImportException is thrown instead.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkImportResponseDTO {

  private String message;
  private int totalRecords;
  private int successfulImports;

  public static BulkImportResponseDTO success(int totalRecords) {
    return BulkImportResponseDTO.builder()
        .message("Todos los estudiantes fueron importados exitosamente")
        .totalRecords(totalRecords)
        .successfulImports(totalRecords)
        .build();
  }
}
