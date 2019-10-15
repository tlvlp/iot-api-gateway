# IoT Server API Gateway

## Service
Part of the [tlvlp IoT project](https://github.com/tlvlp/iot-project-summary)'s server side microservices.

This Dockerized SpringBoot-based service is responsible providing an API gateway to the other microservices:
- Expose API endpoints for higher level interaction with the services
- Expose API for user management
- Provide user profiles based authentication and authorization for the endpoints

## Building and publishing JAR + Docker image
This project is using the [Palantir Docker Gradle plugin](https://github.com/palantir/gradle-docker).
All configuration can be found in the [Gradle build file](build.gradle) file 
and is recommended to be run with the docker/dockerTagsPush task.

## Dockerhub
Repository: [tlvlp/iot-api-gateway](https://cloud.docker.com/repository/docker/tlvlp/iot-api-gateway)

## Deployment
- This service is currently designed as **stateless** and can have an arbitrary number of instances running per Docker Swarm Stack.
- For settings and deployment details see the project's [deployment repository](https://github.com/tlvlp/iot-server-deployment)

## Security
The endpoint is secured with Spring Boot Security using Basic auth.
The following roles are defined:
- **ADMIN**: Administrative access
- **USER**: Regular access to the endpoints
- **BACKEND**: Access for other back end services

## Server-side API
The endpoint details can be found in the below documents:

- [External API](API-EXTERNAL.md): The core API served by this gateway. Accessible with the USER role and secured with TLS.
- [Internal API](API-INTERNAL.md): For backend services with the BACKEND role and uses ordinary HTTP protocol.
- [Admin API](API-ADMIN.md): For administrative purposes and accessible with the ADMIN role and secured with TLS.
