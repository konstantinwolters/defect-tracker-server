# Defect Tracker Server

> This is a prototype of a REST API that can be used for tracking raw material defects
> and deriving corrective actions.

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000)

### Feature
___
- Authentication/Authorization (Spring Security)
  - JWT authentication 
  - Rate Limiting (Token Bucket with Bucket4J & Concurrent Requests Limiting)
- Logging (Logback)
- E-Mail notification

### Prerequisites
___
- Docker service must be running.
- Provide the following values as ```secrets.properties```:
```
## Database configuration
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

##JWT configuration
JWT.SECRET-KEY=

##Email configuration
sender.mail-address=
spring.mail.host=
spring.mail.username=
spring.mail.password=
```
### API Reference 
___
- Enter ```/swagger-ui.html``` after startup for Swagger API documentation.
---
