# FoodMate — Full-Stack Food Ordering Platform

FoodMate is a full-stack food ordering application with separate customer and admin interfaces. It provides authentication, food catalogue management, cart operations, order management, local image uploads, and Razorpay test-payment integration.

## Highlights

- Customer registration and JWT-based login
- Browse and filter food items by category
- Food details and cart management
- Checkout and Razorpay test-payment flow
- Customer order history
- Admin interface for adding/deleting food items
- Admin order dashboard with order-status updates
- Spring Boot REST APIs with MongoDB persistence
- Password hashing with BCrypt
- Stateless JWT authentication
- Local image upload and serving through Spring Boot

## Architecture

```text
React Customer App ──┐
                     ├── REST API ── Spring Boot ── MongoDB
React Admin App ─────┘                    │
                                          └── Razorpay
```

## Tech Stack

| Layer | Technologies |
|---|---|
| Backend | Java 21, Spring Boot 3.5.4, Spring Security, Spring Data MongoDB, JWT |
| Customer UI | React 18, Vite, Axios, Bootstrap |
| Admin UI | React 18, Vite, Axios, Bootstrap |
| Database | MongoDB |
| Payments | Razorpay Test API |
| Build Tools | Maven, npm |

## Repository Structure

```text
FoodMate/
├── foodmate-backend/          # Spring Boot REST API
├── foodmate-user-frontend/    # Customer-facing React application
├── foodmate-admin-frontend/   # Admin React application
├── foodmate-food-images/      # Sample food images
├── Images/                    # Project assets
├── .gitignore
└── README.md
```

## Prerequisites

- Java 21+
- Maven 3.9+ (or use the included Maven wrapper)
- Node.js 18+
- MongoDB running locally or a MongoDB connection string
- Razorpay test account credentials for payment testing

## Configuration

Sensitive values are intentionally not committed to this repository.

### Backend

Set these environment variables before starting Spring Boot:

```text
MONGODB_URI=mongodb://localhost:27017/foodies
JWT_SECRET_KEY=<your-base64-encoded-secret>
RAZORPAY_KEY=<your-razorpay-test-key-id>
RAZORPAY_SECRET=<your-razorpay-test-secret>
```

See `foodmate-backend/.env.example` for the expected names.

### Customer frontend

Create `foodmate-user-frontend/.env`:

```text
VITE_API_BASE_URL=http://localhost:8080/api
VITE_RAZORPAY_KEY=<your-razorpay-test-key-id>
```

Only the Razorpay **key ID** belongs in the browser. The Razorpay secret stays on the backend.

### Admin frontend

Create `foodmate-admin-frontend/.env`:

```text
VITE_API_BASE_URL=http://localhost:8080/api
```

## Run Locally

### 1. Start MongoDB

Make sure MongoDB is running and the configured database is reachable.

### 2. Start the Spring Boot backend

```bash
cd foodmate-backend
./mvnw spring-boot:run
```

On Windows:

```powershell
cd foodmate-backend
.\mvnw.cmd spring-boot:run
```

The API runs on `http://localhost:8080` by default.

### 3. Start the customer application

```bash
cd foodmate-user-frontend
npm install
npm run dev
```

Vite will display the local URL, normally `http://localhost:5173`.

### 4. Start the admin application

Open another terminal:

```bash
cd foodmate-admin-frontend
npm install
npm run dev
```

Vite will display the local URL, normally `http://localhost:5174`.

## Main API Endpoints

### Authentication

- `POST /api/register`
- `POST /api/login`

### Food

- `GET /api/foods`
- `GET /api/foods/{id}`
- `POST /api/foods`
- `DELETE /api/foods/{id}`

### Cart

- `POST /api/cart`
- `GET /api/cart`
- `POST /api/cart/remove`
- `DELETE /api/cart`

### Orders

- `POST /api/orders/create`
- `POST /api/orders/verify`
- `GET /api/orders`
- `DELETE /api/orders/{orderId}`
- `GET /api/orders/all`
- `PATCH /api/orders/status/{orderId}?status={status}`

## Security Notes

- Passwords are hashed using BCrypt.
- JWT is used for stateless authentication.
- Razorpay secret credentials are server-side only.
- API credentials and local environment files are excluded through `.gitignore`.

## Project Scope

This repository is intended as a portfolio implementation demonstrating full-stack development with a Java/Spring Boot backend, React clients, MongoDB, authentication, file handling, and payment integration.
