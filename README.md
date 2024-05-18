# Defect Tracker Server

> This is a prototype of a REST API for tracking raw material defects
> and deriving corrective actions.

![Version](https://img.shields.io/badge/version-0.8.0-blue.svg?cacheSeconds=2592000)


## Features / Technologies
| Feature                                                                      | Technology                                                                                    |
|------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| Authentication/Authorization <br/> - JWT authentication <br/>- Rate Limiting | Spring Security<br/>- jwt.io <br/>- Token Bucket with Bucket4J & Concurrent Requests Limiting |
| Logging                                                                      | Logback                                                                                       |
| File upload/download                                                         | Minio                                                                                         |
| Database                                                                     | PostgreSQL                                                                                    |
| API Documentation                                                            | Swagger                                                                                       |
| Unit Testing                                                                 | JUnit 5                                                                                       |
| Integration Testing                                                          | Spring Boot Test                                                                              |
| Containerization                                                             | Docker                                                                                        |


## Prerequisites
- Docker
- Provide the following values as environment variables:

```env
## PostgreSQL configuration
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD

### JWT configuration
JWT_SECRET_KEY

### E-Mail configuration
SENDER_MAIL_ADDRESS
SPRING_MAIL_HOST
SPRING_MAIL_USERNAME
SPRING_MAIL_PASSWORD

### Minio configuration
MINIO_ENDPOINT
MINIO_ACCESS-KEY
MINIO_SECRET-KEY
MINIO_BUCKET-NAME
```


## API Reference
- Enter ```http://localhost:8080``` after startup for Swagger API documentation.
