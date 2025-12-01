package com.udla.markenx;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost/fake",
		"keycloak.realm=test-realm"
})
class MarkenxServiceApplicationTests {
}
