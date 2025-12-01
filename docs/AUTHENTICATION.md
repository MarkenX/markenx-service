# Authentication & Authorization Guide

## Overview

This service uses **Keycloak** as the authentication provider with **JWT tokens** for authorization. The backend **does NOT handle login directly** - Keycloak manages all authentication flows.

## 🔐 How It Works

### Architecture

```
┌─────────────┐         ┌──────────────┐         ┌──────────────┐
│   Unity     │  ────>  │   Keycloak   │  ────>  │   Backend    │
│   Game      │         │   (Auth)     │         │   API        │
└─────────────┘         └──────────────┘         └──────────────┘
     Client              Identity Provider         Resource Server
```

### Authentication Flow (OAuth2/OIDC)

1. **Student opens Unity game**
2. **Game redirects to Keycloak** login page (hosted at `http://localhost:8081/realms/markenx-realm`)
3. **Student enters credentials** in Keycloak UI
4. **Keycloak validates** email/password
5. **Keycloak issues JWT token** (if credentials valid)
6. **Game receives token** and stores it
7. **Game sends requests** to backend with token in `Authorization` header:
   ```
   Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
8. **Backend validates token** using Keycloak's public key
9. **Backend extracts roles** (STUDENT/ADMIN) from token
10. **Backend grants/denies access** based on role

## 🚫 What You DON'T Need

- ❌ **No `/login` endpoint** - Keycloak handles this
- ❌ **No password validation** in backend - Keycloak does it
- ❌ **No session management** - JWT is stateless
- ❌ **No token generation** - Keycloak issues tokens

## ✅ What You DO Need

### 1. Admin Endpoints (Require ADMIN role)

```
POST   /api/markenx/admin/students          Create student
GET    /api/markenx/admin/students          List all students (paginated)
GET    /api/markenx/admin/students/{id}     Get student by ID
PUT    /api/markenx/admin/students/{id}     Update student
DELETE /api/markenx/admin/students/{id}     Delete student
```

### 2. Student Endpoints (Require STUDENT or ADMIN role)

```
GET    /api/markenx/students/me                      Get own profile
GET    /api/markenx/students/{id}/tasks              Get assigned tasks
```

### 3. Public Endpoints (No authentication)

Currently none - all endpoints require authentication.

## 📋 API Usage Examples

### Unity Game - Login Flow

```csharp
// 1. Redirect to Keycloak login
string keycloakUrl = "http://localhost:8081/realms/markenx-realm/protocol/openid-connect/auth";
string clientId = "unity-game-client";
string redirectUri = "http://localhost:3000/callback";
string loginUrl = $"{keycloakUrl}?client_id={clientId}&redirect_uri={redirectUri}&response_type=code";

// Open browser or embedded webview
Application.OpenURL(loginUrl);

// 2. After login, Keycloak redirects with authorization code
// Exchange code for token
string tokenUrl = "http://localhost:8081/realms/markenx-realm/protocol/openid-connect/token";
var tokenRequest = new UnityWebRequest(tokenUrl, "POST");
tokenRequest.SetRequestHeader("Content-Type", "application/x-www-form-urlencoded");
string body = $"grant_type=authorization_code&code={authCode}&client_id={clientId}&redirect_uri={redirectUri}";
tokenRequest.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));

// 3. Store received token
string jwtToken = jsonResponse["access_token"];
PlayerPrefs.SetString("jwt_token", jwtToken);
```

### Unity Game - API Calls

```csharp
// Get student profile
string token = PlayerPrefs.GetString("jwt_token");
UnityWebRequest request = UnityWebRequest.Get("http://localhost:8080/api/markenx/students/me");
request.SetRequestHeader("Authorization", $"Bearer {token}");

yield return request.SendWebRequest();

