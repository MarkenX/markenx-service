package com.udla.markenx.shared.infrastructure.out.security.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import com.udla.markenx.shared.application.port.out.auth.AuthenticationServicePort;
import com.udla.markenx.classroom.domain.exceptions.DomainException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keycloak implementation of the authentication service port.
 * 
 * Provides Keycloak-specific user management using the Admin REST API.
 * This is the infrastructure layer adapter that implements the domain port.
 */
@Component
public class KeycloakAdminAdapter implements AuthenticationServicePort {

  private final RestTemplate restTemplate;
  private final String keycloakUrl;
  private final String realm;
  private final String adminUsername;
  private final String adminPassword;
  private final String clientId;

  public KeycloakAdminAdapter(
      RestTemplate restTemplate,
      @Value("${keycloak.auth-server-url}") String keycloakUrl,
      @Value("${keycloak.realm}") String realm,
      @Value("${keycloak.admin.username}") String adminUsername,
      @Value("${keycloak.admin.password}") String adminPassword,
      @Value("${keycloak.admin.client-id:admin-cli}") String clientId) {
    this.restTemplate = restTemplate;
    this.keycloakUrl = keycloakUrl;
    this.realm = realm;
    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
    this.clientId = clientId;
  }

  @Override
  public String createUser(
      String email,
      String password,
      String firstName,
      String lastName,
      String role,
      boolean requirePasswordChange) {
    try {
      String accessToken = getAdminAccessToken();

      // Create user payload
      Map<String, Object> userPayload = new HashMap<>();
      userPayload.put("username", email);
      userPayload.put("email", email);
      userPayload.put("firstName", firstName);
      userPayload.put("lastName", lastName);
      userPayload.put("enabled", true);
      userPayload.put("emailVerified", true);

      // Set credentials
      Map<String, Object> credential = new HashMap<>();
      credential.put("type", "password");
      credential.put("value", password);
      credential.put("temporary", requirePasswordChange);
      userPayload.put("credentials", List.of(credential));

      // Set required actions if password change is required
      if (requirePasswordChange) {
        userPayload.put("requiredActions", List.of("UPDATE_PASSWORD"));
      }

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(accessToken);

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);

      String usersUrl = String.format("%s/admin/realms/%s/users", keycloakUrl, realm);
      ResponseEntity<String> response = restTemplate.exchange(
          usersUrl,
          HttpMethod.POST,
          request,
          String.class);

      // Extract user ID from Location header
      String location = response.getHeaders().getLocation().toString();
      String userId = location.substring(location.lastIndexOf('/') + 1);

      // Assign role
      assignRole(userId, role, accessToken);

      return userId;

    } catch (HttpClientErrorException e) {
      throw new KeycloakException("Error creating user in Keycloak: " + e.getResponseBodyAsString());
    } catch (Exception e) {
      throw new KeycloakException("Unexpected error creating user in Keycloak", e);
    }
  }

  @Override
  public void deleteUser(String keycloakUserId) {
    try {
      String accessToken = getAdminAccessToken();

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);

      HttpEntity<Void> request = new HttpEntity<>(headers);

      String userUrl = String.format("%s/admin/realms/%s/users/%s", keycloakUrl, realm, keycloakUserId);
      restTemplate.exchange(userUrl, HttpMethod.DELETE, request, Void.class);

    } catch (HttpClientErrorException e) {
      throw new KeycloakException("Error deleting user from Keycloak: " + e.getResponseBodyAsString());
    } catch (Exception e) {
      throw new KeycloakException("Unexpected error deleting user from Keycloak", e);
    }
  }

  @Override
  public void disableUser(String email) {
    try {
      String accessToken = getAdminAccessToken();

      // Find user by email
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);
      HttpEntity<Void> searchRequest = new HttpEntity<>(headers);

      String usersUrl = String.format("%s/admin/realms/%s/users?username=%s&exact=true", keycloakUrl, realm, email);
      ResponseEntity<List> searchResponse = restTemplate.exchange(usersUrl, HttpMethod.GET, searchRequest, List.class);

      List users = searchResponse.getBody();
      if (users == null || users.isEmpty()) {
        throw new KeycloakException("User not found with email: " + email);
      }

      // Get user ID
      @SuppressWarnings("unchecked")
      Map<String, Object> user = (Map<String, Object>) users.get(0);
      String userId = (String) user.get("id");

      // Update user to disable
      Map<String, Object> updatePayload = new HashMap<>();
      updatePayload.put("enabled", false);

      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Map<String, Object>> updateRequest = new HttpEntity<>(updatePayload, headers);

      String userUrl = String.format("%s/admin/realms/%s/users/%s", keycloakUrl, realm, userId);
      restTemplate.exchange(userUrl, HttpMethod.PUT, updateRequest, Void.class);

    } catch (HttpClientErrorException e) {
      throw new KeycloakException("Error disabling user in Keycloak: " + e.getResponseBodyAsString());
    } catch (Exception e) {
      throw new KeycloakException("Unexpected error disabling user in Keycloak", e);
    }
  }

  @Override
  public boolean userExists(String email) {
    try {
      String accessToken = getAdminAccessToken();

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);

      HttpEntity<Void> request = new HttpEntity<>(headers);

      String usersUrl = String.format("%s/admin/realms/%s/users?username=%s&exact=true", keycloakUrl, realm, email);
      ResponseEntity<List> response = restTemplate.exchange(usersUrl, HttpMethod.GET, request, List.class);

      return response.getBody() != null && !response.getBody().isEmpty();

    } catch (Exception e) {
      throw new KeycloakException("Error checking if user exists in Keycloak", e);
    }
  }

  private String getAdminAccessToken() {
    try {
      String tokenUrl = String.format("%s/realms/master/protocol/openid-connect/token", keycloakUrl);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      String body = String.format(
          "grant_type=password&client_id=%s&username=%s&password=%s",
          clientId, adminUsername, adminPassword);

      HttpEntity<String> request = new HttpEntity<>(body, headers);

      ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

      Map<String, Object> responseBody = response.getBody();
      if (responseBody == null || !responseBody.containsKey("access_token")) {
        throw new KeycloakException("Failed to obtain admin access token");
      }

      return (String) responseBody.get("access_token");

    } catch (Exception e) {
      throw new KeycloakException("Error obtaining Keycloak admin token", e);
    }
  }

  private void assignRole(String userId, String roleName, String accessToken) {
    try {
      // Get role ID
      String rolesUrl = String.format("%s/admin/realms/%s/roles/%s", keycloakUrl, realm, roleName);
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);
      HttpEntity<Void> request = new HttpEntity<>(headers);

      ResponseEntity<Map> roleResponse = restTemplate.exchange(rolesUrl, HttpMethod.GET, request, Map.class);
      Map<String, Object> role = roleResponse.getBody();

      if (role == null) {
        throw new KeycloakException("Role not found: " + roleName);
      }

      // Assign role to user
      String userRolesUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakUrl, realm,
          userId);
      HttpEntity<List<Map<String, Object>>> assignRequest = new HttpEntity<>(List.of(role), headers);

      restTemplate.exchange(userRolesUrl, HttpMethod.POST, assignRequest, Void.class);

    } catch (Exception e) {
      throw new KeycloakException("Error assigning role to user", e);
    }
  }

  public static class KeycloakException extends DomainException {
    public KeycloakException(String message) {
      super(message);
    }

    public KeycloakException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
