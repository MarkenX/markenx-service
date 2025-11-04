package com.udla.markenx.application.data.generators;

import java.time.Duration;

public interface RandomTimeGenerator {
  Duration durationInMinutes(int maxMinutes);
}
