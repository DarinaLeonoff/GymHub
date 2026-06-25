# GymHub

Backend platform for managing fitness clubs, memberships, trainers and training sessions.

---

## Project Overview
For development notes and learning progress, see DEVLOG.md (Russian).
### Goal

GymHub is a backend platform designed to simplify fitness club management by providing a centralized system for handling memberships, training sessions, trainers, and client interactions.

The project aims to model a real-world business domain while applying modern backend development practices, including secure authentication, database versioning, automated testing, and scalable architecture.

### Key Features

- [X] User registration
- [X] Authentication and authorization
- [ ] Gym management
- [ ] Membership management
- [ ] Training scheduling
- [ ] Trainer management
- [ ] Notifications

---

## Architecture

### Architecture Style

Modular monolithic application built with Spring Boot.

The application is organized into feature-based modules with clear separation of responsibilities between controllers, services, repositories, and domain entities.

The architecture is designed to allow future extraction of independent services if required.

### Main Modules
| Module | Responsibility |
|----------|----------|
| Auth | Registration, authentication and JWT token management |
| User | User profiles and account management |
| Gym | Gym information and staff management |
| Membership | Membership plans and subscriptions |
| Training | Training sessions and attendance management |
| Notification | User notifications and events (planned) |

### Domain Model

Core entities:

- User
- Gym
- Membership
- MembershipPlan
- TrainingSession
- TrainingRegistration

---

## Tech Stack

### Backend

- Java 21
- Spring Boot

### Database

- PostgreSQL
- Flyway

### Security

- Spring Security
- JWT

### Testing

- JUnit 5
- Mockito
- DataJpaTest
- Testconteiners

### Infrastructure

- Docker (planned)
- GitHub Actions (planned)

---

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Installation

### Prerequisites

- Java 21
- Maven 3.9+
- Docker Desktop (optional)
- PostgreSQL 17+

### Environment Variables

Create .env file from .env.example:
```
cp .env.example .env
```

Example:

JWT_SECRET=your-jwt-secret
DB_USERNAME=postgres
DB_PASSWORD=postgres

### Start PostgreSQL

If Docker Compose is configured:

docker compose up -d

### Run Application

mvn spring-boot:run

---
## Authentication

The application uses JWT authentication.

Authentication flow:

1. Register user 
2. Login using credentials 
3. Receive JWT token 
4. Send token in Authorization header

Example:

Authorization: Bearer <jwt-token>

--- 

## Database

### Migrations

Database schema is managed with Flyway.

Migration scripts are located in:

src/main/resources/db/migration

### Schema

TODO

---

## Testing

### Run Tests

```bash
mvn test
```

### Coverage

TODO

---

## Author

Daria

Java Backend Developer (in progress 🚀)
