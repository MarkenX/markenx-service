package com.udla.markenx.shared.domain.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for security-related operations.
 * Provides methods to check current user's authentication and roles.
 */
public final class SecurityUtils {

  private SecurityUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Checks if the current authenticated user has ADMIN role.
   * 
   * @return true if user has ADMIN role, false otherwise
   */
  public static boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }

  /**
   * Checks if the current authenticated user has STUDENT role.
   * 
   * @return true if user has STUDENT role, false otherwise
   */
  public static boolean isStudent() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"));
  }

  /**
   * Gets the authenticated username from security context.
   * 
   * @return Username or "anonymous" if not authenticated
   */
  public static String getAuthenticatedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return "anonymous";
  }
}
