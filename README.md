# 🩸 Blood Donation Backend API

A highly scalable, production-ready RESTful API built to power the Blood Donation mobile application. 

This backend is built using **Java 17**, **Spring Boot 3**, and **PostgreSQL**. It features robust stateless authentication via **JWT**, password encryption via **BCrypt**, and a custom **6-digit OTP verification** flow.

---

## ✨ Core Features & Security

*   **Stateless Authentication:** Uses JSON Web Tokens (JWT) for secure, scalable session management.
*   **Password Security:** All passwords are mathematically hashed using BCrypt before reaching the database.
*   **OTP Validation:** Custom OTP microservice that generates, stores, and validates 6-digit codes for User Registration and Password Recovery.
*   **Role-Based Access Control (RBAC):** API endpoints are strictly secured and filtered. Admin endpoints are inaccessible to standard `USER` roles.
*   **Live Metrics:** Custom aggregated SQL queries via Spring Data JPA to provide live dashboard analytics (Total Donors, Active Requests, etc.).

---

## 🛠️ Tech Stack & Architecture

*   **Language:** Java 17
*   **Framework:** Spring Boot 3.2
*   **Build Tool:** Maven
*   **Database:** PostgreSQL
*   **ORM:** Hibernate / Spring Data JPA
*   **Validation:** Jakarta Bean Validation
*   **JSON Serialization:** Jackson

### Multi-Tier Architecture
The codebase strictly follows a multi-tier design pattern to ensure maintainability:
1.  **Controllers (`@RestController`):** Handles incoming HTTP requests and routes them.
2.  **Services (`@Service`):** Contains the core business logic, validation, and complex calculations.
3.  **Repositories (`@Repository`):** Interfaces extending `JpaRepository` for seamless, boilerplate-free database operations.
4.  **DTOs (Data Transfer Objects):** Strictly separates database Entities from the JSON models exposed to the mobile app.

---

## 🚀 Setup & Installation Guide

### Prerequisites
*   Java 17 installed
*   Maven installed
*   PostgreSQL installed and running locally on port `5432`

### Step 1: Database Setup
1. Open pgAdmin or your PostgreSQL CLI.
2. Create a new database named `blood_donation_db`.
   *(Note: You do not need to create tables. Hibernate will automatically create them when the server starts).*

### Step 2: Configure Application Properties
Open `src/main/resources/application.properties` and ensure your database credentials match your local setup:

```properties
server.port=8080
server.address=0.0.0.0

spring.datasource.url=jdbc:postgresql://localhost:5432/blood_donation_db
spring.datasource.username=postgres
spring.datasource.password=YourSecurePassword
```
*(The `0.0.0.0` binding allows physical mobile devices on your Wi-Fi network to connect to the server).*

### Step 3: Build and Run
Open a terminal in the root directory and run:
```bash
mvn spring-boot:run
```
The server will start on `http://localhost:8080`.

---

## 📡 Key API Endpoints Overview

### Authentication (`/api/auth`)
*   `POST /register` - Creates a new user profile.
*   `POST /login` - Authenticates via Email OR Phone and returns a JWT token.
*   `POST /forgot-password` - Generates a 6-digit OTP and prints it to the console.
*   `POST /verify-otp` - Validates the OTP against the database and returns a reset token.

### Users & Donors (`/api/users`, `/api/donors`)
*   `GET /profile` - Fetches the securely authenticated user's profile and donation stats.
*   `PUT /profile` - Updates location and blood group details.
*   `GET /all` - Retrieves a list of all active donors.
*   `GET /search` - Filters donors dynamically by Blood Group and City.

### Blood Requests (`/api/requests`)
*   `POST /create` - Creates an emergency blood request.
*   `GET /my-requests` - Fetches requests made by the logged-in user.

### Admin (`/api/admin`)
*   `GET /dashboard` - Fetches aggregated analytics (Total Donors, Requests, Notifications).

---

## 💾 Database Schema Overview

The ORM automatically generates the following normalized tables:
*   `users`: Stores credentials, role, location, and blood group.
*   `donors`: Links to `users` and tracks donation availability and timestamps.
*   `blood_requests`: Tracks emergency requests, required blood groups, and statuses.
*   `otp_tokens`: Securely stores temporary OTP codes linked to phone numbers with strict expiration times.
*   `notifications`: Tracks in-app alerts and read/unread status.

---
*Built as a practical assignment evaluation by Abhijeet.*
