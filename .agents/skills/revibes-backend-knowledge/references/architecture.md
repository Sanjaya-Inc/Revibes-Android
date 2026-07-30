# Revibes Backend Architecture & Integration Reference

## 1. Project Structure Overview

```
Revibes-Backend/
├── functions/
│   ├── src/
│   │   ├── index.ts                # Entry point registering Cloud Function `v1`
│   │   ├── constant/               # Global constants (region, db names, currency)
│   │   ├── controllers/            # Controller classes handling domain business logic
│   │   ├── dto/                    # Zod / TypeScript request/response contracts
│   │   ├── handlers/https/         # Express routing handlers & Cloud Function exports
│   │   ├── middlewares/            # Auth, Error handling, and body parser middlewares
│   │   ├── models/                 # Firestore BaseModel subclasses & entities
│   │   └── utils/                  # Firebase, JWT, Decorator, Geolocation, Formatters
```

---

## 2. Core Architectural Design Patterns

### HTTP Handler & Decorator Pattern
- Controllers are decorated with route definitions (`registerRoute`) and error wrapping (`wrapError`).
- Routes are registered dynamically via custom `Routes` class in `handlers/https/route.ts`.
- Responses are formatted uniformly via `AppResponse` (`{ status: "success", data: ... }` or `{ status: "error", message: ... }`).

### Error Handling & Middleware Pipeline
1. **Body Parser / Cors**: `bodyParser` middleware parses JSON & form data.
2. **Auth Middleware**: `authenticate` extracts Bearer JWT token from `Authorization` header, verifies claims, and attaches user context to Express `Request`.
3. **Admin Check**: `adminOnly` enforces role-based access control (`ADMIN` / `SUPER_ADMIN`).
4. **Error Handler**: `errorHandler` catches `AppError` instances and unknown exceptions, transforming them to HTTP status responses.

---

## 3. Revibes Android Integration Guidelines

When creating networking code in Revibes Android (e.g. Ktorfit interfaces, repositories, MVI state):

1. **Base URL Configuration**:
   - Point Ktor client base URL to Firebase Cloud Functions `v1` endpoint: `https://<region>-<project_id>.cloudfunctions.net/v1/`.
2. **Header Mapping**:
   - `Authorization: Bearer <access_token>` required for all protected endpoints.
3. **Data Classes / DTOs**:
   - Match field names exactly with backend response DTOs (`camelCase`).
   - Use `@SerialName` annotations in Kotlin data classes if field names deviate.
4. **Timestamp Mapping**:
   - Backend returns ISO 8601 strings or Firestore Timestamps (`_seconds`, `_nanoseconds`). Format appropriately in Kotlin.
5. **Error Contract**:
   - Backend errors follow:
     ```json
     {
       "status": "error",
       "message": "Error description message",
       "code": 400
     }
     ```
