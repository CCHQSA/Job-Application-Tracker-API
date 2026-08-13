# Job Application Tracker

A web application for managing job applications and interviews in one place.

The main goal of the project is to make the job search process easier to organize. Instead of keeping application details, interview dates, and notes in different places, users can manage everything from one application.

## Features

### Authentication

- User registration and login
- JWT-based authentication
- HTTP-only authentication cookies
- Secure password hashing
- Protected user data

### Job Applications

Users can create and manage their job applications.

Each application can contain the company, position, location, salary range, currency, application date, job URL, description, and personal notes.

Applications can be moved through different stages:

- Applied
- Screening
- Interview
- Technical Interview
- Offer
- Rejected
- Withdrawn

Users can add, edit, delete, and update the status of their applications.

### Interviews

Each job application can have multiple interviews.

Users can keep track of:

- Interview type
- Date and time
- Interviewer
- Location
- Meeting URL
- Notes
- Interview status

Interview statuses include:

- Scheduled
- Rescheduled
- Completed
- Cancelled

Users can create, edit, delete, reschedule, and update interviews.

### Dashboard

The dashboard provides a quick overview of the current job search.

It shows information such as:

- Total applications
- Active applications
- Scheduled interviews
- Offers received
- Recent applications
- Upcoming interviews

### Statistics

The statistics page gives a more detailed view of the user's job search.

It includes:

- Applications by status
- Applications over time
- Applications by location
- Interview rate
- Offer rate
- Rejection rate
- Interview statistics

### Account Settings

Users can manage their account from the settings page.

- Update profile information
- Change password
- Delete account

### Validation and Error Handling

The application validates user input before processing it and uses global exception handling to provide consistent error responses.

User ownership is also checked when working with applications and interviews so that users can only modify their own data.

## Technologies

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Thymeleaf
- Jakarta Bean Validation
- Lombok
- Maven
- HTML
- CSS

## Purpose

This is a personal portfolio project built to practice Java and Spring Boot development.

It combines backend development, database management, authentication, validation, and frontend development in one project.
