package com.udla.markenx.infrastructure.out.database.seeders.generators.interfaces;

import java.time.Duration;

public interface RandomTimeGenerator {
  Duration durationInMinutes(int maxMinutes);
}
