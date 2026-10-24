# BOOKLY – Online Book Store

A full-stack online bookstore with JWT authentication, role-based access (User / Admin), a shopping cart, checkout and order history.

## Tech Stack

| Layer    | Technology |
|----------|------------|
| Backend  | Java 21, Spring Boot, Spring Security (JWT), Spring Data JPA / Hibernate, Bean Validation, Lombok |
| Database | MySQL |
| Frontend | Angular 18 (standalone components, route guards, HTTP interceptors), TypeScript, CSS |

## Features

- **Authentication** – sign up / log in, stateless JWT auth, BCrypt password hashing
- **Role-based authorization** – route guards on the frontend, `hasRole` rules on the backend
- **Catalog** – browse, search/filter and view book details (auto-seeded with 24 books on first run)
- **Cart** – add, update quantity, remove, clear
- **Checkout & orders** – checkout converts the cart into an order (price snapshot per item) and shows purchase history
- **Profile** – edit profile details and change password
- **Admin panel** – manage books (CRUD) and other administrators
- **Error handling** – global exception handler on the API, error interceptor + toast notifications on the UI

## Project Structure

```
.
├── demo/                 # Spring Boot REST API (controller → service → repository → entity)
└── bookstore-frontend/   # Angular SPA (core / features / shared)
```

## Getting Started

### Prerequisites
- JDK 21
- MySQL 8+ running locally
- Node.js 18+ and npm

### 1. Backend

```bash
cd demo
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. The `bookstore` database is created automatically and seeded with sample books.

Configuration is read from environment variables (defaults are for local development only):

| Variable      | Default                  | Purpose |
|---------------|--------------------------|---------|
| `DB_URL`      | `jdbc:mysql://localhost:3306/bookstore?createDatabaseIfNotExist=true` | JDBC URL |
| `DB_USERNAME` | `root`                   | DB user |
| `DB_PASSWORD` | `root`                   | DB password |
| `JWT_SECRET`  | local dev placeholder    | JWT signing key (**set your own, 32+ chars, for any deployment**) |

### 2. Frontend

```bash
cd bookstore-frontend
npm install
npm start
```

Open `http://localhost:4200`. The API URL is set in `src/environments/environment.ts`.

### 3. Create an admin account

Anyone who signs up gets the `USER` role. To create the first admin, register normally, then promote that account:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'you@example.com';
```

Log in again and the admin panel becomes available. Further admins can be created from the admin panel.

## API Overview

| Area   | Endpoints |
|--------|-----------|
| Auth   | `POST /api/auth/login`, `POST /api/users/register` |
| Users  | `GET/PUT /api/users/me`, change password |
| Books  | `GET /api/books[/{id}]` · `POST/PUT/DELETE` (admin only) |
| Cart   | `GET/POST /api/cart`, `PUT/DELETE /api/cart/{id}` |
| Orders | `POST /api/orders/checkout`, `GET /api/orders[/{id}]` |
| Admins | `/api/admins/**` (admin only) |

## Notes

- This is a portfolio/learning project – payment is simulated (checkout completes immediately).
- Book cover images are loaded from the Open Library covers API.
