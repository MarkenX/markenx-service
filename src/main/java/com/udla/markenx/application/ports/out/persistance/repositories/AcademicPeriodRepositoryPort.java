package com.udla.markenx.application.ports.out.persistance.repositories;

import com.udla.markenx.core.models.AcademicPeriod;

public interface AcademicPeriodRepositoryPort {
  AcademicPeriod save(AcademicPeriod period);
}
