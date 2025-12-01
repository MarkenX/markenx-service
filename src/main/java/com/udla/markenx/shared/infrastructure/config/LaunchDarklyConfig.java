package com.udla.markenx.shared.infrastructure.config;

import com.launchdarkly.sdk.server.LDClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaunchDarklyConfig {

  @Bean
  public LDClient ldClient() {
    String sdkKey = System.getenv("LAUNCHDARKLY_SDK_KEY");
    LDClient client = new LDClient(sdkKey);

    if (!client.isInitialized()) {
      System.out.println("❌ LaunchDarkly client no inicializado");
    } else {
      System.out.println("✅ LaunchDarkly client inicializado correctamente");
    }

    return client;
  }
}