if (request.result == UnityWebRequest.Result.Success) {
    string json = request.downloadHandler.text;
    StudentProfile profile = JsonUtility.FromJson<StudentProfile>(json);
    Debug.Log($"Welcome, {profile.firstName}!");
}
```

### cURL Examples

**Get student profile (as student):**
```bash
curl -X GET http://localhost:8080/api/markenx/students/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**List all students (as admin):**
```bash
curl -X GET "http://localhost:8080/api/markenx/admin/students?page=0&size=10" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Create student (as admin):**
```bash
curl -X POST http://localhost:8080/api/markenx/admin/students \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@student.com",
    "password": "initialPass123"
  }'
```

**Update student (as admin):**
```bash
curl -X PUT http://localhost:8080/api/markenx/admin/students/1 \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@student.com"
  }'
```

**Delete student (as admin):**
```bash
curl -X DELETE http://localhost:8080/api/markenx/admin/students/1 \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

## 🔑 Getting a Token for Testing

### Option 1: Using Postman/cURL (Direct Password Grant - For Testing Only)

```bash
curl -X POST http://localhost:8081/realms/markenx-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=markenx-service" \
  -d "username=student@example.com" \
  -d "password=password123"
```

Response:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 300
}
```

### Option 2: Using Keycloak Admin (For ADMIN token)

```bash
curl -X POST http://localhost:8081/realms/markenx-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" \
  -d "username=admin" \
  -d "password=admin123"
```

## 🛡️ Security Configuration

### Current Setup

File: `src/main/java/com/udla/markenx/infrastructure/config/SecurityConfiguration.java`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/markenx/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/markenx/students/**").hasAnyRole("STUDENT", "ADMIN")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );
    return http.build();
  }
}
```

### Role Mapping

- **ADMIN**: Can access all endpoints
- **STUDENT**: Can access own profile and assigned tasks

JWT roles are automatically extracted from the token's `realm_access.roles` claim.

## 🧪 Testing Authentication

### 1. Start Services

```bash
# Start Keycloak + MySQL
docker compose -f docker-compose.dev.yml up -d

# Start Backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 2. Verify Keycloak

Visit: http://localhost:8081

- Realm: `markenx-realm` (auto-created)
- Admin user: `admin` / `admin123` (auto-created)
- Test student: `student@example.com` / `student123` (created via API)

### 3. Get Token

```bash
# Admin token
curl -X POST http://localhost:8081/realms/markenx-realm/protocol/openid-connect/token \
  -d "grant_type=password" \
  -d "client_id=admin-cli" \
  -d "username=admin" \
  -d "password=admin123" \
  | jq -r '.access_token'
```

### 4. Test Endpoint

```bash
TOKEN="paste_token_here"

curl -X GET http://localhost:8080/api/markenx/admin/students \
  -H "Authorization: Bearer $TOKEN"
```

## 🚨 Common Issues

### 1. "401 Unauthorized"
- **Cause**: Missing or invalid token
- **Fix**: Ensure token is in `Authorization: Bearer <token>` header

### 2. "403 Forbidden"
- **Cause**: User doesn't have required role
- **Fix**: Verify user has STUDENT or ADMIN role in Keycloak

### 3. "Token expired"
- **Cause**: JWT tokens expire (default 5 minutes)
- **Fix**: Request a new token

### 4. "Invalid signature"
- **Cause**: Backend can't verify token (Keycloak not running)
- **Fix**: Ensure Keycloak is running and accessible

## 📚 Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [OAuth 2.0 RFC](https://datatracker.ietf.org/doc/html/rfc6749)
- [JWT.io - Decode tokens](https://jwt.io)
- [Spring Security OAuth2](https://spring.io/projects/spring-security-oauth)

## 🎯 Next Steps

1. **For Unity Integration**: Implement OAuth2 flow using UnityWebRequest
2. **For Password Change**: Redirect user to Keycloak account console
3. **For Logout**: Clear stored token and redirect to Keycloak logout endpoint
4. **For Token Refresh**: Implement refresh token flow (see Keycloak docs)
