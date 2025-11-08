# Keycloak Configuration for MarkenX

This guide explains how to configure Keycloak for the MarkenX service.

## Quick Setup

### Option 1: Import Realm Configuration (Recommended)

1. **Start Keycloak** (via Docker Compose):
   ```bash
   docker compose -f docker-compose.dev.yml up -d keycloak
   ```

2. **Access Keycloak Admin Console**:
   - URL: http://localhost:7080
   - Username: `admin` (from `KEYCLOAK_ADMIN` env variable)
   - Password: Your `KEYCLOAK_ADMIN_PASSWORD` env variable

3. **Import Realm**:
   - Click on the dropdown in the top-left corner (next to "Master")
   - Select "Create Realm"
   - Click "Browse" and select `keycloak-realm-config.json`
   - Click "Create"

4. **Update Client Secret** (if needed):
   - Go to "Clients" → "markenx-service"
   - Navigate to "Credentials" tab
   - Copy the "Client Secret" and update your `.env` file if using client authentication

### Option 2: Manual Configuration

If you prefer to configure manually:

1. **Create Realm**:
   - Realm name: `markenx`
   - Enable: ON

2. **Create Roles**:
   - Go to "Realm roles" → "Create role"
   - Create two roles:
     - `ADMIN` (for administrators)
     - `STUDENT` (for students)

3. **Create Admin User**:
   - Go to "Users" → "Add user"
   - Username: `admin@markenx.com`
   - Email: `admin@markenx.com`
   - Email verified: ON
   - First name: Admin
   - Last name: User
   - Click "Create"
   - Go to "Credentials" tab → "Set password"
   - Password: `admin123` (temporary: OFF)
   - Go to "Role mapping" tab → "Assign role"
   - Select "ADMIN" role

4. **Create Clients**:

   **Backend Service Client** (`markenx-service`):
   - Client ID: `markenx-service`
   - Client Protocol: openid-connect
   - Access Type: bearer-only
   - Valid Redirect URIs: `http://localhost:8080/*`

   **Unity Game Client** (`markenx-unity-client`):
   - Client ID: `markenx-unity-client`
   - Client Protocol: openid-connect
   - Access Type: public
   - Valid Redirect URIs: `http://localhost:*`, `https://*.markenx.com/*`
   - Web Origins: `http://localhost:*`, `https://*.markenx.com`
   - Direct Access Grants: ON

   **Admin Web Client** (`markenx-admin-web`):
   - Client ID: `markenx-admin-web`
   - Client Protocol: openid-connect
   - Access Type: public
   - Valid Redirect URIs: `http://localhost:3000/*`, `https://admin.markenx.com/*`
   - Web Origins: `http://localhost:3000`, `https://admin.markenx.com`
   - Direct Access Grants: ON

## Environment Variables

Update your `.env` file with:

```env
# Keycloak Admin Credentials
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=your_secure_password_here

# MySQL for Keycloak
MYSQL_KEYCLOAK_USER=keycloak
MYSQL_KEYCLOAK_PASSWORD=keycloak_password
```

## Testing Authentication

### Get Access Token (Direct Grant)

```bash
curl -X POST "http://localhost:7080/realms/markenx/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=markenx-unity-client" \
  -d "grant_type=password" \
  -d "username=admin@markenx.com" \
  -d "password=admin123"
```

Response:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cC...",
  "token_type": "Bearer"
}
```

### Use Token in API Requests

```bash
curl -X POST "http://localhost:8080/api/markenx/admin/students" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123"
  }'
```

## Role Mapping

- **ADMIN**: Full access to all endpoints
  - Create/manage students
  - Create/manage game scenarios
  - View all statistics
  
- **STUDENT**: Limited access
  - View assigned tasks
  - Submit game attempts
  - View personal statistics

## Password Requirements

Default password policy (can be adjusted in Keycloak):
- Minimum length: 8 characters
- Must contain at least one uppercase letter
- Must contain at least one number

## First Login Password Change

Students created via the API will have `requiredActions: ["UPDATE_PASSWORD"]` set automatically, forcing them to change their password on first login.

To test this flow:
1. Create a student via API with `temporary=true`
2. Student logs in with initial password
3. Keycloak redirects to password change form
4. Student sets new password
5. Access is granted

## Troubleshooting

### Keycloak not starting
- Check MySQL is running: `docker ps | grep mysql`
- Check logs: `docker logs keycloak`
- Verify `.env` file has correct credentials

### Cannot obtain token
- Verify realm name is `markenx`
- Check client ID matches configuration
- Ensure user has correct roles assigned
- Check user credentials are correct

### 401 Unauthorized on API calls
- Verify token is not expired (default: 5 minutes)
- Check `Authorization: Bearer <token>` header format
- Ensure user has required role (ADMIN/STUDENT)
- Check issuer-uri in `application-dev.properties`

## Production Considerations

For production deployment:

1. **Use HTTPS** for all Keycloak endpoints
2. **Change default admin password** immediately
3. **Configure SMTP** for email verification and password reset
4. **Enable SSL** for database connections
5. **Set up realm exports** for backup/disaster recovery
6. **Configure session timeouts** appropriately
7. **Enable event logging** and monitoring
8. **Use client secrets** for confidential clients (not public)

## References

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [JWT.io](https://jwt.io/) - Debug JWT tokens
