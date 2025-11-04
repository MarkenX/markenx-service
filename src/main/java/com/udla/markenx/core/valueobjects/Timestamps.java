package com.udla.markenx.core.valueobjects;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import lombok.Getter;

@Getter
public class Timestamps {
  private final Instant createdAt;
  private Instant updatedAt;

  public Timestamps() {
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }

  public void markUpdated() {
    this.updatedAt = Instant.now();
  }

  public LocalDate getCreatedDate() {
    return createdAt.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public LocalDate getUpdatedDate() {
    return updatedAt.atZone(ZoneId.systemDefault()).toLocalDate();
  }
}
