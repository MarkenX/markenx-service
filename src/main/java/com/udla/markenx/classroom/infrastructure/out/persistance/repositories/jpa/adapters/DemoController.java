package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.LDUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  private final LDClient ldClient;

  public DemoController(LDClient ldClient) {
    this.ldClient = ldClient;
  }

  @GetMapping("/api/demo")
  public String demoFeature() {
    LDUser user = new LDUser.Builder("user-123").build();
    boolean isEnabled = ldClient.boolVariation("endpoint-feature", user, false);

    if (isEnabled) {
      return "Funcionalidad nueva activada!";
    } else {
      return "Funcionalidad antigua";
    }
  }
}
