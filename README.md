# Job Application Tracker API

A Spring Boot application for managing job applications, companies, interviews, and the overall job search process.

The project was built to practice developing a backend application with Spring Boot, Spring Security, JWT authentication, PostgreSQL, JPA/Hibernate, validation, and Docker.

## Features

* User registration and authentication
* JWT-based authentication
* HTTP-only authentication cookies
* Password hashing
* User-specific data and ownership checks
* Job application management
* Company information management
* Interview management
* Application and interview statuses
* Request validation
* Global exception handling
* Application statistics
* PostgreSQL persistence
* Docker and Docker Compose support

## Technologies

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Docker
* Docker Compose

## Project Structure

The application follows a layered architecture:

```text
src/main/java/
└── ...
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    ├── security
    └── exception
```

Controllers handle HTTP requests, services contain application logic, repositories communicate with the database, and DTOs are used to transfer data between the API and clients.

## Authentication

Authentication is implemented using Spring Security and JWT.

Passwords are stored using password hashing rather than plain text. After authentication, the JWT is stored in an HTTP-only cookie and is used to authenticate protected requests.

User ownership checks are also applied to application data so that users cannot access or modify another user's records.

## Database

The application uses PostgreSQL with Spring Data JPA and Hibernate.

The main domain objects include users, job applications, companies, and interviews, with relationships between them managed through JPA.

## Running the Application

### Using Docker Compose

Clone the repository:

```bash
git clone https://github.com/CCHQSA/Job-Application-Tracker-API.git
cd Job-Application-Tracker-API
```

Start the application:

```bash
docker compose up --build
```

The application and PostgreSQL database will be started as Docker containers.

### Running with Maven

Make sure Java and PostgreSQL are installed and configured.

Then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Configuration

Application-specific configuration should be provided through environment variables or the project's configuration files.

Typical configuration includes:

* PostgreSQL connection
* Database credentials
* JWT secret
* Application port

Do not commit real secrets or production credentials to the repository.

## What I Practiced

This project helped me practice:

* Building REST APIs with Spring Boot
* Layered application architecture
* Spring Security
* JWT authentication
* Authentication cookies
* Password hashing
* JPA/Hibernate relationships
* PostgreSQL
* DTO-based application design
* Validation
* Exception handling
* Docker and Docker Compose
* Designing user-specific data access
* Building backend features from requirements

## Future Improvements

Possible future improvements include:

* More comprehensive automated tests
* API documentation with OpenAPI/Swagger
* CI/CD pipeline
* Improved deployment configuration
* Additional statistics and filtering
* Email or notification integration

## Author

Mykola Lotockiy

GitHub: https://github.com/CCHQSA
