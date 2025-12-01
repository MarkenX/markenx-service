#!/bin/bash
# ============================================================
# init.sh — Development Environment Setup Script
# ============================================================
# This script automates the initialization of the local development environment.
# It performs the following steps:
#   1. Loads environment variables from the .env file.
#   2. Replaces placeholders in the init.sql file with actual environment values.
#   3. Generates a final SQL file (init.final.sql) ready for MySQL initialization.
#   4. Starts all containers defined in docker-compose.dev.yml in detached mode.
#
# NOTE:
# - Run this script from the project root directory.
# - Ensure the .env and init.sql files exist before executing.
# - This setup is intended for development use only.
# ============================================================

# Export all variables defined in .env to the current shell session
set -a
source .env
set +a

# Replace environment variable placeholders in init.sql with actual values
echo "Substituting environment variables in init.sql..."
envsubst < init.sql > init.final.sql
echo "Generated file: init.final.sql"

# Start Docker containers using the development compose file
echo "Starting Docker Compose stack..."
docker compose -f docker-compose.dev.yml up -d
echo "Development environment is up and running."
