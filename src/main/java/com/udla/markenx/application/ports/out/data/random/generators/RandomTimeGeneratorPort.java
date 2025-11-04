package com.udla.markenx.application.ports.out.data.random.generators;

import java.time.Duration;

public interface RandomTimeGeneratorPort {
  Duration durationInMinutes(int maxMinutes);
}
