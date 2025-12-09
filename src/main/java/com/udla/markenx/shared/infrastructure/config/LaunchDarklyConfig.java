package com.udla.markenx.shared.infrastructure.config;

import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaunchDarklyConfig {

  @Value("${launchdarkly.sdk-key:sdk-9177cd48-da72-4f44-9d5e-4b5105478d5b}")
  private String sdkKey;

  @Bean
  public LDClient ldClient() {
    LDClient client = new LDClient(sdkKey);

    if (!client.isInitialized()) {
      System.out.println("❌ LaunchDarkly client no inicializado");
    } else {
      System.out.println("✅ LaunchDarkly client inicializado correctamente");
    }

    return client;
  }
}
