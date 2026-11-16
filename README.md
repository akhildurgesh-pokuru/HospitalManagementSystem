
# Hospital Management System — Spring Boot & JWT

## Project Overview

The **Hospital Management System** is a RESTful backend application developed using Java, Spring Boot, Spring Security, JWT Authentication, Spring Data JPA, and MySQL.

This project provides a secure way to manage patient records. Users can register, log in, and perform CRUD (Create, Read, Update, Delete) operations on patient information after successful authentication.

The main objective of this project is to understand **backend development, REST API implementation, JWT-based authentication, Spring Security, database integration, and layered architecture**.

## Technologies Used

- **Java 17** — Backend programming language
- **Spring Boot** — Application development framework
- **Spring Web** — RESTful API development
- **Spring Security** — Authentication and API security
- **JWT (JSON Web Token)** — Token-based authentication
- **Spring Data JPA / Hibernate** — Database operations
- **MySQL** — Relational database
- **Maven** — Dependency management
- **Lombok** — Reduces boilerplate code
- **Bean Validation** — Request data validation
- **JUnit 5 and Mockito** — Unit testing
- **Postman** — API testing

## Features

### 1. User Authentication
- Register new users with name, email, and password.
- Store passwords securely using BCrypt hashing.
- Authenticate users using their email and password.
- Generate JWT tokens after successful login.
- Validate JWT tokens for protected API requests.

### 2. Patient Management
- Add new patient records.
- View all registered patients.
- Retrieve patient details using patient ID.
- Update existing patient information.
- Delete patient records.

### 3. Application Security
- JWT-based authentication.
- Stateless session management.
- Public registration and login endpoints.
- Protected patient management endpoints.
- Password hashing using BCrypt.
- JWT authentication filter to validate incoming tokens.

## Project Architecture

The project follows a layered architecture:

**Client → Controller → Service → Repository → MySQL Database**

- **Controller Layer:** Handles incoming HTTP requests and returns responses.
- **Service Layer:** Contains business logic.
- **Repository Layer:** Performs database operations using Spring Data JPA.
- **Entity Layer:** Maps Java classes to database tables.
- **DTO Layer:** Transfers data between application layers.
- **Security Layer:** Handles JWT authentication and API authorization.

## Project Structure

```text
src/
├── main/
│   ├── java/hospital/ManagementSystem/
│   │   ├── Config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtService.java
│   │   ├── Controller/
│   │   │   ├── AuthController.java
│   │   │   └── PatientController.java
│   │   ├── Dto/
│   │   │   ├── AuthRequest.java
│   │   │   ├── AuthResponse.java
│   │   │   └── PatientDto.java
│   │   ├── Entity/
│   │   │   ├── User.java
│   │   │   ├── Role.java
│   │   │   └── Patient.java
│   │   ├── Repository/
│   │   │   ├── UserRepository.java
│   │   │   └── PatientRepository.java
│   │   ├── Service/
│   │   │   ├── AuthService.java
│   │   │   └── PatientService.java
│   │   └── ManagementSystemApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/hospital/ManagementSystem/
        └── ...
```

## REST API Endpoints

### Authentication APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate and generate JWT |

### Patient APIs (JWT Required)

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/patients` | Add a new patient |
| GET | `/api/patients` | Retrieve all patients |
| GET | `/api/patients/{id}` | Retrieve a patient by ID |
| PUT | `/api/patients/{id}` | Update patient details |
| DELETE | `/api/patients/{id}` | Delete a patient |

## Sample API Requests

### Register User

**POST** `/api/auth/register`

```json
{
  "name": "Admin User",
  "email": "admin@example.com",
  "password": "StrongPassword123!"
}
```

### Login User

**POST** `/api/auth/login`

```json
{
  "email": "admin@example.com",
  "password": "StrongPassword123!"
}
```

Sample response:

```json
{
  "token": "<your-jwt-token>",
  "message": "Login successful"
}
```

### Add Patient

**POST** `/api/patients`

```json
{
  "name": "Rahul Kumar",
  "age": 27,
  "gender": "Male",
  "disease": "Fever",
  "address": "Chennai",
  "phoneNumber": "9876543210"
}
```

Protected endpoints require the following HTTP header:

```text
Authorization: Bearer <your-jwt-token>
```

## Database Design

The application uses two primary database tables.

### Users Table
Stores registered user information.

Fields: `id`, `name`, `email`, `password`, `role`

### Patients Table
Stores patient information.

Fields: `id`, `name`, `age`, `gender`, `disease`, `address`, `phone_number`

## How JWT Authentication Works

1. A user registers through the registration API.
2. The password is hashed before being saved in MySQL.
3. The user submits login credentials.
4. Spring Security verifies the credentials.
5. The application generates a JWT token after successful login.
6. The client sends the token in the `Authorization` header.
7. The JWT authentication filter validates the token.
8. If authentication succeeds, the client can access protected patient APIs.

## How to Run the Project

### Prerequisites

- Java 17 or compatible JDK
- Maven
- MySQL Server
- IntelliJ IDEA, Eclipse, or VS Code
- Postman

### Setup

1. Clone the repository.
2. Create a MySQL database named `hospital_db`.
3. Configure your database connection in `application.properties`.
4. Configure a strong JWT signing secret through an environment variable.
5. Run `ManagementSystemApplication.java` or execute:

```bash
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

Ensure that database passwords and JWT secrets are not committed to GitHub.

## Testing

The project includes JUnit 5 and Mockito tests for the service and controller layers.

Run tests using:

```bash
mvn test
```

APIs can also be tested manually using Postman.

Recommended testing sequence:

1. Register a user.
2. Log in using the registered credentials.
3. Copy the JWT token.
4. Add the JWT token as a Bearer token in Postman.
5. Test patient Create, Read, Update, and Delete operations.

## Learning Outcomes

Through this project, I gained practical experience in:

- Developing RESTful APIs using Spring Boot.
- Implementing JWT authentication.
- Securing endpoints with Spring Security.
- Applying Controller–Service–Repository architecture.
- Performing CRUD operations with Spring Data JPA.
- Integrating MySQL with Spring Boot.
- Handling DTOs, entities, and request validation.
- Writing unit tests using JUnit 5 and Mockito.
- Testing secured APIs using Postman.

## Future Enhancements

- Doctor management
- Appointment scheduling
- Hospital billing
- Role-based access control
- Patient search and pagination
- Swagger/OpenAPI documentation
- Frontend integration using React or Angular

## Conclusion

The Hospital Management System demonstrates the implementation of secure RESTful APIs using Spring Boot and JWT authentication. It is a backend mini-project designed to strengthen practical knowledge of Java development, Spring Security, database operations, and REST API testing.
