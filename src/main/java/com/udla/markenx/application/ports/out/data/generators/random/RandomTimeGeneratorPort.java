package com.udla.markenx.application.ports.out.data.generators.random;

import java.time.Duration;

public interface RandomTimeGeneratorPort {
  Duration durationInMinutes(int maxMinutes);
}
