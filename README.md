# 🎓 Sree Next Gen School

A full-stack school management application built using **Spring Boot**, **Angular 21 SSR**, **PostgreSQL**, **Docker**, and **Docker Compose**.

---

# 🏗️ Tech Stack

## Backend
- Java 21
- Spring Boot 3
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL

## Frontend
- Angular 21
- Angular SSR
- Express
- SCSS

## Infrastructure
- Docker
- Docker Compose

---

# 📁 Project Structure

```
Sree Next Gen School
│
├── .env
├── .env.example
├── docker-compose.yml
├── README.md
│
├── SreeNextGenSchool/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│
└── SreeNextGenSchoolFe/
    ├── Dockerfile
    ├── package.json
    └── src/
```

---

# ⚙️ Environment Setup

Copy the example environment file.

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Linux / macOS

```bash
cp .env.example .env
```

Update the values inside `.env` if required.

---

# 🚀 Start the Application

Build and start all services:

```powershell
docker compose up --build
```

Run in detached mode:

```powershell
docker compose up -d --build
```

---

# 🌱 Spring Profiles

The project supports two Spring profiles.

## Development

The default Docker Compose configuration starts the backend using:

```text
SPRING_PROFILES_ACTIVE=dev
```

Run:

```powershell
docker compose up --build
```

---

## Production

Temporarily override the profile:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
docker compose up --build
```

Remove the override afterwards:

```powershell
Remove-Item Env:SPRING_PROFILES_ACTIVE
```

---

# 🛠️ Local Build

## Backend

```powershell
cd SreeNextGenSchool
.\mvnw clean package
```

## Frontend

```powershell
cd SreeNextGenSchoolFe
npm ci
npm run build
```

---

# 🐳 Docker Commands

## Build Images

```powershell
docker compose build
```

## Start Services

```powershell
docker compose up
```

## Start in Background

```powershell
docker compose up -d
```

## View Logs

```powershell
docker compose logs -f
```

## Stop Services

```powershell
docker compose down
```

## Rebuild Everything

```powershell
docker compose down
docker compose build --no-cache
docker compose up -d
```

---

# 🌐 Application URLs

| Service | URL |
|----------|-----|
| Angular SSR | http://localhost:4000 |
| Spring Boot API | http://localhost:8080 |
| PostgreSQL | localhost:5432 |
| Swagger UI *(Development)* | http://localhost:8080/swagger-ui.html |
| Actuator Health | http://localhost:8080/actuator/health |

---

# ✅ Verify the Stack

## Backend Health

```powershell
Invoke-WebRequest http://localhost:8080/actuator/health
```

Expected response:

```json
{"status":"UP"}
```

---

## Frontend SSR

```powershell
Invoke-WebRequest http://localhost:4000
```

or simply open:

```
http://localhost:4000
```

---

# 🏛️ Architecture

```
                Browser
                    │
                    ▼
        Angular 21 SSR (Docker)
                    │
               HTTP Proxy
                    │
                    ▼
       Spring Boot API (Docker)
                    │
              Spring Data JPA
                    │
                    ▼
      PostgreSQL Database (Docker)
```

---

# 🔐 Features

- JWT Authentication
- Spring Security
- Angular Server Side Rendering (SSR)
- Dockerized Full Stack Architecture
- PostgreSQL Persistence
- Health Checks
- Environment Variable Configuration
- Spring Profiles (Development & Production)
- Multi-stage Docker Builds
- Non-root Containers
- Docker Networking

---

# 📄 License

This project is intended for educational and portfolio purposes.