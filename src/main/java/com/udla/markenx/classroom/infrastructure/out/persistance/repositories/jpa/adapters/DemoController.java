package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @GetMapping("/api/demo")
  public String demoFeature() {
    return "Demo endpoint: LaunchDarkly removed.";
  }
}
