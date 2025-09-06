# secure-software (Spring Boot + PostgreSQL over TLS)

[Running the Project Live](running_app.mp4)

**Last updated:** 2025-09-06

This repository is a secure-by-default Java Spring Boot REST API with a PostgreSQL backend. It demonstrates:

- End-to-end **TLS**: the API serves **HTTPS** on port **8443** using a Java keystore, and PostgreSQL is configured to **require SSL** with server certs.
- **Credential safety**: bcrypt password hashing, auth endpoints, and transport-layer encryption so credentials are never sent in cleartext.
- **SQL injection resilience**: persistence uses Spring Data JPA (and/or prepared statements) so inputs are **parameterized**, not concatenated SQL.
- **Input validation**: `jakarta.validation` annotations plus global validation error handling.

It is intended to address the scenario:

> _Given a social‑engineering attack (e.g., a phishing email pretending to be IT asking employees for login credentials) **and** a web application that could otherwise be vulnerable to SQL injection: provide code and configuration that ensures secure access to the database over HTTPS/TLS and prevents SQL injection._

---

## Architecture Overview

- **Language/Framework**: Java 21, Spring Boot 3.5.x
- **Database**: PostgreSQL 16 (Dockerized) with SSL enabled (`ssl = on`)
- **Build**: Maven (`mvnw` wrapper included)
- **Containers**: `docker-compose` spins up both the API and Postgres
- **HTTPS**: Configured via `server.ssl.*` in `src/main/resources/application.properties` with a keystore at `src/main/resources/certs/keystore.p12` (self‑signed for local dev)
- **JDBC/TLS**: Datasource URL enforces TLS and full cert verification:

```properties
# src/main/resources/application.properties
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:certs/keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=secure-software

spring.datasource.url=jdbc:postgresql://postgres:5432/account?ssl=true&sslmode=verify-full&sslrootcert=/app/certs/root.crt
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

> The Postgres container is built with `ssl = on` and uses `server.crt`/`server.key`. The API trusts the DB via `root.crt`. (See `postgresql/Dockerfile` and `docker-compose.yml`.)

---

## Threat Scenario → Controls Mapping (Phishing + SQLi)

| Threat                                 | Control in This Project                                                                                                                                                          | Where                                                                                            |
| -------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ |
| Phishing attempts to steal credentials | Transport encryption (**HTTPS**), bcrypt hashing, and session auth prevent credential sniffing; sample `curl` uses TLS. Educate users to **never** enter creds from email links. | `application.properties` (TLS), `SecurityConfig.java` (auth), BCrypt via `BCryptPasswordEncoder` |
| SQL Injection via form fields          | Use **JPA repositories** / ORM which parameterize queries under the hood; no string‑built SQL. (Optional: JdbcTemplate with `?` placeholders shown below.)                       | `UserDAO.java`, `BusinessDAO.java`, `UserBO.java`, `BusinessBO.java`                             |
| Data tampering / bad input             | **Jakarta Bean Validation** (`@Valid`, annotations on DTOs) and a global `ValidationHandler` to return safe error messages.                                                      | `dto/*`, `controllers/validation/ValidationHandler.java`                                         |
| MITM of DB connection                  | **PostgreSQL TLS** with server cert, and JDBC set to `sslmode=verify-full` with `root.crt`.                                                                                      | `postgresql/Dockerfile`, `application.properties`                                                |

---

## API Surface

Controllers discovered in code:

- `POST /singUp` – create a user account ( Authentication Not Required)
- `POST /authenticate` – authenticate a user (Authentication Not Required)
- `GET  /signOut` – terminate session (Authentication Not Required)
- `POST /createAccount` – create a business/account record ( Authentication Required)
- `POST /getAccount` – fetch account details by id ( Authentication Required)

See `curl_test/requests.txt` for ready‑to‑run examples. For local, the included examples use `-k` to accept the self‑signed certificate.

---

## Build & Run

### Prerequisites

- Docker + Docker Compose
- (Optional) Java 21 + Maven 3.9 if you want to run outside Docker

### Option A: Full stack with Docker Compose (recommended)

```bash
# from project root
docker compose up --build
# API:  https://localhost:8443
# DB:   postgres:5432 (inside the compose network), TLS required
```

> **Certificates (local dev)**: A self‑signed `keystore.p12` is included for quick start. For production use a real CA‑issued cert and rotate secrets.

---

**Why this proves secure DB access over the network:** The API talks to Postgres using a JDBC URL that **requires TLS** (`ssl=true&sslmode=verify-full`) and a trusted **root CA** (`sslrootcert=/app/certs/root.crt`). All traffic is encrypted on the wire. The API itself is only reachable via **HTTPS** (port 8443), so clients submit credentials over TLS as well.

---

## Testing with cURL (HTTPS)

Create an user (self‑signed TLS shown with `-k` for local only):

```bash
curl -kv -X POST https://localhost:8443/singUp \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "John",
        "lastName": "Doe",
        "email": "nirajan.acharya@gmail.com",
        "password": "Test123!",
        "address": "123 Main Street",
        "phoneNumber": "3303079480"
      }' \
      -c cookies.txt
```

Authenticate:

```bash
curl -kv -X POST "https://localhost:8443/authenticate" \
  -H "Content-Type: application/json" \
  -d '{"email":"nirajan.acharya@gmail.com","password":"Test123!"}' \
  -c cookies.txt
```

Sign out:

```bash
 curl -kv -X GET https://localhost:8443/signOut -b cookies.txt
```

Create Business Account:

```bash
curl -kv -X POST https://localhost:8443/createAccount \
  -H "Content-Type: application/json" \
  -d '{
    "accountName": "Test Account",
    "accountOwner": "Nirajan Acharya",
    "accountEmail": "nirajan@example.com",
    "accountBalance": 1000,
    "address": "123 Test Street",
    "phoneNumber": "1234567890"
  }' \
  -b cookies.txt
```

Get Business Account:

```bash
curl -kv -X POST https://localhost:8443/getAccount \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "b5ff83b1-ba00-4148-b21f-ccc818b081b2"
  }' \
  -b cookies.txt
```

---

## Validation & Error Handling

- DTOs under `src/main/java/.../dto` use `jakarta.validation` annotations.
- `ValidationHandler` returns clean validation messages (no stack traces, no SQL).

This reduces injection surface and leaks of sensitive info.

---

## Project Layout (key files)

```
project1/
├─ Dockerfile
├─ docker-compose.yml
├─ postgresql/
│  ├─ Dockerfile        # enables ssl = on, ships server.crt/server.key/root.crt
├─ src/main/resources/
│  ├─ application.properties   # HTTPS + JDBC-over-TLS
│  └─ certs/keystore.p12       # local dev HTTPS cert (self-signed)
├─ src/main/java/com/secure_software/secure_software/
│  ├─ config/SecurityConfig.java
│  ├─ controllers/*.java
│  ├─ services/*BO.java
│  ├─ dao/*DAO.java
│  └─ entities/*.java
└─ curl_test/requests.txt
```

---
