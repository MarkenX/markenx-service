# Postman Collection Setup Guide

## 🚀 Quick Start

### 1. Import the Collection

1. Open Postman
2. Click **Import** button (top left)
3. Drag and drop `MarkenX-API.postman_collection.json`
4. Collection appears in left sidebar as "MarkenX API - Auto Refresh"

### 2. Test It Immediately

**No manual token setup needed!** Just:

1. Open any request (e.g., "Get All Students")
2. Click **Send**
3. ✅ Token auto-fetches and request succeeds!

---

## 🔍 How Auto-Refresh Works

### Pre-Request Script (Collection Level)

The collection has a pre-request script that runs **before every request**:

1. **Checks token expiry** (stored in collection variables)
2. **If expired or missing** (30-second buffer):
   - Fetches new token from Keycloak
   - Saves to collection variables
3. **If still valid**:
   - Uses existing token
   - Logs time remaining

### View the Magic Happening

**Open Postman Console** to see auto-refresh in action:
- Press `Ctrl + Alt + C` (Windows) or `Cmd + Alt + C` (Mac)
- Or: **View → Show Postman Console**

**Console output:**
```
🔄 Refreshing token...
✅ Token refreshed! Expires in 300 seconds
```

Or if token is still valid:
```
✅ Token valid for 245 seconds
```

---

## 📋 Available Requests

### Admin - Students Folder

1. **Get All Students**
   - `GET /admin/students?page=0&size=10`
   - Paginated list of students
   
2. **Get Student by ID**
   - `GET /admin/students/{id}`
   - Single student details

3. **Create Student**
   - `POST /admin/students`
   - Body example:
   ```json
   {
     "email": "newstudent@example.com",
     "firstName": "John",
     "lastName": "Doe",
     "password": "SecurePass123!",
     "enrollmentCode": "STU2025A"
   }
   ```

4. **Update Student**
   - `PUT /admin/students/{id}`
   - No password field for updates

5. **Delete Student**
   - `DELETE /admin/students/{id}`

### Authentication Folder

- **Get Token (Manual)**: Only for testing - auto-refresh handles this automatically

---

## 🔑 Credentials

### Admin User
- **Username:** `admin@markenx.com`
- **Password:** `admin123`
- **Role:** ADMIN

### Student User (for future endpoints)
- **Username:** `student@markenx.com`
- **Password:** `student123`
- **Role:** STUDENT

---

## ⚙️ How Authorization Works

### Collection-Level Auth
- **Type:** Bearer Token
- **Token:** `{{access_token}}` (variable)
- All requests inherit this automatically

### Request-Level Auth
- Each request uses **"Inherit auth from parent"**
- No need to configure per-request

---

## 🛠️ Customization

### Change Credentials

Edit the **Pre-request Script** at collection level:

1. Click collection name → **Pre-request Script** tab
2. Find these lines:
   ```javascript
   { key: 'username', value: 'admin@markenx.com' },
   { key: 'password', value: 'admin123' },
   ```
3. Change to different user (e.g., `student@markenx.com` / `student123`)

### Adjust Token Refresh Buffer

Change the 30-second buffer:
```javascript
if (!currentToken || !tokenExpiry || now >= (tokenExpiry - 30)) {
//                                                          ^^^ change this
```

---

## 🐛 Troubleshooting

### Issue: Still Getting 401

**Check Console:**
1. Open Console (`Ctrl+Alt+C`)
2. Send request
3. Look for error messages

**Common fixes:**
- ✅ Keycloak running? `docker ps | grep keycloak`
- ✅ Spring Boot running? `docker ps | grep service`
- ✅ Correct credentials in pre-request script?

### Issue: "Cannot read property 'access_token'"

**Fix:** Collection variables not initialized
1. Click Collection → **Variables** tab
2. Verify these exist (leave values empty):
   - `access_token`
   - `token_expiry`
   - `refresh_token`

### Issue: Token Refresh Fails

**Console shows:** `❌ Token refresh failed`

**Debug:**
1. Test Keycloak manually:
   ```bash
   curl -X POST http://localhost:7080/realms/markenx/protocol/openid-connect/token \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=password" \
     -d "client_id=markenx-admin-web" \
     -d "username=admin@markenx.com" \
     -d "password=admin123"
   ```
2. If this fails, Keycloak isn't running or credentials are wrong

---

## 📊 Collection Variables

View/edit at: **Collection → Variables** tab

| Variable | Purpose | Auto-Managed |
|----------|---------|--------------|
| `access_token` | Current JWT token | ✅ |
| `token_expiry` | Unix timestamp when token expires | ✅ |
| `refresh_token` | OAuth2 refresh token (not currently used) | ✅ |

---

## 🎯 Next Steps

1. **Test all CRUD operations:**
   - Create a student
   - Get student by ID
   - Update student
   - Delete student

2. **Leave Postman running:**
   - Send requests 6+ minutes apart
   - Watch auto-refresh kick in

3. **Check Console logs:**
   - See exactly when tokens refresh
   - Monitor token expiry countdowns

---

## 💡 Tips

- **No manual token copying needed** - auto-refresh handles everything
- **Tokens expire every 5 minutes** - script fetches new ones automatically
- **30-second buffer** - refreshes before actual expiration to avoid race conditions
- **Console is your friend** - always have it open when testing
- **Works across Postman sessions** - variables persist when you close/reopen Postman

---

**You're all set! 🎉**

Just import the collection and start testing. The auto-refresh will handle authentication transparently.
