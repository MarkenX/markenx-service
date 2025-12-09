package com.udla.markenx.shared.infrastructure.config;

import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaunchDarklyConfig {

  @Value("${launchdarkly.sdk-key:}")
  private String sdkKey;

  @Bean
  @ConditionalOnProperty(name = "launchdarkly.enabled", havingValue = "true")
  public LDClient ldClient() {
    if (sdkKey == null || sdkKey.isEmpty()) {
      throw new IllegalArgumentException("LaunchDarkly SDK key must not be empty when LaunchDarkly is enabled");
    }
    
    LDClient client = new LDClient(sdkKey);

    if (!client.isInitialized()) {
      System.out.println("❌ LaunchDarkly client no inicializado");
    } else {
      System.out.println("✅ LaunchDarkly client inicializado correctamente");
    }

    return client;
  }
}
