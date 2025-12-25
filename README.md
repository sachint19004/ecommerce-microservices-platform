# E-Commerce Microservices Platform

A distributed e-commerce backend built with Java and Spring Boot, demonstrating a scalable Microservices architecture.

## 🏗 Architecture
* **API Gateway (Port 8080):** Central entry point using Spring Cloud Gateway to route traffic.
* **Product Service (Port 8081):** Manages inventory and product details (PostgreSQL).
* **Order Service (Port 8082):** Handles order placement and transactions (PostgreSQL).
* **User Service (Port 8083):** (Planned) For user authentication and management.

## 🚀 Key Features
* **Inter-Service Communication:** Synchronous communication between Order and Product services using **OpenFeign**.
* **Centralized Routing:** API Gateway routes all client requests to internal microservices.
* **Data Consistency:** Atomic transactions ensure stock is reduced only when orders are successfully placed.
* **Security:** Role-based access control and API protection (Spring Security).

## 🛠 Tech Stack
* **Core:** Java 21, Spring Boot 3.4
* **Database:** PostgreSQL
* **Microservices:** Spring Cloud Gateway, OpenFeign
