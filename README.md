# Tan Khoa Learning Center Management Backend

A robust, secure, and maintainable backend application for Tan Khoa Learning Center built using **Spring Boot 3.4+**, **Java 21**, and **Spring Data MongoDB**.

---

## Technical Stack & Architecture

- **Core Framework**: Spring Boot 3.4.4
- **Database**: MongoDB (via Spring Data MongoDB & MongoRepository)
- **Security**: Spring Security with Stateless JWT Authentication & Custom Authorization
- **Documentation**: Springdoc OpenAPI (Swagger UI)
- **Utilities**: Project Lombok, Apache POI (Excel reporting)
- **OS/Java Compatibility**: Fully configured for JDK 21+ / JDK 24 compilation compatibility

### Folder & Package Directory Structure
The codebase follows a refined technical layered packaging structure to improve modularity and maintainability:
```text
com.example.TanKhoaLearningCenterBE
├── config/              # Global configuration classes (MongoDB auditing, Spring beans)
├── controller/          # REST API endpoints
│   ├── request/         # Input DTOs (Request payloads)
│   └── response/        # Output DTOs (Response payloads)
├── security/            # Security filters (JWT, UserDetails service, Security config)
├── service/             # Business logic interfaces & implementation classes
├── repository/          # Spring Data MongoDB Repository interfaces
├── entity/              # Database document models
├── exception/           # Exception definitions, codes, & GlobalExceptionHandler
├── dto/                 # Shared Data Transfer Objects
├── validators/          # Custom validators & annotation constraints
└── utils/               # Constants, Enums, helpers, and Startup listeners
```

---

## Key Features

1. **MongoDB Database Layer**: Fully migrated relational database schema into document-based mapping. Handles DBRefs (`@DBRef`) for referencing class, course, and student documents.
2. **MongoDB Transaction Management**: Configured with a `MongoTransactionManager` bean to guarantee data consistency and rollback capability on operations annotated with `@Transactional`.
3. **Stateless Security & JWT**: Stateless session management using JWT Bearer tokens. Includes whitelisted URLs for Swagger UI, API docs, and VNPay callbacks.
4. **Single-Admin Registration Service**: Safe admin registration API (`/api/auth/admin/register`) ensuring that only a single administrator account can exist in the system (returning `409 Conflict` if an admin is already registered).
5. **Unified Error Handling**: Intercepts all custom domain and runtime exceptions via a global `@ControllerAdvice` handler. Automatically resolves `@ResponseStatus` status codes and maps errors into a unified `ErrorResponse` DTO.
6. **VNPay Integration**: Integrates VNPay payment gateway. The callback endpoint automatically verifies transactions and performs a same-tab browser redirection back to the frontend student portal.
7. **Developer Ergonomics**: Prints the server port and active Swagger UI endpoints clearly in the terminal at startup. Lombok warnings about superclass equality and infinite recursive `toString()` stack overflows have been resolved.

---

## Configuration & Local Environments

The application uses Spring Boot's configuration import to load local credentials without checking them into git control.

### 1. `application.yml`
Configured to use environment variables with fallback placeholders:
- `MONGODB_URI`: Connection string for MongoDB (default: local instance).
- `JWT_SECRET_KEY`: Signing key for JWT filter.
- `VNPAY_TMN_CODE` / `VNPAY_SECRET_KEY`: VNPay terminal code and secret hash key.
- `CORS_ORIGINS`: Allowed origins for CORS policy.

### 2. `application-local.yml` (Git Ignored)
For local development, create a file named `src/main/resources/application-local.yml`. Spring Boot will automatically detect and load it on startup if present. Put your sensitive testing keys here:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<user>:<password>@cluster0.mongodb.net/LearningCenter?appName=Cluster0

application:
  security:
    jwt:
      secret-key: <your_jwt_signing_key>

vnpay:
  tmn-code: <your_vnpay_tmn_code>
  secret-key: <your_vnpay_secret_key>
```

---

## Getting Started

### Prerequisites
- **Java**: JDK 21 or later
- **Maven**: 3.8+ (or use the provided Maven wrapper `./mvnw`)
- **Database**: Running MongoDB instance (or Atlas Cluster)

### Running Locally

1. **Clone the repository**
2. **Setup credentials**: Populate `src/main/resources/application-local.yml` as described above.
3. **Compile the application**:
   ```bash
   ./mvnw clean compile
   ```
4. **Run the project**:
   ```bash
   ./mvnw spring-boot:run
   ```
5. **Verify startup**:
   Once started, look for the following startup banner in the terminal:
   ```text
   ==========================================================
     Application is running!
     Local Server Port: 8080
     Swagger UI URL:    http://localhost:8080/swagger-ui/index.html
   ==========================================================
   ```

### Running Tests
Execute the unit and context bootstrap tests:
```bash
./mvnw test
```

---

## API Documentation
The API is documented interactively using OpenAPI. Boot up the server locally and visit:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **API Docs (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
