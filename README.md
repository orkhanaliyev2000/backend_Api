# java-backend-template

Production-ready Java 21 + Spring Boot 3.x backend template with PostgreSQL, Flyway, Docker Compose, CI, and observability.

## File tree

```
.
├── .editorconfig
├── .env.example
├── .github
│   ├── CODEOWNERS
│   ├── dependabot.yml
│   └── workflows
│       └── ci.yml
├── .gitignore
├── Dockerfile
├── Makefile
├── README.md
├── build.gradle.kts
├── config
│   └── checkstyle
│       └── checkstyle.xml
├── docker-compose.prod.yml
├── docker-compose.yml
├── gradle
│   └── wrapper
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── javabackendtemplate
│   │   │               ├── JavaBackendTemplateApplication.java
│   │   │               ├── api
│   │   │               │   ├── controller
│   │   │               │   │   ├── ApiExceptionHandler.java
│   │   │               │   │   └── UserController.java
│   │   │               │   ├── dto
│   │   │               │   │   ├── CreateUserRequest.java
│   │   │               │   │   ├── UpdateUserRequest.java
│   │   │               │   │   └── UserResponse.java
│   │   │               │   └── mapper
│   │   │               │       └── UserMapper.java
│   │   │               ├── application
│   │   │               │   ├── exception
│   │   │               │   │   └── NotFoundException.java
│   │   │               │   ├── port
│   │   │               │   │   └── UserRepositoryPort.java
│   │   │               │   └── service
│   │   │               │       └── UserService.java
│   │   │               ├── domain
│   │   │               │   └── User.java
│   │   │               └── infrastructure
│   │   │                   ├── config
│   │   │                   │   ├── ClockConfig.java
│   │   │                   │   ├── RequestIdFilter.java
│   │   │                   │   ├── SecurityConfig.java
│   │   │                   │   └── SecurityDisabledConfig.java
│   │   │                   └── repository
│   │   │                       ├── UserJpaRepository.java
│   │   │                       └── UserRepositoryAdapter.java
│   │   └── resources
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── application.yml
│   │       └── db
│   │           └── migration
│   │               └── V1__init.sql
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── javabackendtemplate
│       │               ├── application
│       │               │   └── UserServiceTest.java
│       │               ├── architecture
│       │               │   └── ArchitectureTest.java
│       │               └── integration
│       │                   └── UserIntegrationTest.java
│       └── resources
│           └── application-test.yml
```

## Dev

### One-command dev run (auto Docker Compose)
Spring Boot can auto-start the Docker Compose dependencies when using the `dev` profile.

```
SPRING_PROFILES_ACTIVE=dev gradle bootRun
```

> Note: This repository does not include `gradle-wrapper.jar` because binary files are not supported by the delivery channel.
> If you need the wrapper, run `gradle wrapper --gradle-version 8.14.3` locally to regenerate it and then use `./gradlew`.

### Run from IDE (IntelliJ / Eclipse / VS Code)
If you want to start the app via the IDE **Run** button:

1. Ensure the database is running:
   ```
   make dev-up
   ```
2. Set active profile to `dev`:
   - Environment variable: `SPRING_PROFILES_ACTIVE=dev`, or
   - JVM option: `-Dspring.profiles.active=dev`
3. Use the main class:
   ```
   com.example.javabackendtemplate.JavaBackendTemplateApplication
   ```

### Alternative: using Makefile
```
make dev-up
make run-dev
```

To stop dependencies:
```
make dev-down
```

## Testing

```
gradle test
```

## DB migrations

- Migration files live in `src/main/resources/db/migration/`
- The initial schema is in `V1__init.sql`
- Add new changes via `V2__...`, `V3__...` and never edit applied migrations

## Production

- Docker Compose is disabled in `prod`
- Database configuration comes from environment variables:
  - `DATABASE_URL`
  - `DATABASE_USERNAME`
  - `DATABASE_PASSWORD`

Build and run the Docker image:
```
gradle bootJar

docker build -t java-backend-template:latest .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://db-host:5432/app \
  -e DATABASE_USERNAME=app_user \
  -e DATABASE_PASSWORD=app_pass \
  java-backend-template:latest
```

Optional compose file for prod-like runs (no DB):
```
docker compose -f docker-compose.prod.yml up
```

## Secrets

- Never commit secrets to git.
- Use `.env` locally (see `.env.example`).
- In production, use a secret store (AWS Secrets Manager, Vault, Kubernetes Secrets) and inject via env.

## Observability

- Actuator endpoints enabled: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`
- Liveness/Readiness probes are enabled by default.
- Request ID is propagated via `X-Request-Id` and included in log pattern.

## OpenAPI

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Security

Security is disabled by default to keep the template simple. To enable JWT-based auth:

1. Set `app.security.enabled=true`
2. Provide your OAuth2/JWT configuration in `application-prod.yml` or env

## Troubleshooting

- If Docker Compose does not start automatically, ensure Docker is running.
- If Flyway fails, make sure the DB user has permissions and the DB is reachable.

## Quick checklist

### Run locally
1. `SPRING_PROFILES_ACTIVE=dev gradle bootRun`
2. Open `http://localhost:8080/swagger-ui.html`

### Deploy to prod
1. Build: `gradle bootJar`
2. Build image: `docker build -t java-backend-template:latest .`
3. Run with env secrets injected (no DB container in prod)

## Architecture rules (team agreements)

1. `api` layer depends on `application` and `domain` only.
2. `application` defines ports and use cases; no Spring or database-specific code.
3. `infrastructure` contains adapters (DB, messaging, external APIs) and configuration.
4. Never store secrets in git; use env/secret store.
5. Use Flyway migrations for schema changes; no `ddl-auto` beyond `validate`.
6. Always validate input with `jakarta.validation`.
7. Use `ProblemDetail` for API errors.
8. Keep `docker-compose.yml` for dev only; prod uses external DB.
9. Add tests for every new endpoint (unit + integration).
10. Keep logging structured and include request IDs.
