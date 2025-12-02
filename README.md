# Task Tracker

A backend service for managing tasks.  
This project demonstrates clean REST API design, persistent data storage, and containerized local development.

## Tech Stack
- Java 21 / Spring Boot
- Spring Web · Spring Data JPA · PostgreSQL
- Docker Compose for local development
- Gradle
- JUnit & Mockito for testing

## Purpose
This project serves as a compact and extensible backend example, focusing on:
- Domain modeling and persistence
- Clear separation of controller/service/repository layers
- Practical local deployment using Docker
- REST best practices and DTO-based API design

## Running Locally

### Start PostgreSQL using Docker

Make sure your `docker-compose.yml` is in the project root, then start the database:
```bash
docker compose up -d
```

### Start the application

Run the Spring Boot application locally:
```bash
./gradlew bootRun
```

The API will be available at: http://localhost:8080/api/tasks

## Stopping the application

### Stop PostgreSQL (Docker)

```bash
docker compose down
```
This stops and removes the database container.

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

## API Documentation

Once the application is running, the OpenAPI/Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html