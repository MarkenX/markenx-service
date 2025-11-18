package com.udla.markenx.shared.application.port.out.auth;

/**
 * Port for authentication system user management operations.
 * 
 * Provides technology-agnostic interface for managing users in the
 * authentication system.
 * The actual implementation could be Keycloak, Auth0, AWS Cognito, or any other
 * provider.
 */
public interface AuthenticationServicePort {

  /**
   * Creates a new user in the authentication system.
   *
   * @param email                 the user's email (used as username)
   * @param password              the initial password
   * @param firstName             the user's first name
   * @param lastName              the user's last name
   * @param role                  the role to assign (e.g., STUDENT, ADMIN)
   * @param requirePasswordChange whether to force password change on first login
   * @return the external authentication system user ID
   */
  String createUser(
      String email,
      String password,
      String firstName,
      String lastName,
      String role,
      boolean requirePasswordChange);

  /**
   * Deletes a user from the authentication system.
   *
   * @param externalUserId the external authentication system user ID
   */
  void deleteUser(String externalUserId);

  /**
   * Checks if a user exists in the authentication system by email.
   *
   * @param email the user's email
   * @return true if user exists, false otherwise
   */
  boolean userExists(String email);
}
