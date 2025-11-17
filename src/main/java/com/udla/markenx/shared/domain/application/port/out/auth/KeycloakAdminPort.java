package com.udla.markenx.shared.domain.application.port.out.auth;

/**
 * Port for Keycloak user management operations.
 * Implements the outbound port for authentication system integration.
 */
public interface KeycloakAdminPort {

  /**
   * Creates a new user in Keycloak with the specified credentials and role.
   *
   * @param email                 the user's email (also used as username)
   * @param password              the initial password for the user
   * @param firstName             the user's first name
   * @param lastName              the user's last name
   * @param role                  the role to assign (STUDENT or ADMIN)
   * @param requirePasswordChange whether to force password change on first login
   * @return the Keycloak user ID
   */
  String createUser(
      String email,
      String password,
      String firstName,
      String lastName,
      String role,
      boolean requirePasswordChange);

  /**
   * Deletes a user from Keycloak.
   *
   * @param keycloakUserId the Keycloak user ID
   */
  void deleteUser(String keycloakUserId);

  /**
   * Checks if a user exists in Keycloak by email.
   *
   * @param email the user's email
   * @return true if user exists, false otherwise
   */
  boolean userExists(String email);
}
