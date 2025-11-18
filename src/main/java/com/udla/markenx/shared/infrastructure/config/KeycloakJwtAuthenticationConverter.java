package com.udla.markenx.shared.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts Keycloak JWT tokens to Spring Security authentication tokens.
 * Extracts roles from both realm_access and resource_access claims.
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  /**
   * Converts a JWT token into a Spring Security authentication token.
   * Combines default authorities with Keycloak realm roles.
   * 
   * @param jwt the JWT token from Keycloak
   * @return authentication token with all authorities
   */
  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    // Combine default JWT authorities with Keycloak realm roles
    Collection<GrantedAuthority> authorities = Stream.concat(
        defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
        extractResourceRoles(jwt).stream()).collect(Collectors.toList());

    return new JwtAuthenticationToken(jwt, authorities);
  }

  /**
   * Extracts roles from the Keycloak JWT 'realm_access' claim.
   * Converts them to Spring Security GrantedAuthority objects with ROLE_ prefix.
   * 
   * @param jwt the JWT token
   * @return collection of granted authorities from realm roles
   */
  @SuppressWarnings("unchecked")
  private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
    // Get realm_access claim from JWT
    Map<String, Object> realmAccess = jwt.getClaim("realm_access");

    // Return empty collection if no realm roles found
    if (realmAccess == null || !realmAccess.containsKey("roles")) {
      return Stream.<GrantedAuthority>empty().collect(Collectors.toList());
    }

    // Extract roles array from realm_access
    Collection<String> roles = (Collection<String>) realmAccess.get("roles");

    // Convert each role to GrantedAuthority with ROLE_ prefix
    return roles.stream()
        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        .collect(Collectors.toList());
  }
}
