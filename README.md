# Job Application Tracker

**Live Demo:** https://job-application-tracker-api-jgp0.onrender.com/

A web application for managing job applications and interviews in one place.

The project was built to practice Java and Spring Boot development, including authentication, database persistence, validation, and web application development.

## Features

### Authentication

* User registration and login
* JWT-based authentication
* HTTP-only authentication cookies
* Password hashing
* Protected user data

### Job Applications

Users can create and manage their job applications.

Applications can contain:

* Company
* Position
* Location
* Salary range
* Currency
* Application date
* Job URL
* Description
* Personal notes

Application statuses include:

* Applied
* Screening
* Interview
* Technical Interview
* Offer
* Rejected
* Withdrawn

### Interviews

Each application can have multiple interviews.

Users can manage:

* Interview type
* Date and time
* Interviewer
* Location
* Meeting URL
* Notes
* Interview status

Interview statuses include:

* Scheduled
* Rescheduled
* Completed
* Cancelled

### Dashboard

The dashboard provides an overview of the current job search, including:

* Total applications
* Active applications
* Scheduled interviews
* Offers received
* Recent applications
* Upcoming interviews

### Statistics

The statistics page provides information about:

* Applications by status
* Applications over time
* Applications by location
* Interview rate
* Offer rate
* Rejection rate
* Interview statistics

### Account Settings

Users can:

* Update profile information
* Change their password
* Delete their account

### Validation and Security

* User input validation
* Global exception handling
* User ownership checks for applications and interviews

## Technologies

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Thymeleaf
* Jakarta Bean Validation
* Lombok
* Maven
* HTML
* CSS

## Project Structure

The project is organized into separate components for handling different responsibilities:

```text
src/main/java/
├── controller
├── service
├── repository
├── entity
├── dto
├── security
└── exception
```

## Running the Application

Clone the repository:

```bash
git clone https://github.com/CCHQSA/Job-Application-Tracker-API.git
cd Job-Application-Tracker-API
```

The project includes Docker configuration for running the application.

```bash
docker compose up --build
```

You can also run the application with Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Purpose

This is a personal portfolio project created to practice Java and Spring Boot development and to build a more complete web application.

## Author

Mykola Lotockiy

GitHub: https://github.com/CCHQSA
