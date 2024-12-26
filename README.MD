## Docker Compose Setup for **Book the House**

---

### *Services Overview*

### **1. bth-ui (Book the House UI)**
- **Purpose**: The main Spring Boot UI application.
- **Image**: `bth-ui:latest`
- **Environment Variables**:
    - `SPRING_PROFILES_ACTIVE`: Specifies the active Spring profile (`docker` in this case).
    - `GOOGLE_CLIENT_ID` & `GOOGLE_CLIENT_SECRET`: Required for Google OAuth2 authentication.
    - `GITHUB_CLIENT_ID` & `GITHUB_CLIENT_SECRET`: Required for GitHub OAuth2 authentication.
---

### **2. Redis**
- **Purpose**: Provides caching capabilities for the application.
- **Image**: `redis:latest`

---

### **3. Redis Commander**
- **Purpose**: A lightweight web-based Redis management UI.
- **Image**: `rediscommander/redis-commander`
- **Environment Variables**:
    - `PORT`: Exposes the Redis Commander UI on port `8091`.
    - `REDIS_HOSTS`: Configured to connect to the `bth-redis` instance (`redis:bth-redis:6379`).
