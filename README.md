# Verda-Flow Onboarding Portal

Onboarding portal for Dispatchers, Drivers, and Customers of the
Verda-Flow platform.\
The system enables users to register, verify, and get provisioned before
the main platform launch.

------------------------------------------------------------------------

## ✨ Features

-   Multi-role onboarding (Dispatcher / Driver / Customer)
-   Secure registration and authentication
-   Email verification and notifications
-   Real-time messaging support (STOMP over WebSocket)
-   Template-based email rendering
-   Environment-based configuration
-   API documentation via Swagger UI

------------------------------------------------------------------------

## 🧱 Tech Stack

### Backend

-   Java
-   Spring Boot
-   Spring Data JPA
-   Spring Security
-   STOMP WebSocket Messaging

### Email & Templates

-   SendGrid Mail Service
-   Apache Velocity Template Engine

### Testing

-   Postman (API functional testing)
-   JMeter (performance testing)

------------------------------------------------------------------------

## 🌐 Environments

### Development

Isolated environment for developers to test and implement changes
independently.

### QA

Environment dedicated for quality assurance and functional validation.

### Staging

Production-like environment used for: - Final validation -
Demonstrations - Training - Pre-release verification

### Production

Live environment deployed with: - 2 application servers - Load balancer

------------------------------------------------------------------------

## 🚀 Deployment Architecture

Application is deployed behind a load balancer with environment-specific
configurations for Dev, QA, Staging, and Production tiers.

------------------------------------------------------------------------

## 📚 API Documentation

### Development

https://dev.verdaflow.com/verdaflow/swagger-ui.html

### QA

https://qa.verdaflow.com/verdaflow/swagger-ui.html

### Staging

https://staging.verdaflow.com/verdaflow/swagger-ui.html

------------------------------------------------------------------------

## 🧪 Testing Strategy

### API Testing Levels

**Level 1 --- Developer Testing** - Tool: Postman\
- Scope: Functional API validation

**Level 2 --- Performance Testing** - Tool: JMeter\
- Scope: Load and stress testing

------------------------------------------------------------------------

## 🔐 Security Testing

-   End-to-End security validation
-   Conducted by dedicated security testing team
-   Covers authentication, authorization, and data protection flows

------------------------------------------------------------------------

## ⚙️ Configuration

Configuration includes: - Database connection - SendGrid API key - JWT /
Security settings - WebSocket configuration - Environment profiles (dev,
qa, staging, prod)

Managed using Spring profiles.

------------------------------------------------------------------------

## 📨 Email System

-   Email delivery via SendGrid
-   Dynamic email templates using Apache Velocity
-   Used for account verification and onboarding communication

------------------------------------------------------------------------

## 👥 User Roles

  Role         Description
  ------------ ----------------------------------------
  Dispatcher   Operations management and coordination
  Driver       Fleet personnel onboarding
  Customer     Service user onboarding

------------------------------------------------------------------------

## 📦 Build & Run

### Prerequisites

-   Java 17+
-   Maven

### Build

mvn clean install

### Run

mvn spring-boot:run

### Run with Profile

mvn spring-boot:run -Dspring-boot.run.profiles=dev

------------------------------------------------------------------------
