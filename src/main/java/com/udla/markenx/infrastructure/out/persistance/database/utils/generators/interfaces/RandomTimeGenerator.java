package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.interfaces;

import java.time.Duration;

public interface RandomTimeGenerator {
  Duration durationInMinutes(int maxMinutes);
}
