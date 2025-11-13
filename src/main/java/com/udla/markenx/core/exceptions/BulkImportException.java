package com.udla.markenx.core.exceptions;

import java.util.Map;

public class BulkImportException extends DomainException {

  private final int totalRecords;
  private final Map<Integer, String> failedImports;

  public BulkImportException(String message, int totalRecords, Map<Integer, String> failedImports) {
    super(message);
    this.totalRecords = totalRecords;
    this.failedImports = failedImports;
  }

  public int getTotalRecords() {
    return totalRecords;
  }

  public Map<Integer, String> getFailedImports() {
    return failedImports;
  }

  public int getFailureCount() {
    return failedImports.size();
  }
}
