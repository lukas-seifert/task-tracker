# Task Tracker

A backend service for managing tasks, including a small static frontend.
This project demonstrates clean REST API design, persistent data storage, containerized local development, and a minimal UI built with JavaScript.

## Tech Stack
- Java 21 · Spring Boot 3
- Spring Web · Spring Data JPA · PostgreSQL
- Docker Compose for local development
- Gradle
- JUnit & Mockito
- Simple HTML/JS frontend

## Purpose
This project serves as a compact and extensible backend example, focusing on:
- REST API design with DTOs and validation
- Domain modeling and persistence with Spring Data JPA
- Clear controller/service/repository architecture
- Practical deployment setup using Docker 
- Optional UI for interacting with the API

## Running Locally

### Start PostgreSQL using Docker

Ensure sure your `docker-compose.yml` is in the project root, then run:
```bash
docker compose up -d
```
This starts a PostgreSQL instance configured for the application.

### Start the Spring Boot application

Run the backend locally:
```bash
./gradlew bootRun
```

### Application URLs

Once running:
- Frontend: http://localhost:8080/
- API root: http://localhost:8080/api/tasks
- Swagger UI: http://localhost:8080/swagger-ui/index.html

The frontend is served from `src/main/resources/static` and interacts directly with the REST API.

## Stopping the application

### Stop PostgreSQL (Docker)

```bash
docker compose down
```

### Stop the Spring Boot application

Press `Ctrl + C` in the terminal where `./gradlew bootRun` is running.

## Running Tests
```bash
./gradlew test
```

## API Overview

### List tasks (with pagination & filters)

`GET /api/tasks`

Query parameters:
- `page` (default: 0)
- `size` (default: 10)
- `sort` (e.g. `sort=dueDate,asc`)
- `status` (optional, e.g. `OPEN`, `IN_PROGRESS`, `DONE`)
- `priority` (optional, e.g. `LOW`, `MEDIUM`, `HIGH`)

### Create a task
`POST /api/tasks`

### Get a task by id
`GET /api/tasks/{id}`

### Update a task
`PUT /api/tasks/{id}`

### Delete a task
`DELETE /api/tasks/{id}`

## Code Style & Formatting

This project enforces consistent code formatting using the Spotless Gradle plugin.
To automatically format the project, run:

```bash
./gradlew spotlessApply
```

To verify formatting:
```bash
./gradlew spotlessCheck
```

The formatting rules are based on the Eclipse formatter profile in `config/format.xml`.