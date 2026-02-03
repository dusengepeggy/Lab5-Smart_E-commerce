# E-Commerce API

Spring Boot backend for an e-commerce system. Exposes **REST** and **GraphQL** APIs for products, categories, users, orders, order items, inventory, and reviews. Includes error handling, AOP logging, performance monitoring, and Swagger documentation.

## Tech Stack

- **Java 21** · **Spring Boot 4** · **PostgreSQL**
- **Spring Web MVC** (REST) · **Spring GraphQL** · **Spring JDBC**
- **Spring AOP** (logging & performance) · **Springdoc OpenAPI 3** (Swagger UI)
- **Lombok** · **Validation** · **Actuator**

## Features

| Area | Description |
|------|-------------|
| **REST API** | CRUD for products, categories, users, orders, order items, inventory. Pagination and filters for products. |
| **GraphQL** | Queries for products (with category/stock), orders (with items), and full CRUD for **reviews**. GraphiQL enabled for exploration. |
| **Error handling** | REST: global exception handler with validation and `NotFoundException`. GraphQL: `GraphQLExceptionResolver` maps exceptions to `NOT_FOUND`, `BAD_REQUEST`, `INTERNAL_ERROR`. |
| **AOP** | Logging aspect (entry/exit on controllers and services). Performance aspect (execution time; warns when > 500 ms). |
| **Swagger** | OpenAPI 3 config with title, description, server. Controllers tagged (Products, Categories, Users, Orders, Inventory, Order Items) for grouped docs. |

## Prerequisites

- **JDK 21**
- **Maven**
- **PostgreSQL** (running and reachable)

## Getting Started

### 1. Clone and open

```bash
git clone https://github.com/dusengepeggy/Lab5-Smart_E-commerce.git

```

### 2. Database

Create a PostgreSQL database and tables for:

- **Product**, **Category**, **User**, **Order**, **OrderItem**, **Inventory**, **Review**


### 3. Environment variables

Create a `.env` file in the project root (or set these in your environment):

```env
DB_URL=jdbc:postgresql://localhost:5432/your_database
DB_USER=your_user
DB_PASSWORD=your_password
```

`application.yaml` imports `optional:file:.env`, so these variables are picked up when the app runs.

### 4. Run the application

```bash
mvn spring-boot:run
```

The app starts on the default port (usually **8080**).

## API Documentation

### Swagger UI (REST)

- **URL:** `http://localhost:8080/swagger-ui.html` (or `/swagger-ui/index.html` depending on Springdoc version)
- Use it to browse and try all REST endpoints. Endpoints are grouped by tag: Products, Categories, Users, Orders, Inventory, Order Items.

### GraphQL (GraphiQL)

- **URL:** `http://localhost:8080/graphql`
- With GraphiQL enabled, you can run queries and mutations from the browser.

**Example queries**

```graphql
query {
  product(id: 1) {
    productId
    name
    price
    stockQuantity
  }
  reviewsByProduct(productId: 1) {
    review_id
    user_id
    rating
    comment
    review_date
  }
}
```

**Example mutations (reviews)**

```graphql
mutation {
  createReview(userId: 1, productId: 1, rating: 5, comment: "Great product!") {
    review_id
    review_date
  }
}

mutation {
  updateReview(id: 1, rating: 4, comment: "Updated comment") {
    review_id
    rating
    comment
  }
}

mutation {
  deleteReview(id: 1)
}
```

## Project Structure

```
src/main/java/com/example/e_commerce/
├── ECommerceApplication.java
├── aspect/           # AOP: LoggingAspect, PerformanceAspect
├── config/           # OpenApiConfig (Swagger)
├── controller/       # REST controllers + GraphQLController
├── dao/              # JDBC access (Product, Category, User, Order, OrderItem, Inventory, Review)
├── dto/              # Request/response DTOs and JSON wrappers
├── graphql/          # GraphQLExceptionResolver
├── model/            # Domain entities
├── service/          # Business logic
└── utils/            # GlobalExceptionHandler, NotFoundException
```

- **REST:** Controllers call services; services use DAOs. Exceptions are handled by `GlobalExceptionHandler`.
- **GraphQL:** `GraphQLController` resolves queries/mutations via services; `GraphQLExceptionResolver` turns exceptions into GraphQL errors.
- **AOP:** Aspects apply to `controller` and `service` packages for logging and timing.

## REST Endpoints Overview

| Base path | Resource | Main actions |
|-----------|----------|--------------|
| `/api/products` | Products | GET (list with pagination/filters), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| `/api/category` | Categories | GET, POST, PUT `/{id}`, DELETE `/{id}` |
| `/api/users` | Users | GET, POST `/register`, PUT `/{id}`, DELETE |
| `/api/orders` | Orders | GET `/{id}`, GET (by user), POST, PUT `/{id}`, DELETE `/{id}` |
| `/api/order-items` | Order items | GET `/{id}`, GET (by order), POST, PUT `/{id}`, DELETE `/{id}` |
| `/api/inventory` | Inventory | GET `/{id}`, GET (by product), POST, PUT `/{id}`, DELETE `/{id}` |

## GraphQL Operations Overview

**Queries:** `products`, `product`, `order`, `ordersByUser`, `review`, `reviewsByProduct`, `reviewsByUser`, `reviews`  
**Mutations:** `createReview`, `updateReview`, `deleteReview`

Schema is defined in `src/main/resources/graphql/schema.graphqls`.

## Configuration

- **application.yaml** – Datasource (`DB_URL`, `DB_USER`, `DB_PASSWORD`), GraphQL schema location, GraphiQL enabled.
- **Profiles** – `application-dev.yaml`, `application-test.yaml`, `application-prod.yaml` for environment-specific settings.

---

