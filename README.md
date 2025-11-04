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

The API will be available at: http://localhost:8080

## Running Tests
```bash
./gradlew test
```