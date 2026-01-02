# 🔐 Auth Service – Authentication Microservice

Authentication and authorization **microservice** built with **Spring Boot**, designed as part of a **microservices architecture** and ready for **CI/CD**, **Docker**, **Kubernetes**, and **GitOps with Argo CD**.

This service is responsible for **user management**, **registration**, **login**, and **JWT token issuance/validation**, and is intended to integrate with an **API Gateway** or other backend services.

---

## 📌 Key Features

* User registration
* Authentication (login)
* JWT generation and validation
* Security with Spring Security
* Containerization with Docker
* Automated CI/CD pipeline
* Kubernetes deployment
* GitOps-based delivery with Argo CD

---

## 🧱 Architecture

```text
┌────────────┐
│   Client   │
└─────┬──────┘
      │
┌─────▼──────┐
│ API Gateway│ (optional)
└─────┬──────┘
      │
┌─────▼────────────┐
│ Auth Service     │
│ - Register       │
│ - Login          │
│ - JWT            │
└─────┬────────────┘
      │
┌─────▼──────┐
│ PostgreSQL │
└────────────┘
```

---

## 🔗 Available Endpoints

### 🔸 User Registration

```http
POST /v1/user/register
```

**Request example:**

```json
{
  "username": "user",
  "email": "user@example.com"
  "pwd": "password123",
  "rol": "ADMIN"
}
```

---

### 🔸 Login

```http
POST /v1/auth/login
```

**Request example:**

```json
{
  "username": "user",
  "pwd": "password123"
}
```

**Response example:**

```json
{
  "username":"user"
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 🐳 Docker

The project includes a **Dockerfile** to build the service image.

```bash
docker build -t auth-service .
docker run -p 8080:8080 auth-service
```

---

## ☸️ Kubernetes

Kubernetes deployment includes:

* Deployment
* Service
* ConfigMap / Secret (depending on configuration)

```bash
kubectl apply -f k8s/
```

---

## 🚀 CI/CD

The project includes a **Continuous Integration and Continuous Deployment pipeline** that performs:

1. Project build
2. Test execution
3. Docker image build
4. Push to GitHub container registry (GHCI)
5. Kubernetes manifest update

Tools used:

* GitHub Actions
* Docker
* Kubernetes

---

## 🔄 Argo CD (GitOps)

Deployments are managed using **Argo CD**, following the **GitOps** approach:

* Kubernetes manifests are stored in Git
* Argo CD automatically synchronizes changes
* Versioned deployments with easy rollback

---

## 🛠️ Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Docker
* Kubernetes
* Argo CD
* GitHub actions CI/CD

---

## 📂 Project Structure

```text
auth-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── Dockerfile
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
├── .github/workflows/
│   └── ci.yml
|   └── cd.yml
├── argocd/
|   └── application.yml
└── README.md
```

---

## 🧪 Testing

Includes **unit and integration tests** for:

* Controllers
* Services
* Security configuration

---

## 👤 Author

**Oscar Vega**
Backend Developer – Spring Boot | Microservices 



