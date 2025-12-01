-- ============================================================
-- Minimal Database Initialization Script
-- Project: MarkenX
-- ============================================================
-- This script prepares the base MySQL environment for local development.
-- It performs the following actions:
--   1. Sets the proper UTF-8 character encoding.
--   2. Creates the required databases.
--   3. Creates users and assigns privileges.
--   4. Verifies successful creation with a confirmation message.
--
-- NOTE:
-- - This script is intended for DEVELOPMENT USE ONLY.
-- - Variables like ${MYSQL_MARKENX_USER} are replaced by init.sh using envsubst.
-- ============================================================


-- ============================================================
-- CHARACTER ENCODING CONFIGURATION
-- ============================================================
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;


-- ============================================================
-- DATABASE CREATION
-- ============================================================
-- Create the MarkenX database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS markenx_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Create the Keycloak database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS keycloak_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- USER CREATION AND PRIVILEGES
-- ============================================================
-- Create the MySQL user for MarkenX with the configured password
CREATE USER IF NOT EXISTS '${MYSQL_MARKENX_USER}'@'%' IDENTIFIED BY '${MYSQL_MARKENX_PASSWORD}';

-- Create the MySQL user for Keycloak with the configured password
CREATE USER IF NOT EXISTS '${MYSQL_KEYCLOAK_USER}'@'%' IDENTIFIED BY '${MYSQL_KEYCLOAK_PASSWORD}';

-- Grant full privileges on the MarkenX database to its user
GRANT ALL PRIVILEGES ON markenx_db.* TO '${MYSQL_MARKENX_USER}'@'%';

-- Grant full privileges on the Keycloak database to its user
GRANT ALL PRIVILEGES ON keycloak_db.* TO '${MYSQL_KEYCLOAK_USER}'@'%';

-- Apply changes to the MySQL privilege system
FLUSH PRIVILEGES;


-- ============================================================
-- VERIFICATION
-- ============================================================
-- Simple confirmation message to verify successful execution
SELECT 'Databases successfully created' AS status;
